package fact.it.employeeservice.controller;

import fact.it.employeeservice.model.Employee;
import fact.it.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void fillDB(){
        if(employeeRepository.count()==0){
            employeeRepository.save(new Employee(1, "Arno","Vangoetsenhoven","E051448"));
            employeeRepository.save(new Employee(2, "Niel","Everaerts","E684572"));
            employeeRepository.save(new Employee(1, "Lloyd","Moons","E000215"));
            employeeRepository.save(new Employee(3, "Bram","Brabants","E551864"));
        }

        System.out.println(employeeRepository.findEmployeeByEmployeeNumber("E000215").getSurName());
    }

    @GetMapping("/employees/gardenCenterId/{gardenCenterId}")
    public List<Employee> getEmployeeByGardenCenterId(@PathVariable int gardenCenterId){
        return employeeRepository.findEmployeeByGardenCenterId(gardenCenterId);
    }

    @GetMapping("/employees/surName/{surName}")
    public Employee getEmployeeBySurName(@PathVariable String surName){
        return employeeRepository.findEmployeeBySurName(surName);
    }

    @GetMapping("/employees/name/{name}")
    public Employee getEmployeeByName(@PathVariable String name){
        return employeeRepository.findEmployeeByName(name);
    }

    @GetMapping("/employees/{employeeNumber}")
    public Employee getEmployeeByEmployeeNumber(@PathVariable String employeeNumber){
        return employeeRepository.findEmployeeByEmployeeNumber(employeeNumber);
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee){

        employeeRepository.save(employee);

        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee updatedEmployee){
        Employee retrievedEmployee = employeeRepository.findEmployeeByEmployeeNumber(updatedEmployee.getEmployeeNumber());

        retrievedEmployee.setName(updatedEmployee.getName());
        retrievedEmployee.setSurName(updatedEmployee.getSurName());
        retrievedEmployee.setGardenCenterId(updatedEmployee.getGardenCenterId());

        employeeRepository.save(retrievedEmployee);

        return retrievedEmployee;
    }

    @DeleteMapping("/employees/{employeeNumber}")
    public ResponseEntity deleteEmployee(@PathVariable String employeeNumber){
        Employee employee = employeeRepository.findEmployeeByEmployeeNumber(employeeNumber);
        if(employee!=null){
            employeeRepository.delete(employee);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
