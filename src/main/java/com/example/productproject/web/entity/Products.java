package com.example.productproject.web.entity;

import com.example.productproject.web.dto.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private String name;
    private String description;
    private Float price;
    public Products(Product product){
        this.id = null;
        this.name = product.name();
        this.description = product.description();
        this.price = product.price();
    }
}
