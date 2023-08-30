package tool.test.controller;

import org.springframework.web.bind.annotation.*;
import tool.document.api.docpi.annotations.Documentation;
import tool.test.model.Student;

@RestController
@Documentation(value = "Test Rest Controller")
@RequestMapping("/test2")
public class TestCloneController {

    @GetMapping("/testAadharApi")
    public String testAadharApi(@RequestParam String code, @RequestParam String name, @RequestParam String aadharCard) {
        return "Aadhar card is : " + aadharCard;
    }

    @GetMapping("/testGetApi")
    public String testGetApi() {return "Test Get Api";}

    @PostMapping("/testPostApi")
    public String testPostApi() {return "Test Post Api";}

    @DeleteMapping("/testDeleteMapping")
    public String testDeleteMapping() {return "Test Delete Api";}

    @PatchMapping("/testPatchMapping")
    public String testPatchMapping() {return "Test Patch Api";}
    @PutMapping("/testPutMapping")
    public String testPutMapping() {return "Test Put Api";}

    @PostMapping("/student")
    public String testStudent(@RequestBody Student student) {
        return "Student is : " + student.getFirstName() +"-"+student.getLastName();
    }

    @PostMapping("/string")
    public String testString(@RequestBody String string) {
        return "String is : " + string;
    }

}
