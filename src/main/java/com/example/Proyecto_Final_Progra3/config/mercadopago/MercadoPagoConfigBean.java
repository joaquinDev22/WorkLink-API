package com.example.Proyecto_Final_Progra3.config.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MercadoPagoConfigBean {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        if (accessToken != null && !accessToken.isBlank() && !accessToken.contains("TEST-ACCESS-TOKEN")) {
            MercadoPagoConfig.setAccessToken(accessToken);
            log.info("Mercado Pago SDK inicializado con éxito.");
        } else {
            log.warn("Mercado Pago Access Token no configurado o en modo placeholder de pruebas.");
        }
    }
}

