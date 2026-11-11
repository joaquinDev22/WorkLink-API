package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.TipoMedioCobro;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MedioCobroRequestDTO(
        @NotNull(message = "El tipo de medio de cobro es obligatorio")
        TipoMedioCobro tipo,

        @NotBlank(message = "El titular es obligatorio")
        @Size(max = 120, message = "El titular no puede superar los 120 caracteres")
        String titular,

        @Pattern(regexp = "^(|[0-9]{7,11})$", message = "El documento del titular debe tener entre 7 y 11 dígitos")
        String documentoTitular,

        @Pattern(regexp = "^(|\\d{22})$", message = "El CBU o CVU debe tener exactamente 22 dígitos")
        String cbuCvu,

        @Size(max = 40, message = "El alias no puede superar los 40 caracteres")
        String alias,

        @Email(message = "El correo de Mercado Pago no tiene un formato válido")
        String mercadoPagoEmail
) {}
