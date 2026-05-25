package com.easyinvest.exceptions;

import com.easyinvest.dtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e,
                                                                           HttpServletRequest request) {

        LocalDateTime date = LocalDateTime.now();
        HttpStatus status = HttpStatus.resolve(HttpStatus.BAD_REQUEST.value());
        String message = e.getMessage();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(), status.value(), message, path );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException e,
                                                                          HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        e.getStatusCode();
        String message = e.getReason();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(), e.getStatusCode().value(), message, path);

        return ResponseEntity.status(e.getStatusCode())
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException e,
                                                                      HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        List<String> erros = e.getBindingResult()
                .getFieldErrors()
                .stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()
                ).toList();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(), HttpStatus.BAD_REQUEST.value(), "Erro de validação", path, erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientBalanceException(InsufficientBalanceException e,
                                                                               HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.BAD_REQUEST.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);

    }

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAssetNotFoundException (AssetNotFoundException e,
                                                                          HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.NOT_FOUND.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidQuantityException (InvalidQuantityException e,
                                                                            HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.BAD_REQUEST.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(PositionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePositionNotFoundException (PositionNotFoundException e,
                                                                             HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.NOT_FOUND.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException (UserNotFoundException e,
                                                                         HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.NOT_FOUND.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderNotFoundException(OrderNotFoundException e,
                                                                         HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.NOT_FOUND.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidTransactionTypeException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransactionTypeNotFound(InvalidTransactionTypeException e,
                                                                          HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.now();
        String path = request.getRequestURI();

        ErrorResponseDTO error = new ErrorResponseDTO(date.toString(),HttpStatus.NOT_FOUND.value(), e.getMessage(), path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}