package cs213.photoAlbum.model;

import cs213.photoAlbum.model.User;

public class Album {

	private String name;
	private User owner;

	/**
	 * Create an Album with an album name
	 * and a user that owns the album
	 */
	public Album(String nm, User user) {
		name = nm;
		owner = user;
	}


	/**
	 * Albums are considered the same if 
	 * they have the same owner and the same
	 * album name
	 */
	@Override
	public boolean equals(Object o) {
		if(! (o instanceof Album) ) {
			return false;
		}

		Album other = (Album) o;

		if(other.getOwner().equals(owner) && other.getName().equals(name)) {
			return true;
		}

		return false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

}
