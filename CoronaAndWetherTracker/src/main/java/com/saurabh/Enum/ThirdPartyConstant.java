package com.saurabh.Enum;

public class ThirdPartyConstant {
	
	
	public enum URL
	{
		
		CORONA("https://covid-19-india-data-by-zt.p.rapidapi.com/GetIndiaHistoricalDataBetweenDates?"),
		CORONARAPID("https://covid-193.p.rapidapi.com/statistics?country="),
		WETHER("https://community-open-weather-map.p.rapidapi.com/weather?callback=test&id=2172797&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=")
		;
		String url;
		URL(String url)
		{
			this.url=url;
		}
		public String getURL()
		{
			return url;
		}
	}
	
	public enum Header
	{
		CORONAHOST("x-rapidapi-host","covid-19-india-data-by-zt.p.rapidapi.com"),
		CORONARAPIDHOST("x-rapidapi-host","covid-193.p.rapidapi.com"),
		WETHERHOST("x-rapidapi-host","community-open-weather-map.p.rapidapi.com"),
		KEY("x-rapidapi-key","3b72fbd0c2mshe3df2f23f03a462p19f203jsndc7fa4a51e4b")
		;
		
		String key;String value;
		
		Header(String key,String value)
		{
			this.key=key;
			this.value=value;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public String getValue()
		{
			return value;
		}
		
	}

}
