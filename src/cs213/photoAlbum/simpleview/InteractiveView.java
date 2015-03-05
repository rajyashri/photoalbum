package cs213.photoAlbum.simpleview;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import cs213.photoAlbum.control.SingleUserController;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.User;

public class InteractiveView {

	
	IUser owner;
	public InteractiveView(IUser user) {
	// TODO Auto-generated constructor stub
	
		owner = user;
	
	}
	
	
  
	public void userEdit(){
		
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\nWelcome"+ owner.getFullName());
		printUsage();
	        
		while(!sc.hasNext("loggout")){
		
				String userChoice = sc.next();
		
		switch(userChoice) {
		
		case "listAlbums" :
		{
			System.out.println("You are in listalbums");
			
			break;
		}
		case "createAlbum" :
		{
			if(!sc.hasNextLine())
			{
			String albumName = sc.next();
			System.out.println("Created new album ::" + albumName);
			}
			else
				System.out.println("User did not provide albumName");
			break;
		}
		
		case "deleteAlbum" :
		{
				if(!sc.hasNextLine())
				{
		    	String albumName = sc.next();
				System.out.println("Deleted the album ::" + albumName);
				}
				else
					System.out.println("User did not provide albumName!");
			
			break;		
		}
	
		default :
			System.out.println("Invalid Operation");
			break;
		
		}
		printUsage();	
	}
		return;

	}
	
	public void printUsage(){
		System.out.println("\nEnter the command to perform the operation");
		System.out.println("\tlistAlbums");
		System.out.println("\tcreateAlbum <albumName>");
		System.out.println("\tdeleteAlbum <albumName>");
		System.out.println("\tloggout");
	}
	}
	
	

