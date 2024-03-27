package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Category;
import com.progetto.ecommercebackend.services.CategoryService;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //CREATE
    @PostMapping("/category")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> addNewCategory(@RequestBody String category){
        try {
            categoryService.addNewCategory(category);
            return new ResponseEntity<>("La categoria " + category + " è stata aggiunta nel database.", HttpStatus.CREATED);
        }catch(CustomException e){
            return new ResponseEntity<>("Impossibile aggiungere la categoria.", HttpStatus.BAD_REQUEST);
        }
    }

    //READ
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }


    //UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id , @RequestBody Category category){
        categoryService.updateCategoryById(id, category);
        return new ResponseEntity<>("La categoria con ID "+ id + " è stata aggiornata nel database", HttpStatus.OK);
    }

    //DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id ){
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("La categoria con ID "+ id + " è stata eliminata nel database", HttpStatus.OK);
    }
}
