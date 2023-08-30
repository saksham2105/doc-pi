package tool.document.api.docpi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import tool.document.api.docpi.models.Docket;

import java.util.Map;

@RestController
@RequestMapping("/docpi-ui")
public class DocpiRenderingController {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/index.html")
    public String generateHtmlTemplate() {
        Map<String, ?> docketBeans = applicationContext.getBeansOfType(Docket.class);
        Docket docket = null;
        for (Object o : docketBeans.values()) {
            if (o instanceof Docket) {
                docket = (Docket) o;
                break;
            }
        }
        Context context = new Context();
        context.setVariable("heading", docket.getHeading());
        context.setVariable("controllers", docket.getControllers());

        return templateEngine.process("index", context);
    }
}
