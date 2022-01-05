package de.example.haegertime.advice;

public class ItemExistsException extends RuntimeException{
    public ItemExistsException(String message) {
        super(message);
    }
}
