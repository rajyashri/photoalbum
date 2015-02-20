package cs213.photoAlbum.control;

import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.simpleview.InteractiveView;

public class LoginController {

	private User userModel;
	private InteractiveView userView;
	private String userid;
	private String pwd;
	
	public LoginController(User userModel, InteractiveView userView){
		this.userModel = userModel;
		this.userView = userView;
		
		
	}
	
	/**
	 * 
	 * @param userName
	 * @param passWord
	 * @return true if successfully created a new user
	 */
	public boolean createUser(String userName,String passWord){
		return false;
		
	}
	
	/**
	 * checks if user exist and returns true 
	 */
	
	public boolean checkUserExist(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 * @param userName
	 * @returns the list of users on record
	 */
	public IUser listUsers(String userName)
	{
		return null;
		
	}
	
	
}
