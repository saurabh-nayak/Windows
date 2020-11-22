package com.saurabh.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.saurabh.CustomException.AddUserException;
import com.saurabh.Enum.ResponseConstant;
import com.saurabh.Enum.ThirdPartyConstant;
import com.saurabh.POJO.MyUserDetails;
import com.saurabh.POJO.ResponseData;
import com.saurabh.POJO.User;
import com.saurabh.POJO.UserDetail;
import com.saurabh.operation.GenerateCurrentDate;
import com.saurabh.operation.GenerateUniqueId;
import com.saurabh.operation.JSONParser;
import com.saurabh.operation.JWTGenerator;
import com.saurabh.operation.ValidateMandatoryParameters;
import com.saurabh.operation.XMLParser;
import com.saurabh.repository.DynamicUserDetailRepository;
import com.saurabh.service.CacheManagerService;
import com.saurabh.service.MyUserDetailsService;
import com.saurabh.service.UserDetailService;
import com.saurabh.service.UserService;

@RestController
public class MyControllers {

	public static String sql;
	@Autowired
	ValidateMandatoryParameters validateMandatoryParameters;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	HttpServletRequest req;
	@Autowired
	JWTGenerator jwtGenerator;

	@Autowired
	MyUserDetailsService userDetailsService;

	@Autowired
	JSONParser jsonParser;
	
	@Autowired
	XMLParser xmlParser;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	UserService userService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CacheManagerService cacheManagerService;

	@Autowired
	DynamicUserDetailRepository dynamicUserDetailRepo;

	private static final Logger logger = LoggerFactory.getLogger(MyControllers.class);

	@RequestMapping(value = "/admin", method = { RequestMethod.GET })
	public Map<Integer, Object> admin() {
		Map<Integer, Object> userDetailMap = new HashMap<>();

		List<UserDetail> userDetailList = userDetailService.search();
		for (int i = 0; i < userDetailList.size(); i++) {
			userDetailMap.put(userDetailList.get(i).getId(), userDetailList.get(i));
		}
		return userDetailMap;
	}

	@RequestMapping(value = "/testException", method = { RequestMethod.POST })
	public void testExeceptions(@RequestBody String msg) throws AddUserException {
		if (msg.equals("not found")) {

			throw new AddUserException("test exception");
		}
		System.out.println("propagated");
	}

