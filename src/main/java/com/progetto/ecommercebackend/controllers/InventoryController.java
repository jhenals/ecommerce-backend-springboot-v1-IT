package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.InventoryService;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    //UPDATE
    @RequestMapping(value = ("inventory-quantity"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> updateBookQuantityInInventory(@RequestParam Long bookId, @RequestParam Integer qty){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book", inventoryService.updateBookQuantityInInventory(bookId, qty)))
                        .message("Quantitò del libro aggiornata.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    //UPDATE
    @RequestMapping(value = ("num-purchases"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseEntity<HttpResponse> incrementNumPurchases(@RequestParam Long bookId){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book",  inventoryService.incrementNumPurchases(bookId)))
                        .message("Un nuovo acquisto è stato effettuato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


}
