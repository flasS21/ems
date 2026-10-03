package com.internal.ems.employee;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internal.ems.employee.dto.EmployeeRequest;
import com.internal.ems.employee.dto.EmployeeResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {

    EmployeeResponse response = employeeService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);

  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {

    EmployeeResponse response = employeeService.getById(id);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);

  }

  @GetMapping
  public ResponseEntity<List<EmployeeResponse>> getAll() {

    List<EmployeeResponse> response = employeeService.getAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);

  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody EmployeeRequest request) {

    EmployeeResponse response = employeeService.update(id, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    employeeService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();

  }

}
