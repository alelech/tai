package tai.dropbox.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

/**
 * Grants access to user information
 */
@Component
public class UserInfoDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Returns access token from database for given user
	 * @param username
	 * @return
	 */
	public String getAccesTokenForUsername(String username){
		String accessToken = jdbcTemplate.queryForObject("select access_token from users where username = ?", new Object[] { username }, String.class);
		return accessToken;
	}
	
	/**
	 * Updates acces token for given user
	 * @param username
	 * @param accessToken
	 */
	public void putAccessTokenForUsername(final String username, final String accessToken){
		jdbcTemplate.update("update users set access_token = ? where username = ?",new PreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, accessToken);
				ps.setString(2, username);
			}
		});
	}
	
}
