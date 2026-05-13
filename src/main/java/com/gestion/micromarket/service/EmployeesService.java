package com.gestion.micromarket.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.gestion.micromarket.config.SecurityContext;
import com.gestion.micromarket.dto.EmployeesRequestDTO;
import com.gestion.micromarket.dto.EmployeesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.enums.Role;
import com.gestion.micromarket.exception.SecurityAuthorizationException;
import com.gestion.micromarket.repository.EmployeesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeesService {

    /** Repositorio de empleado */
    private final EmployeesRepository employeesRepository;

    /** Contexto de seguridad y sesión */
    private final SecurityContext security;

    /**
     * Método privado para reutilizar la lógica de validación de administrador.
     */
    private void validateAdminRole() {
        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
    }

    /**
     * Registra un nuevo empleado en el sistema
     * 
     * @param employeesRequestDTO
     * @return mensaje de confirmación de creación
     * @throws RuntimeException si el número de documento ya está registrado
     */
    public MessageResponseDTO createrEmployees(EmployeesRequestDTO employeesRequestDTO) {

        validateAdminRole();

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

    /**
     * Obtiene la lista completa de empleados registrados
     * 
     * @return lista de empleados como DTOs de respuesta
     */
    public List<EmployeesResponseDTO> getAllEmployees() {
        validateAdminRole();

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

    /**
     * Busca un empleado por su identificador único
     * 
     * @param id
     * @return empleado encontrado envuelto en un {@link Optional}
     */
    public Optional<EmployeesResponseDTO> getEmployeeById(Long id) {
        validateAdminRole();

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

    /**
     * Busca un empleado por su número de documento
     * 
     * @param documentNumber
     * @return empleado encontrado como DTO de respuesta
     * @throws RuntimeException si no existe un empleado con el número de documento
     *                          especificado
     */
    public EmployeesResponseDTO getEmployeeByDocumentNumber(String documentNumber) {
        validateAdminRole();

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

    /**
     * Obtiene todos los empleados que tienen un rol específico
     * 
     * @param role
     * @return lista de empleados con ese rol
     * @throws RuntimeException si ningún empleado tiene el rol especifico
     */
    public List<EmployeesResponseDTO> getEmployeesByRole(Role role) {
        validateAdminRole();


        List<Employees> employees = employeesRepository.findByRole(role);

        if (!Role.administrator.equals(role)
                && !Role.cashier.equals(role)
                && !Role.assistant.equals(role)) {
            throw new RuntimeException(
                    "El rol debe de ser 'administrator', 'cashier', o 'assistant' no: " + role);
        }

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

    /**
     * Obtiene los empleados filtrados por su esatdo
     * 
     * @param active
     * @return lista de empleado con el estado específico
     * @throws RuntimeException si ningún empleado tiene el estado indicado
     */
    public List<EmployeesResponseDTO> getEmployyesByActive(Boolean active) {
        validateAdminRole();

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

    /**
     * Obtiene los empleados contratados dentro de un rango de fecha inicio a fecha
     * fin
     * 
     * @param startDate
     * @param endDate
     * @return lista de empleados contrtados en ese lapso
     * @throws RuntimeException si alguna de las dos fechas es null, si la fecha
     *                          inicio es mayor a la fecha fin o si ningun empleado
     *                          fue contratado en ese lapso
     */
    public List<EmployeesResponseDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {

        validateAdminRole();

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

    /**
     * Reemplaza completamente los datos de un empleado existente.
     * Todos los campos son obligatorios. El campo active es opcional y solo se
     * actualiza si viene presente en el DTO.
     * 
     * @param id
     * @param employeesRequestDTO
     * @return empleado actualizado como DTO de respuesta
     * @throws RuntimeException si el empleado no fué encontrado, algun campo es
     *                          null o el numero de documento ya pertenece a otro
     *                          empleado
     */
    public EmployeesResponseDTO updateEmployeeComplete(Long id, EmployeesRequestDTO employeesRequestDTO) {

        validateAdminRole();

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

    /**
     * Actualiza uno o más campos específicos de un empleado sin reemplazar el
     * registro completo.
     * Solo se modifican los campos que vengan con valor no nulo en el DTO.
     * 
     * @param id
     * @param employeesRequestDTO
     * @return empleado con los datos modificados como DTO de respuesta
     * @throws RuntimeException si el empleado no se encuentra, si el numero de
     *                          dovumento ya esta utilizado por otro empleado
     */
    public EmployeesResponseDTO updateEmployee(Long id, EmployeesRequestDTO employeesRequestDTO) {

        validateAdminRole();

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

    /**
     * Elimina un empleado del sistema con el id
     * 
     * @param id
     * @return mensaje de confirmación de eliminación
     * @throws RuntimeException si el id empleado no fué encontrado
     */
    public MessageResponseDTO deleteEmployeeByid(Long id) {

        validateAdminRole();

        Employees employee = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con el id: " + id));

        employeesRepository.delete(employee);

        return new MessageResponseDTO("Empleado Eliminado Correctamente 😀");
    }

    /**
     * Elimina un empleado del sistema con el número de documento
     * 
     * @param documentNumber
     * @return mensaje de confirmación de eliminación
     * @throws RuntimeException si el numero de documento no se encontró
     */
    public MessageResponseDTO deleteEmployeeByNumberDocument(String documentNumber) {

        validateAdminRole();

        Employees employee = employeesRepository.findByDocumentNumber(documentNumber).orElseThrow(
                () -> new RuntimeException("Empleado no econtrado con el numero de documento: " + documentNumber));

        employeesRepository.delete(employee);

        return new MessageResponseDTO("Empleado Eliminado Correctamente 😀");
    }

}