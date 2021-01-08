package fact.it.employeeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.employeeservice.model.Employee;
import fact.it.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1 = new Employee(1, "Arno","Vangoetsenhoven","E051448");
    private Employee employee2 = new Employee(2, "Niel","Everaerts","E684572");
    private Employee employee3 = new Employee(1, "Lloyd","Moons","E000215");
    private Employee employeeToBeDeleted = new Employee(3, "Bram","Brabants","E551864");

    @BeforeEach
    public void beforeAllTests() {
        employeeRepository.deleteAll();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.save(employeeToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        employeeRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenEmployee_whenGetEmployeeByEmployeeNumber_thenReturnJsonEmployee() throws Exception {

        mockMvc.perform(get("/employees/{employeeNumber}", "E000215"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gardenCenterId", is(1)))
                .andExpect(jsonPath("$.surName", is("Lloyd")))
                .andExpect(jsonPath("$.name", is("Moons")))
                .andExpect(jsonPath("$.employeeNumber", is("E000215")));
    }

    @Test
    public void givenEmployee_whenGetEmployeeByGardenCenterId_thenReturnJsonEmployees() throws Exception {

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee3);

        mockMvc.perform(get("/employees/gardenCenterId/{gardenCenterId}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gardenCenterId", is(1)))
                .andExpect(jsonPath("$[0].surName", is("Arno")))
                .andExpect(jsonPath("$[0].name", is("Vangoetsenhoven")))
                .andExpect(jsonPath("$[0].employeeNumber", is("E051448")))
                .andExpect(jsonPath("$[1].gardenCenterId", is(1)))
                .andExpect(jsonPath("$[1].surName", is("Lloyd")))
                .andExpect(jsonPath("$[1].name", is("Moons")))
                .andExpect(jsonPath("$[1].employeeNumber", is("E000215")));
    }

    @Test
    public void givenEmployee_whenGetEmployeeBySurName_thenReturnJsonEmployees() throws Exception {

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee2);

        mockMvc.perform(get("/employees/surName/{surName}", "Niel"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gardenCenterId", is(2)))
                .andExpect(jsonPath("$[0].surName", is("Niel")))
                .andExpect(jsonPath("$[0].name", is("Everaerts")))
                .andExpect(jsonPath("$[0].employeeNumber", is("E684572")));
    }

    @Test
    public void givenEmployee_whenGetEmployeeByName_thenReturnJsonEmployees() throws Exception {

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);

        mockMvc.perform(get("/employees/name/{name}", "Vangoetsenhoven"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gardenCenterId", is(1)))
                .andExpect(jsonPath("$[0].surName", is("Arno")))
                .andExpect(jsonPath("$[0].name", is("Vangoetsenhoven")))
                .andExpect(jsonPath("$[0].employeeNumber", is("E051448")));
    }

    @Test
    public void whenPostEmployee_thenReturnJsonEmployee() throws Exception {
        Employee employee4 = new Employee(3, "Gianni","Rutten","E055718");

        mockMvc.perform(post("/employees")
                .content(mapper.writeValueAsString(employee4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gardenCenterId", is(3)))
                .andExpect(jsonPath("$.surName", is("Gianni")))
                .andExpect(jsonPath("$.name", is("Rutten")))
                .andExpect(jsonPath("$.employeeNumber", is("E055718")));
    }

    @Test
    public void givenEmployee_whenPutEmployee_thenReturnJsonEmployee() throws Exception {

        Employee updatedEmployee = new Employee(2, "Lloyd!!!","Moons","E000215");

        mockMvc.perform(put("/employees")
                .content(mapper.writeValueAsString(updatedEmployee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gardenCenterId", is(2)))
                .andExpect(jsonPath("$.surName", is("Lloyd!!!")))
                .andExpect(jsonPath("$.name", is("Moons")))
                .andExpect(jsonPath("$.employeeNumber", is("E000215")));
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/employees/{employeeNumber}", "E551864")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoEmployee_whenDeleteEmployee_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/employees/{employeeNumber}", "E004861")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
