package org.example.lab5.Service;

import lombok.Setter;
import org.example.lab5.Entity.Category;
import org.example.lab5.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    public void saveCategory(Category category){
        categoryRepository.save(category);
    }
}
