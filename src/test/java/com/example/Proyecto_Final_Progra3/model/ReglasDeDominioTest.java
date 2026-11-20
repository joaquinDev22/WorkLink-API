package com.example.Proyecto_Final_Progra3.model;

import com.example.Proyecto_Final_Progra3.model.enterprise.EntrevistaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoEntrevista;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion.*;
import static org.junit.jupiter.api.Assertions.*;

/** Reglas de negocio que viven en el dominio: no necesitan base de datos. */
class ReglasDeDominioTest {

    @Test
    void laPostulacionSoloAvanza() {
        assertTrue(PENDIENTE.puedeAvanzarA(VISUALIZADO));
        assertTrue(VISUALIZADO.puedeAvanzarA(ENTREVISTA_AGENDADA));
        assertTrue(ENTREVISTA_AGENDADA.puedeAvanzarA(RECHAZADO));
        assertTrue(PENDIENTE.puedeAvanzarA(RECHAZADO));

        assertFalse(VISUALIZADO.puedeAvanzarA(PENDIENTE));
        assertFalse(ENTREVISTA_AGENDADA.puedeAvanzarA(VISUALIZADO));
        assertFalse(RECHAZADO.puedeAvanzarA(PENDIENTE));
        assertFalse(VISUALIZADO.puedeAvanzarA(VISUALIZADO));
        assertFalse(PENDIENTE.puedeAvanzarA(null));
    }

    @Test
    void rechazadoEsFinal() {
        assertTrue(RECHAZADO.esFinal());
        for (EstadoPostulacion e : EstadoPostulacion.values()) {
            assertFalse(RECHAZADO.puedeAvanzarA(e));
        }
    }

    @Test
    void entrevistaProgramadaSoloSiNoPasoNiSeCancelo() {
        EntrevistaEntity futura = EntrevistaEntity.builder().fecha(LocalDate.now().plusDays(1)).hora(LocalTime.NOON).build();
        EntrevistaEntity pasada = EntrevistaEntity.builder().fecha(LocalDate.now().minusDays(1)).hora(LocalTime.NOON).build();
        EntrevistaEntity cancelada = EntrevistaEntity.builder().fecha(LocalDate.now().plusDays(1)).hora(LocalTime.NOON)
                .status(EstadoEntrevista.CANCELADA).build();

        assertTrue(futura.estaProgramada());
        assertFalse(pasada.estaProgramada());
        assertFalse(cancelada.estaProgramada());
    }

    @Test
    void elRecruiterPuedeIrseSoloRecienALos15Dias() {
        LocalDateTime pedido = LocalDateTime.of(2026, 11, 1, 10, 0);
        RecruiterEntity r = RecruiterEntity.builder().bajaSolicitadaEn(pedido).build();

        assertEquals(pedido.plusDays(15), r.fechaBajaHabilitada());
        assertFalse(r.puedeDarseDeBajaSolo(pedido.plusDays(14)));
        assertTrue(r.puedeDarseDeBajaSolo(pedido.plusDays(15)));

        RecruiterEntity sinPedido = RecruiterEntity.builder().build();
        assertNull(sinPedido.fechaBajaHabilitada());
        assertFalse(sinPedido.puedeDarseDeBajaSolo(LocalDateTime.now()));
        assertFalse(sinPedido.isVinculacionVista());
    }
}
