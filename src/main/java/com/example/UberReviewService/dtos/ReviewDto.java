package com.example.UberReviewService.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReviewDto {
    private Long id;
    private String content;
    private Long booking;
    private Double rating;
    private Date createdAt;
    private Date updatedAt;
}
