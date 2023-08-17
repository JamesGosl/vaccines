package org.james.gos.vaccines.vaccines.service.apadter;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.vaccines.doman.entity.Vaccines;
import org.james.gos.vaccines.vaccines.doman.vo.response.VAUResp;
import org.james.gos.vaccines.vaccines.doman.vo.response.VaccinesResp;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * VaccinesAdapter
 *
 * @author James Gosl
 * @since 2023/08/16 22:00
 */
@Slf4j
public class VaccinesAdapter {
    private static SAXReader saxReader;

    static {
        VaccinesAdapter.saxReader = SpringUtil.getBean(SAXReader.class);
    }

    public static Vaccines build(Long id, String content) {
        Vaccines vaccines = new Vaccines();
        vaccines.setId(id);
        vaccines.setContent(content);
        return vaccines;
    }

    public static Vaccines build(Long aid) {
        Vaccines vaccines = new Vaccines();
        vaccines.setAccountId(aid);
        return vaccines;
    }

    public static VaccinesResp build(VaccinesDTO vaccinesDTO) {
        if (vaccinesDTO == null) {
            return null;
        }
        try {
            Document read = saxReader.read(new ByteArrayInputStream(vaccinesDTO.getContent().getBytes(StandardCharsets.UTF_8)));
            Element rootElement = read.getRootElement();
            String name = rootElement.element("name").getStringValue();
            String age = rootElement.element("age").getStringValue();
            String gender = rootElement.element("gender").getStringValue();
            String brith = rootElement.element("brith").getStringValue();
            String hist = rootElement.element("hist").getStringValue();

            List<VaccinesResp.Vaccination> vaccinationList = new ArrayList<>();
            for (Element element : read.getRootElement().element("Vaccinations").elements("Vaccination")) {
                String Vaccinations_name = element.element("name").getStringValue();
                String time = element.element("time").getStringValue();
                String dose = element.element("dose").getStringValue();
                String Vaccine_batch_number = element.element("Vaccine_batch_number").getStringValue();
                String manufacturer = element.element("manufacturer").getStringValue();
                String Inoculation_unit = element.element("Inoculation_unit").getStringValue();
                vaccinationList.add(new VaccinesResp.Vaccination(Vaccinations_name, time, dose, Vaccine_batch_number, manufacturer, Inoculation_unit));
            }
            return new VaccinesResp(name, age, gender, brith, hist, vaccinationList);
        } catch (DocumentException e) {
            log.error("解析错误->{}", vaccinesDTO.getId());
            return null;
        }
    }

    public static VAUResp buildAUV(AccountDTO accountDTO, UserResp user, VaccinesDTO vaccines) {
        VAUResp vauResp = new VAUResp();
        if(vaccines != null) {
            vauResp.setId(vaccines.getId());
            if (vaccines.getContent() != null) {
                vauResp.setState(YesOrNoEnum.YES.getStatus());
            } else {
                vauResp.setState(YesOrNoEnum.NO.getStatus());
            }
        }
        vauResp.setUsername(accountDTO.getUsername());
        vauResp.setName(user.getName());
        return vauResp;
    }
}
