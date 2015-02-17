package cs213.photoAlbum.model;

import java.util.Calendar;
import java.util.Map;

public class Photo {

	private String fileName;
	private String caption;
	private Calendar dateTime;
	private Map<String, String> tags;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the dateTime
	 */
	public Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Add a tag to this photo
	 */
	public void addTag(String tag, String value) {
		tags.put(tag, value);
	}

	/**
	 * @param tag tag name of interest
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag) {
		return tags.containsKey(tag);
	}

	/**
	 * Remove a tag from this photo
	 */
	public void removeTag(String tag) {
		tags.remove(tag);
	}

}
