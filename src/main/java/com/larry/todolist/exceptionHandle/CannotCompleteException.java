package com.larry.todolist.exceptionHandle;

public class CannotCompleteException extends RuntimeException{

    public CannotCompleteException (String message){
        super(message);
    }

}
