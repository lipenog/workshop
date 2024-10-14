package com.example.productproject.web.controller;

import com.example.productproject.web.service.OrdersService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class StripeEventsController {
    private final OrdersService ordersService;
    @Value("${stripe.api.secret}")
    private String endpointSecret;

    @Autowired
    public StripeEventsController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // trata o evento
        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted((Session) stripeObject);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
        return ResponseEntity.ok("");
    }

    private void handleCheckoutSessionCompleted(Session session){
        ordersService.updatePayedOrder(session);
    }

}
