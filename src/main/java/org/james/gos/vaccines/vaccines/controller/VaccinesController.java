package org.james.gos.vaccines.vaccines.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 疫苗信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 19:22
 */
@RestController
@RequestMapping("/vaccines")
@Api(tags = "疫苗 相关接口")
public class VaccinesController {
}
