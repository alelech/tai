package tai.dropbox.controller;

import com.dropbox.core.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tai.dropbox.auth.NeedsAuthentication;
import tai.dropbox.data.DropboxDao;
import tai.dropbox.data.UserInfoDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class LoginController {

    private static final String WEB_AUTH_OBJECT_ATTR = "webAuthObject";
    private static final String APP_KEY = "xwaq9ujw7aoskjm";
    private static final String APP_SECRET = "2znk2stxkigwknv";

    @Autowired
    private UserInfoDao userInfo;


    private DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

    /**
     * Handles request for root page (/)
     *
     * @return
     */
    @RequestMapping("/")
    @NeedsAuthentication
    public ModelAndView getMainPage() {

        Subject user = SecurityUtils.getSubject();
        System.out.println("USER " + user);

        String accessToken = userInfo.getAccesTokenForUsername((String) user.getPrincipal());

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

    /**
     * Handles request for login page
     *
     * @param mav
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView doLogin(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password, ModelAndView mav) {
        Subject subject = SecurityUtils.getSubject();
        if (username != null && password != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            subject.login(token);
        }
//        System.out.println("!!!"+username+"!!!");
        if (subject.isAuthenticated()) {
            mav.setViewName("redirect:main");
        }
        return mav;
    }

    /**
     * Handles return from OAuth authorization
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping("/gotToken")
    @NeedsAuthentication
    public ModelAndView gotAccesToken(@RequestParam(required = false, value = "code") String code, @RequestParam(required = false, value = "state") String state) {

        Map<String, String[]> result = new HashMap<>();
        result.put("code", new String[]{code});
        result.put("state", new String[]{state});
        DbxWebAuth webAuth = (DbxWebAuth) SecurityUtils.getSubject().getSession().getAttribute(WEB_AUTH_OBJECT_ATTR);
        DbxAuthFinish authFinish = null;
        if (webAuth != null) {
            SecurityUtils.getSubject().getSession().removeAttribute(WEB_AUTH_OBJECT_ATTR);
            try {
                authFinish = webAuth.finish(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String accessToken = authFinish.accessToken;
            Subject subject = SecurityUtils.getSubject();
            userInfo.putAccessTokenForUsername((String) subject.getPrincipal(), accessToken);

            ModelAndView mav = new ModelAndView();
            mav.setViewName("authComplete");
            mav.addObject("dropbox-token", authFinish.accessToken);
            return mav;
        } else {
            return null;
        }
    }

    /**
     * Starts process of OAuth auhtorization
     *
     * @param request
     * @return
     */
    @RequestMapping("/fetchToken")
    private String fetchToken(HttpServletRequest request) {

        HttpSession session = request.getSession();
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(DropboxDao.APP_NAME, Locale.getDefault().toString());
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionKey);
        String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/gotToken";
        DbxWebAuth webAuth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo, redirectUrl, dbxSessionStore);
        String authorizeUrl = webAuth.start();

        SecurityUtils.getSubject().getSession().setAttribute(WEB_AUTH_OBJECT_ATTR, webAuth);

        return "redirect:" + authorizeUrl;

    }

    @RequestMapping("/logout")
    private String doLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:login";
    }


    private ModelAndView getLoginError() {
        return new ModelAndView("not_logged");
    }


}
