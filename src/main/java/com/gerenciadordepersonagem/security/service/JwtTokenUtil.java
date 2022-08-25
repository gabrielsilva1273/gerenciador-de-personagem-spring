package com.gerenciadordepersonagem.security.service;

import com.gerenciadordepersonagem.security.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenUtil {
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    private int jwtHoursToExpire = 6;

    private long EXPIRE_DURATION = jwtHoursToExpire * 60 * 60 * 1000;
    private Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    public String generateAccessToken (User user) {
        return Jwts.builder()
                .setIssuer("Gabriel").setIssuedAt(new Date())
                .setSubject(String.format("%s %s %s", user.getId(), user.getEmail(),user.getUserRole()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS384, SECRET_KEY).compact();
    }

    public boolean validateAccessToken (String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Token expirado", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("O token é nulo, vazio ou apenas espaço em branco", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("Token Inválido", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Token não suportado", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Falha na validação da assinatura");
        }
        return false;
    }

    public String getSubject (String token) {
        return parseClaims(token).getSubject();
    }
    public String getOwnerId (String token){
        String[] jwtSubject = getSubject(token).split(" ");
        return jwtSubject[0];
    }
    public Date getExpiration(String token){
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims (String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
