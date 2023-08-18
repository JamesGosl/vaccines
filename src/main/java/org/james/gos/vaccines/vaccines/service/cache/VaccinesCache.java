package org.james.gos.vaccines.vaccines.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.event.RedisApplicationEvent;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.james.gos.vaccines.common.event.RedisVaccinesApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.vaccines.doman.entity.Vaccines;
import org.james.gos.vaccines.vaccines.mapper.VaccinesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Objects;

/**
 * VaccinesCache
 *
 * @author James Gosl
 * @since 2023/08/16 21:47
 */
@Component
@Slf4j
public class VaccinesCache implements ApplicationListener<RedisApplicationEvent<?>> {

    @Autowired
    private VaccinesMapper vaccinesMapper;

    /**
     * 获取疫苗信息 热点数据
     *
     * @param aid 账户ID
     */
    @Cacheable(cacheNames = CacheKey.VACCINES, key = "'vaccines-aid-' + #aid")
    public VaccinesDTO getVaccinesByAid(Long aid) {
        VaccinesDTO vaccinesDTO = RedisUtils.keysOne(RedisKey.getVaccinesAid(aid), VaccinesDTO.class);

        if(Objects.isNull(vaccinesDTO)) {
            Example example = new Example(Vaccines.class);
            example.createCriteria().andEqualTo("accountId", aid);
            vaccinesDTO = VaccinesDTO.build(vaccinesMapper.selectOneByExample(example));
            if (Objects.nonNull(vaccinesDTO)) {
                RedisUtils.set(RedisKey.getVaccines(vaccinesDTO.getId(), vaccinesDTO.getAccountId()), vaccinesDTO);
            }
        }

        return vaccinesDTO;
    }

    /**
     * 获取疫苗信息
     *
     * @param id 疫苗ID
     */
    @Cacheable(cacheNames = CacheKey.VACCINES, key = "'vaccines-id-' + #id")
    public VaccinesDTO getVaccines(Long id) {
        VaccinesDTO vaccinesDTO = RedisUtils.keysOne(RedisKey.getVaccines(id), VaccinesDTO.class);

        if(Objects.isNull(vaccinesDTO)) {
            vaccinesDTO = VaccinesDTO.build(vaccinesMapper.selectByPrimaryKey(id));
            if(Objects.nonNull(vaccinesDTO)) {
                RedisUtils.set(RedisKey.getVaccines(vaccinesDTO.getId(), vaccinesDTO.getAccountId()), vaccinesDTO);
            }
        }

        return vaccinesDTO;
    }

    /**
     * 更新疫苗信息
     *
     * @param vaccines 疫苗信息
     */
    public YesOrNoEnum inVaccines(Vaccines vaccines) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(vaccinesMapper.insertSelective(vaccines));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            VaccinesDTO vaccinesDTO = VaccinesDTO.build(vaccines);
            // 加入缓存
            if (RedisUtils.set(RedisKey.getVaccines(vaccinesDTO.getId(), vaccinesDTO.getAccountId()), vaccinesDTO)) {
                // 发布事件
                RedisUtils.publish(RedisChannelEnum.VACCINES, vaccinesDTO);
            } else {
                // TODO 补偿策略
                log.debug("更新疫苗信息缓存失败");
            }
        }
        return yesOrNo;
    }

    /**
     * 更新疫苗信息
     *
     * @param vaccines 疫苗信息
     */
    @CacheEvict(cacheNames = CacheKey.VACCINES, key = "'vaccines-aid-' + #vaccines.accountId")
    public YesOrNoEnum upVaccines(Vaccines vaccines) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(vaccinesMapper.updateByPrimaryKeySelective(vaccines));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            VaccinesDTO vaccinesDTO = VaccinesDTO.build(vaccines);
            // 更新缓存
            if (RedisUtils.set(RedisKey.getVaccines(vaccinesDTO.getId(), vaccinesDTO.getAccountId()), vaccinesDTO)) {
                // 发布事件
                RedisUtils.publish(RedisChannelEnum.VACCINES, vaccinesDTO);
            } else {
                // 补偿策略
            }
        }
        return yesOrNo;
    }

    @Override
    public void onApplicationEvent(RedisApplicationEvent event) {
        if(event instanceof RedisClearApplicationEvent) {
            CacheUtils.del(CacheKey.VACCINES);
        }
        else if(event instanceof RedisVaccinesApplicationEvent) {
            VaccinesDTO vaccines = ((RedisVaccinesApplicationEvent) event).getValue();
            CacheUtils.del(CacheKey.VACCINES, "vaccines-id-" + vaccines.getId());
            CacheUtils.del(CacheKey.VACCINES, "vaccines-aid-" + vaccines.getAccountId());
        }
    }
}
