package org.james.gos.vaccines.account.service;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.PageBaseResp;

import java.util.List;

/**
 * 账户表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:23
 */
public interface IAccountService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 成功则返回失败报错
     */
    AccountDTO login(String username, String password);

    /**
     * 获得账号信息
     *
     * @param aid 账号id
     * @return 账号信息
     */
    AccountDTO getAccount(Long aid);

    /**
     * 效验权限
     * @param aid id
     */
    AccountDTO validate(Long aid);

    /**
     * 获得所有账号信息
     *
     * @param aid 账号id
     * @return 账号信息
     */
    List<AccountDTO> getAccountAll(Long aid);

    /**
     * 获得所有账号信息
     *
     * @param aid 账号id
     * @param request 分页请求
     * @return 账号信息
     */
    PageBaseResp<AccountDTO> getAccountPage(Long aid, PageBaseReq request);

    /**
     * 增加更改账户
     *
     * @param accountReq 账户信息
     */
    void insertAndUpdate(Long aid, AccountReq accountReq);

    /**
     * 删除账户
     * @param aid 操作人的id
     * @param id 删除的账户id
     */
    void deleted(Long aid, Long id);

    /**
     * 获取账户用户信息
     *
     * @param username 账户名
     */
    List<AUResp> au(Long aid, String username);


    /**
     * 添加好友请求
     *
     * @param aid 账户id
     * @param id 被添加id
     */
    void friend(Long aid, Long id);
}
