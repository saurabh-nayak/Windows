package com.saurabh.repository;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.saurabh.Enum.ResponseConstant;
import com.saurabh.POJO.ResponseData;
@Repository
public class DynamicUpdateUserDetailRepository implements DynamicUserDetailRepository{
	@PersistenceUnit
	EntityManagerFactory entityManagerFactory;
	
	ResponseData response;
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Modifying
	public ResponseData updateUserDetail(String sql) {
		// TODO Auto-generated method stub
		try {
		EntityManager entityManager=entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		Query queryPersistence=entityManager.createNativeQuery(sql);
		int result=queryPersistence.executeUpdate();
		if(result==1)
		{
			entityManager.getTransaction().commit();
			 response=new ResponseData(ResponseConstant.DATA_UPDATED.getErrorMessage(),ResponseConstant.DATA_UPDATED.getErrorCode());
		}else {
			entityManager.getTransaction().rollback();
			response=new ResponseData(ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorMessage(),ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorCode());
		}
	}catch(Exception e) {
		System.out.println("Exception occured: "+e.getMessage());
	}
		return response;
		}

}
