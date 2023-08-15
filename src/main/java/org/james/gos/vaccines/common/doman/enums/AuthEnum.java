package org.james.gos.vaccines.common.doman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * AuthEnum 权限枚举
 *
 * @author James Gosl
 * @since 2023/08/14 22:17
 */
@AllArgsConstructor
@Getter
public enum AuthEnum {
    ADMIN(0, "管理员"),
    DOCTOR(1, "医生"),
    USER(2, "接种者");
    ;

    private final Integer auth;
    private final String desc;

    private static final Map<Integer, AuthEnum> cache;

    static {
        cache = Arrays.stream(AuthEnum.values()).collect(Collectors.toMap(AuthEnum::getAuth, Function.identity()));
    }

    public static AuthEnum of(Integer type) {
        return cache.get(type);
    }
}
