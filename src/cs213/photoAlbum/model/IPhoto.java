/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public interface IPhoto extends Comparable<IPhoto> {

	/**
	 * @return the fileName
	 */
	public String getFileName();
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName);

	/**
	 * @return the caption
	 */
	public String getCaption();

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption);

	/**
	 * @return the dateTime
	 */
	public Calendar getDateTime();

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Calendar dateTime);

	/**
	 * Add a tag to this photo
	 */
	public void addTag(String tag, String value);

	/**
	 * @param tag tag name of interest
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag);

	/**
	 * Remove a tag from this photo
	 */
	public void removeTag(String tag);

}
