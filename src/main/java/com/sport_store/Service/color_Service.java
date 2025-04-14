package com.sport_store.Service;

import com.sport_store.Entity.Colors;
import com.sport_store.Repository.color_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class color_Service {
    private final color_Repository color_repository;

    public Colors saveColor(String color) {
        Colors colors = Colors
                .builder()
                .color(color)
                .build();
        return color_repository.save(colors);
    }

    public void deleteColor(int color_id) {
        color_repository.deleteById(color_id);
    }

    public Colors findColorById(int color_id) {
        return color_repository.findById(color_id).orElse(null);
    }

    public List<Colors> getAllColors() {
        return color_repository.findAll();
    }
}
