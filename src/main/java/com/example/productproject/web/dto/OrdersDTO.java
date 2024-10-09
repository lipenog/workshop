package com.example.productproject.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class OrdersDTO {
    @Size(min = 1, message = "orders.error.quantity")
    private List<OrdersItemDTO> products;
}
