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
        int i = repository.getAll(1000, null).size();
        repository.save(new Review("Content", true, 1L, 1L));
        Assertions.assertEquals(i + 1, repository.getAll(1000, null).size());
    }

    @Test
    void createTestFail() {
        int i = repository.getAll(1000, null).size();
        try {
            repository.save(new Review("Content", true, -1L, 1L));
            repository.save(new Review("Content", true, 1L, -1L));
            Assertions.assertEquals(i, repository.getAll(1000, null).size());
        } catch (Exception ignored) {
        } finally {
            Assertions.assertEquals(i, repository.getAll(1000, null).size());
        }
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
        //Review review = repository.save(new Review("Content", true, 1L, 1L));
        Review review = repository.save(new Review(1L,
                "This film is not too bad.",
                true,
                2L,
                2L,
                10
        ));
        review.setContent("NewContent");
        Review saveReview = repository.update(review);
        Assertions.assertEquals(review.getContent(), saveReview.getContent());
        try {
            repository.update(new Review(saveReview.getReviewId(), "FailContentUser", true, -1L, 1L, 0));
            repository.update(new Review(saveReview.getReviewId(), "FailContentFilm", true, 1L, -1L, 0));
        } catch (Exception ignored) {
        } finally {
            Assertions.assertEquals("NewContent", repository.getById(review.getReviewId()).get().getContent());

        }
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
        repository.save(new Review("Content", true, 2L, 1L));
        repository.save(new Review("Content", true, 3L, 1L));
        repository.save(new Review("Content", true, 1L, 2L));
        repository.save(new Review("Content", true, 2L, 2L));
        repository.save(new Review("Content", true, 3L, 2L));
        Assertions.assertEquals(2, repository.getAll(2, null).size());
        Assertions.assertEquals(6, repository.getAll(10, null).size());
        Assertions.assertEquals(3, repository.getAll(10, 1L).size());
    }

    @Test
    void operationLike() {

}
}