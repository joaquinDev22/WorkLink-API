package com.example.Proyecto_Final_Progra3.service.employee.employeeImpl;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidadEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidadId;
import com.example.Proyecto_Final_Progra3.model.employee.HabilidadEntity;
import com.example.Proyecto_Final_Progra3.repository.employee.EmployeeHabilidadRepository;
import com.example.Proyecto_Final_Progra3.repository.employee.HabilidadRepository;
import com.example.Proyecto_Final_Progra3.service.employee.employeeInterfaces.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements IEmployeeService {

    private final HabilidadRepository habilidadRepository;
    private final EmployeeHabilidadRepository employeeHabilidadRepository;

    @Override
    public List<HabilidadEntity> getAllHabilities() {
        return habilidadRepository.findAll();
    }

    @Override
    @Transactional
    public PerfilUsuarioDTO.HabilidadDTO addHabilidad(EmployeeEntity emp, String nombreHabilidad, Integer nivel) {
        HabilidadEntity habilidad = habilidadRepository.findByNombreHabilidad(nombreHabilidad)
                .orElseGet(() -> {
                    HabilidadEntity h = new HabilidadEntity();
                    h.setNombreHabilidad(nombreHabilidad);
                    return habilidadRepository.save(h);
                });

        EmployeeHabilidadEntity empHab = new EmployeeHabilidadEntity();
        EmployeeHabilidadId id = new EmployeeHabilidadId();
        id.setEmployeeId(emp.getEmployeeId());
        id.setHabilidadId(habilidad.getHabilidadId());

        empHab.setId(id);
        empHab.setEmployee(emp);
        empHab.setHabilidad(habilidad);
        empHab.setNivel(nivel);

        employeeHabilidadRepository.save(empHab);

        PerfilUsuarioDTO.HabilidadDTO dto = new PerfilUsuarioDTO.HabilidadDTO();
        dto.setHabilidadId(habilidad.getHabilidadId());
        dto.setNombre(habilidad.getNombreHabilidad());
        dto.setNivel(nivel);
        return dto;
    }

    @Override
    @Transactional
    public void removeHabilidad(EmployeeEntity emp, Integer habilidadId) {
        EmployeeHabilidadId id = new EmployeeHabilidadId();
        id.setEmployeeId(emp.getEmployeeId());
        id.setHabilidadId(habilidadId);

        employeeHabilidadRepository.deleteById(id);
    }
}
