package org.james.gos.vaccines.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.user.doman.vo.request.UserReq;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 19:21
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户 相关接口")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @ApiOperation("获取当前用户信息")
    public ApiResult<UserResp> getUser(@Aid Long aid) {
        return ApiResult.success(userService.getUser(aid));
    }

    @PostMapping
    @ApiOperation("更新当前用户信息")
    public ApiResult<Void> updateUser(@Aid Long aid, @Valid @RequestBody UserReq userReq) {
        userService.updateUser(aid, userReq);
        return ApiResult.success();
    }
}
