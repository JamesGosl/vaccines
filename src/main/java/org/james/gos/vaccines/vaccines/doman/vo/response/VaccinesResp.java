package org.james.gos.vaccines.vaccines.doman.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 疫苗信息响应
 *
 * @author James Gosl
 * @since 2023/08/16 21:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("疫苗信息响应")
public class VaccinesResp {
    private String name;
    private String age;
    private String gender;
    private String brith;
    private String hist;
    private List<Vaccination> vaccinations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Vaccination {
        private String name;
        private String time;
        private String dose;
        private String Vaccine_batch_number;
        private String manufacturer;
        private String Inoculation_unit;
    }
}
