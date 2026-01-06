package com.example.UberReviewService;

import com.example.UberReviewService.adapters.CreateReviewDtoToReviewAdaptor;
import com.example.UberReviewService.controllers.ReviewController;
import com.example.UberReviewService.dtos.CreateReviewDto;
import com.example.UberReviewService.services.ReviewService;
import com.example.uberprojectentityservice.models.Review;
import com.example.uberprojectentityservice.models.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private CreateReviewDtoToReviewAdaptor createReviewDtoToReviewAdaptor;

    @Test
    public void testFindReviewById_Success() {
        long reviewId = 1L;
        Review mockReview = Review.builder().build();
        mockReview.setId(reviewId);

        when(reviewService.findReviewById(reviewId))
                .thenReturn(Optional.of(mockReview));

        ResponseEntity<Review> response =
                reviewController.findReviewById(reviewId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewId, response.getBody().getId());
    }

    @Test
    public void testCreateReview_Success() {
        CreateReviewDto requestDto = new CreateReviewDto();
        Booking booking = new Booking();
        booking.setId(1L);
        requestDto.setBookingId(booking.getId());
        Review incomingReview = Review.builder()
                                .content("test review")
                                .rating(4.5)
                                .booking(booking)
                                .build();
        when(createReviewDtoToReviewAdaptor.convertDto(requestDto)).thenReturn(incomingReview);

        Review savedReview = Review.builder()
                            .content(incomingReview.getContent())
                            .rating(incomingReview.getRating())
                            .booking(incomingReview.getBooking())
                            .build();

        when(reviewService.createReview(incomingReview)).thenReturn(savedReview);

        ResponseEntity<?> reponse = reviewController.createReview(requestDto);
        assertEquals(HttpStatus.CREATED, reponse.getStatusCode());
    }
}


