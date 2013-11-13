package tai.dropbox.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.DbxFile;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.Folder;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;


@Controller
public class LoginController {
	
	@Autowired
	private JdbcTemplate template;
	private String accessToken;
	
	private static final String APP_KEY = "xwaq9ujw7aoskjm";
	private static final String APP_SECRET = "2znk2stxkigwknv";
	
	private DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
	private DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
	
	@RequestMapping("/index.htm")
	public ModelAndView getIndexPage() {
		
		Subject user = SecurityUtils.getSubject();		
		accessToken = template.queryForObject("select access_token from users where username = ?", new Object[]{user.getPrincipal()}, String.class);

		if(accessToken == null){
			return getUnauthorized();
		}else{
			return getFiles();

		}		

	}

	private ModelAndView getAuthorized(String accessToken) {
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}

	private ModelAndView getUnauthorized() {

		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(dbxRequestConfig, dbxAppInfo);
		String authorizeUrl = webAuth.start();
	
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("unauthorized");
		mav.addObject("link", authorizeUrl);
		return mav;


	}

	@RequestMapping("/files.htm")
	public ModelAndView getFiles() {
		return getDropboxFolderModelAndView("/");

	}
	
	@RequestMapping(value = "/folder.htm", method = RequestMethod.GET)
	public ModelAndView folderDetails(Model model, @RequestParam("path") String path) {
		return getDropboxFolderModelAndView(path);

	}

	private ModelAndView getDropboxFolderModelAndView(String path) {
		List<DbxFile> files = new ArrayList<DbxFile>(); 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("files");

		try {
			DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
			System.out.println("Linked account: " + client.getAccountInfo().displayName);

			
			DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
			for(DbxEntry child : listing.children){
				DbxFile file = new DbxFile();
				file.setName(child.name);
				file.setImg("res/img/" + child.iconName + ".gif");
				file.setPath(child.path);
				file.setFolder(child.isFolder());				
				files.add(file);
			}
			mav.addObject("username", client.getAccountInfo().displayName);

		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		mav.addObject("files", files);
		return mav;
	}
	


}
