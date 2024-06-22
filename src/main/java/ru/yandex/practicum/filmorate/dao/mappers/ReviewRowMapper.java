package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<Review> {
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("review_id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("ispositive"));
        review.setUserId(rs.getLong("user_id"));
        review.setFilmId(rs.getLong("film_id"));
        Integer i = rs.getInt("useful");
        if (i == null) {
            review.setUseful(0);
        } else {
            review.setUseful(rs.getInt("useful"));
        }
        return review;
    }
}
