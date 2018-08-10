package com.larry.todolist.exceptionHandle;

import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TaskControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorMsg>> handleValidationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ErrorMsg> errorList = constraintViolations.stream()
                .map(c -> new ErrorMsg(c.getPropertyPath().toString(), c.getInvalidValue(), c.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotCompleteException.class)
    public ResponseEntity<String> handleCannotCompleteException(CannotCompleteException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JDBCException.class)
    public ResponseEntity<ErrorMsg> handleJdbcException(JDBCException e) {
        ErrorMsg errorMsg = new ErrorMsg("todo", "", "이미 등록된 할 일 입니다.");
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }
}
