package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mapper.UserRowMapper;
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
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbcOperations.update("INSERT INTO users (email,login,name,birthday)" +
                        " VALUES(:email,:login,:name,:birthday)",
                params, keyHolder, new String[]{"user_id"});
        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public List<User> getUserFriends(User user) {
        return jdbcOperations.queryForList("SELECT friend_id FROM friends WHERE userId = :userId",
                Map.of("userId", user.getId()), User.class);
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbcOperations.update("INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)",
                Map.of("userId", user.getId(), "friendId", friend.getId()));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        jdbcOperations.update("DElETE FROM friends WHERE user_id = :userId AND friend_id = :friendId",
                Map.of("userId", user.getId(), "friendId", friend.getId()));
    }

}
