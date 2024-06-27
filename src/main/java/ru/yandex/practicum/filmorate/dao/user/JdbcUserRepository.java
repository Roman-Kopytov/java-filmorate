package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.EventRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.LikesRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final UserRowMapper userRowMapper;
    private final LikesRowMapper likesRowMapper;

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(jdbcOperations.queryForObject("""
                SELECT * FROM users
                WHERE user_id =:userId
                """, Map.of("userId", userId), userRowMapper));
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query("""
                SELECT * FROM users
                ORDER BY USER_ID
                """, userRowMapper);
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("EMAIL", user.getEmail(),
                "LOGIN", user.getLogin(),
                "NAME", user.getName(),
                "BIRTHDAY", user.getBirthday());
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = """
                INSERT INTO USERS (EMAIL,LOGIN,NAME,BIRTHDAY)
                VALUES(:EMAIL,:LOGIN,:NAME,:BIRTHDAY)
                """;
        jdbcOperations.update(sql, params, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        Map<String, Object> map = Map.of("ID", user.getId(),
                "EMAIL", user.getEmail(),
                "LOGIN", user.getLogin(),
                "NAME", user.getName(),
                "BIRTHDAY", user.getBirthday());
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = """
                UPDATE USERS
                SET EMAIL=:EMAIL,LOGIN=:LOGIN,NAME=:NAME,BIRTHDAY=:BIRTHDAY
                WHERE USER_ID=:ID""";
        jdbcOperations.update(sql, params);
        return jdbcOperations.queryForObject("SELECT * FROM users WHERE user_id =:userId",
                Map.of("userId", user.getId()), userRowMapper);
    }

    @Override
    public void deleteUser(long userId) {
        jdbcOperations.update("""
                DELETE FROM USERS
                WHERE USER_ID = :userId
                """, Map.of("userId", userId));
    }

    @Override
    public List<User> getUserFriends(User user) {
        return jdbcOperations.query("""
                SELECT * FROM USERS
                WHERE user_id IN (SELECT FRIEND_ID FROM FRIENDSHIP
                                  WHERE USER_ID = :userId)
                """, Map.of("userId", user.getId()), userRowMapper);
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        String sql = """
                SELECT * from USERS u
                WHERE user_id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = :userId)
                AND user_id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = :otherUserId)
                """;
        return jdbcOperations.query(sql,
                Map.of("userId", user.getId(),
                        "otherUserId", otherUser.getId()), userRowMapper);
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbcOperations.update("""
                        INSERT INTO FRIENDSHIP (USER_ID,FRIEND_ID)
                        VALUES (:userId,:friendId)
                        """,
                Map.of("userId", user.getId(), "friendId", friend.getId()));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcOperations.update("""
                DElETE FROM FRIENDSHIP
                WHERE user_id = :userId AND friend_id = :friendId
                """, Map.of("userId", user.getId(), "friendId", friend.getId()));
        jdbcOperations.update("DElETE FROM FRIENDSHIP WHERE user_id = :userId AND friend_id = :friendId",
                Map.of("userId", user.getId(), "friendId", friend.getId()));
    }

    @Override
    public List<Event> getFeed(long id) {
        return jdbcOperations.query("SELECT * FROM EVENT WHERE user_id =:userId",
                Map.of("userId", id), new EventRowMapper());
    }

    @Override
    public List<Long> getRecommendation(long id) {
        String query = """
                SELECT l2.USER_ID, count(l2.FILM_ID) rate
                FROM likes l1
                LEFT JOIN likes l2 ON (l1.film_id=l2.FILM_ID AND l1.user_id!=l2.USER_ID)
                LEFT JOIN users u ON (l2.USER_ID!=l1.user_id)
                WHERE l1.user_id = :id
                GROUP BY L2.user_id
                HAVING  rate > 1
                ORDER BY rate DESC
                LIMIT 10
                """;

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of("id", id));
        List<Long> longList = jdbcOperations.queryForList(query, params, Long.class);
        return longList;
    }

}
