package org.james.gos.vaccines.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.auth.service.IAuthService;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 权限信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 16:45
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "权限 相关接口")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @GetMapping("/list")
    @ApiOperation("获取所有权限信息")
    public ApiResult<List<AuthResp>> getAuthAll() {
        return ApiResult.success(authService.getAuthAll());
    }

    @GetMapping
    @ApiOperation("根据ID获取权限信息")
    public ApiResult<AuthResp> getAuth(@Valid IdReq idReq) {
        return ApiResult.success(authService.getAuth(idReq.getId()));
    }
}
