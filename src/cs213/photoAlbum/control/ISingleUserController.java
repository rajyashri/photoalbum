package cs213.photoAlbum.control;

import cs213.photoAlbum.model.IAlbum;

public interface ISingleUserController {

		/**
		 * @return true if user has this album
		 */
		public boolean hasAlbum(String albumName);

		/**
		 * @return album if user has it, null otherwise
		 */
		public IAlbum getAlbum(String albumName);

		/**
		 * @return true if album removed, false if it did not exist
		 */
		public boolean removeAlbum(String albumName);

		/**
		 * @return true if album added, false if it already existed
		 */
		public boolean addAlbum(String albumName);

		/**
		 * Add a photo to the user's album
		 * @return true if photo added to album successfully, false otherwise
		 */
		
	}



