package tool.document.api.docpi.models;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
@Data
@Builder
public class Entity {
    private String name;
    private List<Field> columns;
}
