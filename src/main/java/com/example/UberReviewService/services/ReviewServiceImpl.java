package com.example.UberReviewService.services;


import com.example.UberReviewService.repositories.ReviewRepository;
import com.example.uberprojectentityservice.models.Review;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public Review createReview(Review review) {
        Review review1 = Review.builder()
                .content(review.getContent())
                .rating(review.getRating())
                .booking(review.getBooking())   // <-- IMPORTANT
                .build();
        return reviewRepository.save(review1);
    }

    @Override
    public Optional<Review> findReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review updateReview(Review review, Long id) {

        Optional<Review> review1 = reviewRepository.findById(id);

        review1.ifPresent(existing -> {
            if (review.getRating() != null) existing.setRating(review.getRating());
            if (review.getContent() != null) existing.setContent(review.getContent());
        });

        return reviewRepository.save(review1.get());
    }

    @Override
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public boolean deleteReviewById(Long id) {
        try {
            Optional<Review> review = reviewRepository.findById(id);
            if(review.isEmpty()) return false;
            reviewRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
