package tool.test.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Employee {
    private String code;
    private String name;
}
