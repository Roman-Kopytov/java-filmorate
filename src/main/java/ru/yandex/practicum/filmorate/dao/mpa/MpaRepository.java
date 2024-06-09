package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRepository {
    List<Mpa> findById(Integer id);

    List<Mpa> getAll();

    Mpa getById(Integer id);


}
