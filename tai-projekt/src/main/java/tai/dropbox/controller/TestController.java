package tai.dropbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	JdbcTemplate template;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView test(ModelAndView mav){
		mav.setViewName("test");
		mav.addObject("test", "testVal");
		return mav;
	}
	
}
