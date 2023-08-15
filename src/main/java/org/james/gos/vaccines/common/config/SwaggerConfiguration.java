package org.james.gos.vaccines.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger 相关配置
 *
 * @author James Gosl
 * @since 2023/08/14 23:12
 */
@EnableSwagger2WebMvc
@SpringBootConfiguration
public class SwaggerConfiguration {

    @Bean(value = "defaultApi2")
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 配置网站的基本信息
                .apiInfo(new ApiInfoBuilder()
                        .title("Vaccines 接口文档")
                        .version("v1.0")
                        .description("Vaccines 接口文档")
                        .contact(new Contact("James Gos", "http://127.0.0.1", "JamesGos@163.com"))
                        .build())
                .select()
                // 指定接口的位置
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }










//    /**
//     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
//     **/
//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
//    }
//
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || Objects.equals(ManagementPortType.get(environment), ManagementPortType.DIFFERENT));
//    }
}
