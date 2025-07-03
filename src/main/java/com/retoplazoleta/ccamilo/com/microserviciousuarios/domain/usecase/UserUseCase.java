package com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.usecase;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.api.IUserServicePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.DomainConstants;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserDomainException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserValidationMessage;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IPasswordPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.RoleCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.constants.DomainConstants.*;


@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IPasswordPersistencePort passwordPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    @Override
    public void createUser(User user, String role) {
        if (isNullOrEmpty(role)){

            user.setRol(getRoleByNombre());
            validateUserFieldsClient(user);
        }
        else {
            user.setRol(getRoleByNombre(role));
            validateUserFields(user);
        }
        user.setClave(passwordPersistencePort.encriptarClave(user.getClave()));
        userPersistencePort.saveUser(user);
    }

    @Override
    public void createUser(User user) {
        createUser(user, null);
    }

    @Override
    public User login(String correo, String clave) {
        loginField(correo, clave);
        User user = findByCorreo(correo);
        passwordPersistencePort.esClaveValida(clave, user.getClave());
        return user;
    }

    @Override
    public User findByCorreo(String correo) {
        User user = userPersistencePort.getUsuarioByCorreo(correo);
        if (user == null)
            throw new UserDomainException(UserValidationMessage.NO_DATA_FOUND.getMensaje());
        return user;
    }

    @Override
    public User findById(Long idUser) {
        User user = userPersistencePort.getUsuarioById(idUser);
        if (user == null)
            throw new UserDomainException(UserValidationMessage.NO_DATA_FOUND.getMensaje());
        return user;
    }

    @Override
    public List<User> fetchEmployeesAndClients(Long idChef, Long idCliente) {
        List<Long> ids = List.of(idChef, idCliente);
        List<RoleCode> roles = List.of(RoleCode.EMPLEADO,RoleCode.CLIENTE);
        List<User>  fetchEmployeesAndClients = userPersistencePort.fetchEmployeesAndClients(ids, roles);
        if (fetchEmployeesAndClients == null && fetchEmployeesAndClients.isEmpty())
            throw new UserDomainException(UserValidationMessage.NO_DATA_FOUND_USERS.getMensaje());
        return fetchEmployeesAndClients;
    }

    private void validateUserFields(User user) {

        if (isNullOrEmpty(user.getNombre())) {
            throw new UserDomainException(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getApellido())) {
            throw new UserDomainException(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje());
        }

        if (!isNumeric(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje());
        }


        if (isNullOrEmpty(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje());
        }

        if (!isValidPhone(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_INVALIDO.getMensaje());
        }

        if (isNullOrEmpty(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje());
        }


        if (isNullOrEmpty(user.getClave())) {
            throw new UserDomainException(UserValidationMessage.CLAVE.getMensaje());
        }

        if (userPersistencePort.getUsuarioByCorreo(user.getCorreo()) != null) {
            throw new UserDomainException(UserValidationMessage.CORREO_REGISTRADO.getMensaje());
        }

        if (!isValidEmail(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_INVALIDO.getMensaje());
        }

        if (user.getFechaNacimiento() == null) {
            throw new UserDomainException(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje());
        }

        if (!isAdult(user.getFechaNacimiento())) {
            throw new UserDomainException(UserValidationMessage.NO_MAYOR_DE_EDAD.getMensaje());
        }
    }


    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches(REGEX_VALID_EMAIL.getMessage(), email);
    }

    private boolean isValidPhone(String phone) {
        String regex = REGEX.getMessage();
        return Pattern.matches(regex, phone);
    }


    private boolean isNumeric(String str) {
        return str.matches(MATCHES.getMessage());
    }

    private boolean isAdult(LocalDate fechaNacimiento) {
        LocalDate today = LocalDate.now();
        return fechaNacimiento.plusYears(18).isBefore(today) || fechaNacimiento.plusYears(18).isEqual(today);
    }

    private Role getRoleByNombre(String rol) {
        if (rol == null || rol.trim().isEmpty()) {
            return userPersistencePort.getRoleByNombre(RoleCode.CLIENTE.name());
        } else if (rol.equals(RoleCode.ADMIN.name())) {
            return userPersistencePort.getRoleByNombre(RoleCode.PROPIETARIO.name());
        } else if (rol.equals(RoleCode.PROPIETARIO.name())) {
            return userPersistencePort.getRoleByNombre(RoleCode.EMPLEADO.name());
        }

        throw new UserDomainException(UserValidationMessage.ROLE_INVALIDO.getMensaje());
    }

    private Role getRoleByNombre() {
        return getRoleByNombre(null);
    }

    private void loginField(String correo, String clave) {


        if (isNullOrEmpty(correo)) {
            throw new UserDomainException(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje());
        }

        if (!isValidEmail(correo)) {
            throw new UserDomainException(UserValidationMessage.CORREO_INVALIDO.getMensaje());
        }


        if (isNullOrEmpty(clave)) {
            throw new UserDomainException(UserValidationMessage.CLAVE.getMensaje());
        }


    }

    private void validateUserFieldsClient(User user) {

        if (isNullOrEmpty(user.getNombre())) {
            throw new UserDomainException(UserValidationMessage.NOMBRE_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getApellido())) {
            throw new UserDomainException(UserValidationMessage.APELLIDO_OBLIGATORIO.getMensaje());
        }

        if (isNullOrEmpty(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_OBLIGATORIO.getMensaje());
        }

        if (!isNumeric(user.getNumeroDocumento())) {
            throw new UserDomainException(UserValidationMessage.DOCUMENTO_NO_NUMERICO.getMensaje());
        }


        if (isNullOrEmpty(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_OBLIGATORIO.getMensaje());
        }

        if (!isValidPhone(user.getCelular())) {
            throw new UserDomainException(UserValidationMessage.CELULAR_INVALIDO.getMensaje());
        }

        if (isNullOrEmpty(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_OBLIGATORIO.getMensaje());
        }


        if (isNullOrEmpty(user.getClave())) {
            throw new UserDomainException(UserValidationMessage.CLAVE.getMensaje());
        }

        if (userPersistencePort.getUsuarioByCorreo(user.getCorreo()) != null) {
            throw new UserDomainException(UserValidationMessage.CORREO_REGISTRADO.getMensaje());
        }

        if (!isValidEmail(user.getCorreo())) {
            throw new UserDomainException(UserValidationMessage.CORREO_INVALIDO.getMensaje());
        }

        if (user.getFechaNacimiento() == null) {
            throw new UserDomainException(UserValidationMessage.FECHA_NACIMIENTO_OBLIGATORIA.getMensaje());
        }
    }


}
