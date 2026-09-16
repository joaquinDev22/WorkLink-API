package com.example.Proyecto_Final_Progra3.auditoria;

import com.example.Proyecto_Final_Progra3.auditoria.model.AuditoriaOperacionesEntity;
import com.example.Proyecto_Final_Progra3.auditoria.repository.AuditoriaOperacionesRepository;
import com.example.Proyecto_Final_Progra3.config.util.BeanUtil;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
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
        if (target instanceof AuditoriaOperacionesEntity) {
            return;
        }

        try {
            AuditoriaOperacionesRepository repository = BeanUtil.getBean(AuditoriaOperacionesRepository.class);

            AuditoriaOperacionesEntity auditoria = new AuditoriaOperacionesEntity();
            auditoria.setTablaAfectada(target.getClass().getSimpleName());
            auditoria.setOperacion(operacion);

            Integer id = obtenerId(target);
            auditoria.setIdRegistroAfectado(id != null ? id : 0);

            String username = obtenerNombreUsuario();
            auditoria.setUsuario(username);

            auditoria.setDescripcion(
                    String.format("Operación %s realizada en la entidad %s", operacion, auditoria.getTablaAfectada())
            );
            auditoria.setFechaOperacion(LocalDateTime.now());

            repository.save(auditoria);

        } catch (Exception e) {
            log.error("Error al registrar la auditoría para la entidad {}: {}", target.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    private String obtenerNombreUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "Sistema";
    }

    private Integer obtenerId(Object target) {
        try {
            Method getIdMethod = target.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(target);

            if (id instanceof Number number) {
                return number.intValue();
            } else if (id != null) {
                return Integer.parseInt(id.toString());
            }
        } catch (Exception ignored) {
        }
        return 0;
    }
}