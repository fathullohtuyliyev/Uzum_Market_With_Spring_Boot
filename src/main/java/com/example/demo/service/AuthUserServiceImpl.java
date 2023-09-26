package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.entity.ActivateCodes;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserData;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ActivateCodesRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.util.JwtTokenUtil;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.mapper.UserMapper.USER_MAPPER;
import static com.example.demo.util.JwtTokenUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {
    private final RoleRepository roleRepository;
    private final ActivateCodesRepository activateCodesRepository;
    private final UserDataRepository userDataRepository;
    private final AuthUserRepository authUserRepository;
    private final JavaMailSenderService javaMailSenderService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final MultimediaService multimediaService;

    @Override
    public void save(AuthUserCreateDto dto, HttpServletResponse response) {
            if (authUserRepository.existsAuthUserByPhoneAndEmail(dto.phone, dto.email)) {
                throw new BadParamException();
            }
            if (dto.images!=null && !multimediaService.isExist(dto.images)) {
                throw new BadParamException();
            }
            Role role = roleRepository.findByName("CUSTOMER")
                        .orElseThrow(NotFoundException::new);

            AuthUser authUser = USER_MAPPER.toEntity(dto);
            ActivateCodes activateCodes = ActivateCodes.builder()
                    .authUser(authUser)
                    .build();
            String text = String.format("""
                    <h1>%d</h1>\s
                    ushbu kod akkauntingizni faollashtirish uchun tasdiqlash kodi.
                    Uni hech kimga bermang !!!
                    """, activateCodes.getCode());
            authUser.setActive(false);
            AuthUser save = authUserRepository.save(authUser);
            save.setRoles(Set.of(role));
            authUserRepository.save(save);
            javaMailSenderService.send(activateCodes,text);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(save);
            log.info("{} saved",dto1);
            String encodedEmail = textEncodeWithJwt(dto1.email);
            response.setHeader("email",encodedEmail);
            response.setStatus(201);
    }

    @Override
    public void activate(String codeBase64, HttpServletRequest request) {
            byte[] decode = Decoders.BASE64.decode(codeBase64);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : decode) {
                stringBuilder.append((char)b);
            }

            String encodedEmail = request.getHeader("email");
            String email = getText(encodedEmail);
            Integer code = Integer.valueOf(stringBuilder.toString());

            authUserRepository.findByEmail(email)
                    .orElseThrow(NotFoundException::new);

                ActivateCodes activateCodes = activateCodesRepository.findByCode(code)
                        .orElseThrow(BadParamException::new);
                AuthUser authUser = activateCodes.getAuthUser();

            if (authUser.getEmail().equals(email)) {
                authUserRepository.updateAuthUserActiveById(authUser.getId(),true);
            }else {
                throw new BadParamException();
            }
    }


    @Override
    public void checkAndSendPasswordToEmail(String email, HttpServletResponse response) {
            StringBuilder stringBuilder = new StringBuilder();
            byte[] decode = Decoders.BASE64.decode(email);

            for (byte b : decode) {
                stringBuilder.append((char)b);
            }
            email = stringBuilder.toString();
            if (authUserRepository.existsAuthUserByEmailAndActiveTrue(email)) {
                String temporaryPassword = String.valueOf(new Random().nextInt(100000,999999));
                authUserRepository.updatePassword(email, passwordEncoder.encode(temporaryPassword));
                String text = String.format("""
                        <h1>%s</h1>
                        <h6>Hurmatli foydalanuvchi bu sizning bir marotabalik parolingiz.</h6>
                        <h6>Agar siz login qilishga urinmagan bo'lsangiz,</h6>
                        <h6>ushbu xabarni o'chirib yuborishingizni va</h6>
                        <h6>hech kimga ushbu parolni bermasligingizni so'rab qolamiz !!!</h6>\s
                        """, temporaryPassword);
                javaMailSenderService.send(email, text);
                System.out.println("email = " + email);
                email = textEncodeWithJwt(email);
                System.out.println("Encoded email " + email);
                response.setHeader("email",email);
            }
    }

    @Override
    public AuthUserGetDto login(String password,
                                HttpServletRequest request,
                                HttpServletResponse response) {

            String email = request.getHeader("email");
            email = getText(email);

            if (email==null || email.isBlank()) {
                throw new BadParamException();
            }

            byte[] decode = Decoders.BASE64.decode(password);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : decode) {
                stringBuilder.append((char) b);
            }
            password = stringBuilder.toString();
            String userDate = request.getRemoteAddr();
            String token = jwtTokenUtil.generateToken(email, password, userDate);

            if (token==null) {
                throw new ForbiddenAccessException();
            }

                AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                        .orElseThrow(NotFoundException::new);

            Set<String> collect = authUser.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            if (collect.contains("ADMIN") || collect.contains("SUPER_ADMIN")) {
                UserData userData = UserData.builder()
                        .user(authUser)
                        .data(userDate)
                        .expireTime(LocalDateTime.now().plusHours(3))
                        .build();
                userDataRepository.save(userData);
            }

                AuthUserGetDto dto = USER_MAPPER.toDto(authUser);
                log.info("{} login with {}",dto,request.getHeader("User-Agent"));
            List<String> roles = authUser.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();
            dto.setRoles(roles);
                response.setHeader("Authorization",token);

                String text = String.format("""
                        Hurmatli foydalanuvchi\s
                        Sizning hisobingizga %s qurilma %s da kirdi.
                        Agar siz bo'lmasangiz, iltimos bizga murojat qiling !!!
                        """, request.getHeader("User-Agent"), LocalDateTime.now());
                javaMailSenderService.send(email,text);

                authUserRepository.updatePassword(email,null);
                return dto;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
            String remoteAddr = request.getRemoteAddr();
            userDataRepository.deleteByUserData(remoteAddr);
    }

    @Override
    public AuthUserGetDto update(AuthUserUpdateDto dto) {
            if (dto.images!=null && !multimediaService.isExist(dto.images)) {
                throw new BadParamException();
            }
            authUserRepository.updateAuthUser(dto.firstName,dto.lastName,
                    dto.images,dto.gender,dto.birthdate,dto.id);
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(dto.id)
                    .orElseThrow(NotFoundException::new);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(authUser);
            method2(dto1, authUser);
            log.info("{} updated",dto1);

            return dto1;
    }

    @Override
    public AuthUserGetDto get(UUID id, HttpServletRequest request) {
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(id)
                    .orElseThrow(NotFoundException::new);
            if (!Objects.requireNonNullElse(getEmail(request),"").
                    equals(Objects.requireNonNullElse(authUser.getEmail(),""))) {
                throw new ForbiddenAccessException();
            }
            AuthUserGetDto dto = USER_MAPPER.toDto(authUser);
            method2(dto, authUser);
            log.info("{} gave",dto);
            return dto;
    }

    private static void method2(AuthUserGetDto dto, AuthUser authUser){
        List<String> roles = authUser.getRoles()
                .stream()
                .map(Role::getName)
                .toList();
        dto.setRoles(roles);
    }

    @Override
    public Page<AuthUserGetDto> users(Pageable pageable) {
            Page<AuthUser> all = authUserRepository.findAll(pageable);
            List<UUID> uuidList = all.getContent()
                    .stream()
                    .map(AuthUser::getId)
                    .toList();
            log.info("{} gave users",uuidList);
            Page<AuthUserGetDto> dto = USER_MAPPER.toDto(all);
            if (dto!=null && !dto.isEmpty()) {
                List<AuthUser> content = all.getContent();
                List<AuthUserGetDto> dtoContent = dto.getContent();
                for (int i = 0; i < dtoContent.size(); i++) {
                    method2(dtoContent.get(i),content.get(i));
                }
                dto = new PageImpl<>(dtoContent,dto.getPageable(),dtoContent.size());
            }
            return dto;
    }

    @Override
    public void online(UUID userId) {
            authUserRepository.updateAuthUserOnlineTrueById(userId);
    }

    @Override
    public void offline(UUID userId) {
            authUserRepository.updateAuthUserOnlineFalseById(userId);
    }

    @Override
    public boolean existEmail(String email) {
            return authUserRepository.existsAuthUserByEmail(email);
    }

    @Override
    public boolean existPhone(String phone) {
            return authUserRepository.existsAuthUserByPhone(phone);
    }
}
