package com.project.eat.review;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BadWordRepository extends JpaRepository<BadwordVO, Object> {

}
