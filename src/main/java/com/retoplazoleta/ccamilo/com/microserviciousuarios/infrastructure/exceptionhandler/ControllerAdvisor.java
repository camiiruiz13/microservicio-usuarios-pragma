package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.exceptionhandler;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.application.exception.TokenInvalidException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.exception.UserDomainException;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(UserDomainException.class)
    public ResponseEntity<GenericResponseDTO<Void>> handleDomainException(UserDomainException ex) {
        return new ResponseEntity<>(ResponseUtils.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponseDTO<Void>> handleInfrastructureException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResponseUtils.buildResponse(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDTO<Void>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ResponseUtils.buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
