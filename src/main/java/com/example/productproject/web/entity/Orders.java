package com.example.productproject.web.entity;

import com.example.productproject.web.dto.OrdersDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private String name;
    private String mail;
    private String phone;
    private String city;
    private String country;
    private String line1;
    private String line2;
    @Column(name = "postal_code")
    private String postalCode;
    private String state;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "orders")
    private Set<OrdersItems> ordersItemsSet;
}
