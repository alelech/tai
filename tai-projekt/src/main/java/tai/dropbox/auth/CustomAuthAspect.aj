package tai.dropbox.auth;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

public aspect CustomAuthAspect extends AuthAspect {

	pointcut returnsMav(): execution(@NeedsAuthentication ModelAndView *(..));
	pointcut methodWithRole(HasRole role): execution( String *(..)) && @annotation(role);
	
	protected String getNotLoggedViewName(){
		return "not_logged";
	}

}
