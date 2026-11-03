package com.example.miniservice.common;

public class ListingNotFoundException extends RuntimeException {

    public ListingNotFoundException(Long id) {
        super("Listing not found: " + id);
    }
}
