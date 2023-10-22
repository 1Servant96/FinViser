package com.example.tracking_budget.db.service;

import com.example.tracking_budget.db.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    Category findByName(String name);

    void delete(Long id);

}