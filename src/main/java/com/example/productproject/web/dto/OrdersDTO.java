package com.example.productproject.web.dto;

import com.example.productproject.web.entity.Orders;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class OrdersDTO {
    @Size(min = 1, message = "orders.error.quantity")
    @Valid // TODO remember that this tag makes the validator catch inside orders item dto
    private List<OrdersItemDTO> products;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String mail;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String phone;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String city;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String country;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String line1;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String line2;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String postalCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String state;

    public OrdersDTO(Orders orders){
        this.id = orders.getId();
        this.name = orders.getName();
        this.mail = orders.getMail();
        this.phone = orders.getPhone();
        this.city = orders.getCity();
        this.country = orders.getCountry();
        this.line1 = orders.getLine1();
        this.line2 = orders.getLine2();
        this.postalCode = orders.getPostalCode();
        this.state = orders.getState();
        this.products = orders.getOrdersItemsSet().stream().map(OrdersItemDTO::new).collect(Collectors.toList());
    }
}
