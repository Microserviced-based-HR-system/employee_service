package com.hris.employee_service.service;

        import com.hris.employee_service.model.Employee;
        import com.hris.employee_service.model.EmpRole;
        import com.hris.employee_service.model.EmployeeRole;
        import com.hris.employee_service.repository.CompanyRepository;
        import com.hris.employee_service.repository.EmployeeRepository;
        import com.hris.employee_service.repository.EmployeeRoleRepository;
        import jakarta.persistence.EntityNotFoundException;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.*;

// Business logic methods
@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository, EmployeeRoleRepository employeeRoleRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.employeeRoleRepository = employeeRoleRepository;
    }
    public Optional<Employee> getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId);
    }
    public List<Employee> getEmployeeByCompanyId(Long id){
        return employeeRepository.findByCompanyId(id);
    }
    public List<Employee> getEmployeesByDepartmentId(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }
    public Employee saveEmployee(Employee employee, Long companyId) {
        try {
            if (companyRepository.findById(companyId).isEmpty()) {
                throw new EntityNotFoundException("Company with ID " + companyId + " not found for new employee");
            }

            Optional<Employee> existingEmp = employeeRepository.findByEmail(employee.getEmail());
            if (existingEmp.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            employee.setCompanyId(companyId);
            employee.setEmpRoles(Collections.singleton(employeeRoleRepository.findByEmpRole(EmpRole.EMPLOYEE_ROLE).orElse(null)));
            return employeeRepository.save(employee);
        } catch (Exception e) {
            log.error("Error saving employee: " + e.getMessage(), e);
            throw e;
        }
    }
    public Employee updateEmployee(Long comId, String id, Employee updateEmp){
        Optional<Employee> upd = employeeRepository.findById(id);
        if(upd.isPresent()){
            Employee exist = upd.get();
            exist.setCompanyId(comId);
            exist.setDob(updateEmp.getDob());
            exist.setAddress(updateEmp.getAddress());
            exist.setContactNumber(updateEmp.getContactNumber());
            exist.setEmail(updateEmp.getEmail());
            exist.setBankAccount(updateEmp.getBankAccount());
            exist.setSalary(updateEmp.getSalary());
            exist.setEndDate(updateEmp.getEndDate());
            exist.setJobGradeId(updateEmp.getJobGradeId());
            exist.setEmpRoles(updateEmp.getEmpRoles());
            return employeeRepository.save(exist);
        } else {
            throw new EntityNotFoundException("Employee with ID " + updateEmp.getCompanyId() + " not found for update");
        }
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }
}