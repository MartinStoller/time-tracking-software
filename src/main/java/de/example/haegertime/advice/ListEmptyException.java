package de.example.haegertime.advice;

public class ListEmptyException extends RuntimeException{
    public ListEmptyException(String message) {
        super(message);
    }
}
