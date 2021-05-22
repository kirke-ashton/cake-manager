package com.waracle.cakemanager.web;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.http.ResponseEntity;
import com.waracle.cakemanager.model.ErrorResponse;
//import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;
import com.waracle.cakemanager.exception.DatabaseException;

import org.springframework.web.bind.annotation.ResponseStatus;

@RestControllerAdvice
public class CakeGlobalExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //public final ResponseEntity<Object> handleDatabaseException(Exception ex, WebRequest request) throws Exception {
    public ErrorResponse badRequest(Exception ex) {
//        ErrorResponse response = new ErrorResponse(400, "Bad Request");
//        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ErrorResponse(400, "Bad Request - " + ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) { //} throws Exception {
    public ErrorResponse internalServerError(Exception ex) {
//        ErrorResponse response = new ErrorResponse(500, "Internal Server Error");
//        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ErrorResponse(500, "Internal Server Error - " + ex.getMessage());
    }
}
