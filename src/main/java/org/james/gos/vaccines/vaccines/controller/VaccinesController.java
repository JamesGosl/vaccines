package org.james.gos.vaccines.vaccines.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.PageBaseResp;
import org.james.gos.vaccines.common.doman.vo.response.ApiResult;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.james.gos.vaccines.vaccines.doman.vo.response.VAUResp;
import org.james.gos.vaccines.vaccines.doman.vo.response.VaccinesResp;
import org.james.gos.vaccines.vaccines.service.IVaccinesService;
import org.james.gos.vaccines.vaccines.service.apadter.VaccinesAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    @Autowired
    private IVaccinesService vaccinesService;

    @GetMapping
    @ApiOperation("获取疫苗信息")
    public ApiResult<VaccinesResp> getVaccines(@Valid IdReq idReq) {
        return ApiResult.success(VaccinesAdapter.build(vaccinesService.getVaccines(idReq.getId())));
    }

    @GetMapping("/vau")
    @ApiOperation("获取疫苗列表")
    public ApiResult<List<VAUResp>> vau(@Aid Long aid) {
        return ApiResult.success(vaccinesService.vau(aid));
    }

    @GetMapping("/vauPage")
    @ApiOperation("获取疫苗分页")
    @Deprecated
    public ApiResult<PageBaseResp<VAUResp>> vauPage(@Aid Long aid, @Valid PageBaseReq request) {
        return ApiResult.success(vaccinesService.vau(aid, request));
    }


    @PostMapping("/upload")
    @ApiOperation("上传疫苗信息")
    public ApiResult<Void> upload(@Aid Long aid, @Valid IdReq idReq, @RequestParam MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            byte[] bs = new byte[inputStream.available()];
            int read = inputStream.read(bs);
            String vaccines = new String(bs, StandardCharsets.UTF_8);
            vaccinesService.upload(aid, idReq, vaccines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.success();
    }

    @RequestMapping("download")
    public void download(@Aid Long aid, @Valid IdReq idReq, HttpServletResponse response) throws IOException {
        vaccinesService.download(aid, idReq, response);
    }
}
