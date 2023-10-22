package com.example.tracking_budget.db.service.impl;

import com.example.tracking_budget.db.entity.Category;
import com.example.tracking_budget.db.repo.CategoryRepository;
import com.example.tracking_budget.db.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findByName(String name) {
        try{
        return categoryRepository.findByCategoryName(name);}
        catch (NullPointerException n) {
            throw new RuntimeException(n);
        }
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
