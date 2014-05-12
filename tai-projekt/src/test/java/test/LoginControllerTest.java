package test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tai.dropbox.controller.LoginController;
import tai.dropbox.data.UserInfoDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest( SecurityUtils.class )
public class LoginControllerTest {


	@Mock
	private UserInfoDao userInfo;
	
	@Mock
	private Subject subject;

	@InjectMocks
	private LoginController loginController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	    when(subject.getPrincipal()).thenReturn("user");
	    when(subject.isAuthenticated()).thenReturn(true);
	    
	    PowerMockito.mockStatic(SecurityUtils.class);
	    PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);

		this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

	}

	@Test
	public void unauthorizedUserTest() throws Exception {

	    when(userInfo.getAccesTokenForUsername("user")).thenReturn(null);
	    mockMvc.perform(post("/")).andExpect(view().name("authorize"));
	}
	
	@Test
	public void authorizedUserTest() throws Exception {

	    when(userInfo.getAccesTokenForUsername("user")).thenReturn("testToken");
	    mockMvc.perform(post("/")).andExpect(view().name("redirect:main"));
	}
	
	@Test
	public void authenticatedUserTest() throws Exception {

	    mockMvc.perform(post("/login")).andExpect(view().name("redirect:main"));
	}



}
