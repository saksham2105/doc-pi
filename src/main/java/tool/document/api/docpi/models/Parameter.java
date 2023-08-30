package tool.document.api.docpi.models;

import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class Parameter {
    private String name;
    private String value;
    private List<String> defaultValues;
}
