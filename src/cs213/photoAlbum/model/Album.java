/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.List;

import cs213.photoAlbum.util.BinarySearchList;

public class Album implements IAlbum, Serializable {

	static final long serialVersionUID = 2292274;

	private String name;
	private IUser owner;

	private List<IPhoto> photos;

	/**
	 * Create an Album with an album name
	 * and a user that owns the album
	 */
	public Album(String nm, IUser user) {
		name = nm;
		owner = user;
		photos = new BinarySearchList<IPhoto>();
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the owner
	 */
	@Override
	public IUser getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	@Override
	public void setOwner(IUser owner) {
		this.owner = owner;
	}

	/**
	 * Add a photo to this Album
	 * @return true if the photo was added, false if the photo is already in the album
	 */
	@Override
	public boolean addPhoto(IPhoto photo) {
		return photos.add(photo);
	}

	/**
	 * Remove a photo from this Album
	 * @return true if the photo was removed, false if it did not exist
	 */
	@Override
	public boolean removePhoto(IPhoto photo) {
		return photos.remove(photo);
	}

	@Override
	public int compareTo(IAlbum other) {
		int ownerCompare = owner.compareTo(other.getOwner());
		return (ownerCompare == 0) ? (name.compareTo(other.getName())) : (ownerCompare);
	}

	/**
	 * Albums are considered the same if 
	 * they have the same owner and the same
	 * album name
	 */
	@Override
	public boolean equals(Object o) {
		if(! (o instanceof IAlbum) ) {
			return false;
		}

		IAlbum other = (IAlbum) o;

		if(other.getOwner().equals(owner) && other.getName().equals(name)) {
			return true;
		}

		return false;
	}


}
