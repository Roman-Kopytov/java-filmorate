package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final UserRowMapper userRowMapper;

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(jdbcOperations.queryForObject("SELECT * FROM users WHERE user_id =:userId",
                Map.of("userId", userId), userRowMapper));
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("EMAIL", user.getEmail(),
                "LOGIN", user.getLogin(),
                "NAME", user.getName(),
                "BIRTHDAY", user.getBirthday());
        MapSqlParameterSource params = new MapSqlParameterSource(map);
        String sql = "INSERT INTO USERS (EMAIL,LOGIN,NAME,BIRTHDAY)" +
                " VALUES(:EMAIL,:LOGIN,:NAME,:BIRTHDAY)";
        jdbcOperations.update(sql, params, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public List<User> getUserFriends(User user) {
        return jdbcOperations.query("SELECT * FROM USERS WHERE user_id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = :userId)",
                Map.of("userId", user.getId()), userRowMapper);
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbcOperations.update("INSERT INTO FRIENDSHIP (USER_ID,FRIEND_ID) " +
                        "VALUES (:userId,:friendId)",
                Map.of("userId", friend.getId(), "friendId", user.getId()));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcOperations.update("DElETE FROM FRIENDSHIP WHERE user_id = :userId AND friend_id = :friendId",
                Map.of("userId", friend.getId(), "friendId", user.getId()));
    }

}
