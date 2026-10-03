package com.internal.ems.employee;

import com.internal.ems.common.exception.DuplicateResourceException;
import com.internal.ems.common.exception.ResourceNotFoundException;
import com.internal.ems.department.Department;
import com.internal.ems.department.DepartmentRepository;
import com.internal.ems.employee.dto.EmployeeRequest;
import com.internal.ems.employee.dto.EmployeeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
        }

        if (employeeRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Employee with phone '" + request.phone() + "' already exists");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.departmentId()));

        Employee employee = new Employee(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                department);

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    public EmployeeResponse getById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Validate uniqueness only if changed
        if (!employee.getEmail().equals(request.email()) && employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
        }

        if (!employee.getPhone().equals(request.phone()) && employeeRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Employee with phone '" + request.phone() + "' already exists");
        }

        // Update department if changed
        if (!employee.getDepartment().getId().equals(request.departmentId())) {
            Department newDepartment = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.departmentId()));
            employee.setDepartment(newDepartment);
        }

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());

        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employeeRepository.delete(employee);
    }
}
