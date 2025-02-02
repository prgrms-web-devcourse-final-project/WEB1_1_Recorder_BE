package com.revup.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revup.auth.dto.token.TokenInfo;
import com.revup.exception.ExpiredTokenException;
import com.revup.exception.InvalidTokenException;
import com.revup.exception.UnsupportedTokenException;
import com.revup.user.entity.LoginType;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


@Component
public class RevUpJwtProvider {

    @Value("${jwt.secret-key}")
    private static String SECRET_KEY;

    public static final String LOGIN_TYPE = "loginType";
    public static final String SOCIAL_ID = "socialId";

    public RevUpJwtProvider(@Value("${jwt.secret-key}") String secretKey) {
        SECRET_KEY = secretKey;
    }

    private static Key getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public TokenInfo getTokenUserPrincipal(String token) {
        Long userId = getUserId(token);
        String loginType = getLoginType(token);
        String socialId = getSocialId(token);
        return TokenInfo.of(userId, socialId, LoginType.findByType(loginType));
    }

    public String getTokenType(String token) throws JsonProcessingException {
        String[] parts = token.split("\\.");
        String headerBase64 = parts[0];
        byte[] decodedHeader = Base64.getUrlDecoder().decode(headerBase64);


        // 디코딩된 결과를 String으로 변환하여 반환
        String headerString = new String(decodedHeader);
        return new ObjectMapper().readTree(headerString).get("tokenType").asText();
    }

    private Long getUserId(String token) {
        return Long.parseLong(getTokenSubject(getKey(), token));
    }

    private String getLoginType(String token) {
        return (String) getTokenClaims(getKey(), token).get(LOGIN_TYPE);
    }

    private String getSocialId(String token) {
        return (String) getTokenClaims(getKey(), token).get(SOCIAL_ID);
    }

    private static Claims getTokenClaims(Key key, String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw UnsupportedTokenException.EXCEPTION;
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    private static String getTokenSubject(Key key, String token) {
        return getTokenClaims(key,token)
                .getSubject();
    }
}
