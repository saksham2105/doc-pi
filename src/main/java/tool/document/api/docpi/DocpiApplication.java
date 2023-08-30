package tool.document.api.docpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tool.document.api.docpi.models.Docket;

@SpringBootApplication
@ComponentScan(basePackages = {"tool.test.controller", "tool.document.api.docpi"})
public class DocpiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocpiApplication.class, args);
	}

	@Bean
	public Docket initializedBean() throws Exception {
		Docket docket = new Docket();
		docket.setBasePackage("tool");
		docket.setHeading("Open API Documentation - Docpi");
		docket.init();
		return docket;
	}

}
