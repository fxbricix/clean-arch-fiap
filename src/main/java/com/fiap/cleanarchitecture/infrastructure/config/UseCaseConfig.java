package com.fiap.cleanarchitecture.infrastructure.config;

import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.application.usecases.TipoUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ITipoUsuarioUseCase tipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new TipoUsuarioUseCaseImpl(tipoUsuarioGateway);
    }
}
