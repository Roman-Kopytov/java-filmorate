package ru.yandex.practicum.filmorate.dao.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.ReviewRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.ReviewsLikeRowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcReviewRepository implements ReviewRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final ReviewRowMapper reviewRowMapper;
    private final ReviewsLikeRowMapper reviewsLikeRowMapper;

    @Override
    public Review save(Review review) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("CONTENT", review.getContent(),
                "ISPOSITIVE", review.getIsPositive(),
                "USER_ID", review.getUserId(),
                "FILM_ID", review.getFilmId());
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = "INSERT INTO reviews (CONTENT, ISPOSITIVE, USER_ID, FILM_ID)" +
                " VALUES(:CONTENT, :ISPOSITIVE, :USER_ID, :FILM_ID)";
        jdbcOperations.update(sql, params, keyHolder);
        review.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return getById(review.getId()).get();
    }

    @Override
    public Optional<Review> getById(Long reviewId) {
        return Optional.ofNullable(jdbcOperations.queryForObject("SELECT r.*, SUM(rl.USEFUL) USEFUL FROM reviews r " +
                        "RIGHT JOIN reviews_likes rl ON r.review_id=rl.review_id WHERE r.review_id =:reviewId " +
                        "GROUP BY r.REVIEW_ID",
                Map.of("reviewId", reviewId), reviewRowMapper));
    }

    @Override
    public Review update(Review review) {
        Map<String, Object> map = Map.of("ID", review.getId(),
                "CONTENT", review.getContent(),
                "ISPOSITIVE", review.getIsPositive(),
                "USER_ID", review.getUserId(),
                "FILM_ID", review.getFilmId());
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = "UPDATE reviews" +
                " SET CONTENT=:CONTENT,ISPOSITIVE =:ISPOSITIVE, USER_ID=:USER_ID, FILM_ID=:FILM_ID " +
                "WHERE review_id=:ID";
        jdbcOperations.update(sql, params);
        return jdbcOperations.queryForObject("SELECT * FROM reviews WHERE review_id =:reviewId",
                Map.of("reviewId", review.getId()), reviewRowMapper);
    }

    @Override
    public Review delete(long reviewId) {
        Review review = getById(reviewId).get();
        Map<String, Object> map = Map.of("reviewId", reviewId);
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        jdbcOperations.update("DELETE FROM REVIEWS WHERE REVIEW_ID=:reviewId",
                params);
        return review;
    }

    @Override
    public List<Review> getAll(int count, Long filmId) {
        List<Review> reviewList;
        if (filmId == null) {
            reviewList = jdbcOperations.query("SELECT * FROM reviews LIMIT :count",
                    Map.of("count", count), reviewRowMapper);
        } else {
            reviewList = jdbcOperations.query("SELECT * FROM reviews " +
                            "WHERE film_id = :filmId " +
                            "LIMIT :count",
                    Map.of("filmId", filmId, "count", count), reviewRowMapper);
        }
        return reviewList;
    }

    @Override
    public Review operationLike(Long id, Long userId, Integer useful) {
        Map<String, Object> map = Map.of(
                "review_id", id,
                "user_id", userId,
                "useful", useful);
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = "MERGE INTO reviews_likes (review_id, user_id, useful)" +
                " VALUES(:review_id,:user_id,:useful)";
        jdbcOperations.update(sql, params);
        return getById(id).get();
    }

    @Override
    public Review deleteLike(Long id, Long userId) {
        Map<String, Object> map = Map.of(
                "review_id", id,
                "user_id", userId);
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = "DELETE FROM REVIEWS_LIKES WHERE user_id = :user_id AND review_id= :review_id";
        jdbcOperations.update(sql, params);
        return getById(id).get();
    }


}