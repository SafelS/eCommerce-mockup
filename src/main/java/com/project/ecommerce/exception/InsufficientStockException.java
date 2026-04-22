package com.project.ecommerce.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int available, int requested ) {

        super("Insufficient stock for product '" + productName +"' : requested " + requested + ", available " + available );
    }
}
