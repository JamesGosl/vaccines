package org.james.gos.vaccines.vaccines.mapper;

import org.james.gos.vaccines.vaccines.doman.entity.Vaccines;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 疫苗表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:06
 */
@Repository
public interface VaccinesMapper extends Mapper<Vaccines> {
}
