package tai.dropbox.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import tai.dropbox.auth.HasRole;
import tai.dropbox.auth.NeedsAuthentication;
import tai.dropbox.data.DropboxDao;
import tai.dropbox.data.UserInfoDao;
import tai.dropbox.model.DbxFile;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

@Controller
public class FileController {
	
	@Autowired
	private UserInfoDao userInfo;
	
	@Autowired
	private DropboxDao dropboxDao;

	/**
	 * Handles request for main page
	 * @return
	 */
	@RequestMapping("/main")
    @NeedsAuthentication
	public ModelAndView getFiles() {
		Subject user = SecurityUtils.getSubject();
		System.out.println("USER" + user);

		String accessToken = userInfo.getAccesTokenForUsername((String) user.getPrincipal());

		if (accessToken != null) {
			return getDropboxFolderModelAndView("/");
		}
		else{
			ModelAndView mav = new ModelAndView();
			mav.setViewName("authorize");
			return mav;
		}

	}

	/**
	 * Returns view with folder 
	 * @param model
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "/folder", method = RequestMethod.GET)
    @NeedsAuthentication
	public ModelAndView folderDetails(Model model, @RequestParam("path") String path) {
		return getDropboxFolderModelAndView(path);
	}
	
	/**
	 * Handles upload request
	 * @param file
	 * @param parentDir
	 * @return
	 */
	@RequestMapping("/upload")
	public String upload(@RequestParam(value="file")MultipartFile file,@RequestParam(value="dir")String parentDir){
		Subject user = SecurityUtils.getSubject();
		String accessToken = userInfo.getAccesTokenForUsername((String) user.getPrincipal());
		try {
			dropboxDao.uploadFileFromStream(accessToken, parentDir, file.getOriginalFilename(), file.getSize(), file.getInputStream());
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/folder?path="+parentDir;
	}
	
	/**
	 * Handles download request for a given path
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<byte[]> downloadFile(@RequestParam String path){
		Subject user = SecurityUtils.getSubject();
		if(!path.startsWith("/")){
			path = "/"+path;
		}
		String accessToken = userInfo.getAccesTokenForUsername((String) user.getPrincipal());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			DbxEntry.File f = dropboxDao.downloadFileToStream(path, bos, accessToken);
		    String contentType = dropboxDao.getContentTypeOfFile(path, accessToken);
		    //save as..
		    responseHeaders.set("Content-Disposition", "attachment; filename=\""+f.name+"\"");
		    responseHeaders.set("Content-Type", contentType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(bos.toByteArray(),responseHeaders, HttpStatus.OK);
	}

	/**
	 * Prepares data for view of files
	 * @param path
	 * @return
	 */
	private ModelAndView getDropboxFolderModelAndView(String path) {
		List<DbxFile> files = new ArrayList<DbxFile>();
		Subject user = SecurityUtils.getSubject();
		String accessToken = userInfo.getAccesTokenForUsername((String) user.getPrincipal());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("files");

		try {
			files = dropboxDao.getFileListInPath(path, accessToken);
			mav.addObject("username", dropboxDao.getDropboxName(accessToken));
		} catch (DbxException e) {
			e.printStackTrace();
		}
		mav.addObject("files", files);
		mav.addObject("path", path);
		return mav;
	}

    @HasRole(role = "administrator")
    public static String prepareUploadButton(){
        return "<li><a href=\"#upload\" data-toggle=\"modal\">Upload file...</a></li>";
    }
    
    @HasRole(role = "administrator")
    public static String prepareAdminButton(){
        return "<li><a href=\"admin\">Admin panel</a></li>";
    }
    
	@RequestMapping("/admin")
    @NeedsAuthentication
	public ModelAndView getAdminPanel() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("unauthorized");
		return mav;

	}

}
