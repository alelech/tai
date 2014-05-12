package tai.dropbox.auth;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.ModelAndView;

public aspect AuthAspect {
    pointcut returnsMav(): execution(@tai.dropbox.auth.NeedsAuthentication ModelAndView *(..));

    ModelAndView around(): returnsMav(){
        boolean isAuth = SecurityUtils.getSubject().isAuthenticated();
        System.out.println("##################################");
        System.out.println("isAuth ?X?X? "+(isAuth?"yes":"no"));
        System.out.println("##################################");
        if(isAuth){
            return proceed();
        }
        else{
            ModelAndView mav = new ModelAndView("not_logged");
            return mav;
        }
    }
}
