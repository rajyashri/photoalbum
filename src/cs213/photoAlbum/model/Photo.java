/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Calendar;

import cs213.photoAlbum.util.BinarySearchList;

public class Photo implements IPhoto, Serializable {

	static final long serialVersionUID = 829229;
	
	private static class Tag implements Serializable, Comparable<Tag> {
		static final long serialVersionUID = 54833;
		
		private String key;
		private String value;
		
		public Tag(String ky, String val) {
			key = ky;
			value = val;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other instanceof Tag) {
				Tag tag = (Tag) other;
				return (key.equals(tag.getKey()) && 
						value.equals(tag.getValue())); 
			}
			
			return false;
		}

		@Override
		public int compareTo(Tag other) {
			int keyCompare = key.compareTo(other.getKey());
			return (keyCompare == 0) ? (value.compareTo(other.getValue())) : (keyCompare);
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}

	private String fileName;
	private String caption;
	private Calendar dateTime;
	
	private BinarySearchList<Tag> tags;

	/**
	 * Create a photo with a valid file name, caption,
	 * will infer date and time from file
	 * @param fileName the path to photo file, file must exist
	 * @param caption a String caption for this file
	 * @throws FileNotFoundException if file specified by fileName doesn't exist
	 */
	public Photo(String fileName, String caption) throws FileNotFoundException {
		this.fileName = fileName;
		this.caption = caption;
		this.tags = new BinarySearchList<Tag>();

		// Try to open the file to get last modified time
		File f = new File(fileName);
		if(!f.exists()) {
			throw new FileNotFoundException("File " + fileName + " does not exist");
		}

		dateTime = Calendar.getInstance();
		dateTime.setTimeInMillis(f.lastModified());
		dateTime.set(Calendar.MILLISECOND, 0);
	}

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
		tags.add(new Tag(tag, value));
	}

	/**
	 * @param tag tag name of interest
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	/**
	 * Remove a tag from this photo
	 */
	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}

	/**
	 * Determine if this photo has a tag of a certain type and value
	 * @param tag tag type of interest
	 * @param value tag value corresponding to tag type
	 * @return true if this Photo has a tag with this name
	 */
	public boolean hasTag(String tag, String value) {
		return tags.contains(new Tag(tag, value));
	}

	/**
	 * Determine if this photo has a tag of a certain type and value
	 * @param tag tag type of interest
	 * @param value tag value corresponding to tag type
	 * @return true if this Photo had a tag with this name
	 */
	public boolean removeTag(String tag, String value) {
		return tags.remove(new Tag(tag, value));
	}


	@Override
	public boolean equals(Object other) {
		if(other instanceof IPhoto) {
			IPhoto photo = (IPhoto) other;
			return fileName.equals(photo.getFileName()); 
		}
		
		return false;
	}

	@Override
	public int compareTo(IPhoto other) {
		return fileName.compareTo(other.getFileName());	
	}

}
