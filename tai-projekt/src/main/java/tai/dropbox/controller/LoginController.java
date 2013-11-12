package tai.dropbox.controller;

import java.util.Locale;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;


@Controller
public class LoginController {
	
	@Autowired
	private JdbcTemplate template;
	
	private static final String APP_KEY = "xwaq9ujw7aoskjm";
	private static final String APP_SECRET = "2znk2stxkigwknv";
	
	private DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
	private DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
	
	@RequestMapping("/index.htm")
	public ModelAndView getIndexPage() {
		
		Subject user = SecurityUtils.getSubject();		
		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[]{user.getPrincipal()}, String.class);

		if(accessToken == null){
			return getUnauthorized();
		}else{
			return getAuthorized(accessToken);
		}		

	}

	private ModelAndView getAuthorized(String accessToken) {
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}

	private ModelAndView getUnauthorized() {

		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(dbxRequestConfig, dbxAppInfo);
		String authorizeUrl = webAuth.start();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("unauthorized");
		mav.addObject("link", authorizeUrl);
		return mav;


	}


}
