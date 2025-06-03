package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.util;

public class SwaggerConstants {

    private SwaggerConstants() {}

    public static final String NOMBRE_DESC = "Nombre";
    public static final String NOMBRE_EXAMPLE = "juan";

    public static final String APELLIDO_DESC = "Apellido del usuario";
    public static final String APELLIDO_EXAMPLE = "Pérez";

    public static final String NUMERO_DOC_DESC = "Número de documento del usuario";
    public static final String NUMERO_DOC_EXAMPLE = "236456";

    public static final String CELULAR_DESC = "Número de celular";
    public static final String CELULAR_EXAMPLE = "+573121234566";

    public static final String FECHA_NAC_DESC = "Fecha de generación";
    public static final String FECHA_NAC_EXAMPLE = "01/01/2024";
    public static final String FECHA_NAC_PATTERN = "^\\d{2}/\\d{2}/\\d{4}$";

    public static final String CORREO_DESC = "Correo electrónico del usuario";
    public static final String CORREO_EXAMPLE = "usuario@example.com";

    public static final String CLAVE_DESC = "Clave del usuario, entre 6 y 13 caracteres";
    public static final String CLAVE_EXAMPLE = "clave123";
    public static final int CLAVE_MIN = 6;
    public static final int CLAVE_MAX = 13;
}