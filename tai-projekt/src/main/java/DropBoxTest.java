import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;


public class DropBoxTest {
	private static final String APP_KEY = "xwaq9ujw7aoskjm";
	private static final String APP_SECRET = "2znk2stxkigwknv";

	public static void main(String[] args) {
		DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("tai test", Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(dbxRequestConfig, dbxAppInfo);
		String authorizeUrl = webAuth.start();
		System.out.println(authorizeUrl);
	}
}
