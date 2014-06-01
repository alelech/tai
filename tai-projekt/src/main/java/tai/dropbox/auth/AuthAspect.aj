package tai.dropbox.auth;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

public abstract aspect AuthAspect {

	
    abstract pointcut returnsMav();

    abstract pointcut methodWithRole(HasRole role);

    String around(HasRole role): methodWithRole(role){
        if(SecurityUtils.getSubject().hasRole(role.role())){
            return proceed(role);
        }
        else{
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
    
    protected abstract String getNotLoggedViewName();
}
