package com.example.UberReviewService.controllers;

import com.example.UberReviewService.adapters.CreateReviewDtoToReviewAdaptor;
import com.example.UberReviewService.dtos.CreateReviewDto;
import com.example.UberReviewService.dtos.ReviewDto;
import com.example.UberReviewService.models.Review;
import com.example.UberReviewService.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CreateReviewDtoToReviewAdaptor createReviewDtoToReviewAdaptor;


    //constructor injection
    public ReviewController(ReviewService reviewService,  CreateReviewDtoToReviewAdaptor createReviewDtoToReviewAdaptor) {
        this.reviewService = reviewService;
        this.createReviewDtoToReviewAdaptor = createReviewDtoToReviewAdaptor;
    }

    //create new review
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@Validated @RequestBody CreateReviewDto request) {
        Review incomingReview = this.createReviewDtoToReviewAdaptor.convertDto(request);
        if (incomingReview == null) {
            return new ResponseEntity<>("Invalid Arguments",HttpStatus.BAD_REQUEST);
        }
        Review review = this.reviewService.createReview(incomingReview);
        ReviewDto response = ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .booking(review.getBooking().getId())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //get review by id
    @GetMapping("/{id}")
    public ResponseEntity<Review> findReviewById(@PathVariable Long id) {
        return reviewService.findReviewById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //get list of all the reviews
    @GetMapping
    public ResponseEntity<List<Review>> findAllReviews() {
        List<Review> reviewList = reviewService.findAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

    //update the review
    @PutMapping("/update")
    public ResponseEntity<String> updateReview(@RequestBody(required = false) Review review, @RequestParam Long id) {
        return new ResponseEntity(reviewService.updateReview(review, id), HttpStatus.OK);
    }

    //delete review by id
    @DeleteMapping("/{id}")
    public boolean deleteReviewById(@PathVariable Long id) {
        return reviewService.deleteReviewById(id);
    }
}
