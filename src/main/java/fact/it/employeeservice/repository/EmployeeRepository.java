package fact.it.employeeservice.repository;

import fact.it.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findEmployeeBySurName(String surName);
    Employee findEmployeeByName(String name);
    List<Employee> findEmployeeByGardenCenterId(int gardenCenterId);
    Employee findEmployeeByEmployeeNumber(String employeeNumber);
}
