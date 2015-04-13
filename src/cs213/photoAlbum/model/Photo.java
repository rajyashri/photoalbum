
/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
				if(key.equals("location")) {
					// Can only be one location tag
					return true;
				} else {
					// Must match both key and value
					return (key.equals(tag.getKey()) && 
							value.equals(tag.getValue())); 
				}
			}
			
			return false;
		}

		@Override
		public int compareTo(Tag other) {
			int keyCompare = key.compareTo(other.getKey());

			if(key.equals("location")) {
				// Only care about key in the case of location tags
				return keyCompare;
			} else if(other.getKey().equals("location")) {
				// Only care about key in the case of location tags
				return keyCompare;
			} else if(key.equals("person") && !other.getKey().equals("person")) {
				return -1;
			} else if(other.getKey().equals("person") && !other.getKey().equals("person")) {
				return 1;
			}

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
		this.caption = caption;
		this.tags = new BinarySearchList<Tag>();

		// Try to open the file to get last modified time
		File f = new File(fileName);
		if(!f.exists()) {
			throw new FileNotFoundException("File " + fileName + " does not exist");
		}

		try {
			this.fileName = f.getCanonicalPath();
			dateTime = Calendar.getInstance();
			dateTime.setTimeInMillis(f.lastModified());
			dateTime.set(Calendar.MILLISECOND, 0);
		} catch(Exception e) {
			throw new FileNotFoundException("File " + fileName + " does not exist");
		}
	}

	/**
	 * Create a photo with a valid file name and blank caption
	 * will infer date and time from file
	 * @param fileName the path to photo file, file must exist
	 * @throws FileNotFoundException if file specified by fileName doesn't exist
	 */
	public Photo(String fileName) throws FileNotFoundException {
		this(fileName, "");
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
	public boolean addTag(String tag, String value) {
		return tags.add(new Tag(tag, value));
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

	/**
	 * Get a tag's value
	 * @return the value of first tag found with specified key
	 */
	public String getTagValue(String key) {
		for(Tag t : tags) {
			if(t.getKey().equals(key)) {
				return t.getValue();
			}
		}

		return null;
	}

	/**
	 * Find if a tag exists with a specified value
	 * @return true if a tag with the specified value exists
	 */
	public boolean hasTagWithValue(String value) {
		for(Tag t : tags) {
			if(t.getValue().equals(value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return a List<String> of each tag in the form "key : value"
	 */
	public List<String> getTagStrings() {
		List<String> tagStrings = new ArrayList<String>(tags.size());

		for(Tag  t : tags) {
			tagStrings.add(t.getKey() + ":" + t.getValue());
		}

		return tagStrings;
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
