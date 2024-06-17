package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralMpaService implements MpaService {
    private final MpaRepository mpaRepository;


    @Override
    public Mpa getById(int id) {
        return Optional.ofNullable(mpaRepository.getById(id)).orElseThrow(() -> new NotFoundException("Mpa not found with id: " + id));

    }

    @Override
    public List<Mpa> getAllGenres() {
        return mpaRepository.getAll();
    }
}
