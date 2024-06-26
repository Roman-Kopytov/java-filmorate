package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import java.util.*;

@RestController
@RequestMapping("/reviews")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private static final int LIKE = 1;
    private static final int DISLIKE = -1;
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public Review create(@Valid @RequestBody Review review) {
        log.info("==>POST /review  {}", review);
        Review newReview = reviewService.create(review);
        log.info("POST /review <== {}", newReview);
        return newReview;
    }

    @PutMapping
    @Validated({Marker.Update.class})
    public Review update(@Valid @RequestBody Review review) {
        log.info("==>PUT /review  {}", review);
        Review updatedReview = reviewService.update(review);
        log.info("PUT /review <== {}", updatedReview);
        return updatedReview;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review addLike(@PathVariable("reviewId") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("==>PUT /review  id {} userId {}", reviewId, userId);
        return reviewService.operationLike(reviewId, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review addDislike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("==>PUT /review  id {} userId {}", id, userId);
        return reviewService.operationLike(id, userId, false);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review deleteDislike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("==>DELETE /review  id {} userId {}", id, userId);
        return reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("==>DELETE /review  id {} userId {}", id, userId);
        return reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review deleteReview(@PathVariable(name = "id") Long reviewId) {
        log.info("==>DELETE /review  {}", reviewId);
        return reviewService.delete(reviewId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getById(@PathVariable Long id) {
        log.info("==>GET /review  {}", id);
        return reviewService.getById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Review> getAll(@RequestParam(name = "filmId", required = false) Long filmId, @RequestParam(name = "count", defaultValue = "10") int count) {
        return reviewService.getAll(count, filmId);
    }
}



