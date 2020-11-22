package com.saurabh.repository;



import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.saurabh.POJO.User;


@Component
public interface UserRepository extends JpaRepository<User, Integer>, CrudRepository<User,Integer>{
	
	@Query(value="select id , pass , roles , userName , status from user where userName = ?1 and status<> 2",nativeQuery = true )
	Optional<User> findByUserName(String userName);
	
	@Query(value="select id from user", nativeQuery = true)
	public Set<Object> retrieveAllId();
	
	
	@Query(value="SELECT id, userName , roles, status FROM user where status = 0 or status = 2" , nativeQuery = true)
	public List<Object> findUnApprovedUser();
	
	@Modifying
	@Transactional(rollbackFor  = Exception.class)
	@Query(value="UPDATE user set pass= ?1 where username=?2", nativeQuery = true)
	public int updatePassword(String password, String username);
	
	@Query(value="SELECT id , pass , roles , userName , status FROM user", nativeQuery = true)
	public List<User> getAllUsers();

}
