package org.james.gos.vaccines.common.plugins;

import org.james.gos.vaccines.common.utils.IdWorkerUtils;
import tk.mybatis.mapper.genid.GenId;

/**
 * 通用Mapper ID 生成策略
 *
 * @author James Gosl
 * @since 2023/08/16 11:02
 */
public class IdWorker implements GenId<Long> {

    @Override
    public Long genId(String table, String column) {
        return IdWorkerUtils.getId();
    }
}
