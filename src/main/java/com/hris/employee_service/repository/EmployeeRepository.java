package com.hris.employee_service.repository;

import com.hris.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

//Data Accesses Layer
@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    @Query("select e from Employee e where e.department.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") String departmentId);
    Optional<Employee> findByEmail(@Param("email") String email);
}
