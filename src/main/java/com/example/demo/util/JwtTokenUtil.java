package com.example.demo.util;

import com.example.demo.exception.BadParamException;
import com.example.demo.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUtil {
    private static final String keyForCommon="MIICXAIBAAKBGQCSZFLVMALTGN2YUMXQNR0GKL8SQBW9HLBT7Z8IIDCQGBCJKQDMD61DOJR0KLJTTHBNUXI562VUT58EPPP7VXTSPDZ2N6UVLNQKUJTT61KDETJCMANXAKFMQ0NTEN9O3ZOU5LAUWMILJROFG64JSGIHTDIFHMEM4CXQIDAQABAOGADO3YEPCDLOUZWC6PZMC2CNZ0RDEHUNPHVBOVYR7YKMUXPX9GOBAN5BPW6KOF6JJKA0MUZN4QT79YZZYQMKHCJN10USQ8GRSSAEENQHTEBBQ6D26PYIO3DROUY2AHNEHZJMHIQCKZGC1BOJZJHZJFDYFDED3IEXBHBMGNH00AECQQD9AL1W1T9RKCWKZOPGCA8IX7DK9LIARN7FBGBEX6OV0PKMSSJ1R0EBA9JOHHW0PQ9HWI48REZKWCQW5CPFPFAKEALB9ZEQYEIP5OILO2L0I7CWU2BTCNGOBDGRJ2OKUIJZ9SVPIC6QPUG5OFAIKUANRULB6EVX2G25ERP01OQJBAPMO0QIQBX1QCOEKIM6T63ZQOEZORQCOLFONUXDWF4TS55YUN5MWFEOVOB9BQGTPISDZCWHXMQIUDZPATVFAM0CQB11ZPDWKIQKCP0GAM5WY7ZQCOABECV2O3KZ15SC8V6XM6ODZKGFWLZFOKDFOJEH3OB0IQQHY3NR3M16P6MLTECQBIPT51FS3Q2AYET9K3JFIVFDAHK1CHLEC4YZGBFDFUCAC7RB1IHYE1I2WHJ2HDF8QXKX9G26O8FPWNGFAS";
    private static final String keyForLogin="MIICXAIBAAKBGQCVMALTGN2YUMXQNR0GKL8SQBW9HLBT7Z8IIDCQGBCJKQDMD61DOJR0KLJTTHBNUXI562VUT58EPPP7VXTSPDZ2N6UVLNQKUJTT61KDETJCMANXAKFMQ0NTEN9O3ZOU5LAUWMILJROFG64JSGIHTDIFHMEM4CXQIDAQABAOGADO3YEPCDLOUZWC6PZMC2CNZ0RDEHUNPHVBOVYR7YKMUXPX9GOBAN5BPW6KOF6JJKA0MUZN4QT79YZZYQMKHCJN10USQ8GRSSAEENQHTEBBQ6D26PYIO3DROUY2AHNEHZJMHIQCKZGC1BOJZJHZJFDYFDED3IEXBHBMGNH00AECQQD9AL1W1T9RKCWKZOPGCA8IX7DK9LIARN7FBGBEX6OV0PKMSSJ1R0EBA9JOHHW0PQ9HWI48REZKWCQW5CPFPFAKEALB9ZEQYEIP5OILO2L0I7CWU2BTCNGOBDGRJ2OKUIJZ9SVPIC6QPUG5OFAIKUANRULB6EVX2G25ERP01OQJBAPMO0QIQBX1QCOEKIM6T63ZQOEZORQCOLFONUXDWF4TS55YUN5MWFEOVOB9BQGTPISDZCWHXMQIUDZPATVFAM0CQB11ZPDWKIQKCP0GAM5WY7ZQCOABECV2O3KZ15SC8V6XM6ODZKGFWLZFOKDFOJEH3OB0IQQHY3NR3M16P6MLTECQBIPT51FS3Q2AYET9K3JFIVFDAHK1CHLEC4YZGBFDFUCAC7RB1IHYE1I2WHJ2HDF8QXKX9G26O8FPWNGFAS";
    public final CustomUserDetailsService customUserDetailsService;
    public final PasswordEncoder passwordEncoder;


    public static String textEncodeWithJwt(@NonNull String text){

        Date expiration = new Date(System.currentTimeMillis()
                + 1000 * 60 * 3);
        String compact = Jwts.builder()
                .setSubject(text)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .setIssuer("http://localhost:8080")
                .signWith(keyForCommon(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println(compact);
        return compact;
    }

    public String generateToken(@NonNull String username, @NonNull String password, @NonNull String userDate){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Date expiration = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 30L);

        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new BadParamException();
        }
            String compact = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(expiration)
                    .setIssuer("http://localhost:8080")
                    .signWith(keyForLogin(), SignatureAlgorithm.HS256)
                    .compact();
            System.out.println(compact);
            return compact;
    }
    private static Key keyForLogin(){
        byte[] bytes = Decoders.BASE64.decode(keyForLogin);
        return Keys.hmacShaKeyFor(bytes);
    }
    private static Key keyForCommon(){
        byte[] bytes = Decoders.BASE64.decode(keyForCommon);
        return Keys.hmacShaKeyFor(bytes);
    }
    private static boolean isValidForLogin(@NonNull String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForLogin())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("claims = " + claims);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }
    private static boolean isValidForCustom(@NonNull String encoded){
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForCommon())
                    .build()
                    .parseClaimsJws(encoded)
                    .getBody();

            System.out.println("claims = " + claims);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static String getEmail(HttpServletResponse res,@NonNull String token){
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForLogin())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (isValidForLogin(token)) {
                return null;
            }

            System.out.println("claims = " + claims);
            return claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            res.setStatus(401);
            return null;
        }
    }
    public static String getEmail(HttpServletRequest req){
        try {

            String authorization = req.getHeader("Authorization");
            String substring = authorization.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForLogin())
                    .build()
                    .parseClaimsJws(substring)
                    .getBody();

            if (!isValidForLogin(authorization)) {
                return null;
            }

            System.out.println("claims = " + claims);
            return claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getText(String encoded){
        try {
            if (encoded==null) {
                throw new BadParamException();
            }
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForCommon())
                    .build()
                    .parseClaimsJws(encoded)
                    .getBody();

            if (!isValidForCustom(encoded)) {
                return null;
            }

            System.out.println("claims = " + claims);
            return claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
