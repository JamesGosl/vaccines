package org.james.gos.vaccines.vaccines.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.exception.InsertRuntimeException;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.vaccines.mapper.VaccinesMapper;
import org.james.gos.vaccines.vaccines.service.IVaccinesService;
import org.james.gos.vaccines.vaccines.service.apadter.VaccinesAdapter;
import org.james.gos.vaccines.vaccines.service.cache.VaccinesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * VaccinesService
 *
 * @author James Gosl
 * @since 2023/08/16 21:46
 */
@Service
public class VaccinesService implements IVaccinesService {
    @Resource
    private VaccinesMapper vaccinesMapper;
    @Autowired
    private VaccinesCache vaccinesCache;
    @Autowired
    private IAccountService accountService;

    @Override
    public void insertVaccines(Long aid) {
        if (vaccinesMapper.insertSelective(VaccinesAdapter.build(aid)) == YesOrNoEnum.NO.getStatus()) {
            throw new InsertRuntimeException("插入疫苗信息失败-" + aid);
        }
    }

    @Override
    public VaccinesDTO getVaccines(Long id) {
        return vaccinesCache.getVaccines(id);
    }

    @Override
    public VaccinesDTO getVaccinesByAid(Long aid) {
        return vaccinesCache.getVaccinesByAid(aid);
    }

    @Override
    public AUVResp getVaccinesAll(Long aid) {
        return null;
    }

    @Override
    public void upload(Long aid, IdReq idReq, String vaccines) {
        // 校验账户
        VaccinesDTO vaccinesDTO = getVaccines(idReq.getId());
        if (vaccinesDTO.getAccountId().equals(aid) ||
                AuthEnum.of(accountService.getAccount(aid).getAuth()).equals(AuthEnum.ADMIN)) {
            vaccinesCache.upload(VaccinesAdapter.build(idReq.getId(), vaccines));
        } else {
            throw new AccountRuntimeException("权限不足");
        }
    }

    @Override
    public void download(Long aid, IdReq idReq, HttpServletResponse response) {
        // 校验账户
        VaccinesDTO vaccinesDTO = getVaccines(idReq.getId());
        if (vaccinesDTO.getAccountId().equals(aid) ||
                AuthEnum.of(accountService.getAccount(aid).getAuth()).equals(AuthEnum.ADMIN)) {
            try {
                response.reset();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(UUID.randomUUID() + ".xml", "UTF-8"));
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(getVaccines(idReq.getId()).getContent().getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
            }
        } else {
            throw new AccountRuntimeException("权限不足");
        }
    }
}
