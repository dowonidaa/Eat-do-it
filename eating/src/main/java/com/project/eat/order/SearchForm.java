package com.project.eat.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
public class SearchForm {

    private String selectedType;
    private String searchOption;
    private String searchText;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.now();
    private int page=1;
    private int pageBlock=5;
}
