package com.example.productproject.web.entity;

import com.example.productproject.web.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    private LocalDate creation;
    public Products(ProductDTO productDTO){
        this.id = null;
        this.name = productDTO.getName();
        this.description = productDTO.getDescription();
        this.price = productDTO.getPrice();
    }

    @PrePersist
    private void setCreationDate(){
        this.creation = LocalDate.now(ZoneId.of("UTC"));
    }

}
