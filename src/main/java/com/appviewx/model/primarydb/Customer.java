package com.appviewx.model.primarydb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private UUID customerId;

    private String name;

    private String address;

}
