package tai.dropbox.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.model.DbxFile;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.File;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

@Controller
public class FileController {
	
	@Autowired
	private JdbcTemplate template;

	@RequestMapping("/main")
	public ModelAndView getFiles() {
		Subject user = SecurityUtils.getSubject();
		System.out.println("USER" + user);

		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);

		if (accessToken != null) {
			return getDropboxFolderModelAndView("/");
		}
		else{
			ModelAndView mav = new ModelAndView();
			mav.setViewName("authorize");
			return mav;
		}

	}

	@RequestMapping(value = "/folder", method = RequestMethod.GET)
	public ModelAndView folderDetails(Model model, @RequestParam("path") String path) {
		return getDropboxFolderModelAndView(path);
	}
	
/*	@RequestMapping("/download")
	@ResponseBody
	public byte[] downloadFile(@RequestParam String path){
		//String path = req.getRequestURI().substring((req.getContextPath()+"/download").length());
		Subject user = SecurityUtils.getSubject();
		if(!path.startsWith("/")){
			path = "/"+path;
		}
		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
		    client.getFile(path, null, bos);
		    System.out.println(path);
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}*/

	private ModelAndView getDropboxFolderModelAndView(String path) {
		List<DbxFile> files = new ArrayList<DbxFile>();
		Subject user = SecurityUtils.getSubject();
		String accessToken = template.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("files");

		try {
			DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
			DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
			System.out.println("Linked account: " + client.getAccountInfo().displayName);
			DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
			for (DbxEntry child : listing.children) {
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
