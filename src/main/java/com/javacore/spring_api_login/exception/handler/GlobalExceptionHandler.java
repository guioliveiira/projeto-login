package com.javacore.spring_api_login.exception.handler;

import com.javacore.spring_api_login.exception.custom.BusinessException;
import com.javacore.spring_api_login.exception.custom.InvalidCredentialsException;
import com.javacore.spring_api_login.exception.custom.ResourceNotFoundException;
import com.javacore.spring_api_login.exception.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptio(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Recurso não encontrado path={} message={}",
                request.getRequestURI(),
                ex.getMessage());


        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        log.warn("Erro de negócio path={} message={}",
                request.getRequestURI(),
                ex.getMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        log.warn("Falha de autenticação path={} message={}",
                request.getRequestURI(),
                ex.getMessage());

        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.warn("Erro de validação path={} details={}",
                request.getRequestURI(),
                details);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", details, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMisMatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format(
                "O parâmetro '%s' é inválido: '%s'",
                ex.getName(),
                ex.getValue()
        );

        log.warn("Tipo de parâmetro inválido path={} param={} value={}",
                request.getRequestURI(),
                ex.getName(),
                ex.getValue());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, null, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleViolationException(
            HttpServletRequest request) {

        log.warn("Violação de integridade no banco path={}", request.getRequestURI());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de violação", null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro inesperado path={}", request.getRequestURI(), ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno",
                List.of(ex.getMessage()),
                request
        );
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            List<String> details,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .details(details)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
