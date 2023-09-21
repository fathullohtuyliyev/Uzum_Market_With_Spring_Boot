package com.example.demo.service;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.entity.ActivateCodes;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.UserData;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ActivateCodesRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.util.JwtTokenUtil;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import static com.example.demo.mapper.UserMapper.USER_MAPPER;
import static com.example.demo.util.JwtTokenUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {
    private final ActivateCodesRepository activateCodesRepository;
    private final UserDataRepository userDataRepository;
    private final AuthUserRepository authUserRepository;
    private final JavaMailSenderService javaMailSenderService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(AuthUserCreateDto dto) {
        try {
            if (authUserRepository.existsAuthUserByPhoneAndEmail(dto.phone, dto.email)) {
                throw new BadParamException();
            }
            AuthUser authUser = USER_MAPPER.toEntity(dto);
            ActivateCodes activateCodes = ActivateCodes.builder()
                    .authUser(authUser)
                    .build();
            String text = String.format("""
                    <h1>%d</h1>\s
                    ushbu kod akkauntingizni faollashtirish uchun tasdiqlash kodi.
                    Uni hech kimga bermang !!!
                    """, activateCodes.getCode());
            javaMailSenderService.send(activateCodes,text);
            authUser.setActive(false);
            AuthUser save = authUserRepository.save(authUser);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(save);
            log.info("{} saved",dto1);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                            .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void activate(String codeBase64) {
        try {
            byte[] decode = Decoders.BASE64.decode(codeBase64);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : decode) {
                stringBuilder.append((char)b);
            }
            Integer code = Integer.valueOf(stringBuilder.toString());
            ActivateCodes activateCodes = activateCodesRepository.findByCode(code)
                    .orElseThrow(NotFoundException::new);
            if (activateCodes.getCode().equals(code)) {
                AuthUser authUser = activateCodes.getAuthUser();
                authUser.setActive(true);
                authUserRepository.save(authUser);
            }else {
                throw new BadParamException();
            }
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String remoteAddr = request.getRemoteAddr();
        userDataRepository.deleteByUserData(remoteAddr);
    }

    @Override
    public boolean checkAndSendPasswordToEmail(String email, HttpServletResponse response) {
        try {
            if (authUserRepository.existsAuthUserByEmail(email)) {
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
                email = textEncodeWithJwt(email);
                System.out.println("Encoded email " + email);
                response.setHeader("email",email);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserGetDto login(String password,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            String email = request.getHeader("email");
            email = getText(email);

            if (email==null || email.isBlank()) {
                throw new BadParamException();
            }

            String userDate = request.getRemoteAddr();
            String token = jwtTokenUtil.generateToken(email, password, userDate);

            if (token==null) {
                throw new ForbiddenAccessException();
            }

                AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                        .orElseThrow(NotFoundException::new);

            authUserRepository.updateAuthUserBlockedByEmail(true,email);

            if (new HashSet<>(authUser.getRoles()).containsAll(List.of("ADMIN","SUPER_ADMIN"))) {
                UserData userData = UserData.builder()
                        .user(authUser)
                        .data(userDate)
                        .build();
                userDataRepository.save(userData);
            }

                AuthUserGetDto dto = USER_MAPPER.toDto(authUser);
                log.info("{} login with {}",dto,request.getHeader("User-Agent"));

                response.setHeader("Authorization","Bearer "+token);

                String text = String.format("""
                        Hurmatli foydalanuvchi\s
                        Sizning hisobingizga %s qurilma %s da kirdi.
                        Agar siz bo'lmasangiz, iltimos bizga murojat qiling !!!
                        """, request.getHeader("User-Agent"), LocalDateTime.now());
                javaMailSenderService.send(email,text);

                authUserRepository.updatePassword(email,null);
                return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserGetDto update(AuthUserUpdateDto dto) {
        try {

            authUserRepository.updateAuthUser(dto.firstName,dto.lastName,
                    dto.imagePath,dto.gender,dto.birthdate,dto.id);
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(dto.id)
                    .orElseThrow(NotFoundException::new);
            AuthUserGetDto dto1 = USER_MAPPER.toDto(authUser);

            log.info("{} updated",dto1);

            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserGetDto get(UUID id, HttpServletRequest request) {
        try {
            AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(id)
                    .orElseThrow(NotFoundException::new);
            if (!Objects.requireNonNullElse(getEmail(request),"").
                    equals(Objects.requireNonNullElse(authUser.getEmail(),""))) {
                throw new ForbiddenAccessException();
            }
            AuthUserGetDto dto = USER_MAPPER.toDto(authUser);
            log.info("{} gave",dto);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<AuthUserGetDto> users(Pageable pageable) {
        try {
            Page<AuthUser> all = authUserRepository.findAll(pageable);
            int allSize = authUserRepository.findAllSize();
            if (all.getContent().size()< allSize) {
                all = new PageImpl<>(authUserRepository.findAll(),
                        PageRequest.of(0,allSize),allSize);
            }
            List<UUID> uuidList = all.getContent()
                    .stream()
                    .map(AuthUser::getId)
                    .toList();
            log.info("{} gave users",uuidList);
            return USER_MAPPER.toDto(all);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
