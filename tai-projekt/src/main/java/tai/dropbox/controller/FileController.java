package tai.dropbox.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.model.DbxFile;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class FileController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/main")
	public ModelAndView getFiles() {
		Subject user = SecurityUtils.getSubject();
		System.out.println("USER" + user);

		String accessToken = jdbcTemplate.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);

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
	
	@RequestMapping("/upload")
	public String upload(@RequestParam(value="file")MultipartFile file,@RequestParam(value="dir")String parentDir){
		Subject user = SecurityUtils.getSubject();
		String accessToken = jdbcTemplate.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		
		try {
			client.uploadFile(parentDir+file.getOriginalFilename(), DbxWriteMode.add(), file.getSize() , file.getInputStream());
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/folder?path="+parentDir;
	}
	
	@RequestMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<byte[]> downloadFile(@RequestParam String path){
		//String path = req.getRequestURI().substring((req.getContextPath()+"/download").length());
		Subject user = SecurityUtils.getSubject();
		if(!path.startsWith("/")){
			path = "/"+path;
		}
		String accessToken = jdbcTemplate.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DbxEntry.File f = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
		    f = client.getFile(path, null, bos);
		    //java api nie zwraca content type (smuteczek) , jestesmy leniwi wiec zamiast samemu zgadywac pytam dropboxa przez http :D
		    HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", "Bearer "+accessToken);
		    headers.set("Accept", MediaType.APPLICATION_JSON.toString());
		    HttpEntity<String> req = new HttpEntity<>(headers);
		    ResponseEntity<String> metadata = restTemplate.exchange("https://api.dropbox.com/1/metadata/dropbox{path}", HttpMethod.GET, req, String.class, path);
		    Map<String, String> meta = (new ObjectMapper()).readValue(metadata.getBody(), Map.class);
		    String contentType = meta.get("mime_type");
		    //save as..
		    responseHeaders.set("Content-Disposition", "attachment; filename=\""+f.name+"\"");
		    responseHeaders.set("Content-Type", contentType);
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> returnValue = new ResponseEntity<byte[]>(bos.toByteArray(),responseHeaders, HttpStatus.OK);
		return returnValue;
	}

	private ModelAndView getDropboxFolderModelAndView(String path) {
		List<DbxFile> files = new ArrayList<DbxFile>();
		Subject user = SecurityUtils.getSubject();
		String accessToken = jdbcTemplate.queryForObject("select access_token from users where username = ?", new Object[] { user.getPrincipal() }, String.class);
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
			e.printStackTrace();
		}
		mav.addObject("files", files);
		mav.addObject("path", path);
		return mav;
	}

}
