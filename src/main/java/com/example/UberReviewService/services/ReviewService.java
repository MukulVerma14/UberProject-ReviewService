package com.example.UberReviewService.services;



import com.example.uberprojectentityservice.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    public Optional<Review> findReviewById(Long id);

    public Review createReview(Review review);

    public List<Review> findAllReviews();

    public Review updateReview(Review review, Long id);

    public boolean deleteReviewById(Long id);

}
