package com.example.Proyecto_Final_Progra3.service.enterprise;

import com.example.Proyecto_Final_Progra3.DTO.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;


    //METODO PARA CREAR EL PREFIL DE UNA EMPRESA//
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto) {
        Perfil perfil = perfilRepository.findById(dto.getPrefil_id())
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));

        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setSitio_web(dto.getSitio_web());
        empresa.setUbicacion(dto.getUbicacion());
        empresa.setPerfil(perfil);

        empresaRepository.save(empresa);

        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getDescripcion(),
                empresa.getSitio_web(),
                empresa.getUbicacion(),
                empresa.getPerfil().getId()
        );

    }

    public EmpresaResponseDTO editarEmpresa(int id, EmpresaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esa empresa no existe"));

        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setSitio_web(dto.getSitio_web());
        empresa.setUbicacion(dto.getUbicacion());

        empresaRepository.save(empresa);

        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getDescripcion(),
                empresa.getSitio_web(),
                empresa.getUbicacion(),
                empresa.getPerfil().getId()
        );
    }

    public List<Empresa> mostrarEmpresas() {
        return  empresaRepository.findAll();
    }

    public Empresa buscarEmpresaXid(int id){
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));
    }

    public List<Empresa> buscarEmpresasXidPerfil(int idPerfil){
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));
        return empresaRepository.findByPerfil(perfil);
    }


    public void eliminarEmpresa(int idEmpresa){
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));

        empresaRepository.delete(empresa);
    }



}
