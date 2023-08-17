package org.james.gos.vaccines.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.account.doman.dto.AccountPageDTO;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.system.domain.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.request.UsernameReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.account.service.adapter.AccountAdapter;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.request.IdsReq;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.ApiPageResult;
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
    public ApiResult<AccountResp> getAccount() {
        return ApiResult.success(AccountAdapter.buildAccountResp(accountService.getAccount(RequestHolder.get().getId())));
    }

    @GetMapping("/list")
    @ApiOperation("获取所有账号信息")
    public ApiResult<List<AccountResp>> getAccountAll() {
        return ApiResult.success(AccountAdapter.buildAccountResp(accountService.getAccountAll(RequestHolder.get().getId())));
    }

    @PostMapping
    @ApiOperation("添加或修改账号")
    public ApiResult<Void> insertAndUpdate(@Valid @RequestBody AccountReq accountReq) {
        accountService.insertAndUpdate(RequestHolder.get().getId(), accountReq);
        return ApiResult.success();
    }

    @DeleteMapping
    @ApiOperation("删除账号")
    public ApiResult<Void> delete(@Valid IdReq idReq) {
        accountService.deleted(RequestHolder.get().getId(), idReq.getId());
        return ApiResult.success();
    }

    @DeleteMapping("/ids")
    @ApiOperation("批量删除账号")
    public ApiResult<Void> delete(@Valid IdsReq idReqs) {
        for (Long id : idReqs.getId()) {
            accountService.deleted(RequestHolder.get().getId(), id);
        }
        return ApiResult.success();
    }

    @GetMapping("/au")
    @ApiOperation("查找账户")
    public ApiResult<List<AUResp>> au(@Valid UsernameReq usernameReq) {
        return ApiResult.success(accountService.au(RequestHolder.get().getId(), usernameReq.getUsername()));
    }
}
