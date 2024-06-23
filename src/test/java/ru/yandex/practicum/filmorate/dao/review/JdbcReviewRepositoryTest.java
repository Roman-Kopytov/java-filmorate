package ru.yandex.practicum.filmorate.dao.review;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dao.mappers.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {JdbcReviewRepository.class, ReviewRowMapper.class})
class JdbcReviewRepositoryTest {
    private final JdbcReviewRepository repository;

    @Test
    void createTestOk() {
        Review review = new Review("Content", true, 1L, 1L);
        Review newReview = repository.save(review);
        Assertions.assertEquals(review.getContent(), newReview.getContent());
    }

    @Test
    void getByIdTestOkAndFail() {
        Review review = new Review("Content", true, 1L, 1L);
        Review newReview = repository.save(review);
        Assertions.assertEquals(newReview, repository.getById(newReview.getReviewId()).get());
        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class,
                () -> repository.getById(2L));
    }

    @Test
    void updateTest() {
        Review review = new Review("Content", true, 1L, 1L);
        Review newReview = repository.save(review);
        newReview.setContent("newContent");
        Assertions.assertEquals(newReview.getContent(), repository.update(newReview).getContent());
    }

    @Test
    void delete() {
        Review review = new Review("Content", true, 1L, 1L);
        Review newReview = repository.save(review);
        Review deleteReview = repository.delete(newReview.getReviewId());
        Assertions.assertEquals(newReview, deleteReview);
        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class,
                () -> repository.getById(newReview.getReviewId()));
    }

    @Test
    void getById() {
        Review review = new Review("Content", true, 1L, 1L);
        Review newReview = repository.save(review);
        Assertions.assertEquals(newReview, repository.getById(newReview.getReviewId()).get());
        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class,
                () -> repository.getById(2L));
    }

    @Test
    void getAll() {
        repository.save(new Review("Content", true, 1L, 1L));
        repository.save(new Review("Content", true, 1L, 1L));
        repository.save(new Review("Content", true, 1L, 1L));
        repository.save(new Review("Content", true, 1L, 2L));
        repository.save(new Review("Content", true, 1L, 2L));
        repository.save(new Review("Content", true, 1L, 2L));
        Assertions.assertEquals(2, repository.getAll(2, null).size());
        Assertions.assertEquals(3, repository.getAll(10, 1L).size());

    }
}