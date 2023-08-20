package org.james.gos.vaccines.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Jwt 的Token 解析与生成
 *
 * @author James Gosl
 * @since 2023/08/15 20:03
 */
@Slf4j
public class JwtUtils {

    /**
     * token秘钥，请勿泄露，请勿随便修改
     */
    @Value("${jwt.secret:vaccines}")
    private String secret;

    private static final String UID_CLAIM = "uid";
    private static final String CREATE_TIME = "createTime";

    /**
     * JWT 生成Token.
     * JWT 构成: header, payload, signature
     */
    public String createToken(Long aid) {
        // build token
        return JWT.create()
                // 只存一个aid 信息，其他的自己去redis 查
                .withClaim(UID_CLAIM, aid)
                // 创建时间
                .withClaim(CREATE_TIME, new Date())
                // 过期时间 不设置过期时间 否则还得有其他复杂的操作
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 2))
                // signature
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 解密Token
     * JWT 构成: header, payload, signature
     */
    public Map<String, Claim> verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
//            log.error("decode error,token:{}", token, e);
        }
        return null;
    }

    /**
     * 根据Token 获取Aid
     * JWT 构成: header, payload, signature
     */
    public Long getAidOrNull(String token) {
        return Optional.ofNullable(verifyToken(token))
                .map(map -> map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
    }
}
