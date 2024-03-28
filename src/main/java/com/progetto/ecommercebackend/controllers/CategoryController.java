package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Category;
import com.progetto.ecommercebackend.services.CategoryService;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //CREATE
    @PostMapping("/category")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> addNewCategory(@RequestBody String categoryName){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("category",  categoryService.addNewCategory(categoryName)))
                        .message("Una nuova categoria è stata creata.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    //READ
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }


    //UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> updateCategory(@PathVariable("id") Long id , @RequestBody Category category){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("category",   categoryService.updateCategoryById(id, category)))
                        .message("Categoria aggiornata.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    //DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> deleteCategoryById(@PathVariable("id") Long id ){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("category", categoryService.deleteCategoryById(id)))
                        .message("Categoria eliminata.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
