package com.example.Proyecto_Final_Progra3.service.bootcamp;

import com.example.Proyecto_Final_Progra3.exceptions.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampEmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BootcampEmpresaService {
    private final BootcampEmpresaRepository bootcampEmpresaRepository;

    public BootcampEmpresa guardar(BootcampEmpresa bootcamp) {
        if (bootcamp.getTitulo() == null) {
            throw new BootcampInvalidoException("El titulo es invalido");
        }

    }

}
