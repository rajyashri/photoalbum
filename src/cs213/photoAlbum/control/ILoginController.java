/**
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.control;



public interface ILoginController {

	/** check if user exist
	 * 
	 * @param username and password
	 * @return true if user
	 */
	
	public boolean checkUserExist(String username, String password);
	

}
