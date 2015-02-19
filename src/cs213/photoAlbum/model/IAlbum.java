/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import cs213.photoAlbum.model.User;

public interface IAlbum extends Comparable<IAlbum> {

	/**
	 * @return the name
	 */
	public String getName(); 

	/**
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * @return the owner
	 */
	public IUser getOwner();

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(IUser owner);

	/**
	 * Add a photo to this album
	 * @return true if the photo was added, false otherwise (i.e. duplicate photo)
	 */
	public boolean addPhoto(IPhoto photo);

	/**
	 * Remove a photo from this album
	 * @return true if the photo was removed, false otherwise
	 */
	public boolean removePhoto(IPhoto photo);

}
