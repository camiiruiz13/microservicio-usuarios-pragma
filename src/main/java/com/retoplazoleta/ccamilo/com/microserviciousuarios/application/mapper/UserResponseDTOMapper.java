package com.retoplazoleta.ccamilo.com.microserviciousuarios.application.mapper;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.dto.response.UserDTOResponse;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.DATE_TIME_FORMATER;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.UserMessagesException.ILEGAL_FROMATER;

@Mapper(componentModel = "spring",uses = RoleResponseMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserResponseDTOMapper {

    @Mapping(source = "id", target = "idUsuario")
    @Mapping(source = "rol", target = "rol")
    @Mapping(source = "fechaNacimiento", target = "fechaNacimiento", qualifiedByName = "localDateToString")
    UserDTOResponse toDto(User user);

    List<UserDTOResponse> toDtoList(List<User> users);

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

    @Named("localDateToString")
    static String localDateToString(LocalDate date) {
        if (date == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATER.getMessage());
        return date.format(formatter);
    }
}



