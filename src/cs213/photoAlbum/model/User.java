
/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.List;

import cs213.photoAlbum.util.BinarySearchList;

public class User implements IUser, Serializable {

	static final long serialVersionUID = 58436;

	private String userId;
	private String fullName;
	private List<IAlbum> albums;

	public User(String id, String name) {
		userId = id;
		fullName = name;
		albums = new BinarySearchList<IAlbum>();
	}

	/**
	 * @return the userId
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the fullName
	 */
	@Override
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	@Override
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Add an album to this User
	 * @return true if added successfully, false if album already exists
	 */
	@Override
	public boolean addAlbum(IAlbum a){ 
		return albums.add(a);
	}

	/**
	 * Add an album to this User
	 * @return true if added successfully, false if album already exists
	 */
	@Override
	public boolean addAlbum(String albumName){ 
		return albums.add(new Album(albumName, this));
	}

	/**
	 * @return true if this user has this album
	 */
	@Override
	public boolean containsAlbum(IAlbum a) {
		return albums.contains(a);
	}

	/**
	 * @return true if this user has this album
	 */
	@Override
	public boolean containsAlbum(String albumName) {
		return containsAlbum(new Album(albumName, this));
	}

	/**
	 * Remove an album and its photos
	 * @return true if added successfully, false if album already exists
	 */
	@Override
	public boolean removeAlbum(IAlbum a) {
		return albums.remove(a);
	}

	/**
	 * Remove an album and its photos
	 * @return true if added successfully, false if album already exists
	 */
	@Override
	public boolean removeAlbum(String name) {
		return albums.remove(new Album(name, this));
	}

	@Override
	public int compareTo(IUser other) {
		return userId.compareTo(other.getUserId());
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof IUser) {
			return userId.equals( ((IUser) other).getUserId());
		}

		return false;
	}

	/**
	 * Get a list of this user's IAlbums
	 */
	@Override
	public List<IAlbum> getAlbums() {
		return albums;
	}

	/**
	 * @return null if this user does not have an album with specified name
	 */
	@Override
	public IAlbum getAlbum(String albumName) {
		for(IAlbum album : albums) {
			if(album.getName().equals(albumName)) {
				return album;
			}
		}

		return null;
	}


}
