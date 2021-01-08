package fact.it.employeeservice.controller;

import fact.it.employeeservice.model.Employee;
import fact.it.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
            employeeRepository.save(new Employee(1, "Niel","Everaerts","E684572"));
            employeeRepository.save(new Employee(1, "Lloyd","Moons","E000215"));
            employeeRepository.save(new Employee(1, "Bram","Brabants","E551864"));
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
}
