package com.example.productproject.web.service;

import com.example.productproject.exception.InvalidProductException;
import com.example.productproject.web.dto.OrdersDTO;
import com.example.productproject.web.dto.OrdersItemDTO;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    private final ProductsRepository productsRepository;

    @Autowired
    public OrdersService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public String createSessionCheckout(OrdersDTO ordersDTO) throws InvalidProductException {
        List<OrdersItemDTO> orderItemsList = ordersDTO.getProducts();
        // Maps the product and the quantity
        HashMap<Products, Integer> map = new HashMap<>();
        for(OrdersItemDTO item : orderItemsList){
            Optional<Products> products = productsRepository.findById(item.getProductID());
            if(products.isEmpty()) throw new InvalidProductException(item.getProductID(), "orders.error.item.productExists");
            map.compute(products.get(), (k,v) -> {
                if(v == null) return item.getQuantity();

                return v + item.getQuantity();
            });
        }
        System.out.println(map);
        return "";
    }
}
