package com.example.asyncspringboot.service;

import com.example.asyncspringboot.model.Employee;
import com.example.asyncspringboot.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Async
    public void saveEmployee(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<Employee> employees = parseCSVFile(file);
        logger.info("saving list of employees of size {} by {}", employees.size(), Thread.currentThread());
        employees = employeeRepository.saveAll(employees);
        long end = System.currentTimeMillis();
        logger.info("Total time of execution {} ms done by Thread: {}", (end-start), Thread.currentThread());
        CompletableFuture.completedFuture(employees);
    }

    @Async
    public CompletableFuture<List<Employee>> findAllEmployees() {
        long start = System.currentTimeMillis();
        List<Employee> employees = employeeRepository.findAll();
        long end = System.currentTimeMillis();
        logger.info("Total time of execution in findAllEmployeesAsync {} ms by {}", (end-start), Thread.currentThread().getName());
        return CompletableFuture.completedFuture(employees);
    }

    public List<Employee> findAllEmployeesSync() {
        long start = System.currentTimeMillis();
        List<Employee> employees = employeeRepository.findAll();
        long end = System.currentTimeMillis();
        logger.info("Total time of execution in findAllEmployeesSync {} ms", (end-start));
        return employees;
    }

    private List<Employee> parseCSVFile(final  MultipartFile file) throws Exception {
        final List<Employee> employees = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final Employee employee = new Employee();
                    employee.setName(data[1]);
                    employee.setEmail(data[2]);
                    employee.setGender(data[3]);
                    employee.setRole(data[4]);
                    employees.add(employee);
                }
                return employees;
            }
        } catch (final IIOException exception) {
            logger.error("Failed to parse CSV file {}", exception);
            throw new Exception("Failed to parse CSV file {}", exception);
        }
    }
}
