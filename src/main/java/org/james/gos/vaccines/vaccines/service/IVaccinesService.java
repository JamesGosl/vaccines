package org.james.gos.vaccines.vaccines.service;

import org.james.gos.vaccines.common.doman.vo.request.IdReq;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.vaccines.doman.vo.response.VAUResp;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 疫苗表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:24
 */
public interface IVaccinesService {

    /**
     * 增加疫苗信息
     *
     * @param aid 账户ID
     */
    void insertVaccines(Long aid);

    /**
     * 根据账户id 查询疫苗信息
     *
     * @param id 疫苗id
     * @return 疫苗信息
     */
    VaccinesDTO getVaccines(Long id);


    /**
     * 根据账户id 查询疫苗信息
     *
     * @param aid 账户id
     * @return 疫苗信息
     */
    VaccinesDTO getVaccinesByAid(Long aid);


    /**
     * 根据账户Id 查询所有疫苗信息
     *
     * @param aid 账户id
     */
    AUVResp getVaccinesAll(Long aid);

    /**
     * 上传疫苗信息
     *
     * @param aid 账户id
     * @param idReq 疫苗id
     * @param vaccines 疫苗信息
     */
    void upload(Long aid, IdReq idReq, String vaccines);

    /**
     * 下载疫苗信息
     *
     * @param aid 账户id
     * @param idReq 疫苗id
     * @param response 响应
     */
    void download(Long aid, IdReq idReq, HttpServletResponse response);

    /**
     * 疫苗信息集合
     *
     * @param aid 账户id
     */
    List<VAUResp> vau(Long aid);
}
