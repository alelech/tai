package tai.dropbox.auth;

import java.util.logging.Logger;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

public abstract aspect AuthAspect {

	private static Logger logger = Logger.getLogger(AuthAspect.class.getName());
    abstract pointcut returnsMav();

    abstract pointcut methodWithRole(HasRole role);

    String around(HasRole role): methodWithRole(role){
        if(SecurityUtils.getSubject().hasRole(role.role())){
            return proceed(role);
        }
        else{
        	logger.info("Nieuprawniona próba dostêpu ");
            return "";
        }
    }

    ModelAndView around(): returnsMav(){
        boolean isAuth = SecurityUtils.getSubject().isAuthenticated();
        if (isAuth) {
            return proceed();
        } else {
            ModelAndView mav = new ModelAndView(getNotLoggedViewName());
            return mav;
        }
    }
   
    ModelAndView around(): returnsMav() && execution(* *.getAdminPanel()){
    	boolean isAuth = SecurityUtils.getSubject().isAuthenticated();
        if (isAuth && SecurityUtils.getSubject().hasRole("administrator")) {
            ModelAndView mav = new ModelAndView("authorizedAdmin");
            return mav;
        } else {
        	logger.info("Nieuprawniona próba dostêpu przez u¿ytkownika " + SecurityUtils.getSubject().getPrincipal());
            ModelAndView mav = new ModelAndView("unauthorizedAdmin");
            return mav;
        }
    }
    
    protected abstract String getNotLoggedViewName();
}
