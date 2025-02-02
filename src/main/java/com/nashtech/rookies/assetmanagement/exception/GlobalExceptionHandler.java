package com.nashtech.rookies.assetmanagement.exception;

import com.nashtech.rookies.assetmanagement.dto.response.ResponseDto;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDto.<Void>builder()
                        .message(errors.get(0))   //get first error message
                        .build()
        );
    }

    @ExceptionHandler({InvalidUserCredentialException.class, UsernameNotFoundException.class})
    public ResponseEntity<ResponseDto<Void>> handleInvalidUserCredentialException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.<Void>builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ResponseDto<Void>> handleInvalidDateException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDto.<Void>builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseDto<Void>> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.<Void>builder()
                        .message("Your session is expired. Please login again.")
                        .build()
        );
    }

    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<ResponseDto<Void>> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.<Void>builder()
                        .message("Username or password is incorrect. Please try again.")
                        .build()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ResponseDto<Void>> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDto.<Void>builder()
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ResponseDto<Void>> handleException(Exception exception) {
        log.info("Exception occurred....", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDto.<Void>builder()
                        .message("Some errors occur. Please check the server log.")
                        .build()
        );
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ResponseDto<Void>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseDto.<Void>builder()
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({ResourceAlreadyExistException.class})
    protected ResponseEntity<ResponseDto<Void>> handleResourceAlreadyExistException(ResourceAlreadyExistException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ResponseDto.<Void>builder()
                        .message(exception.getMessage())
                        .build()
        );
    }
}