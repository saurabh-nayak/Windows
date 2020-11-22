package com.saurabh.operation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateCurrentDate 
{
    public static String generateCurrentDateAndTime()
    {
    	DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	LocalDateTime dateTime=LocalDateTime.now();
    	return format.format(dateTime);
    }
}
