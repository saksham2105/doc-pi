package tool.test.controller;

import org.springframework.web.bind.annotation.*;
import tool.document.api.docpi.annotations.Documentation;
import tool.test.model.Employee;

import java.util.HashMap;
import java.util.Map;

@RestController
@Documentation(value = "Employee Test Controller")
@RequestMapping("/employee")
public class EmployeeController {

    private Map<String, Employee> employeeMap = new HashMap<>();

    @GetMapping("/testGetApi")
    public Employee testGetApi(@RequestParam String code, @RequestParam String name) {
        return Employee.builder().code(code).name(name).build();
    }

    @PostMapping("/testPostApi")
    public String testPostApi(@RequestBody Employee employee, @RequestParam String code) {return "Test Post Api";}

    @DeleteMapping("/testDeleteMapping")
    public String testDeleteMapping() {return "Test Delete Api";}

    @PatchMapping("/testPatchMapping")
    public String testPatchMapping() {return "Test Patch Api";}
    @PutMapping("/testPutMapping")
    public String testPutMapping() {return "Test Put Api";}

    @PostMapping("/saveEmployee")
    public String saveEmployee(@RequestBody Employee employee) {
        employeeMap.put(employee.getCode(), employee);
        return "Employee Saved";
    }

    @GetMapping("/getEmployee")
    public Object getEmployee(@RequestParam String code) {
        if (employeeMap.containsKey(code)) {
            return employeeMap.get(code);
        } else {
            return "Employee Not Found";
        }
    }

}
