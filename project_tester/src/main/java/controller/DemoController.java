package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import model.CategoryIF;
import model.Project;
import model.ProjectUpload;
import model.Spot;
import security.TokenAuthenticationServiceIF;
import service.AccountServiceIF;
import service.UploadServiceIF;

/**
 * Ein TestController f�r Unittests und zur Erprobung von Ideen
 * @author John
 *
 */
@Controller
@EnableWebMvc
public class DemoController {

	@Autowired
	private TokenAuthenticationServiceIF tokenService;
	
	@Autowired
	private AccountServiceIF accountService;
	
	@Autowired
	private UploadServiceIF uploadService;
	
	private enum SpotParaMap {
		TYPE("spot.type"),
		CITY("spot.city"),
		NAME("spot.name"),
		STREET("spot.street"),
		NUMBER("spot.number"),
		ZIP_CODE("spot.zip_code"),
		TIMES("spot.times");
		
		private String name;
		
		private SpotParaMap(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	
	private enum ProjParaMap {
		ID("project.id"),
		NAME("project.name"),
		CATEGORIES("project.categories"),
		PUBLISHED("project.published"),
		DURATION("project.duration"),
		LIKES("project.likes"),
		DESCRIPTION("project.description"),
		INNOVATION("project.innovation"),
		MOTIVATION("project.motivation"),
		REWARD("project.reward"),
		HASHTAGS("project.hashtags");

		private String name;

		private ProjParaMap(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public final static String URL_DEMO_FILEUPLOAD = "/demo/file";
	@RequestMapping(value=URL_DEMO_FILEUPLOAD, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String fileUploadTest(HttpServletRequest request) throws IOException, ServletException {
		
		String header = request.getHeader("Authorization");
		
		System.out.println("DemoController.fileUploadTest()");
		
		Map<String, String[]> paraMap = request.getParameterMap();
		String[] id = paraMap.get(ProjParaMap.ID.getName());
		String[] name = paraMap.get(ProjParaMap.NAME.getName());
		String[] categories = paraMap.get(ProjParaMap.CATEGORIES.getName());
		String[] duration = paraMap.get(ProjParaMap.DURATION.getName());
		String[] description = paraMap.get(ProjParaMap.DESCRIPTION.getName());
		String[] innovation = paraMap.get(ProjParaMap.INNOVATION.getName());
		String[] motivation = paraMap.get(ProjParaMap.MOTIVATION.getName());
		String[] reward = paraMap.get(ProjParaMap.REWARD.getName());
		String[] hashtags = paraMap.get(ProjParaMap.HASHTAGS.getName());

//		String[] citySpot = paraMap.get(SpotParaMap.CITY.getName());
//		String[] nameSpot = paraMap.get(SpotParaMap.NAME.getName());
//		String[] streetSpot = paraMap.get(SpotParaMap.STREET.getName());
//		String[] numberSpot = paraMap.get(SpotParaMap.NUMBER.getName());
//		String[] zipCodeSpot = paraMap.get(SpotParaMap.ZIP_CODE.getName());
//		String[] timesSpot = paraMap.get(SpotParaMap.TIMES.getName());
		
		String[] typeSpot = paraMap.get(SpotParaMap.TYPE.getName());

		Spot spot = new Spot();
		spot.setType(typeSpot[0]);
		
		Set<String> categoriesToSet = new HashSet<String>();
		categoriesToSet.addAll(Arrays.asList(categories));
		
		Set<String> hashTagsToSet = new HashSet<String>();
		hashTagsToSet.addAll(Arrays.asList(hashtags));
		
		
		Project project = new Project(name[0],
				categoriesToSet,
				Integer.parseInt(duration[0]),
				description[0],
				innovation[0],
				motivation[0],
				reward[0],
			 	hashTagsToSet);
		
		Map<String, MultipartFile> fileMap = ((DefaultMultipartHttpServletRequest)request).getFileMap();
				
		
		return "test";
	}
	

	public final static String URL_USER_DEMO_SIGNUP = "/demo/signup";
	@RequestMapping(value=URL_USER_DEMO_SIGNUP, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String test(){
		String result = "TEST1 /auth/signup -> Controller reached!";
		System.err.println(result);
		return result;
	}
	
	public static final String URL_USER_DEMO_LOGIN = "/demo/login"; 
	@RequestMapping(value=URL_USER_DEMO_LOGIN, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody JsonTokenIF test2(){
		SecurityContext context = SecurityContextHolder.getContext();
		JsonTokenIF result = accountService.createToken(context.getAuthentication().getName());
		context.setAuthentication(null);
		return result;		
	}
	
	public static final String URL_USER_TEST_TOKEN = "/demo/token";
	@RequestMapping(value=URL_USER_TEST_TOKEN, method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String test3() {	
		String result = "TEST3 /account/token -> Controller reached!";
		System.err.println(result);
		return result;
	}
}