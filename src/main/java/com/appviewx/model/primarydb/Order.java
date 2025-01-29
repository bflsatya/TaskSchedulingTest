package com.appviewx.model.primarydb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;

    private String totalAmount;

    private Date orderTime;

}
