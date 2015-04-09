/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.model;

import java.util.ArrayList;
import java.util.List;

public interface IUser extends Comparable<IUser> {

	/**
	 * @return the userId
	 */
	public String getUserId();

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId);

	/**
	 * @return the fullName
	 */
	public String getFullName();

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName);

	/**
	 * Add an album to this user
	 * @return true if the album was added, false if a duplicate album exists
	 */
	public boolean addAlbum(IAlbum a);

	/**
	 * Add an album to this user
	 * @return true if the album was added, false if a duplicate album exists
	 */
	public boolean addAlbum(String albumName);

	/**
	 * @return true if this user has this album
	 */
	public boolean containsAlbum(IAlbum a);

	/**
	 * @return true if the user has an album of this name
	 */
	public boolean containsAlbum(String albumName);

	/**
	 * Remove an album and its photos
	 * @return true if the album was removed, false if the album did not exist
	 */
	public boolean removeAlbum(IAlbum a);
	
	/**
	 * Remove an album and its photos
	 * @return true if the album was removed, false if the album did not exist
	 */
	public boolean removeAlbum(String albumName);

	/**
	 * Get a list of this user's IAlbums
	 */
	public List<IAlbum> getAlbums();

	/**
	 * @return null if this user does not have an album with specified name
	 */
	public IAlbum getAlbum(String name);

}
