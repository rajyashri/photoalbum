/**
 * @author Rajyashri Vasudevamoorthy
 * @author Victor Kaiser-Pendergrast
 */

package cs213.photoAlbum.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.User;

public class UserController implements IUserController {

	public UserController(){

	}
	
	/**
	 * @param username
	 * @return true if successfully created a new user
	 */
	public boolean addUser(String userid, String name){
		if(userExists(userid)) {
			return false;	
		}

		User user = new User(userid, name);
		try {
			FileOutputStream fileOut = new FileOutputStream(getDataDirectory() + "/" + userid);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

			objOut.writeObject(user);

			objOut.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	/**
	 * Save the state of a user
	 * @return true if user saved successfully
	 */
	public boolean saveUser(IUser user) {
		try {
			FileOutputStream fileOut = new FileOutputStream(getDataDirectory() + "/" + user.getUserId());
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

			objOut.writeObject(user);

			objOut.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Deletes a user
	 * @return true if user existed and was deleted
	 */
	public boolean deleteUser(String userid) {
		if(!userExists(userid)) {
			return false;
		}

		try {
			File f = new File(getDataDirectory() + "/" + userid);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * Checks if user exists
	 * @return true if user file exists in data/
	 */
	public boolean userExists(String userid) {
		File f = new File(getDataDirectory() + "/" + userid);
		return f.exists();
	}

	public IUser getUser(String userid) {
		String dataDir = getDataDirectory();

		try {
			File dir = new File(dataDir);		
			FileInputStream fileIn = new FileInputStream(dir + "/" + userid);
			ObjectInputStream objIn = new ObjectInputStream(fileIn);

			Object obj = objIn.readObject();
			objIn.close();
			if(obj instanceof IUser) {
				return (IUser) obj;
			} else {
				System.out.println("This is not an IUser object!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Get a list of users
	 * @returns the list of users in data/
	 */
	public List<IUser> listUsers() {
		List<IUser> users = new ArrayList<IUser>();

		String dataDir = getDataDirectory();

		try {
			File dir = new File(dataDir);		
			String[] files = dir.list();
			
			for(String file : files) {
				// Ignore hidden files, current directory, and parent directory
				if(!file.startsWith(".")) {
					FileInputStream fileIn = new FileInputStream(dir + "/" + file);
					ObjectInputStream objIn = new ObjectInputStream(fileIn);

					Object obj = objIn.readObject();
					if(obj instanceof IUser) {
						users.add((IUser) obj);
					} else {
						System.out.println("This is not an IUser object!");
					}

					objIn.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return users;
	}
	

	/**
	 * Get the directory data/ directory path
	 */
	private String getDataDirectory() {
		try {
			File f = new File(".");
			String path = f.getAbsolutePath().toString();
			if(path.endsWith("/.")) {
				path = path.substring(0, path.length() - 2);
			}
			if(path.endsWith("/bin")) {
				path = path.substring(0, path.length() - 4);
			}

			path = path + "/data";

			f = new File(path);
			if(!f.exists()) {
				f.mkdir();
			}

			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "../data";
	}
	
}
