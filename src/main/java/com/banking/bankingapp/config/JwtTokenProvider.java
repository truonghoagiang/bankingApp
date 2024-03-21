package com.banking.bankingapp.config;

import com.banking.bankingapp.service.UserDetailServiceImp;
import com.banking.bankingapp.service.UserDetailsImp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${token.key}")
    private String secrekey;

    @Value("${token.key.expieration}")
    private Long jwtExpirationAccessToken;


    private SecretKey key(){
        byte[] bytes = Decoders.BASE64.decode(secrekey);
        return Keys.hmacShaKeyFor(bytes);
    }
    public String generateToken(String data){
        //SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secrekey));
        //String username = authentication.getName();
        //UserDetailsImp userPrincipal = (UserDetailsImp) authentication.getPrincipal();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationAccessToken);

        return Jwts.builder().subject(data).expiration(expireDate).signWith(key()).compact();

    }

    public String getUsername(String token){
        Claims claims = Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    public String decodeToken(String token){
        //SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secrekey));
        String data = null;
        try{
            data = Jwts.parser().verifyWith(key()).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch(Exception e){
            throw new RuntimeException("Error decode token: " + e.getMessage());
        }
        return data;
    }

    public boolean validateToken(String token){
        try{
            //SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secrekey));
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
            return true;
        }catch(SignatureException e){
            throw new RuntimeException("Invalid JWT Signature!" + e.getMessage());
        }catch(ExpiredJwtException e){
            throw new RuntimeException("JWT Token is expired!" + e.getMessage());
        }catch(UnsupportedJwtException e){
            throw new RuntimeException("Invalid JWT is unsupport!" + e.getMessage());
        }catch(IllegalArgumentException e){
            throw new RuntimeException("JWT claims String is empty!" + e.getMessage());
        }



    }
}
