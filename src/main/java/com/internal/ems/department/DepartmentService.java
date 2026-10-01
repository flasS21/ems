package com.internal.ems.department;

import com.internal.ems.common.exception.DuplicateResourceException;
import com.internal.ems.common.exception.ResourceNotFoundException;
import com.internal.ems.department.dto.DepartmentRequest;
import com.internal.ems.department.dto.DepartmentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
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
}
