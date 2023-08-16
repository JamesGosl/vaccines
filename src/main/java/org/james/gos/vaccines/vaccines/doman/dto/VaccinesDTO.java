package org.james.gos.vaccines.vaccines.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.vaccines.doman.entity.Vaccines;

/**
 * VaccinesDTO
 *
 * @author James Gosl
 * @since 2023/08/16 21:44
 */
@Data
public class VaccinesDTO {

    /** id */
    private Long id;
    /** 账户id */
    private Long accountId;
    /** 接种信息 */
    private String content;

    public static VaccinesDTO build(Vaccines vaccines) {
        if(vaccines == null) {
            return null;
        }
        VaccinesDTO vaccinesDTO = new VaccinesDTO();
        BeanUtil.copyProperties(vaccines, vaccinesDTO);
        return vaccinesDTO;
    }
}
