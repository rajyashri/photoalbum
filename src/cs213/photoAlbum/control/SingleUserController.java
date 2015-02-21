/**
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.control;

import java.util.List;

import cs213.photoAlbum.model.IAlbum;

public class SingleUserController {

	/**
	 * Create a SingleUserController, specifying user by user ID
	 */
	public SingleUserController(String userId) {
		// TODO
	}

	/**
	 * @return true if user has this album
	 */
	public boolean hasAlbum(String albumName) {
		// TODO
		return false;
	}

	/**
	 * @return album if user has it, null otherwise
	 */
	public IAlbum getAlbum(String albumName) {
		// TODO
		return null;
	}

	/**
	 * @return true if album removed, false if it did not exist
	 */
	public boolean removeAlbum(String albumName) {
		// TODO
		return false;
	}

	/**
	 * @return true if album added, false if it already existed
	 */
	public boolean addAlbum(String albumName) {
		// TODO
		return false;
	}

	/**	
	 * @return true if album added, false if it already existed
	 */
	public List<IAlbum> listAlbums() {
		// TO DO
		return null;
	}
	
	/**
	 * Add a photo to the user's album
	 * @return true if photo added to album successfully, false otherwise
	 */
	public boolean addPhotoToAlbum(String albumName, String photoFilePath) {
		// TODO
		return false;
	}

	/**
	 * Remove a photo from the user's album
	 * @return true if photo existed in album and was removed, false otherwise
	 */
	public boolean removePhotoFromAlbum(String albumName, String photoFilePath) {
		// TODO
		return false;
	}

	
}
