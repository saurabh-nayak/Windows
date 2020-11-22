package com.saurabh.repository;

import java.util.HashMap;
import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saurabh.Enum.MandatoryParameters;
import com.saurabh.POJO.UserDetail;
import com.saurabh.controller.MyControllers;






@Repository
public interface UserDetailRepository extends CrudRepository<UserDetail,Integer> ,JpaRepository<UserDetail,Integer>
{
	   @Query(value="select * from userDetail where name =?1", nativeQuery = true)
       public List<UserDetail> readuserDetailByName(String name);
        
	   @Query(value="select u.id,u.address,u.mobile,u.name from userDetail u", nativeQuery = true)
	   public List<UserDetail> findAllElements();

	   @Modifying
	   @Transactional(rollbackFor = Exception.class)
	   @Query(value="update userDetail set lastLogin =?1 where id=?2", nativeQuery = true)
	   public void lastLogin(String lastLogin, int id);
	   
	   @Modifying
	   @Transactional(rollbackFor = Exception.class)
	   @Query(value="update userDetail set lastModified =?1 where id=?2", nativeQuery = true)
	   public void lastModified(String lastModified,int id);
	   
//	   @Modifying
//	   @Transactional
//	   @Query(MandatoryParameters.MOBILE.getParameterName())
//	   public void updateUserDetails(String sql,HashMap<String, String> queryMap);
    		   
}
