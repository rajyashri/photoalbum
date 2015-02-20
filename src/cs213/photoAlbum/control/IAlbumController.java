package cs213.photoAlbum.control;

import cs213.photoAlbum.model.User;
import cs213.photoAlbum.simpleview.UserView;

public interface IAlbumController {
	
	public boolean addPhotoToAlbum(String albumName, String photoFilePath);

	/**
	 * Remove a photo from the user's album
	 * @return true if photo existed in album and was removed, false otherwise
	 */
	public boolean removePhotoFromAlbum(String albumName, String photoFilePath);

	/**
	 * Add a tag to a photo
	 * @return true if tag added to photo successfully, false otherwise
	 */
	public boolean addTagToPhoto(String albumName, String photoFilePath, String key, String value);

	/**
	 * Remove a tag from a photo
	 * @return true if tag removed, false otherwise
	 */
	public boolean removeTagFromPhoto(String albumName, String photoFilePath, String key, String value);


	
	
}
