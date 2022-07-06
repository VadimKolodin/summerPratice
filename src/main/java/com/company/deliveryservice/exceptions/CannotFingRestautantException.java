package com.company.deliveryservice.exceptions;

public class CannotFingRestautantException extends IllegalArgumentException{
    public CannotFingRestautantException(){
        super();
    }
    public CannotFingRestautantException(String s){
        super(s);
    }
}
