package com.example.miniservice.listing;

import java.math.BigDecimal;
import java.time.Instant;

public record ListingResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String currency,
        String status,
        String sellerId,
        Instant createdAt
) {
    public static ListingResponse from(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getCurrency(),
                listing.getStatus().name(),
                listing.getSellerId(),
                listing.getCreatedAt()
        );
    }
}
