package tai.dropbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {
	
	@Autowired
	private JdbcTemplate template;
	
	@RequestMapping("/index.htm")
	public ModelAndView accountSummary() {
//		List<Account> accounts = accountManager.getAllAccounts();
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("/WEB-INF/views/accountSummary.jsp");
//		mav.addObject("accounts", accounts);
//		return mav;
		
		String token = template.queryForObject("select access_token from users where username = ?", new Object[]{"admin"}, String.class);

		if(token == null){
			ModelAndView mav = new ModelAndView();
			mav.setViewName("unauthorized");
			return mav;
		}else{
			ModelAndView mav = new ModelAndView();
			mav.setViewName("index");
			return mav;
		}
		

	}


}
