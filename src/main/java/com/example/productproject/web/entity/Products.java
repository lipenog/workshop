package com.example.productproject.web.entity;

import com.example.productproject.web.dto.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Products {
    @Id
    private Long id;
    private String name;
    private String description;
    private Float price;
    public Products(Product product){
        this.id = 0L;
        this.name = product.name();
        this.description = product.description();
        this.price = product.price();
    }
}
