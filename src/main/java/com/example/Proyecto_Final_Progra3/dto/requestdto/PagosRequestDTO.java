package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public record PagosRequestDTO(
        @NotNull(message = "La moneda es obligatoria (USD, PESOS)") Moneda moneda,
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto de pago debe ser mayor a 0") BigDecimal monto,
        @NotNull(message = "El medio de pago es obligatorio (TARJETA, BILLETERA_VIRTUAL)") MedioDePago medioDePago
) {}

