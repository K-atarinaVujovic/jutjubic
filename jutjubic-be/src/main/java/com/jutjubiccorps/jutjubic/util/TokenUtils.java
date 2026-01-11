package com.jutjubiccorps.jutjubic.util;
import com.jutjubiccorps.jutjubic.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class TokenUtils
{
    @Value("jutjubic-be")
    private String APP_NAME;

    @Value("i-pitam-ja-sad-tebe-STA-JE-ON-MENI-DAO#?_!1!5&8#B#@#j.@*g$@45#@1$5.,':D,xD,:3,hehe,haha,hoho")
    public String SECRET;

    @Value("1800000") // 30min
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private static final String AUDIENCE_WEB = "web";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private SecretKey getSigningKey(){
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generisanje JWT tokena

    public String generateToken(String username){
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(getSigningKey(), SIGNATURE_ALGORITHM)
                .compact();
    }

    private String generateAudience(){
        return AUDIENCE_WEB;
    }

    private Date generateExpirationDate(){
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    // Citanje iz JWT tokena

    public String getToken(HttpServletRequest request){
        String authHeader = getAuthHeaderFromHeader(request);

        if (authHeader !=null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }

        return null;
    }

    // Preuzimanje info iz tokena
    public String getUsernameFromToken(String token){
        String username;

        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        }
        catch (ExpiredJwtException ex){
            throw ex;
        }
        catch (Exception e){
            username = null;
        }

        return username;
    }

    public Date getIssuedDateFromToken(String token){
        Date issuedAt;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            issuedAt = claims.getIssuedAt();
        }
        catch (ExpiredJwtException ex){
            throw ex;
        }
        catch(Exception e){
            issuedAt = null;
        }

        return issuedAt;
    }

    public String getAudienceFromToken(String token){
        String audience;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        }
        catch(ExpiredJwtException ex){
            throw ex;
        }
        catch(Exception e){
            audience = null;
        }

        return audience;
    }

    public Date getExpirationDateFromToken(String token){
        Date expiration;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        }
        catch(ExpiredJwtException ex){
            throw ex;
        }
        catch(Exception e){
            expiration = null;
        }

        return expiration;
    }

    private Claims getAllClaimsFromToken(String token){
        Claims claims;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch(ExpiredJwtException ex){
            throw ex;
        }
        catch(Exception e){
            claims = null;
        }

        return claims;
    }

    // Validiranje tokena
    public Boolean validateToken(String token, UserDetails userDetails){
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedDateFromToken(token);

        Boolean isValid = username != null && username.equals(userDetails.getUsername()) && userDetails.isEnabled(); // TODO: check if acc is activated

        return isValid;
    }

    // Random util

    public int getExpiredIn(){
        return EXPIRES_IN;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request){
        return request.getHeader(AUTH_HEADER);
    }
}
