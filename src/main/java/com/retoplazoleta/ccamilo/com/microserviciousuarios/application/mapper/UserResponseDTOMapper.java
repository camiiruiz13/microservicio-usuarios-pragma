package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.RoleDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.request.UserDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.Role;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.DATE_TIME_FORMATER;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.ILEGAL_FROMATER;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserResponseDTOMapper {


    @Mapping(source = "id", target = "idUsuario")
    @Mapping(source = "fechaNacimiento", target = "fechaNacimiento", qualifiedByName = "localDateToString")
    UserDTOResponse toDto(User user);

    RoleDTO toRoleDTO(Role role);

    @Named("stringToLocalDate")
    static LocalDate stringToLocalDate(String date) {
        if (date == null || date.isBlank()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATER.getMessage());
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ILEGAL_FROMATER.getMessage(), e);
        }
    }


}