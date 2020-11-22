package com.saurabh.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAHelper 
{
	public static  JPAHelper getInsatance()
	{
		return new JPAHelper();
	}
   public EntityManager getEntityManager()
   {
	   EntityManagerFactory managerFactory=Persistence.createEntityManagerFactory("userDetail");
	   EntityManager entityManager=managerFactory.createEntityManager();
	   return entityManager;
   }
}
