package org.james.gos.vaccines.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.system.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/info")
    @ApiModelProperty("获取系统初始化文件")
    public String info() {
        return systemService.info(RequestHolder.get().getId());
    }

    @GetMapping("/clear")
    @ApiModelProperty("清除系统缓存")
    public ApiResult<Void> clear() {
        systemService.clear(RequestHolder.get().getId());
        return ApiResult.success();
    }
}
