package org.james.gos.vaccines.system.controller;

import io.swagger.annotations.Api;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.system.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统 相关接口
 *
 * @author James Gosl
 * @since 2023/08/16 15:46
 */
@RestController
@Api(tags = "系统相关接口")
public class SystemController {

    @Autowired
    private ISystemService systemService;

    @GetMapping("/info")
    public String info() {
        return systemService.info(RequestHolder.get().getId());
    }
}
