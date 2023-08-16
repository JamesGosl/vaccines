package org.james.gos.vaccines.common.doman.vo.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求信息
 *
 * @author James Gosl
 * @since 2023/08/15 21:33
 */
@Data
@Accessors(chain = true)
public class RequestInfo {

    /**
     * 账户id
     */
    private Long id;
}
