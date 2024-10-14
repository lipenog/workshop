package com.example.productproject.web.service;

import com.example.productproject.exception.InvalidProductException;
import com.example.productproject.web.dto.OrdersDTO;
import com.example.productproject.web.dto.OrdersItemDTO;
import com.example.productproject.web.entity.Orders;
import com.example.productproject.web.entity.OrdersItems;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.OrdersRepository;
import com.example.productproject.web.repository.ProductsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository, ProductsRepository productsRepository) {
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
    }
    public List<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }
    public String createSessionCheckout(OrdersDTO ordersDTO) throws InvalidProductException, StripeException {
        Orders entity = new Orders();

        List<OrdersItemDTO> orderItemsList = ordersDTO.getProducts();

        // Maps the stripe price id and the quantity
        HashMap<Products, Integer> map = new HashMap<>();
        orderItemsList.forEach(item -> {
            // get the product based on the id
            Optional<Products> products = productsRepository.findById(item.getProductID());
            if(products.isEmpty()) throw new InvalidProductException(item.getProductID(), "orders.error.item.productExists");
            // if the key is already in the map sums the quantities
            map.compute(products.get(), (k, v) -> {
                if(v == null) return item.getQuantity();
                return item.getQuantity() + v;
            });
        });

        // Maps the order items entity
        Set<OrdersItems> ordersItemsEntitySet = map.entrySet()
                .stream()
                .map(e -> new OrdersItems(null, entity ,e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
        entity.setOrdersItemsSet(ordersItemsEntitySet);

        // Persists the order entity with the items
        Orders orders = ordersRepository.save(entity);
        
        Session session = createPaymentLink(map, orders.getId());
        return session.getUrl();
    }
    private Session createPaymentLink(HashMap<Products, Integer> map, Long orderID) throws StripeException {
        Map<String, Integer> priceIDMap = map.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                productsIntegerEntry -> {
                                    Products products = productsIntegerEntry.getKey();
                                    try {
                                        // get the stripe product with the stripe id
                                        Product resource = Product.retrieve(products.getStripeID());
                                        // makes the map key the price ID
                                        return resource.getDefaultPrice();
                                    } catch (StripeException e) {
                                        throw new InvalidProductException(products.getId(), "order.error.item.productStripe");
                                    }
                                },
                                // keeps the quantity
                                Map.Entry::getValue
                        )
                );

        // gera o link de pagamento
        SessionCreateParams sessionParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://www.pudim.com.br/")
                .setShippingAddressCollection(
                        SessionCreateParams.
                                ShippingAddressCollection.builder()
                                .addAllAllowedCountry(getAllowedCountries())
                                .build())
                .addAllLineItem(getLineItems(priceIDMap))
                .putMetadata("orderID", String.valueOf(orderID))
                .build();
        return Session.create(sessionParams);
    }

    private List<SessionCreateParams.ShippingAddressCollection.AllowedCountry> getAllowedCountries(){
        return List.of(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US,
                SessionCreateParams.ShippingAddressCollection.AllowedCountry.BR,
                SessionCreateParams.ShippingAddressCollection.AllowedCountry.FR);
    }

    private List<SessionCreateParams.LineItem> getLineItems(Map<String, Integer> map) {
        return map.entrySet().stream().map(e -> {
            String priceID = e.getKey();
            Integer quantity = e.getValue();

            return SessionCreateParams
                    .LineItem.builder()
                    .setQuantity(Long.valueOf(quantity))
                    .setPrice(priceID)
                    .build();
        }).toList();
    }
}
