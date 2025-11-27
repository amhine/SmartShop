package com.microtech.SmartShop.exception;

public class ClientAlreadyDeletedException extends RuntimeException {
    public ClientAlreadyDeletedException(String message) {
        super(message);
    }
}
