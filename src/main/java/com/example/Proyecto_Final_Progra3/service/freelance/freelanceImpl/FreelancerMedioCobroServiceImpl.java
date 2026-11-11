package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.MedioCobroRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.MedioCobroResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.mappers.FreelancerMedioCobroMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.FreelancerMedioCobroEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.TipoMedioCobro;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.FreelancerMedioCobroRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IFreelancerMedioCobroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerMedioCobroServiceImpl implements IFreelancerMedioCobroService {

    private final FreelancerMedioCobroRepository repository;
    private final PerfilRepository perfilRepository;
    private final FreelancerMedioCobroMapper mapper;

    private Long resolverFreelancerId(String username) {
        String resolvedUsername = username;
        if (resolvedUsername == null || resolvedUsername.isBlank()) {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                resolvedUsername = auth.getName();
            }
        }

        if (resolvedUsername == null || resolvedUsername.isBlank()) {
            throw new ResourceNotFoundException("No se encontró usuario autenticado.");
        }

        final String finalUsername = resolvedUsername;
        PerfilEntity perfil = perfilRepository.findByUsuario(finalUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + finalUsername));

        return perfil.getId().longValue();
    }

    private void validarNegocio(MedioCobroRequestDTO dto) {
        if (dto.tipo() == TipoMedioCobro.TRANSFERENCIA) {
            boolean hasCbu = dto.cbuCvu() != null && !dto.cbuCvu().isBlank();
            boolean hasAlias = dto.alias() != null && !dto.alias().isBlank();
            if (!hasCbu && !hasAlias) {
                throw new BadRequestException("Para transferencia bancaria debe especificar al menos CBU/CVU o Alias.");
            }
        } else if (dto.tipo() == TipoMedioCobro.MERCADOPAGO) {
            boolean hasEmail = dto.mercadoPagoEmail() != null && !dto.mercadoPagoEmail().isBlank();
            boolean hasCvu = dto.cbuCvu() != null && !dto.cbuCvu().isBlank();
            boolean hasAlias = dto.alias() != null && !dto.alias().isBlank();
            if (!hasEmail && !hasCvu && !hasAlias) {
                throw new BadRequestException("Para Mercado Pago debe especificar al menos email de la cuenta, CVU o Alias.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MedioCobroResponseDTO getMedioCobro(String username) {
        Long freelancerId = resolverFreelancerId(username);
        return repository.findByFreelancerId(freelancerId)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public MedioCobroResponseDTO upsertMedioCobro(String username, MedioCobroRequestDTO request) {
        validarNegocio(request);
        Long freelancerId = resolverFreelancerId(username);

        FreelancerMedioCobroEntity entity = repository.findByFreelancerId(freelancerId)
                .orElseGet(() -> FreelancerMedioCobroEntity.builder()
                        .freelancerId(freelancerId)
                        .build());

        entity.setTipo(request.tipo());
        entity.setTitular(request.titular().trim());
        entity.setDocumentoTitular(request.documentoTitular() != null && !request.documentoTitular().isBlank() ? request.documentoTitular().trim() : null);
        entity.setCbuCvu(request.cbuCvu() != null && !request.cbuCvu().isBlank() ? request.cbuCvu().trim() : null);
        entity.setAlias(request.alias() != null && !request.alias().isBlank() ? request.alias().trim() : null);
        entity.setMercadoPagoEmail(request.mercadoPagoEmail() != null && !request.mercadoPagoEmail().isBlank() ? request.mercadoPagoEmail().trim() : null);

        FreelancerMedioCobroEntity guardado = repository.save(entity);
        return mapper.toDto(guardado);
    }

    @Override
    @Transactional
    public void deleteMedioCobro(String username) {
        Long freelancerId = resolverFreelancerId(username);
        repository.deleteByFreelancerId(freelancerId);
    }
}
