package com.example.productproject.web.service;

import com.example.productproject.exception.InvalidProductException;
import com.example.productproject.web.dto.OrdersDTO;
import com.example.productproject.web.dto.OrdersItemDTO;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.ProductsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
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

    public String createSessionCheckout(OrdersDTO ordersDTO) throws InvalidProductException, StripeException {
        List<OrdersItemDTO> orderItemsList = ordersDTO.getProducts();
        // Maps the stripe price id and the quantity
        HashMap<String, Integer> map = new HashMap<>();
        for(OrdersItemDTO item : orderItemsList){
            // get the product based on the id
            Optional<Products> products = productsRepository.findById(item.getProductID());
            if(products.isEmpty()) throw new InvalidProductException(item.getProductID(), "orders.error.item.productExists");
            // get the stripe product with the stripe id
            Product resource = Product.retrieve(products.get().getStripeID());
            // if the key is already in the map sums the quantities
            map.compute(resource.getDefaultPrice(), (k,v) -> {
                if(v == null) return item.getQuantity();

                return v + item.getQuantity();
            });
        }
        Session session = createPaymentLink(map);
        return session.getUrl();
    }

    private Session createPaymentLink(HashMap<String, Integer> map) throws StripeException {
        // gera o link de pagamento
        SessionCreateParams sessionParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://www.pudim.com.br/")
                .setShippingAddressCollection(
                        SessionCreateParams.
                                ShippingAddressCollection.builder()
                                .addAllAllowedCountry(getAllowedCountries())
                                .build())
                .addAllLineItem(getLineItems(map))
                .build();
        return Session.create(sessionParams);
    }

    private List<SessionCreateParams.ShippingAddressCollection.AllowedCountry> getAllowedCountries(){
        return List.of(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US,
                SessionCreateParams.ShippingAddressCollection.AllowedCountry.BR,
                SessionCreateParams.ShippingAddressCollection.AllowedCountry.FR);
    }

    private List<SessionCreateParams.LineItem> getLineItems(HashMap<String, Integer> map) {
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
