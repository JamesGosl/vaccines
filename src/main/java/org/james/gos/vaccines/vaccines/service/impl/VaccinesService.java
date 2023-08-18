package org.james.gos.vaccines.vaccines.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.PageBaseResp;
import org.james.gos.vaccines.common.exception.AccountErrorEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.exception.CommonErrorEnum;
import org.james.gos.vaccines.common.exception.InsertRuntimeException;
import org.james.gos.vaccines.friend.doman.dto.FriendDTO;
import org.james.gos.vaccines.friend.service.IFriendService;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.service.IUserService;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.vaccines.doman.vo.response.VAUResp;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private IFriendService friendService;
    @Autowired
    private IUserService userService;

    @Override
    public void insertVaccines(Long aid) {
        if (YesOrNoEnum.NO.equals(vaccinesCache.inVaccines(VaccinesAdapter.build(aid)))) {
            throw new InsertRuntimeException(CommonErrorEnum.SYSTEM_ERROR);
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
    public void upload(Long aid, IdReq idReq, String vaccines) {
        // 校验账户
        VaccinesDTO vaccinesDTO = getVaccines(idReq.getId());
        if (vaccinesDTO.getAccountId().equals(aid) ||
                AuthEnum.of(accountService.getAccount(aid).getAuth()).equals(AuthEnum.ADMIN)) {
            if (YesOrNoEnum.NO.equals(vaccinesCache.upVaccines(VaccinesAdapter.build(idReq.getId(), vaccines)))) {
                throw new RuntimeException("更新失败");
            }
        } else {
            throw new AccountRuntimeException(AccountErrorEnum.NOT_AUTH);
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
            throw new AccountRuntimeException(AccountErrorEnum.NOT_AUTH);
        }
    }

    @Override
    public List<VAUResp> vau(Long aid) {
        // 获取账户
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        switch (auth) {
            // admin 获取所有信息
            case ADMIN:
                List<AccountDTO> accountAll = accountService.getAccountAll(aid);
                return accountAll.stream()
                        // 只查询用户
                        .filter(accountDTO -> AuthEnum.USER.equals(AuthEnum.of(accountDTO.getAuth())))
                        .map(this::vau).collect(Collectors.toList());
            // doctor、user 获取关联的
            case DOCTOR:
            case USER:
                // 关系表
                List<FriendDTO> friends = friendService.getFriends(aid);
                // 关系表中的对应去查询
                List<VAUResp> vauResps = new ArrayList<>(friends.size());
                // 不能查询到医生的
                for (FriendDTO friend : friends) {
                    AccountDTO accountDTO = accountService.getAccount(friend.getFriendId());
                    // 不能查询医生
                    if(auth.equals(AuthEnum.USER) && AuthEnum.of(accountDTO.getAuth()).equals(AuthEnum.DOCTOR)) {
                        continue;
                    }
                    vauResps.add(vau(accountDTO));
                }
                // 查询自己的
                if (auth.equals(AuthEnum.USER)) {
                    vauResps.add(vau(account));
                }
                return vauResps;
        }

        return Collections.emptyList();
    }

    @Override
    public PageBaseResp<VAUResp> vau(Long aid, PageBaseReq request) {
        // 获取账户
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        switch (auth) {
            // admin 获取所有信息
            case ADMIN:
//                Page<User> pageUser = PageMethod.startPage(request.getPage(), request.getLimit());
                List<AccountDTO> accountAll = accountService.getAccountAll(aid);

//                return accountAll.stream().map(this::vau).collect(Collectors.toList());
            // doctor、user 获取关联的
            case DOCTOR:
            case USER:
                // 关系表
                List<FriendDTO> friends = friendService.getFriends(aid);
                // 关系表中的对应去查询
                List<VAUResp> vauResps = new ArrayList<>(friends.size());
                // 不能查询到医生的
                for (FriendDTO friend : friends) {
                    AccountDTO accountDTO = accountService.getAccount(friend.getFriendId());
                    // 不能查询医生
                    if(auth.equals(AuthEnum.USER) && AuthEnum.of(accountDTO.getAuth()).equals(AuthEnum.DOCTOR)) {
                        continue;
                    }
                    vauResps.add(vau(accountDTO));
                }
                // 查询自己的
                if (auth.equals(AuthEnum.USER)) {
                    vauResps.add(vau(account));
                }
//                return vauResps;
        }

        return PageBaseResp.empty();
    }

    public VAUResp vau(AccountDTO accountDTO) {
        // 账户信息
        Long aid = accountDTO.getId();
        // 用户信息
        UserDTO user = userService.getUser(aid);
        // 疫苗信息
        VaccinesDTO vaccines = getVaccinesByAid(aid);
        // 聚合
        return VaccinesAdapter.buildAUV(accountDTO, user, vaccines);
    }
}
