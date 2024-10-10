package com.example.productproject.web.dto;

import com.example.productproject.web.entity.OrdersItems;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long productID;
    @NotNull(message = "orders.error.item.productQuantity")
    @Min(value = 1, message = "orders.error.item.productQuantity")
    private int quantity;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductsDTO productsDTO;

    public OrdersItemDTO(OrdersItems item){
        this.productID = item.getProducts().getId();
        this.productsDTO = new ProductsDTO(item.getProducts());
        this.quantity = item.getQuantity();
    }
}
