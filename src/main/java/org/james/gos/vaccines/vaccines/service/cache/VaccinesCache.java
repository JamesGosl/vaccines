package org.james.gos.vaccines.vaccines.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.vaccines.doman.entity.Vaccines;
import org.james.gos.vaccines.vaccines.mapper.VaccinesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

/**
 * VaccinesCache
 *
 * @author James Gosl
 * @since 2023/08/16 21:47
 */
@Component
@Slf4j
public class VaccinesCache implements ApplicationListener<ClearCacheApplicationEvent> {

    @Autowired
    private VaccinesMapper vaccinesMapper;

    /**
     * 获取疫苗信息
     *
     * @param aid 账户ID
     */
    @Cacheable(cacheNames = CacheKey.VACCINES, key = "'vaccines-aid-' + #aid")
    public VaccinesDTO getVaccinesByAid(Long aid) {
        Example example = new Example(Vaccines.class);
        example.createCriteria().andEqualTo("accountId", aid);
        return VaccinesDTO.build(vaccinesMapper.selectOneByExample(example));
    }

    /**
     * 获取疫苗信息
     *
     * @param id 疫苗ID
     */
    @Cacheable(cacheNames = CacheKey.VACCINES, key = "'vaccines-' + #id")
    public VaccinesDTO getVaccines(Long id) {
        return VaccinesDTO.build(vaccinesMapper.selectByPrimaryKey(id));
    }

    /**
     * 更新疫苗信息
     *
     * @param vaccines 疫苗信息
     */
    public void upload(Vaccines vaccines) {
        if (vaccinesMapper.updateByPrimaryKeySelective(vaccines) == YesOrNoEnum.YES.getStatus()) {
            // 清除缓存
            CacheUtils.del(CacheKey.VACCINES);
        }
    }

    @Override
    public void onApplicationEvent(ClearCacheApplicationEvent event) {
        Object source = event.getSource();
        log.debug("清除缓存 -> {}", source.toString());

        CacheUtils.del(CacheKey.VACCINES);
    }
}
