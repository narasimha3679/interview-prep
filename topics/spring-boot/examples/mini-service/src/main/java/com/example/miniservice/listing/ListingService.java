package com.example.miniservice.listing;

import com.example.miniservice.common.ListingNotFoundException;
import com.example.miniservice.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListingService {

    private final ListingRepository repository;

    public ListingService(ListingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ListingResponse create(CreateListingRequest request) {
        Listing listing = new Listing();
        listing.setTitle(request.title());
        listing.setDescription(request.description());
        listing.setPrice(request.price());
        listing.setCurrency(request.currency());
        listing.setSellerId(request.sellerId());
        listing.setStatus(ListingStatus.DRAFT);
        return ListingResponse.from(repository.save(listing));
    }

    @Transactional(readOnly = true)
    public ListingResponse get(Long id) {
        return repository.findById(id)
                .map(ListingResponse::from)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<ListingResponse> list(ListingStatus status, Pageable pageable) {
        Page<Listing> page = status == null
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return PageResponse.from(page.map(ListingResponse::from));
    }

    @Transactional
    public ListingResponse update(Long id, UpdateListingRequest request) {
        Listing listing = repository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));

        listing.setTitle(request.title());
        listing.setDescription(request.description());
        listing.setPrice(request.price());
        listing.setCurrency(request.currency());
        listing.setStatus(request.status());

        return ListingResponse.from(listing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ListingNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
