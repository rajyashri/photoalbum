/**
 * @author Rajyashri Vasudevamoorthy
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.control;

import java.util.List;

import cs213.photoAlbum.model.IUser;

public interface IUserController {
	
	/**
	 * @param username
	 * @return true if successfully created a new user
	 */
	public boolean addUser(String userid, String name);

	/**
	 * Deletes a user
	 * @return true if user existed and was deleted
	 */
	public boolean deleteUser(String userid);
	
	/**
	 * Checks if user exists
	 * @return true if user file exists in data/
	 */
	public boolean userExists(String userid);

	/**
	 * Get an IUser by userid
	 * @return null if user does not exist
	 */
	public IUser getUser(String userid);
	
	/**
	 * Get a list of users
	 * @returns the list of users in data/
	 */
	public List<IUser> listUsers();

	/**
	 * Save the state of a user
	 * @return true if user saved successfully
	 */
	public boolean saveUser(IUser user);
	
}
