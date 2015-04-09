/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.model;

import java.util.Calendar;
import java.util.List;

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
	 * Add a photo to this album
	 * @return true if the photo was added, false otherwise (i.e. duplicate photo)
	 */
	public boolean addPhoto(String path);

	/**
	 * Add a photo to this album
	 * @return true if the photo was added, false otherwise (i.e. duplicate photo)
	 */
	public boolean addPhoto(String path, String caption);

	/**
	 * Remove a photo from this album
	 * @return true if the photo was removed, false otherwise
	 */
	public boolean removePhoto(IPhoto photo);

	/**
	 * Remove a photo from this album
	 * @return true if the photo was removed, false otherwise
	 */
	public boolean removePhoto(String path);

	/**
	 * @return the earliest photo date
	 */
	public Calendar getFirstDate();

	/**
	 * @return the latest photo date
	 */
	public Calendar getLastDate();

	/**
	 * @return the number of photos in this album
	 */
	public int getPhotoCount();

	/**
	 * @return a list of photos in this album
	 */
	public List<IPhoto> getPhotos();

	/**
	 * @return true if this album contains the photo
	 */
	public boolean hasPhoto(IPhoto photo);

	/**
	 * @return version of photo contained 
	 */
	public IPhoto getPhoto(IPhoto photo);

}
