package tai.dropbox.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.model.DbxFile;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
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
public class LoginController {

	private static final String WEB_AUTH_OBJECT_ATTR = "webAuthObject";
	@Autowired
	private JdbcTemplate template;

	private static final String APP_KEY = "xwaq9ujw7aoskjm";
	private static final String APP_SECRET = "2znk2stxkigwknv";

	private DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	@RequestMapping("/")
	public ModelAndView getMainPage() {

		Subject user = SecurityUtils.getSubject();
		System.out.println("USER" + user);

		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);

		if (accessToken != null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:main");
			return mav;
		} else {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("authorize");
			return mav;
		}

	}
	
	@RequestMapping("/login")
	public ModelAndView doLogin(ModelAndView mav){
		return mav;
	}

	@RequestMapping("/gotToken")
	public ModelAndView gotAccesToken(@RequestParam(required = false, value = "code") String code, @RequestParam(required = false, value = "state") String state) {

		Map<String, String[]> result = new HashMap<>();
		result.put("code", new String[] { code });
		result.put("state", new String[] { state });
		DbxWebAuth webAuth = (DbxWebAuth) SecurityUtils.getSubject().getSession().getAttribute(WEB_AUTH_OBJECT_ATTR);
		DbxAuthFinish authFinish=null;
		if (webAuth != null) {
			try {
				authFinish = webAuth.finish(result);
			} catch (BadRequestException e) {
				e.printStackTrace();
			} catch (BadStateException e) {
				e.printStackTrace();
			} catch (CsrfException e) {
				e.printStackTrace();
			} catch (NotApprovedException e) {
				e.printStackTrace();
			} catch (ProviderException e) {
				e.printStackTrace();
			} catch (DbxException e) {
				e.printStackTrace();
			}
			System.out.println(authFinish.accessToken);
			final String accessToken = authFinish.accessToken;
			final Subject subject = SecurityUtils.getSubject();
			template.update("update users set access_token = ? where username = ?",new PreparedStatementSetter() {			
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, accessToken);
					ps.setString(2, (String) subject.getPrincipal());
				}
			});
			ModelAndView mav = new ModelAndView();
			mav.setViewName("authComplete");
			mav.addObject("dropbox-token", authFinish.accessToken);
			return mav;
		}
		else{
			return null;
			//TODO Error handling
		}
	}

	@RequestMapping("/fetchToken")
	private String fetchToken(HttpServletRequest request) {

		HttpSession session = request.getSession();
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionKey);
		String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/gotToken";
		DbxWebAuth webAuth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo, redirectUrl, dbxSessionStore);
		String authorizeUrl = webAuth.start();

		SecurityUtils.getSubject().getSession().setAttribute(WEB_AUTH_OBJECT_ATTR, webAuth);

		return "redirect:" + authorizeUrl;

	}

	

}
