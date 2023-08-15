package org.james.gos.vaccines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

//@ComponentScans({
//		@ComponentScan(
//				basePackages = "org.james.gos.vaccines"
////				includeFilters = {
////						@ComponentScan.Filter(Configuration.class),
////						@ComponentScan.Filter(Controller.class),
////						@ComponentScan.Filter(Service.class),
////						@ComponentScan.Filter(Component.class),
////				}
//		)
//})
//@SpringBootConfiguration
//@EnableAutoConfiguration
@SpringBootApplication
public class VaccinesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinesApplication.class, args);
	}
}
