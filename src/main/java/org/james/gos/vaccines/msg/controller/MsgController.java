package org.james.gos.vaccines.msg.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 19:20
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "消息 相关接口")
public class MsgController {
}
