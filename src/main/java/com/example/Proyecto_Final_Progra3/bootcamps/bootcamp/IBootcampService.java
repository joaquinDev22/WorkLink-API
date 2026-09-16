package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import java.util.List;

public interface IBootcampService {
    BootcampResponseDTO crearBootcamp(BootcampRequestDTO dto);

    BootcampResponseDTO crearMiBootcamp(String username, BootcampRequestDTO dto);

    void eliminarBootcamp(Integer id);

    BootcampResponseDTO modificarBootcamp(Integer id, BootcampRequestDTO dto);

    List<BootcampResponseDTO> listarBootcamps();

    List<BootcampResponseDTO> listarMisBootcamps(String username);

    BootcampResponseDTO buscarPorId(Integer id);

    List<BootcampResponseDTO> listarDisponibles();

    List<BootcampResponseDTO> listarPorModalidad(String modalidad);
}
