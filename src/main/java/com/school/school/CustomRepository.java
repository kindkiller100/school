package com.school.school;

import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.util.Optional;

@Repository
public interface CustomRepository<T>  {
    Optional<T> findById(Long id);
    default T getIfExists(Long id) {
        return  findById(id)
            .orElseThrow (
                () -> new NotFoundException("Объект с id «" + id + "» не найден")
            );
    }
}
