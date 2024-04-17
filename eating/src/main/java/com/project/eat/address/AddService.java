package com.project.eat.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddService {


    @Autowired
    private AddressDAO_JPA add;

    public List<Address> selectAll_add() {
        return add.findAll();
    }
}
