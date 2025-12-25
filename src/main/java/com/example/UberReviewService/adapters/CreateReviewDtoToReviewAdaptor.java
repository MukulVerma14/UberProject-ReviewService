package com.example.UberReviewService.adapters;

import com.example.UberReviewService.dtos.CreateReviewDto;
import com.example.UberReviewService.models.Review;

public interface CreateReviewDtoToReviewAdaptor {

    public Review convertDto(CreateReviewDto createReviewDto);
}
