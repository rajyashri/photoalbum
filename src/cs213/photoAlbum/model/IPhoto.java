/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public interface IPhoto extends Comparable<IPhoto> {

	/**
	 * Get the filename for this photo
	 * @return the fileName
	 */
	public String getFileName();

	/**
	 * Set the filename for this photo 
	 * @param fileName the fileName to set (must be unique in this user's album)
	 */
	public void setFileName(String fileName);

	/**
	 * Get the caption for this photo
	 * @return the caption
	 */
	public String getCaption();

	/**
	 * Set a caption for this photo
	 * @param caption the caption to set
	 */
	public void setCaption(String caption);

	/**
	 * Get the date this photo was created
	 * @return the dateTime corresponding to the photo file's creation time
	 */
	public Calendar getDateTime();

	/**
	 * Set the date and time that this photo was taken
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Calendar dateTime);

	/**
	 * Add a tag to this photo
	 * @param tag a tag type (e.g. "location")
	 * @param value the value corresponding to tag type (e.g. "Berlin")
	 */
	public void addTag(String tag, String value);

	/**
	 * Determine if this photo has a tag of a certain type
	 * @param tag tag type of interest
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag);

	/**
	 * Remove a tag from this photo
	 * @param tag the tag type to remove
	 * @return true if this Photo had a tag with this name
	 */
	public boolean removeTag(String tag);

	/**
	 * Determine if this photo has a tag of a certain type and value
	 * @param tag tag type of interest
	 * @param value tag value corresponding to tag type
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag, String value);

	/**
	 * Determine if this photo has a tag of a certain type and value
	 * @param tag tag type of interest
	 * @param value tag value corresponding to tag type
	 * @return true if this Photo had a tag with this name
	 */
	public boolean removeTag(String tag, String value);

}
