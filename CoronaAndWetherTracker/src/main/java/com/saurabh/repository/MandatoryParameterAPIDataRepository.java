package com.saurabh.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.saurabh.POJO.MandatoryParameterAPIData;


@Repository
public interface MandatoryParameterAPIDataRepository extends  
CrudRepository<MandatoryParameterAPIData, Integer> {
	
	@Query(value="select * from MandatoryParameterAPIData where APIName=?1", nativeQuery = true)
public MandatoryParameterAPIData retrieveAPIParam(String apiName);
}
