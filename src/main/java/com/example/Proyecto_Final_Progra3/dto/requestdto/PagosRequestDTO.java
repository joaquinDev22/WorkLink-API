package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagosRequestDTO {

    @NotNull(message = "La moneda es obligatoria (USD, PESOS)")
    private Moneda moneda;

    @NotNull(message = "El monto es obligatorio")
    private Integer monto;

    @NotNull(message = "El medio de pago es obligatorio (TARJETA, BILLETERA_VIRTUAL)")
    private MedioDePago medioDePago;
}
