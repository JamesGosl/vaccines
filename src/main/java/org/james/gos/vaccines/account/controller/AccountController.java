package org.james.gos.vaccines.account.controller;

import io.swagger.annotations.Api;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 账户信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 19:18
 */
@RestController
@RequestMapping("/account")
@Api(tags = "账户 相关接口")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping("/login")
    public ApiResult<AccountResp> login(@Valid @RequestBody LoginReq loginReq) {
        return ApiResult.success(accountService.login(loginReq));
    }
}
