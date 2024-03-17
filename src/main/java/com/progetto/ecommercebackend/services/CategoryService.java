package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Category;
import com.progetto.ecommercebackend.repositories.CategoryRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addNewCategory(String category) {
        String catValue="";
        if( category.contains(" ")){
            catValue = category.toLowerCase().replace(" ","-");
        }else{
            catValue = category.toLowerCase();
        }

        Optional<Category> categoryOptional = categoryRepository.findByValue(catValue);
        if( categoryOptional.isPresent()){
            throw new CustomException("Category already exists in the database.");
        }else{
            String name = category.substring(0,1).toUpperCase() + category.substring(1).toLowerCase();
            Category newCategory = new Category();
            newCategory.setName(name);
            newCategory.setValue(catValue);
            categoryRepository.save(newCategory);
            return  newCategory;
        }
    }


    public void deleteCategoryById(Long id) {
        try{
            categoryRepository.deleteCategoryById(id);
        }catch( CustomException e){
            throw new CustomException("Category can not be deleted");
        }
    }

    public Category updateCategoryById(Long id, Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if( categoryOptional.isPresent() ){
            categoryRepository.deleteCategoryById(id);
            Category savedCategory = new Category();
            category.setId(category.getId());
            category.setName(category.getName());
            category.setValue(category.getValue());
            return categoryRepository.save(category);
        }else{
            throw new CustomException("Category with id"+ id + " can not be updated.");
        }
    }
}
