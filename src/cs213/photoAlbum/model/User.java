package cs213.photoAlbum.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String userId;
	private String fullName;
	private List<Album> albums;

	public User(String id, String name) {
		userId = id;
		fullName = name;
		albums = new ArrayList<Album>();
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

}
