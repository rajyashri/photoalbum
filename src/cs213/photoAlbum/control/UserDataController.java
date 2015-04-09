/**
 * @author Rajyashri Vasudevamoorthy
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IPhoto;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.BinarySearchList;

public class UserDataController {

	private IUser user;

	/**
	 * Create a UserAlbumController, specifying user by user ID
	 */
	public UserDataController(IUser user) {
		this.user = user;
	}

	/**
	 * @return true if user has this album
	 */
	public boolean hasAlbum(String albumName) {
		return user.containsAlbum(albumName);
	}

	/**
	 * @return album if user has album, null otherwise
	 */
	public IAlbum getAlbum(String albumName) {
		return user.getAlbum(albumName);
	}

	/**
	 * @return true if album removed, false if it did not exist
	 */
	public boolean removeAlbum(String albumName) {
		return user.removeAlbum(albumName);
	}

	/**
	 * @return true if album added, false if it already existed
	 */
	public boolean addAlbum(String albumName) {
		return user.addAlbum(albumName);
	}

	/**	
	 * @return true if album added, false if it already existed
	 */
	public List<IAlbum> listAlbums() {
		return user.getAlbums();
	}

	/**
	 * Add a photo to the user's album
	 * @return true if photo added to album successfully, false otherwise
	 */
	public boolean addPhotoToAlbum(String photoFilePath, String caption, String albumName) {
		IAlbum album = user.getAlbum(albumName);
		if(album == null) {
			return false;
		}

		return album.addPhoto(photoFilePath, caption);
	}

	/**
	 * Add a photo to the user's album
	 * @return true if photo added to album successfully, false otherwise
	 */
	public boolean addPhotoToAlbum(IPhoto photo, String albumName) {
		IAlbum album = user.getAlbum(albumName);
		if(album == null) {
			return false;
		}

		return album.addPhoto(photo);
	}

	/**
	 * Remove a photo from the user's album
	 * @return true if photo existed in album and was removed, false otherwise
	 */
	public boolean removePhotoFromAlbum(String photoFilePath, String albumName) {
		IAlbum album = user.getAlbum(albumName);
		if(album == null) {
			return false;
		}

		return album.removePhoto(photoFilePath);
	}

	/**
	 * Find the albums that contain this IPhoto
	 * @return a list of IAlbums containing photo
	 */
	public List<IAlbum> getAlbumsContaining(IPhoto photo) {
		List<IAlbum> albums = new ArrayList<IAlbum>();

		for(IAlbum album : user.getAlbums()) {
			if(album.hasPhoto(photo)) {
				albums.add(album);
			}
		}

		return albums;
	}

	/**
	 * Get the photos contained in an album
	 * @return null if album does not exist
	 */
	public List<IPhoto> getPhotos(String albumName) {
		IAlbum album = user.getAlbum(albumName);
		if(album == null) {
			return null;
		}

		return album.getPhotos();
	}

	/**
	 * Get a photo from a specific album
	 * @return null if photo or album does not exist
	 */
	public IPhoto getPhoto(String fileName, String albumName) {
		IAlbum album = user.getAlbum(albumName);
		if(album == null) {
			return null;
		}

		IPhoto thisPhoto;
		try {
			thisPhoto = new Photo(fileName, "");
		} catch (Exception e) {
			return null;
		}
		
		for(IPhoto photo : album.getPhotos()) {
			if(photo.getFileName().equals(thisPhoto.getFileName())) {
				return photo;
			}
		}
		
		return null;
	}

	/**
	 * Add tags to a photo (in all albums)
	 * @return false if photo is not in any album, or if tags exist
	 */
	public boolean addTag(IPhoto photo, String key, String value) {
		List<IAlbum> albums = getAlbumsContaining(photo);

		if(albums.isEmpty()) {
			return false;
		}

		IPhoto curPhoto = albums.get(0).getPhoto(photo);
		return curPhoto.addTag(key, value);
	}

	/**
	 * Remove tag from a photo (in all albums)
	 * @return false if photo is not in any album, or if tags exist
	 */
	public boolean deleteTag(IPhoto photo, String key, String value) {
		List<IAlbum> albums = getAlbumsContaining(photo);

		if(albums.isEmpty()) {
			return false;
		}

		IPhoto curPhoto = albums.get(0).getPhoto(photo);
		return curPhoto.removeTag(key, value);
	}

	/**
	 * Get a list of photos that were taken between startTime and endTime
	 * @return a list of IPhoto, sorted by date
	 */
	public List<IPhoto> getPhotosByDate(Calendar startTime, Calendar endTime) {
		List<IPhoto> matchedPhotos = new BinarySearchList<IPhoto>();
			
		for(IAlbum album : user.getAlbums()) {
			for(IPhoto p : album.getPhotos()) {
				if(p.getDateTime().compareTo(startTime) >= 0 &&
							p.getDateTime().compareTo(endTime) <= 0) {
					if(!matchedPhotos.contains(p)) {
						matchedPhotos.add(p);
					}
				}
			}
		}

		Collections.sort(matchedPhotos, new Comparator<IPhoto>() {
			public int compare(IPhoto a, IPhoto b) {
				return a.getDateTime().compareTo(b.getDateTime());
			}
		});

		return matchedPhotos;
	}

	/**
	 * Get a list of photos that have any of the tags in the String
	 * @param tags a tag list in the form [<tagType:>]"<tagValue>" [,[<tagType:>]"<tagValue>"]
	 * @return a list of IPhoto, sorted by date
	 */
	public List<IPhoto> getPhotosByTagString(String tagString) {
		List<IPhoto> matchedPhotos = null;

		String[] tags = tagString.split(",");

		for(String s : tags) {
			if(s.indexOf(":") != -1 && s.indexOf(":") + 1 != s.length()) {
				String key = s.substring(0, s.indexOf(":")).trim();
				String value = s.substring(s.indexOf(":") + 1).replace("\"", "");

				if(matchedPhotos == null) {
					matchedPhotos = getPhotosByTag(key, value);
				} else {
					List<IPhoto> newList = new BinarySearchList<IPhoto>();
					for(IPhoto p : getPhotosByTag(key, value)) {
						if(matchedPhotos.contains(p)) {
							newList.add(p);
						}
					}
					matchedPhotos = newList;
				}
			} else {
				String value = s.replace("\"", "").trim();
				if(matchedPhotos == null) {
					matchedPhotos = getPhotosByTag(value);
				} else {
					List<IPhoto> newList = new BinarySearchList<IPhoto>();
					for(IPhoto p : getPhotosByTag(value)) {
						if(matchedPhotos.contains(p)) {
							newList.add(p);
						}
					}
					matchedPhotos = newList;
				}
			}

			// No need to continue
			if(matchedPhotos.isEmpty()) {
				break;
			}
		}

		Collections.sort(matchedPhotos, new Comparator<IPhoto>() {
			public int compare(IPhoto a, IPhoto b) {
				return a.getDateTime().compareTo(b.getDateTime());
			}
		});

		return matchedPhotos;
	}

	/**
	 * Get a list of photos that have the given key and value tag
	 * @return a list of IPhoto, sorted by date
	 */
	private List<IPhoto> getPhotosByTag(String key, String value) {
		List<IPhoto> matchedPhotos = new BinarySearchList<IPhoto>();
			
		for(IAlbum album : user.getAlbums()) {
			for(IPhoto p : album.getPhotos()) {
				if(p.hasTag(key, value)) {
					if(key.equals("location")) {
						// Special case where Photo does not take
						// location's key into account in Photo#equals()
						if(!p.getTagValue(key).equals(value)) {
							continue;
						}
					}
					if(!matchedPhotos.contains(p)) {
						matchedPhotos.add(p);
					}
				}
			}
		}

		return matchedPhotos;
	}

	/**
	 * Get a list of photos that have a tag with the given value
	 * @return a list of IPhoto
	 */
	private List<IPhoto> getPhotosByTag(String value) {
		List<IPhoto> matchedPhotos = new BinarySearchList<IPhoto>();
			
		for(IAlbum album : user.getAlbums()) {
			for(IPhoto p : album.getPhotos()) {
				if(p.hasTagWithValue(value)) {
					if(!matchedPhotos.contains(p)) {
						matchedPhotos.add(p);
					}
				}
			}
		}

		return matchedPhotos;
	}

}
