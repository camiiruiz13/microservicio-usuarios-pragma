package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi;

public interface IPasswordPersistencePort {

    String encriptarClave(String clave);

    boolean esClaveValida(String clave, String claveBD);
}
