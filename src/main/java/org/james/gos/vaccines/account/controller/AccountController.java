package org.james.gos.vaccines.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.UsernameReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.account.service.adapter.AccountAdapter;
import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.request.IdsReq;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    @ApiOperation("获取当前账号信息")
    public ApiResult<AccountResp> getAccount(@Aid Long aid) {
        return ApiResult.success(AccountAdapter.buildAccountResp(accountService.getAccount(aid)));
    }

    @GetMapping("/list")
    @ApiOperation("获取所有账号信息")
    public ApiResult<List<AccountResp>> getAccountAll(@Aid Long aid) {
        return ApiResult.success(AccountAdapter.buildAccountResp(accountService.getAccountAll(aid)));
    }

    @PostMapping
    @ApiOperation("添加或修改账号")
    public ApiResult<Void> insertAndUpdate(@Aid Long aid, @Valid @RequestBody AccountReq accountReq) {
        accountService.insertAndUpdate(aid, accountReq);
        return ApiResult.success();
    }

    @DeleteMapping
    @ApiOperation("删除账号")
    public ApiResult<Void> delete(@Aid Long aid, @Valid IdReq idReq) {
        accountService.deleted(aid, idReq.getId());
        return ApiResult.success();
    }

    @DeleteMapping("/ids")
    @ApiOperation("批量删除账号")
    public ApiResult<Void> delete(@Aid Long aid, @Valid IdsReq idReqs) {
        for (Long id : idReqs.getId()) {
            accountService.deleted(aid, id);
        }
        return ApiResult.success();
    }

    @GetMapping("/au")
    @ApiOperation("查找账户")
    public ApiResult<List<AUResp>> au(@Aid Long aid, @Valid UsernameReq usernameReq) {
        return ApiResult.success(accountService.au(aid, usernameReq.getUsername()));
    }
}
