package tai.dropbox.auth;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

public aspect AuthAspect {

    pointcut returnsMav(): execution(@NeedsAuthentication ModelAndView *(..));

    pointcut methodWithRole(HasRole role): execution( String *(..)) && @annotation(role);

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
            ModelAndView mav = new ModelAndView("not_logged");
            return mav;
        }
    }
}
