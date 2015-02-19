/**
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

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
	
	private List<Tag> tags;

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
	public void removeTag(String tag) {
		tags.remove(tag);
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
