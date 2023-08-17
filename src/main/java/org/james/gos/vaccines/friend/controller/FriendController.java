package org.james.gos.vaccines.friend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.friend.doman.vo.response.FAUResp;
import org.james.gos.vaccines.friend.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 好友信息 视图层
 *
 * @author James Gosl
 * @since 2023/08/15 19:19
 */
@RestController
@RequestMapping("/friend")
@Api(tags = "好友 相关接口")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    @PutMapping
    @ApiModelProperty("好友请求")
    public ApiResult<Void> insert(@Aid Long aid, @Valid @RequestBody IdReq idReq) {
        friendService.insert(aid, idReq.getId());
        return ApiResult.success();
    }

    @PostMapping
    @ApiModelProperty("更新好友")
    public ApiResult<Void> update(@Aid Long aid, @Valid @RequestBody IdReq idReq) {
        friendService.update(aid, idReq.getId());
        return ApiResult.success();
    }

    @GetMapping("/list")
    @ApiModelProperty("获取待处理好友请求")
    public ApiResult<List<FAUResp>> list(@Aid Long aid) {
        return ApiResult.success(friendService.list(aid));
    }
}
