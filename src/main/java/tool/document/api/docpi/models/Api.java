package tool.document.api.docpi.models;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
@Builder
public class Api {
    private String name;
    private String endpoint;
    private Method method;
    private String color;
    private String type;
    private String returnType;
    private List<Parameter> parameters;
    private String payload;
}
