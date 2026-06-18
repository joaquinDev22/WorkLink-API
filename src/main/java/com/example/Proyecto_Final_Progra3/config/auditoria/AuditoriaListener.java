package com.example.Proyecto_Final_Progra3.config.auditoria;

import com.example.Proyecto_Final_Progra3.model.auditoria.AuditoriaOperaciones;
import com.example.Proyecto_Final_Progra3.repository.auditoria.AuditoriaOperacionesRepository;
import com.example.Proyecto_Final_Progra3.service.BeanUtil;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

public class AuditoriaListener {

    @PostPersist
    public void postPersist(Object target) {
        registrarOperacion(target, "INSERT");
    }

    @PostUpdate
    public void postUpdate(Object target) {
        registrarOperacion(target, "UPDATE");
    }

    @PostRemove
    public void postRemove(Object target) {
        registrarOperacion(target, "DELETE");
    }

    private void registrarOperacion(Object target, String operacion) {
        AuditoriaOperacionesRepository repository = BeanUtil.getBean(AuditoriaOperacionesRepository.class);
        if (repository != null) {
            AuditoriaOperaciones auditoria = new AuditoriaOperaciones();
            auditoria.setTablaAfectada(target.getClass().getSimpleName());
            auditoria.setOperacion(operacion);

            // Tratamos de obtener el ID usando reflection
            Integer id = obtenerId(target);
            auditoria.setIdRegistroAfectado(id != null ? id : 0);

            // Obtenemos usuario autenticado o "Sistema" si no hay sesión
            String username = "Sistema";
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                username = SecurityContextHolder.getContext().getAuthentication().getName();
            }
            auditoria.setUsuario(username);
            auditoria.setDescripcion(
                    "Operacion " + operacion + " realizada en la entidad " + auditoria.getTablaAfectada());
            auditoria.setFechaOperacion(LocalDateTime.now());

            repository.save(auditoria);
        }
    }

    private Integer obtenerId(Object target) {
        try {
            java.lang.reflect.Method getIdMethod = target.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(target);
            if (id instanceof Integer)
                return (Integer) id;
        } catch (Exception e) {
            // Ignorar, si no tiene getId intentamos con otro
        }
        return 0;
    }
}
