package com.example.UberReviewService.adapters;

import com.example.UberReviewService.dtos.CreateReviewDto;
import com.example.UberReviewService.models.Booking;
import com.example.UberReviewService.models.Review;
import com.example.UberReviewService.repositories.BookingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class CreateReviewDtoToReviewAdaptorImpl implements CreateReviewDtoToReviewAdaptor {

    private BookingRepository bookingRepository;

    public CreateReviewDtoToReviewAdaptorImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Review convertDto(CreateReviewDto createReviewDto) {
        Booking booking = bookingRepository.findById(createReviewDto.getBookingId())
                .orElseThrow(() -> new RuntimeException(
                        "Booking not found for id: " + createReviewDto.getBookingId()
                ));

        return Review.builder()
                .rating(createReviewDto.getRating())
                .content(createReviewDto.getContent())
                .booking(booking)
                .build();
    }
}
