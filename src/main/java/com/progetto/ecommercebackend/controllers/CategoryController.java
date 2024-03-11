package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Category;
import com.progetto.ecommercebackend.services.CategoryService;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping("/category")
    public ResponseEntity<String> addNewCategory(@RequestBody String category){
        try {
            categoryService.addNewCategory(category);
            return new ResponseEntity<>("Category " + category + " is added to the list", HttpStatus.CREATED);
        }catch(CustomException e){
            throw new CustomException("Category can not be added.");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id ){
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category with id "+ id + " is deleted from the database", HttpStatus.OK);
    }
}
