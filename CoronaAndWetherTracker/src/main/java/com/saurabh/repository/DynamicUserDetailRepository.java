package com.saurabh.repository;


import com.saurabh.POJO.ResponseData;


public interface DynamicUserDetailRepository 
{
   public ResponseData updateUserDetail(String sql);
}
