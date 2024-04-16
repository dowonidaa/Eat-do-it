package com.project.eat.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter

public class Payment {

    @Id
    private int payCode;

    private int odrCode;
    private String payMethod;
    private LocalDateTime payDate;
    private int payTotPrice;
    private int payRestPrice;
    private String payNobankUser;
    private String payNobank;


}
