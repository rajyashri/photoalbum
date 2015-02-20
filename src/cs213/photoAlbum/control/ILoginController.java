package cs213.photoAlbum.control;

import cs213.photoAlbum.simpleview.UserView;

public interface ILoginController {

	/** check if user exist
	 * 
	 * @param username and password
	 * @return true if user
	 */
	
	public boolean checkUserExist(String username, String password);
	

}
