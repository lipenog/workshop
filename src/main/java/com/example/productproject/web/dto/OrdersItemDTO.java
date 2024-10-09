package com.example.productproject.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class OrdersItemDTO {
    @NotNull(message = "orders.error.item.productID")
    @Min(value = 0, message = "orders.error.item.productID")
    private int productID;
    @NotNull(message = "orders.error.item.productQuantity")
    @Min(value = 1, message = "orders.error.item.productQuantity")
    private int quantity;
}
