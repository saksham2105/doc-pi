package tool.document.api.docpi.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class Controller {
    private String name;
    private List<Api> apis;
    private String documentation;
}
