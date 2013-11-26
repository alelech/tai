package test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

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

import tai.dropbox.controller.FileController;
import tai.dropbox.data.DropboxDao;
import tai.dropbox.data.UserInfoDao;
import tai.dropbox.model.DbxFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest( SecurityUtils.class )
public class FileControllerTest {
	
	@Mock
	private DropboxDao dropboxDao;
	
	@Mock
	private UserInfoDao userInfo;

	@Mock
	private Subject subject;

	@InjectMocks
	private FileController fileController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	    when(subject.getPrincipal()).thenReturn("user");
	    when(subject.isAuthenticated()).thenReturn(true);
	    
	    PowerMockito.mockStatic(SecurityUtils.class);
	    PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);

	

		this.mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();

	}
	
	@Test
	public void getRootDirFilesTest() throws Exception {
		List<DbxFile> files = new ArrayList<DbxFile>();
	    when(dropboxDao.getFileListInPath("/", "testToken")).thenReturn(files);
	    when(userInfo.getAccesTokenForUsername("user")).thenReturn("testToken");
	    mockMvc.perform(get("/folder").param("path", "/"))
	    	.andExpect(view().name("files"))
	    	.andExpect(model().attribute("path", "/"))
	    	.andExpect(model().attribute("files", files));
	}
	
	
}
