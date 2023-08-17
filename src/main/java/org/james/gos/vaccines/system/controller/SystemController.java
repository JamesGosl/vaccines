package org.james.gos.vaccines.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.system.domain.vo.request.LoginReq;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.system.domain.vo.response.LoginResp;
import org.james.gos.vaccines.system.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统 相关接口
 *
 * @author James Gosl
 * @since 2023/08/16 15:46
 */
@RestController
@RequestMapping("/system")
@Api(tags = "系统相关接口")
public class SystemController {

    @Autowired
    private ISystemService systemService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ApiResult<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
        return ApiResult.success(systemService.login(loginReq.getUsername(), loginReq.getPassword()));
    }

    @GetMapping("/logout")
    @ApiOperation("退出")
    public ApiResult<Void> logout() {
        return ApiResult.success();
    }

    @GetMapping("/info")
    @ApiOperation("获取系统初始化文件")
    public String info(@Aid Long aid) {
        return systemService.info(aid);
    }

    @GetMapping("/clear")
    @ApiOperation("清除系统缓存")
    public ApiResult<Void> clear(@Aid Long aid) {
        systemService.clear(aid);
        return ApiResult.success();
    }
}
