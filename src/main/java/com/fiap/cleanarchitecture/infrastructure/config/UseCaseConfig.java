package com.fiap.cleanarchitecture.infrastructure.config;

import com.fiap.cleanarchitecture.application.gateways.IItemCardapioGateway;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.IItemCardapioUseCase;
import com.fiap.cleanarchitecture.application.interfaces.IRestauranteUseCase;
import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.application.interfaces.IUsuarioUseCase;
import com.fiap.cleanarchitecture.application.usecases.ItemCardapioUseCaseImpl;
import com.fiap.cleanarchitecture.application.usecases.RestauranteUseCaseImpl;
import com.fiap.cleanarchitecture.application.usecases.TipoUsuarioUseCaseImpl;
import com.fiap.cleanarchitecture.application.usecases.UsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ITipoUsuarioUseCase tipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new TipoUsuarioUseCaseImpl(tipoUsuarioGateway);
    }

    @Bean
    public IUsuarioUseCase usuarioUseCase(IUsuarioGateway usuarioGateway,
                                          ITipoUsuarioGateway tipoUsuarioGateway,
                                          IRestauranteGateway restauranteGateway) {
        return new UsuarioUseCaseImpl(usuarioGateway, tipoUsuarioGateway, restauranteGateway);
    }

    @Bean
    public IRestauranteUseCase restauranteUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway) {
        return new RestauranteUseCaseImpl(restauranteGateway, usuarioGateway);
    }

    @Bean
    public IItemCardapioUseCase itemCardapioUseCase(IItemCardapioGateway itemCardapioGateway,
                                                    IRestauranteGateway restauranteGateway,
                                                    IUsuarioGateway usuarioGateway) {
        return new ItemCardapioUseCaseImpl(itemCardapioGateway, restauranteGateway, usuarioGateway);
    }
}
