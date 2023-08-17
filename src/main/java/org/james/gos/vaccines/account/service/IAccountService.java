package org.james.gos.vaccines.account.service;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.dto.AccountPageDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;

import java.util.List;

/**
 * 账户表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:23
 */
public interface IAccountService {

    /**
     * 账户登录
     *
     * @param loginReq 登录信息
     * @return 令牌信息
     */
    AccountResp login(LoginReq loginReq);

    /**
     * 退出登录
     *
     * @param aid 账户id
     */
    void logout(Long aid);

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
     * 获得所有账号信息 分页
     *
     * @param aid 账号id
     * @param pageBaseReq 分页信息
     * @return 账号信息
     */
    AccountPageDTO getAccountAll(Long aid, PageBaseReq pageBaseReq);

    /**
     * 增加更改账户
     *
     * @param accountReq 账户信息
     */
    void insertAndUpdate(Long aid, AccountReq accountReq);

    /**
     * 增加账户
     * @param account 账户信息
     */
    void insert(Account account);

    /**
     * 更新账户
     * @param account 账户信息
     */
    void update(Account account);

    /**
     * 删除账户
     * @param aid 操作人的id
     * @param id 删除的账户id
     */
    void deleted(Long aid, Long id);


    /**
     * 获取所有账户
     */
    List<Account> selectAll();

    /**
     * 根据用户名查询账户
     *
     * @param username 用户名
     * @return 账户信息
     */
    Account selectByUsername(String username);

    /**
     * 根据用户名/权限查询账户
     *
     * @param username 用户名
     * @param auth 权限
     * @return 账户信息
     */
    Account selectByUsername(String username, Integer auth);

    /**
     * 根据用户名查询账户 模糊
     *
     * @param username 用户名
     * @return 账户信息
     */
    List<Account>  selectByUsernameLike(String username);

    /**
     * 根据用户名/权限查询账户 模糊
     *
     * @param username 用户名
     * @param auth 权限
     * @return 账户信息
     */
    List<Account>  selectByUsernameLike(String username, Integer auth);

    /**
     * 根据id 查询账户
     *
     * @param id 账户ID
     * @return 账户信息
     */
    Account selectById(Long id);

    /**
     * 获取账户疫苗情况
     *
     * @param id 账户ID
     */
    List<AUVResp> auv(Long id);

    /**
     * 获取账户疫苗情况
     *
     * @param accountDTO 账户
     */
    AUVResp auv(AccountDTO accountDTO);

    /**
     * 获取账户用户信息
     *
     * @param username 账户名
     */
    List<AUResp> au(Long aid, String username);

    /**
     * 效验Token 是否有效
     *
     * @param token Token
     */
    boolean verify(String token);

    /**
     * 获取用户Id
     *
     * @return 用户Id
     */
    Long getAid(String token);

    /**
     * 添加好友请求
     *
     * @param aid 账户id
     * @param id 被添加id
     */
    void friend(Long aid, Long id);
}
