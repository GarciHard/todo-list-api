package com.garcihard.todolist.security.util;

import com.garcihard.todolist.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .issuer(issuer)
                .subject(userDetails.getUserId().toString())
                .claim("username", userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public UUID extractUserId(String token) {
        return extractClaim(token, claim -> claim.get("subject", UUID.class));
    }

    public String extractUsername(String token) {
        return extractClaim(token, claim -> claim.get("username", String.class));
    }

    public long extractExpirationMillis(String token){
        return extractExpiration(token).getTime();
    }

    public String extractJwtFromRequest(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtUtil.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
