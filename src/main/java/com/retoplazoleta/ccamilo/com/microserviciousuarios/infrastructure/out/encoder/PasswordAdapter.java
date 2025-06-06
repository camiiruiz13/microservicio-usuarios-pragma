package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.out.encoder;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IPasswordPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exception.AutenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;

@RequiredArgsConstructor
public class PasswordAdapter  implements IPasswordPersistencePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encriptarClave(String clave) {
        return passwordEncoder.encode(clave);
    }

    @Override
    public boolean esClaveValida(String clave, String claveBD) {
        if (!passwordEncoder.matches(clave, claveBD)) {
            throw new AutenticationException(ERROR_CREDENCIALES.getMessage());
        }
        return true;
    }

}
