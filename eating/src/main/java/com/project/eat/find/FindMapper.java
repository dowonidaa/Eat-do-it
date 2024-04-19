package com.project.eat.find;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FindMapper {
    //유저Id 찾기
    public List<String> findId(String email);
}


