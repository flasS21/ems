package com.internal.ems.department;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.internal.ems.common.exception.DuplicateResourceException;
import com.internal.ems.common.exception.ResourceConflictException;
import com.internal.ems.common.exception.ResourceNotFoundException;
import com.internal.ems.department.dto.DepartmentRequest;
import com.internal.ems.department.dto.DepartmentResponse;
import com.internal.ems.employee.EmployeeRepository;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper,
            EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Department with name '" + request.name() + "' already exists");
        }

        Department department = new Department(request.name());
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    public DepartmentResponse getById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {

        // find department
        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Check duplicate name only if name changed
        if (!department.getName().equals(request.name()) &&
                departmentRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Department with name '" + request.name() + "' already exists");
        }

        department.setName(request.name());

        // In @Transactional, dirty-checking saves changes automatically, or call
        // save(department)
        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponse(updated);

    }

    @Transactional
    public void delete(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (employeeRepository.existsByDepartmentId(id)) {
            throw new ResourceConflictException("Cannot delete department with active employees assigned to it");
        }

        departmentRepository.delete(department);

    }

}
