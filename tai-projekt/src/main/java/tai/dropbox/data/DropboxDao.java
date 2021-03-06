package tai.dropbox.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tai.dropbox.model.DbxFile;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Grants access to dropbox services
 *
 */
@Component
public class DropboxDao {

	public static final String APP_NAME = "TAI Dropbox";
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Returns content-type for given path (uses restapi)
	 * @param path
	 * @param accessToken
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String getContentTypeOfFile(String path,String accessToken) throws JsonParseException, JsonMappingException, IOException{
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Bearer "+accessToken);
	    headers.set("Accept", MediaType.APPLICATION_JSON.toString());
	    HttpEntity<String> req = new HttpEntity<>(headers);
	    ResponseEntity<String> metadata = restTemplate.exchange("https://api.dropbox.com/1/metadata/dropbox{path}", HttpMethod.GET, req, String.class, path);
	    Map<String, String> meta=null;
		meta = (new ObjectMapper()).readValue(metadata.getBody(), Map.class);
	    String contentType = meta.get("mime_type");
	    return contentType;
	}
	
	/**
	 * Downloads file to passed outputStream
	 * @param path
	 * @param os
	 * @param accessToken
	 * @return
	 * @throws DbxException
	 * @throws IOException
	 */
	public DbxEntry.File downloadFileToStream(String path, OutputStream os, String accessToken) throws DbxException, IOException{
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(APP_NAME, Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		DbxEntry.File f = client.getFile(path, null, os);
		return f;
	}
	
	/**
	 * Get all files in given path
	 * @param path
	 * @param accessToken
	 * @return
	 * @throws DbxException
	 */
	public List<DbxFile> getFileListInPath(String path, String accessToken) throws DbxException{
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(APP_NAME, Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		List<DbxFile> files = new ArrayList<>();
		DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
		for (DbxEntry child : listing.children) {
			DbxFile file = new DbxFile();
			file.setName(child.name);
			file.setImg("res/img/" + child.iconName + ".gif");
			file.setPath(child.path);
			file.setFolder(child.isFolder());
			files.add(file);
		}
		return files;
	}
	
	/**
	 * Upload file from passed stream
	 * @param accessToken
	 * @param parentDir
	 * @param fileName
	 * @param fileSize
	 * @param is
	 * @throws DbxException
	 * @throws IOException
	 */
	public void uploadFileFromStream(String accessToken, String parentDir, String fileName, long fileSize, InputStream is) throws DbxException, IOException{
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(APP_NAME, Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		client.uploadFile(parentDir+fileName, DbxWriteMode.add(), fileSize , is);
	}
	
	/**
	 * Get dropbox display name of user
	 * @param accessToken
	 * @return
	 * @throws DbxException
	 */
	public String getDropboxName(String accessToken) throws DbxException {
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(APP_NAME, Locale.getDefault().toString());
		DbxClient client = new DbxClient(dbxRequestConfig, accessToken);
		return client.getAccountInfo().displayName;
	}
}
