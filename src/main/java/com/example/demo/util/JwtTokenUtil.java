package com.example.demo.util;

import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private static final String key="MIICXAIBAAKBGQCSZFLVMALTGN2YUMXQNR0GKL8SQBW9HLBT7Z8IIDCQGBCJKQDMD61DOJR0KLJTTHBNUXI562VUT58EPPP7VXTSPDZ2N6UVLNQKUJTT61KDETJCMANXAKFMQ0NTEN9O3ZOU5LAUWMILJROFG64JSGIHTDIFHMEM4CXQIDAQABAOGADO3YEPCDLOUZWC6PZMC2CNZ0RDEHUNPHVBOVYR7YKMUXPX9GOBAN5BPW6KOF6JJKA0MUZN4QT79YZZYQMKHCJN10USQ8GRSSAEENQHTEBBQ6D26PYIO3DROUY2AHNEHZJMHIQCKZGC1BOJZJHZJFDYFDED3IEXBHBMGNH00AECQQD9AL1W1T9RKCWKZOPGCA8IX7DK9LIARN7FBGBEX6OV0PKMSSJ1R0EBA9JOHHW0PQ9HWI48REZKWCQW5CPFPFAKEALB9ZEQYEIP5OILO2L0I7CWU2BTCNGOBDGRJ2OKUIJZ9SVPIC6QPUG5OFAIKUANRULB6EVX2G25ERP01OQJBAPMO0QIQBX1QCOEKIM6T63ZQOEZORQCOLFONUXDWF4TS55YUN5MWFEOVOB9BQGTPISDZCWHXMQIUDZPATVFAM0CQB11ZPDWKIQKCP0GAM5WY7ZQCOABECV2O3KZ15SC8V6XM6ODZKGFWLZFOKDFOJEH3OB0IQQHY3NR3M16P6MLTECQBIPT51FS3Q2AYET9K3JFIVFDAHK1CHLEC4YZGBFDFUCAC7RB1IHYE1I2WHJ2HDF8QXKX9G26O8FPWNGFAS";
    public final CustomUserDetailsService customUserDetailsService;
    public final PasswordEncoder passwordEncoder;
    public String generateToken(@NonNull String username,@NonNull String password,String role){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new RuntimeException();
        }
        String compact = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setIssuer("http://localhost:8080")
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println(compact);
        return compact;
    }
    private static Key key(){
        byte[] bytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(bytes);
    }
    public static boolean isValid(HttpServletResponse res, @NonNull String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("claims = " + claims);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        }catch (Exception e){
            e.printStackTrace();
            res.setStatus(400);
            return false;
        }
    }
    public static String getEmail(HttpServletResponse res,@NonNull String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(substring)
                    .getBody();
            System.out.println("claims = " + claims);
            return claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String expireToken(@NonNull String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            claims.setExpiration(new Date(System.currentTimeMillis()-1));
            return Jwts.builder()
                    .signWith(key(),SignatureAlgorithm.HS256)
                    .setIssuer("http://localhost:8080")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()-1))
                    .setSubject(claims.getSubject())
                    .compact();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void addCookie(HttpServletRequest req, HttpServletResponse res,
                                 String name, Object value) {
        Cookie[] cookies = req.getCookies();
        Cookie cookie = new Cookie(name, String.valueOf(value));
        cookie.setSecure(false);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(false);
        if (cookies==null) {
            res.addCookie(cookie);
            return;
        }

        Optional<Cookie> first = Arrays.stream(cookies)
                .filter(cookie1 -> cookie1.getName().equals(name))
                .findFirst();
        first.orElse(cookie).setMaxAge(cookie.getMaxAge());
        res.addCookie(first.orElse(cookie));
    }

    public static void removeCookie(HttpServletRequest req, HttpServletResponse res,
                                    String name, Object value) {
        Cookie[] cookies = req.getCookies();
        Cookie cookie = new Cookie(name, String.valueOf(value));
        cookie.setSecure(false);
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(false);
        if (cookies==null) {
            return;
        }
        Optional<Cookie> first = Arrays.stream(cookies)
                .filter(cookie1 -> cookie1.getName().equals(name))
                .findFirst();
        assert first.orElse(null) != null;
        first.orElse(null).setValue(null);
        res.addCookie(first.get());
    }
}
