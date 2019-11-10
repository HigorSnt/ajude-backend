package org.ajude.exception_handler;

import org.ajude.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("SERVER ERROR", details, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public final ResponseEntity handleEmailException(EmailAlreadyRegisteredException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("EMAIL ALREDY REGISTERED", details, HttpStatus.CONFLICT);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ServletException.class, UnauthorizedException.class})
    public final ResponseEntity handleUnauthorizedException(Exception ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("UNAUTHORIZED", details, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public final ResponseEntity handleCommentException(Exception ex, WebRequest request){
        // TODO
        return null;
    }

    @ExceptionHandler({InvalidDateException.class, InvalidGoalException.class})
    public final ResponseEntity handleInvalidArgsException(Exception ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("INVALID ARGS", details, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity handleNotFoundException(Exception ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("NOT FOUND", details, HttpStatus.NOT_FOUND);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessagingException.class)
    public final ResponseEntity handleMessagingException(Exception ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("MESSAGING FORMAT ERROR", details, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
