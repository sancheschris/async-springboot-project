package com.example.asyncspringboot.controller;

import com.example.asyncspringboot.model.Employee;
import com.example.asyncspringboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<CompletableFuture<List<Employee>>> saveEmployees(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            employeeService.saveEmployee(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/employees", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return employeeService.findAllEmployees().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getEmployeesSync", produces = "application/json")
    public ResponseEntity<List<Employee>> findAllUsersSync() {
        List<Employee> allEmployeesSync = employeeService.findAllEmployeesSync();
        return ResponseEntity.status(HttpStatus.OK).body(allEmployeesSync);
    }

    @GetMapping(value = "/getEmployeesByThread", produces = "application/json")
    public CompletableFuture<ResponseEntity<List<Employee>>> getEmployees() {
        List<CompletableFuture<List<Employee>>> futures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CompletableFuture<List<Employee>> employees = employeeService.findAllEmployees();
            futures.add(employees);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    List<Employee> allEmployees = new ArrayList<>();
                    for (CompletableFuture<List<Employee>> future : futures) {
                        try {
                            allEmployees.addAll(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(allEmployees);
                });
    }
}
