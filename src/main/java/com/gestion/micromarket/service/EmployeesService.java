package com.gestion.micromarket.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.gestion.micromarket.dto.EmployeesRequestDTO;
import com.gestion.micromarket.dto.EmployeesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.entity.enums.Role;
import com.gestion.micromarket.repository.EmployeesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeesService {
    private final EmployeesRepository employeesRepository;

    // ------------------------------- CREATE -------------------------------

    public MessageResponseDTO createrEmployees(EmployeesRequestDTO employeesRequestDTO) {

        if (employeesRepository.findByDocumentNumber(employeesRequestDTO.getDocumentNumber()).isPresent()) {
            throw new RuntimeException("El numero de documento ya existe, por favor ingrese uno diferente: "
                    + employeesRequestDTO.getDocumentNumber());
        }

        Employees employees = new Employees();
        employees.setName(employeesRequestDTO.getName());
        employees.setDocumentNumber(employeesRequestDTO.getDocumentNumber());
        employees.setHireDate(employeesRequestDTO.getHireDate());
        employees.setRole(employeesRequestDTO.getRole());
        employees.setSalary(employeesRequestDTO.getSalary());
        employees.setCreatedAt(LocalDateTime.now());

        employeesRepository.save(employees);

        return new MessageResponseDTO("Empleado creado ¡Correctamente 😊!");

    }

    // ------------------------------- GET ALL ----------------------------------

    public List<EmployeesResponseDTO> getAllEmployees() {
        List<Employees> employees = employeesRepository.findAll();
        List<EmployeesResponseDTO> ListEmployees = new ArrayList<>();

        for (Employees employee : employees) {
            EmployeesResponseDTO employeesResponseDTO = new EmployeesResponseDTO();
            employeesResponseDTO.setId(employee.getId());
            employeesResponseDTO.setName(employee.getName());
            employeesResponseDTO.setDocumentNumber(employee.getDocumentNumber());
            employeesResponseDTO.setRole(employee.getRole().name());
            employeesResponseDTO.setHireDate(employee.getHireDate());
            employeesResponseDTO.setSalary(employee.getSalary());
            employeesResponseDTO.setActive(employee.getActive());
            employeesResponseDTO.setCreatedAt(employee.getCreatedAt());
            ListEmployees.add(employeesResponseDTO);
        }

        return ListEmployees;
    }

    // ------------------------------- GET BY ID
    // -------------------------------------

    public Optional<EmployeesResponseDTO> getEmployeeById(Long id) {
        Employees employee = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con el ID: " + id));

        EmployeesResponseDTO employeesResponseDTO = new EmployeesResponseDTO();
        employeesResponseDTO.setId(employee.getId());
        employeesResponseDTO.setName(employee.getName());
        employeesResponseDTO.setDocumentNumber(employee.getDocumentNumber());
        employeesResponseDTO.setRole(employee.getRole().name());
        employeesResponseDTO.setHireDate(employee.getHireDate());
        employeesResponseDTO.setSalary(employee.getSalary());
        employeesResponseDTO.setActive(employee.getActive());
        employeesResponseDTO.setCreatedAt(employee.getCreatedAt());

        return Optional.of(employeesResponseDTO);
    }

    // ------------------------- GET BY DOCUMENT NUMBER
    // ---------------------------------------

    public EmployeesResponseDTO getEmployeeByDocumentNumber(String documentNumber) {
        Employees employee = employeesRepository.findByDocumentNumber(documentNumber).orElseThrow(
                () -> new RuntimeException("Empleado no econtrado con el numero de documento: " + documentNumber));

        EmployeesResponseDTO employeesResponseDTO = new EmployeesResponseDTO();
        employeesResponseDTO.setId(employee.getId());
        employeesResponseDTO.setName(employee.getName());
        employeesResponseDTO.setDocumentNumber(employee.getDocumentNumber());
        employeesResponseDTO.setRole(employee.getRole().name());
        employeesResponseDTO.setHireDate(employee.getHireDate());
        employeesResponseDTO.setSalary(employee.getSalary());
        employeesResponseDTO.setActive(employee.getActive());
        employeesResponseDTO.setCreatedAt(employee.getCreatedAt());

        return employeesResponseDTO;
    }

    // ------------------------------ GET BY ROLE
    // ------------------------------------------------

    public List<EmployeesResponseDTO> getEmployeesByRole(Role role) {
        List<Employees> employees = employeesRepository.findByRole(role);

        if (employees.isEmpty()) {
            throw new RuntimeException("No se encontraron empleados con el rol: " + role);
        }

        return employees.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private EmployeesResponseDTO mapToResponseDTO(Employees employee) {
        EmployeesResponseDTO employeesResponseDTO = new EmployeesResponseDTO();
        employeesResponseDTO.setId(employee.getId());
        employeesResponseDTO.setName(employee.getName());
        employeesResponseDTO.setDocumentNumber(employee.getDocumentNumber());
        employeesResponseDTO.setRole(employee.getRole().name());
        employeesResponseDTO.setHireDate(employee.getHireDate());
        employeesResponseDTO.setSalary(employee.getSalary());
        employeesResponseDTO.setActive(employee.getActive());
        employeesResponseDTO.setCreatedAt(employee.getCreatedAt());

        return employeesResponseDTO;
    }

    // ------------------------------ GET BY ACTIVE
    // ------------------------------------------------

    public List<EmployeesResponseDTO> getEmployyesByActive(Boolean active) {
        List<Employees> employees = employeesRepository.findByActive(active);

        if (employees.isEmpty()) {
            throw new RuntimeException("No se encontraron empleados con estado activo: " + active);
        }

        return employees.stream().map(this::mapToResponseactiveDTO).collect(Collectors.toList());

    }

    private EmployeesResponseDTO mapToResponseactiveDTO(Employees employee) {
        EmployeesResponseDTO employeesResponseDTO = new EmployeesResponseDTO();
        employeesResponseDTO.setId(employee.getId());
        employeesResponseDTO.setName(employee.getName());
        employeesResponseDTO.setDocumentNumber(employee.getDocumentNumber());
        employeesResponseDTO.setRole(employee.getRole().name());
        employeesResponseDTO.setHireDate(employee.getHireDate());
        employeesResponseDTO.setSalary(employee.getSalary());
        employeesResponseDTO.setActive(employee.getActive());
        employeesResponseDTO.setCreatedAt(employee.getCreatedAt());

        return employeesResponseDTO;
    }

    // ------------------------------ GET BY HIRE DATE RANGE
    // -----------------------------------------------

    public List<EmployeesResponseDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new RuntimeException("Las fechas de inicio y fin son obligatorias");
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("La fecha de inicio no puede ser mayor que la fecha de fin");
        }

        List<Employees> employees = employeesRepository.findByHireDateBetween(startDate, endDate);

        if (employees.isEmpty()) {
            throw new RuntimeException("No se encontraron empleados contratados entre " + startDate + " y " + endDate);
        }

        return employees.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }
    // ------------------------------ UPDATE COMPLETE
    // -----------------------------------------------

    public EmployeesResponseDTO updateEmployeeComplete(Long id, EmployeesRequestDTO employeesRequestDTO) {
        Employees employee = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        if (employeesRequestDTO.getName() == null) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (employeesRequestDTO.getDocumentNumber() == null) {
            throw new RuntimeException("El número de documento es obligatorio");
        }
        if (employeesRequestDTO.getRole() == null) {
            throw new RuntimeException("El rol es obligatorio");
        }
        if (employeesRequestDTO.getHireDate() == null) {
            throw new RuntimeException("La fecha de contratación es obligatoria");
        }
        if (employeesRequestDTO.getSalary() == null) {
            throw new RuntimeException("El salario es obligatorio");
        }

        employeesRepository.findByDocumentNumber(employeesRequestDTO.getDocumentNumber())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException(
                                "El número de documento ya existe: " + employeesRequestDTO.getDocumentNumber());
                    }
                });

        employee.setName(employeesRequestDTO.getName());
        employee.setDocumentNumber(employeesRequestDTO.getDocumentNumber());
        employee.setRole(employeesRequestDTO.getRole());
        employee.setHireDate(employeesRequestDTO.getHireDate());
        employee.setSalary(employeesRequestDTO.getSalary());

        if (employeesRequestDTO.getActive() != null) {
            employee.setActive(employeesRequestDTO.getActive());
        }

        employeesRepository.save(employee);
        return mapToResponseDTO(employee);
    }

    // ------------------------------ UPDATE ONLY ONE O MORE
    // -----------------------------------------------

    public EmployeesResponseDTO updateEmployee(Long id, EmployeesRequestDTO employeesRequestDTO) {
        Employees employee = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        if (employeesRequestDTO.getName() != null) {
            employee.setName(employeesRequestDTO.getName());
        }
        if (employeesRequestDTO.getDocumentNumber() != null) {
            employeesRepository.findByDocumentNumber(employeesRequestDTO.getDocumentNumber())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new RuntimeException("El numero de documento ya existe: "
                                    + employeesRequestDTO.getDocumentNumber());
                        }
                    });
            employee.setDocumentNumber(employeesRequestDTO.getDocumentNumber());
        }

        if (employeesRequestDTO.getRole() != null) {
            employee.setRole(employeesRequestDTO.getRole());
        }

        if (employeesRequestDTO.getHireDate() != null) {
            employee.setHireDate(employeesRequestDTO.getHireDate());
        }

        if (employeesRequestDTO.getSalary() != null) {
            employee.setSalary(employeesRequestDTO.getSalary());
        }

        if (employeesRequestDTO.getActive() != null) {
            employee.setActive(employeesRequestDTO.getActive());
        }

        employeesRepository.save(employee);
        return mapToResponseDTO(employee);
    }

    // ------------------------------ DELETE BY ID
    // -----------------------------------------------

    public MessageResponseDTO deleteEmployeeByid(Long id) {
        Employees employee = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con el id: " + id));

        employeesRepository.delete(employee);

        return new MessageResponseDTO("Empleado Eliminado Correctamente 😀");
    }

    // ------------------------------ DELETE BY DOCUMENT NUMBER
    // -----------------------------------------------

    public MessageResponseDTO deleteEmployeeByNumberDocument(String documentNumber) {
        Employees employee = employeesRepository.findByDocumentNumber(documentNumber).orElseThrow(
                () -> new RuntimeException("Empleado no econtrado con el numero de documento: " + documentNumber));

        employeesRepository.delete(employee);

        return new MessageResponseDTO("Empleado Eliminado Correctamente 😀");
    }

}