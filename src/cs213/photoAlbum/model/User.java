package cs213.photoAlbum.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	static final long serialVersionUID = 58436;

	private String userId;
	private String fullName;
	private ArrayList<Album> albums;

	public User(String id, String name) {
		userId = id;
		fullName = name;
		albums = new ArrayList<Album>();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof User) {
			
		}

		return false;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void addAlbum(Album a){ 
		albums.add(a);
	}

	/**
	 * @return true if this user has this album
	 */
	public boolean containsAlbum(Album a) {
		return albums.contains(a);
	}

	public boolean containsAlbum(String albumName) {
		return containsAlbum(new Album(albumName, this));
	}

	/**
	 * Remove an album and its photos
	 */
	public void removeAlbum(Album a) {
		albums.remove(a);
	}

}
