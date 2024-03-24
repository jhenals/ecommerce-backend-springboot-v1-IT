package com.progetto.ecommercebackend.support.enums;

public enum OrderStatus {

    PENDING("Pending"),
    CREATED("Created"),
    PAID("Paid"), //Ordine Pagato
    PROCESSING("Processing"), //Ordine in fase di elaborazione
    SHIPPED("Shipped"), //Ordine spedito
    DELIVERED("Delivered"), //Ordine consegnato
    CANCELED("Canceled"); //Ordine annullato


    private final String status;

    OrderStatus(String status){
        this.status= status;
    }

    public String getStatus(){
        return status;
    }
}