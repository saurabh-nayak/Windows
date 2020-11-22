package com.saurabh.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateHelper 
{
	SessionFactory sessionFactory=null;
	Session session=null;
	Metadata meta=null;
	Transaction t=null;StandardServiceRegistry serviceRegistry=null;
	
	private HibernateHelper(){}
	
	public static HibernateHelper getInstance()
	{      return new HibernateHelper();    }
	
   public Session getSession()
   {
	     serviceRegistry = new StandardServiceRegistryBuilder().configure().build();  
		 meta = new MetadataSources(serviceRegistry).getMetadataBuilder().build();  
		 sessionFactory = meta.getSessionFactoryBuilder().build();  
		 session = sessionFactory.openSession();  
		 return session;
   }
   
   public void closeResources(Session session)
   {
	   session.close();
	   sessionFactory.close();
	   serviceRegistry.close();
   }
   
   
}
