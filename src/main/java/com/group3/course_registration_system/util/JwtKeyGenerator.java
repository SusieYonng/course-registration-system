package com.group3.course_registration_system.util;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtKeyGenerator {
    /**
     * 生成安全的JWT密钥
     * @return Base64编码的密钥字符串
     */
    public static String generateSecretKey() {
        // 使用HMAC-SHA512算法生成密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        // 将密钥转换为Base64编码的字符串
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}