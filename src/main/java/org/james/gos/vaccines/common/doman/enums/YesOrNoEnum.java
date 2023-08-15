package org.james.gos.vaccines.common.doman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * YesOrNoEnum 0和1 的全局定义
 *
 * @author James Gosl
 * @since 2023/08/14 22:24
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0, "否"),
    YES(1, "是"),
    ;

    private final Integer status;
    private final String desc;

    private static final Map<Integer, YesOrNoEnum> cache;

    static {
        cache = Stream.of(YesOrNoEnum.values()).collect(Collectors.toMap(YesOrNoEnum::getStatus, Function.identity()));
    }

    public static YesOrNoEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? YES.getStatus() : NO.getStatus();
    }
}
