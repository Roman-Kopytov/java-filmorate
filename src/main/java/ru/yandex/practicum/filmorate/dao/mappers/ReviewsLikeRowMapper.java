package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewsLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewsLikeRowMapper implements RowMapper<ReviewsLike> {
    public ReviewsLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReviewsLike u = new ReviewsLike();
        u.setId(rs.getLong("review_id"));
        u.setUserId(rs.getLong("user_id"));
        u.setUseful(rs.getInt("useful"));
        return u;
    }
}
