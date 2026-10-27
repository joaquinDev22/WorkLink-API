package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostulacionVacanteService {
    PostulacionResponseDTO postularseMe(String username, Integer vacanteId, String mensaje, MultipartFile cv);
    PostulacionResponseDTO postularse(Integer vacanteId, Integer employeeId, String mensaje, MultipartFile cv);
    List<PostulacionResponseDTO> listarPostulantes(Integer vacanteId);
    PostulacionResponseDTO cambiarEstado(Integer vacanteId, Integer postulacionId, EstadoPostulacion nuevoEstado, String username);
}
