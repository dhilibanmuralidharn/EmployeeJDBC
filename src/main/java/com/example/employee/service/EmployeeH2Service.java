package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRowMapper;
import com.example.employee.repository.EmployeeRepository;

@Service

public class EmployeeH2Service implements EmployeeRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Employee> getEmployees() {
        List<Employee> eList = db.query("SELECT * FROM EMPLOYEELIST", new EmployeeRowMapper());
        ArrayList<Employee> arrayList = new ArrayList<>(eList);
        return arrayList;
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try {
            Employee employee = db.queryForObject("SELECT * FROM EMPLOYEELIST WHERE employeeId = ?",
                    new EmployeeRowMapper(), employeeId);
            return employee;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Employee addEmployee(Employee employee) {
        db.update("INSERT INTO EMPLOYEELIST (employeeName, email, department) values(?, ?, ?)",
                employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        Employee savedEmployee = db.queryForObject(
                "SELECT * FROM EMPLOYEELIST WHERE employeeName = ? and email = ? and department = ?",
                new EmployeeRowMapper(), employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        return savedEmployee;
    }

    @Override
    public Employee updateEmployee(int employeeId, Employee employee) {
        if (employee.getEmployeeName() != null) {
            db.update("Update EMPLOYEELIST SET employeeName = ? WHERE employeeId = ?", employee.getEmployeeName(),
                    employeeId);
        }
        if (employee.getEmail() != null) {
            db.update("Update EMPLOYEELIST SET email = ? WHERE employeeId = ?", employee.getEmail(), employeeId);
        }
        if (employee.getDepartment() != null) {
            db.update("Update EMPLOYEELIST SET department = ? WHERE employeeId = ?", employee.getDepartment(),
                    employeeId);
        }
        return getEmployeeById(employeeId);
    }

    @Override
    public void deleteEmployee(int employeeId) {
        try {
            db.update("DELETE * FROM EMPLOYEELIST WHERE employeeId = ?", employeeId);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}