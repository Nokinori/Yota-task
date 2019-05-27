package com.nokinori.api.handlers;

import com.nokinori.api.io.ErrorRs;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import com.nokinori.services.exceptions.SimCardBlockageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static java.time.LocalDateTime.now;

/**
 * Class that handel exception and wraps it to {@link ErrorRs}.
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Catches {@link NotFoundException} and return {@link ErrorRs} with note found error code.
     *
     * @param ex the caught {@link NotFoundException}.
     * @return custom error response with not found error code.
     */
    @ResponseBody
    @ExceptionHandler({NotFoundException.class})
    ResponseEntity<ErrorRs> notFoundHandler(NotFoundException ex) {
        log.debug("Entity not found in DB", ex);
        ErrorRs errorRs = ErrorRs.builder()
                .errorCode(ErrorCode.NOT_FOUND.value())
                .errorText(ex.getMessage() != null ? ex.getMessage() : "Entity not found in DB")
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.NOT_FOUND);
    }

    /**
     * Catches {@link SimCardActivationException} and return {@link ErrorRs} with note found error code.
     *
     * @param ex the caught {@link SimCardActivationException}.
     * @return custom error response with activation exception error code.
     */
    @ResponseBody
    @ExceptionHandler({SimCardActivationException.class})
    ResponseEntity<ErrorRs> activationExceptionHandler(SimCardActivationException ex) {
        log.debug("Exception while sim-card activation", ex);
        ErrorRs errorRs = ErrorRs.builder()
                .errorCode(ErrorCode.ACTIVATION_EXCEPTION.value())
                .errorText(ex.getMessage() != null ? ex.getMessage() : "Activation exception")
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches {@link SimCardBlockageException} and return {@link ErrorRs} with note found error code.
     *
     * @param ex the caught {@link SimCardBlockageException}.
     * @return custom error response with activation exception error code.
     */
    @ResponseBody
    @ExceptionHandler({SimCardBlockageException.class})
    ResponseEntity<ErrorRs> blockageExceptionHandler(SimCardBlockageException ex) {
        log.debug("Exception while sim-card blockage", ex);
        ErrorRs errorRs = ErrorRs.builder()
                .errorCode(ErrorCode.BLOCKAGE_EXCEPTION.value())
                .errorText(ex.getMessage() != null ? ex.getMessage() : "Blockage exception")
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches {@link ConstraintViolationException} and return {@link ErrorRs} with note found error code.
     *
     * @param ex the caught {@link ConstraintViolationException}.
     * @return custom error response with activation exception error code.
     */
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    ResponseEntity<ErrorRs> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.debug("Exception while validating request params", ex);
        ErrorRs errorRs = ErrorRs.builder()
                .errorCode(ErrorCode.VALIDATION_EXCEPTION.value())
                .errorText(ex.getMessage() != null ? ex.getMessage() : "Validation exception")
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