	@RequestMapping(value = "/getUser", method = { RequestMethod.POST })
	public List<UserDetail> admin(@RequestHeader(value = "name") String name)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, SQLException {
		if (name != null | !name.isEmpty()) {
			List<UserDetail> userDetailList;
			userDetailList = userDetailService.searchByName(name);
			return userDetailList;
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/user", method = { RequestMethod.POST })
	public String user() {

		return "hello user";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> signUp(@RequestBody String request) throws AddUserException {
		logger.info("in signup API");
		ResponseData response = null;
		Map<String, String> requestMap = null;
		requestMap = jsonParser.parse(request);
		User user = new User();
		@Valid
		UserDetail userDetail = new UserDetail();
		try {
			ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap, "signup");
			if (responseArray.get(0).equals(100)) {
				Set<Object> retrieveId = userService.retrieveAllId();
				int id = new GenerateUniqueId().generateId(retrieveId);
				user.setId(id);
				user.setPass(new BCryptPasswordEncoder().encode(requestMap.get("password")));
				user.setUserName(requestMap.get("username"));
				userDetail.setId(id);
				userDetail.setAddress(requestMap.get("address"));
				userDetail.setMobile(requestMap.get("mobile"));
				userDetail.setName(requestMap.get("name"));
				userDetail.setLastModified(GenerateCurrentDate.generateCurrentDateAndTime());
				User savedUser = userService.add(user);
				UserDetail savedUserDetail = null;
				try {
					savedUserDetail = userDetailService.add(userDetail);
				} catch (Exception e) {
					userService.delete(savedUser);
				}
				if (!savedUser.equals(null) && !savedUserDetail.equals(null)) {
					response = new ResponseData(ResponseConstant.SUCCESSFULL_SIGNUP.getErrorMessage(),
							ResponseConstant.SUCCESSFULL_SIGNUP.getErrorCode());
				} else {
					throw new AddUserException("add user encountered problem");
				}
			} else {
				response = new ResponseData(ResponseConstant.ERROR_SIGNUP.getErrorMessage(),
						ResponseConstant.ERROR_SIGNUP.getErrorCode());
			}
		} catch (AddUserException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
	public ResponseEntity<?> authenticateUser(@RequestBody String request, HttpServletRequest req) {
		logger.info("in authenticate API");
		String jwt = null;User user=null;
		System.out.println(req.getAttribute("username"));
//		req.getAttribute("Content-Type");
//		String reqFormat=null;
		
//		String reqFormat=req.getAttribute("Content-Type").toString();
		Map<String, String> requestMap = null;
//		if(reqFormat.equals("application/json"))
//		{
		requestMap = jsonParser.parse(request);
//		}
//		else if(reqFormat.equals("application/xml"))
//		{
//			requestMap=xmlParser.parse(request);
//		}
		ResponseEntity<ResponseData> response = null;
		ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap, "authenticate");
		if (responseArray.get(0).equals(100)) {

			try {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("username"),
								requestMap.get("password")));
				ServletContext context=req.getServletContext();
				Map<String,User> listUsers=(Map<String,User>)context.getAttribute("listUsers");
				Set<String> keySet=listUsers.keySet();
				if(keySet.contains(requestMap.get("username")))
				{
					user=listUsers.get(requestMap.get("username"));
				}
			MyUserDetails myUserDetails=new MyUserDetails(user);
				//				MyUserDetails myUserDetails = userDetailsService.loadUserByUsername(requestMap.get("username"));
				if (authentication.isAuthenticated()) {

					jwt = jwtGenerator.generateToken(myUserDetails);
					response = ResponseEntity.ok(new ResponseData(jwt, 200, "SUCCESS"));
					userDetailService.setLastLogin(GenerateCurrentDate.generateCurrentDateAndTime(),
							myUserDetails.getId());
				} else {
					response = ResponseEntity
							.ok(new ResponseData(ResponseConstant.USER_STATUS_NOT_ACTIVE.getErrorMessage(),
									ResponseConstant.USER_STATUS_NOT_ACTIVE.getErrorCode()));
				}
			} catch (BadCredentialsException e) {
				logger.error("bad credentials");
				response = ResponseEntity.ok(new ResponseData(ResponseConstant.BAD_CREDENTIAS.getErrorMessage(),
						ResponseConstant.BAD_CREDENTIAS.getErrorCode()));
			}

		} else {
			logger.debug("mandatory fields missing");
			response = ResponseEntity
					.ok(new ResponseData(jwt, ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode(),
							ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorMessage()));
		}
		return response;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> changePassword(@RequestBody String request) throws ClassNotFoundException, SQLException {
		ResponseData response = null;
//		boolean result;
		Map<String, String> requestMap = null;
		requestMap = new JSONParser().parse(request);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		String principal = userDetails.getUsername();
		ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap, "changePassword");
		if (responseArray.get(0).equals(100)) {
			String password = userDetails.getPassword();
			if (!new BCryptPasswordEncoder().matches(requestMap.get("newPassword"), password)) {
				String newPassword = new BCryptPasswordEncoder().encode(requestMap.get("newPassword"));
				int result = userService.changePassword(newPassword, principal);
				if (result == 1) {
					response = new ResponseData(ResponseConstant.PASSWORD_CHANGED_SUCCESSFULL.getErrorMessage(),
							ResponseConstant.PASSWORD_CHANGED_SUCCESSFULL.getErrorCode());
					userDetailService.setLastModified(GenerateCurrentDate.generateCurrentDateAndTime(),
							userDetails.getId());
				} else {
					response = new ResponseData(ResponseConstant.ERROR_CHANGE_PASSWORD.getErrorMessage(),
							ResponseConstant.ERROR_CHANGE_PASSWORD.getErrorCode());
				}

			} else {
				response = new ResponseData(ResponseConstant.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL.getErrorMessage(),
						ResponseConstant.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL.getErrorCode());
			}
		} else {
			response = new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorMessage(),
					ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode());
		}
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/getUnapprovedUsers", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getUnapprovedUsers() {
		List<Object> listUser = new ArrayList<>();
		ResponseEntity<?> response = null;
		try {
			listUser = userService.findUnApprovedUser();
			if (listUser.size() == 0) {
				response = ResponseEntity.ok(new ResponseData(ResponseConstant.NO_UNAPPROVED_USER.getErrorMessage(),
						ResponseConstant.NO_UNAPPROVED_USER.getErrorCode()));
			} else {
				response = ResponseEntity.ok(listUser);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

//    @RequestMapping(value="/*",method = { RequestMethod.POST, RequestMethod.GET}, consumes = "application/json" , produces = "application/json")
//    public ResponseEntity<?> notFound()
//    {
//        throw new ResourceNotFoundException(ResponseConstant.REQUEST_MAPPING_NOT_FOUND.getErrorMessage());
//        
//    }

	@RequestMapping("/updateUserDetails")
	public ResponseEntity<?> getUserDetails(@RequestBody String request) {
		Map<String, String> requestMap = null;
		ResponseData response = null;
//    	Session session=null;
//    	EntityManager entityManager=null;
		// HibernateHelper hibernateHelper=HibernateHelper.getInstance();
//    	JPAHelper jpaHelper=JPAHelper.getInsatance();
//    	Transaction t=null;
		sql = "UPDATE userDetail SET lastModified = '" + GenerateCurrentDate.generateCurrentDateAndTime() + "' , ";
		requestMap = jsonParser.parse(request);
		ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap,
				"updateUserDetails");
		if (responseArray.get(0).equals(100)) {
			Set<String> keySet = requestMap.keySet();
			for (String queryKey : keySet) {
				sql = sql + queryKey + "='" + requestMap.get(queryKey) + "'" + ",";
			}
			sql = sql.substring(0, sql.lastIndexOf(","));
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			sql = sql + " where id= " + userDetails.getId();
//				try {
//					entityManager=jpaHelper.getEntityManager();
//					entityManager.getTransaction().begin();
//					Query queryPersistence=entityManager.createNativeQuery(sql);
//					int result=queryPersistence.executeUpdate();
//					session=hibernateHelper.getSession();
//				    t = session.beginTransaction(); 
//				    int result=session.createSQLQuery(sql).executeUpdate();

			response = dynamicUserDetailRepo.updateUserDetail(sql);
//				if(result==1)
//				{
//				   t.commit();
//					entityManager.getTransaction().commit();
//				   response=new ResponseData(ResponseConstant.DATA_UPDATED.getErrorMessage(),ResponseConstant.DATA_UPDATED.getErrorCode());
//				}else {
//					entityManager.getTransaction().rollback();
//					response=new ResponseData(ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorMessage(),ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorCode());}
//				}catch(Exception e)
//				{ }
//				finally {
//				    hibernateHelper.closeResources(session);
//				   entityManager.close();

//			}
//				}else {
//					response=new ResponseData(ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorMessage(),ResponseConstant.PROBLEM_OCCURED_DURING_UPDATE_DATA.getErrorCode());
		}

		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/getWether", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getWeatherData(@RequestBody String request) {

		Map<String, String> requestMap = null;
		Map<String, String> responseMap = null;
		requestMap = jsonParser.parse(request);
		String responseBody = null;
		// HttpResponse<String> response=null;
		ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap, "getWether");
		if (responseArray.get(0).equals(100)) {
//			
//			response = Unirest.get(ThirdPartyConstant.URL.WETHER.getURL()+requestMap.get("location"))
//				.header(ThirdPartyConstant.Header.WETHERHOST.getKey(), ThirdPartyConstant.Header.WETHERHOST.getValue())
//				.header(ThirdPartyConstant.Header.KEY.getKey(), ThirdPartyConstant.Header.KEY.getValue())
//			HttpHeaders headers=new HttpHeaders();
//			headers.add(ThirdPartyConstant.Header.WETHERHOST.getKey(), ThirdPartyConstant.Header.WETHERHOST.getValue());
//			headers.add(ThirdPartyConstant.Header.KEY.getKey(), ThirdPartyConstant.Header.KEY.getValue());
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.put(ThirdPartyConstant.Header.WETHERHOST.getKey(),
					Arrays.asList(ThirdPartyConstant.Header.WETHERHOST.getValue()));
			map.put(ThirdPartyConstant.Header.KEY.getKey(), Arrays.asList(ThirdPartyConstant.Header.KEY.getValue()));
			HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(map);
			ResponseEntity<String> responseEntity = restTemplate.exchange(
					ThirdPartyConstant.URL.WETHER.getURL() + requestMap.get("location"), HttpMethod.GET, requestEntity,
					String.class);
			responseBody = responseEntity.getBody();
//			responseBody=response.getBody();
			if (responseBody.contains("test(") && responseBody.contains(")")) {
				StringBuffer buffer = new StringBuffer(responseBody);
				buffer.deleteCharAt(buffer.lastIndexOf(")"));
				buffer.delete(0, buffer.indexOf("(") + 1);
				responseMap = new JSONParser().parse(buffer.toString());

			} else {
				responseMap = new JSONParser().parse(responseBody.toString());
			}

		} else {
			ResponseEntity.ok(new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorMessage(),
					ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode()));
		}
		
/*HATEOAS implementation to send related URLs in response*/
		
		Resource<Map<String, String>> resource = new Resource<Map<String, String>>(responseMap);
		String uriString = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
		URI url = ServletUriComponentsBuilder.fromPath(uriString + "/getCorona").build().toUri();
		resource.add(new Link(url.toString()));
//		URI location=ServletUriComponentsBuilder.fromCurrentContextPath()..path("/{id}").buildAndExpand(sevedUser.getId()).toUri();  
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/getCorona", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getCorona(@RequestBody String request) {
//		Map<String,Map<String,String>> cacheThroughDate=new HashMap<>();
		Map<String, String> requestMap = null;
		requestMap = jsonParser.parse(request);
		Map<String, String> responseMap = null;
		String subURl = null;
//		HttpResponse<String> response=null;
		ArrayList<Object> responseArray = validateMandatoryParameters.mandatoryParameters(requestMap, "getCorona");
		if (responseArray.get(0).equals(100) && responseArray.get(1).equals("end_date")) {
			String start_date = requestMap.get("start_date");
			String end_date = requestMap.get("end_date");
			Object cacheResponse = cacheManagerService.cacheIsAvailable(start_date + "," + end_date);
			if (cacheResponse.equals(Boolean.FALSE)) {
				subURl = "start_date=" + start_date + "&end_date=" + end_date;
//			response = Unirest.get(ThirdPartyConstant.URL.CORONA.getURL()+subURl)
//					.header(ThirdPartyConstant.Header.CORONAHOST.getKey(),ThirdPartyConstant.Header.CORONAHOST.getValue())
//					.header(ThirdPartyConstant.Header.KEY.getKey(), ThirdPartyConstant.Header.KEY.getValue())
//					.asString();

				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				map.put(ThirdPartyConstant.Header.CORONAHOST.getKey(),
						Arrays.asList(ThirdPartyConstant.Header.CORONAHOST.getValue()));
				map.put(ThirdPartyConstant.Header.KEY.getKey(),
						Arrays.asList(ThirdPartyConstant.Header.KEY.getValue()));
				HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(map);
				ResponseEntity<String> responseEntity = restTemplate.exchange(
						ThirdPartyConstant.URL.CORONA.getURL() + subURl, HttpMethod.GET, requestEntity, String.class);
				responseMap = jsonParser.parse(responseEntity.getBody());
				cacheManagerService.setCache(start_date + "," + end_date, responseEntity.getBody());
			} else {
				try {
					responseMap = (Map<String, String>) cacheResponse;
				} catch (Exception e) {
					System.out.println("error occurred in conversion from object to map : " + e.getMessage());
				}
			}
		} else if (responseArray.get(0).equals(100) && responseArray.get(1).equals("country")) {
//			response = Unirest.get(ThirdPartyConstant.URL.CORONARAPID.getURL()+requestMap.get("country"))
//					.header(ThirdPartyConstant.Header.CORONARAPIDHOST.getKey(),ThirdPartyConstant.Header.CORONARAPIDHOST.getValue())
//					.header(ThirdPartyConstant.Header.KEY.getKey(),ThirdPartyConstant.Header.KEY.getValue())
//					.asString();
			Object cacheResponse = cacheManagerService.cacheIsAvailable("country");
			if (cacheResponse.equals(Boolean.FALSE)) {
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				map.put(ThirdPartyConstant.Header.CORONARAPIDHOST.getKey(),
						Arrays.asList(ThirdPartyConstant.Header.CORONARAPIDHOST.getValue()));
				map.put(ThirdPartyConstant.Header.KEY.getKey(),
						Arrays.asList(ThirdPartyConstant.Header.KEY.getValue()));
				HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(map);
				ResponseEntity<String> responseEntity = restTemplate.exchange(
						ThirdPartyConstant.URL.CORONARAPID.getURL() + requestMap.get("country"), HttpMethod.GET,
						requestEntity, String.class);
				responseMap = jsonParser.parse(responseEntity.getBody());
				cacheManagerService.setCache("country", responseEntity.getBody());
			} else {
				responseMap = (Map<String, String>) cacheResponse;
			}
		} else {
			return ResponseEntity.ok(new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorMessage(),
					ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode()));
		}
		return ResponseEntity.ok(responseMap);
	}

}
