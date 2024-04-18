package com.project.eat.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Setter
@Getter
public class DateForm {

    private OrderType orderType;
    private String searchOption;
    private String searchText;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.now();
}
