/**
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.control;

import java.util.Calendar;

import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IPhoto;

public class PhotoController {
	
	public PhotoController(){
		
	}
	
	/**
	 * 
	 * @param tagName
	 * @return list of photos in the album using the tag name provided by the user
	 */
	
	public IPhoto listByTag(String tagName, String albumName){
		return null;
		
	}
	/**
	 * 
	 * @param tagName
	 * @return list of photos in the album using the Date provided by the user
	 */
	
	public IPhoto listByDate(Calendar date){
		return null;
		
	}
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean movePhoto(IAlbum a, IAlbum b){
		return false;
		
	}
	/**
	 * Add a tag to a photo
	 * @return true if tag added to photo successfully, false otherwise
	 */
	public boolean addTagToPhoto(String albumName, String photoFilePath, String key, String value) {
		// TODO
		return false;
	}

	/**
	 * Remove a tag from a photo
	 * @return true if tag removed, false otherwise
	 */
	public boolean removeTagFromPhoto(String albumName, String photoFilePath, String key, String value) {
		// TODO
		return false;
	}
	
    
	

}
