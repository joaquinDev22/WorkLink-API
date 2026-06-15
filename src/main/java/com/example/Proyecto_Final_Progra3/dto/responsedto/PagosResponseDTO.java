package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import lombok.Data;

@Data
public class PagosResponseDTO {
    private Integer pagoId;
    private Moneda moneda;
    private Integer monto;
    private MedioDePago medioDePago;
    private Integer proyectoId;
}
