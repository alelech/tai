package tai.dropbox.controller;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.DbxFile;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.Folder;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;


@Controller
@Scope("session")
public class LoginController {
	
	@Autowired
	private JdbcTemplate template;
	private String accessToken;
	
	private static final String APP_KEY = "xwaq9ujw7aoskjm";
	private static final String APP_SECRET = "2znk2stxkigwknv";
	
	private DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
	private DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());

	private DbxWebAuth webAuth;
	
	@RequestMapping("/")
	public String getMainPage(HttpServletRequest request){
		return getIndexPage(request);
	}
	
	@RequestMapping("/logSuccess")
	public String getIndexPage(HttpServletRequest request) {
		
		Subject user = SecurityUtils.getSubject();		
		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[]{user.getPrincipal()}, String.class);

		if(accessToken == null){
			return getUnauthorized(request);
		}else{
			return "/";
//			return getAuthorized(accessToken);
//			return getFiles().toString();

		}		

	}
	
	@RequestMapping("/mainView")
	public ModelAndView unauthorizedLogin(ModelAndView mav){
		System.out.println("ABC");
		return mav;
	}

	private ModelAndView getAuthorized(String accessToken) {
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping("/gotToken")
	public void gotAccesToken(HttpServletRequest req, @RequestParam(required=false,value="code")String code,@RequestParam(required=false,value="state")String state){
		System.out.println("TOKEN !");
		Map<String, String[]> result = new HashMap<>();
		result.put("code", new String[]{code});
		result.put("state", new String[]{state});
		DbxAuthFinish authFinish=null;
		try {
			authFinish = webAuth.finish(result);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsrfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotApprovedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(authFinish.accessToken);
		
//		
//		 if (requestToken == null) return error("Couldn't find request token in session.");
//		 session.removeAttribute("dropbox-request-token");
//
//		 // Check 'oauth_token' to make sure this request is really from Dropbox.
//		 String key = request.getParameter("oauth_token");
//		 if (key == null) return error("Missing parameter 'oauth_token'.");
//		 if (!secureStringEquals(key, requestToken.key)) return error("Invalid 'oauth_token' parameter.");
//
//		 // Finish authorization to get an "access token".
//		 DbxAuthFinish authFinish;
//		 try {
//		     authFinish = auth.finish(requestToken);
//		 }
//		 DbxAccessToken accessToken = authResponse.accessToken;
	}

	private String getUnauthorized(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionKey);
		String redirectUrl = request.getScheme()+"://"
				+request.getServerName()+":"
				+request.getServerPort()
				+request.getContextPath()+"/gotToken";
		webAuth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo, redirectUrl, dbxSessionStore);
		String authorizeUrl = webAuth.start();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("unauthorized");
		mav.addObject("link", authorizeUrl);
		
		return "redirect:"+authorizeUrl;

	}

	@RequestMapping("/files.htm")
	public ModelAndView getFiles() {
		return getDropboxFolderModelAndView("/");

	}
	
	@RequestMapping(value = "/folder.htm", method = RequestMethod.GET)
	public ModelAndView folderDetails(Model model, @RequestParam("path") String path) {
		return getDropboxFolderModelAndView(path);

	}

	private ModelAndView getDropboxFolderModelAndView(String path) {
		List<DbxFile> files = new ArrayList<DbxFile>(); 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("files");

		try {
			DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
			System.out.println("Linked account: " + client.getAccountInfo().displayName);

			
			DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
			for(DbxEntry child : listing.children){
				DbxFile file = new DbxFile();
				file.setName(child.name);
				file.setImg("res/img/" + child.iconName + ".gif");
				file.setPath(child.path);
				file.setFolder(child.isFolder());				
				files.add(file);
			}
			mav.addObject("username", client.getAccountInfo().displayName);

		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		mav.addObject("files", files);
		return mav;
	}
	


}
