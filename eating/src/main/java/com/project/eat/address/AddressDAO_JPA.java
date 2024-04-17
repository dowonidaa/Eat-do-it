package com.project.eat.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressDAO_JPA extends JpaRepository<Address, Object>{

	//***jpa에 내장된 함수 또는 약속된 규칙에 맞게 정의한 함수.*** 
	public List<Address> findAll();

}//end interface
