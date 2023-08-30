package tool.document.api.docpi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import tool.document.api.docpi.annotations.Documentation;
import tool.document.api.docpi.controllers.DocpiRenderingController;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Data
public class Docket {
    private String basePackage = "tool";
    private String heading;
    private List<Controller> controllers;
    private List<Entity> entities;

    public Docket() {
        controllers = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public void init() throws Exception {
        populateControllers();
        populateEntities();
    }

    private void populateEntities() {
      List<Class> entityClasses = getEntityClasses();
      for (Class c : entityClasses) {
          Entity entity = Entity.builder().name(c.getName()).build();
          List<Field> columns = new ArrayList<>();
          for (Field field : c.getDeclaredFields()) {
              columns.add(field);
          }
          entity.setColumns(columns);
          entities.add(entity);
      }
    }

    private void populateControllers() throws Exception {
        try {
            List<Class> controllerClasses = getControllerClasses();
            Class docpiController = DocpiRenderingController.class;
            Map<String, String> borderColorMap = new HashMap<>();
            borderColorMap.put("GET", "#339CFF");
            borderColorMap.put("POST", "#4CAF50");
            borderColorMap.put("PUT", "#915FD6");
            borderColorMap.put("PATCH", "#FF5733");
            borderColorMap.put("DELETE", "#F20B16");

            for (Class c : controllerClasses) {
                if (c.getName().equals(docpiController.getName())) {
                    continue;
                }
                int lastDotIndex = c.getName().lastIndexOf(".");
                String controllerSimpleName = c.getName().substring(lastDotIndex + 1);
                Controller controller = Controller.builder().name(controllerSimpleName).build();
                if (c.isAnnotationPresent(Documentation.class)) {
                    Documentation annotation = (Documentation) c.getAnnotation(Documentation.class);
                    String value = annotation.value();
                    controller.setDocumentation(value);
                }
                Method[] methods = c.getMethods();
                List<Api> apis = new ArrayList<>();
                String endpointPrefix = "";
                if (c.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = (RequestMapping) c.getAnnotation(RequestMapping.class);
                    endpointPrefix = requestMapping.value()[0];
                }
                String endpointSuffix = "";
                boolean isApi = false;
                String type = "";
                String border = "";
                String payload = "";
                for (Method method : methods) {
                    Api api = Api.builder().build();
                    List<tool.document.api.docpi.models.Parameter> parameters = new ArrayList<>();
                    for (Parameter parameter : method.getParameters()) {
                        if (parameter.isAnnotationPresent(RequestParam.class)) {
                            tool.document.api.docpi.models.Parameter p = tool.document.api.docpi.models.Parameter.builder()
                                    .name(parameter.getName()).value("").build();
                            parameters.add(p);
                        }
                        if (parameter.isAnnotationPresent(RequestBody.class)) {
                            Class<?> parameterClass = Class.forName(parameter.getType().getName());
                            try {
                                Method builderMethod = parameterClass.getMethod("builder");
                                if (builderMethod != null) {
                                    Object builderInstance = builderMethod.invoke(null);
                                    Method buildMethod = builderInstance.getClass().getMethod("build");
                                    Object objectInstance = buildMethod != null ? buildMethod.invoke(builderInstance) : new Object();
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    payload = objectMapper.writeValueAsString(objectInstance);
                                }

                            } catch (Exception e) {
                                Constructor<?> constructor = parameterClass.getDeclaredConstructor();
                                Object objectInstance = constructor.newInstance();
                                ObjectMapper objectMapper = new ObjectMapper();
                                payload = objectMapper.writeValueAsString(objectInstance);
                            }
                        }
                    }
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        isApi = true;
                        type = RequestMethod.GET.name();
                        border = "#B0E0E6";
                        endpointSuffix = method.getAnnotation(GetMapping.class).value()[0];
                    }
                    else if (method.isAnnotationPresent(PostMapping.class)) {
                        isApi = true;
                        border = "#4CAF50";
                        type = RequestMethod.POST.name();
                        endpointSuffix = method.getAnnotation(PostMapping.class).value()[0];
                    }
                    else if (method.isAnnotationPresent(RequestMapping.class)) {
                        isApi = true;
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        type = requestMapping.method()[0].name();
                        border = borderColorMap.get(type);
                        endpointSuffix = method.getAnnotation(RequestMapping.class).value()[0];
                    }
                    else if (method.isAnnotationPresent(PutMapping.class)) {
                        type = RequestMethod.PUT.name();
                        border = "#FFA500";
                        isApi = true;
                        endpointSuffix = method.getAnnotation(PutMapping.class).value()[0];
                    }
                    else if (method.isAnnotationPresent(DeleteMapping.class)) {
                        border = "#FF6347";
                        type = RequestMethod.DELETE.name();
                        isApi = true;
                        endpointSuffix = method.getAnnotation(DeleteMapping.class).value()[0];
                    }
                    else if (method.isAnnotationPresent(PatchMapping.class)) {
                        type = RequestMethod.PATCH.name();
                        border = "#9400D3";
                        isApi = true;
                        endpointSuffix = method.getAnnotation(PatchMapping.class).value()[0];
                    }
                    if (isApi) {
                        api.setPayload(payload);
                        api.setColor(border);
                        api.setMethod(method);
                        api.setEndpoint(endpointPrefix + endpointSuffix);
                        api.setType(type);
                        api.setName(method.getName());
                        api.setParameters(parameters);
                        apis.add(api);
                        isApi = false;
                        payload = "";
                    }
                }
                controller.setApis(apis);
                controllers.add(controller);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Class> getControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RestController.class);

        List<Class> controllerClasses = new ArrayList<>();
        for (Class<?> clazz : classes) {
            controllerClasses.add(clazz);
        }
        return controllerClasses;
    }

    private List<Class> getEntityClasses() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

        List<Class> controllerClasses = new ArrayList<>();
        for (Class<?> clazz : classes) {
            controllerClasses.add(clazz);
        }
        return controllerClasses;
    }
}
