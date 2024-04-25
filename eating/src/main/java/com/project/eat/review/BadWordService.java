package com.project.eat.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
//import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BadWordService {


    @Autowired
    private BadWordRepository badWordRepository;


    public List<BadwordVO> badwordVOList(){
        return badWordRepository.findAll();
    }

}
