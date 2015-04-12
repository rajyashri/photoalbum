<<<<<<< local
/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.simpleview;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs213.photoAlbum.control.UserDataController;
import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IPhoto;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.Photo;

public class InteractiveView {

	private static final String LOGOUT = "logout";
	private static final String ALBUM_CREATE = "createAlbum";
	private static final String ALBUM_DELETE = "deleteAlbum";
	private static final String ALBUM_LIST = "listAlbums";
	private static final String PHOTO_ADD = "addPhoto";
	private static final String PHOTO_REMOVE = "removePhoto";
	private static final String PHOTO_LIST = "listPhotos";
	private static final String PHOTO_MOVE = "movePhoto";
	private static final String PHOTO_LIST_INFO = "listPhotoInfo";
	private static final String TAG_ADD = "addTag";
	private static final String TAG_DELETE = "deleteTag";
	private static final String GET_PHOTOS_BY_DATE = "getPhotosByDate";
	private static final String GET_PHOTOS_BY_TAG = "getPhotosByTag";

	private static final String COMMAND_SPLIT_REGEX = "([^\"^:][a-zA-Z0-9/_\\.-]*|\".+?\")\\s*";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");

	private IUser user;
	private UserDataController controller;

	private Scanner stdin;
	private Pattern inputRegex;

	private String commandLine;

	public InteractiveView(IUser user) {
		this.user = user;	
		controller = new UserDataController(user);
		stdin = new Scanner(System.in);
		inputRegex = Pattern.compile(COMMAND_SPLIT_REGEX);
	}

	/**
	 * Start an interactive mode, in which
	 * the user's photos and albums can be manipulated
	 */
	public void startInteractiveMode() {
		String[] command;
		while(true) {
			command = getCommand();
			if(command.length == 0) {
				continue;
			}

			switch(command[0]) {
				case LOGOUT:
					// Return, exiting interactive mode
					return;
				case ALBUM_CREATE:
					if(command.length == 2) {
						if(controller.addAlbum(command[1])) {
							System.out.println("created album for user " + user.getUserId());
						} else {
							System.out.println("album exists for user " + user.getUserId());
						}
					} else {
						printInvalidCommand();
					}
					break;
				case ALBUM_DELETE:
					if(command.length == 2) {
						if(controller.removeAlbum(command[1])) {
							System.out.println("deleted album for user " + user.getUserId());
						} else {
							System.out.println("album does not exist for user " + user.getUserId());
						}
					} else {
						printInvalidCommand();
					}
					break;
				case ALBUM_LIST:
					List<IAlbum> albums = controller.listAlbums();
					if(albums.isEmpty()) {
						System.out.println("no albums exist for user " + user.getUserId());
					} else {
						System.out.println("Albums for user " + user.getUserId());
						for(IAlbum album : albums) {
							Calendar first = album.getFirstDate();
							if(first == null ){
								System.out.println(album.getName() + " number of photos: " + album.getPhotoCount());
							} else {
								System.out.println(album.getName() + " number of photos: " + album.getPhotoCount() +
									       	", " + DATE_FORMAT.format(album.getFirstDate().getTime()) + " - " +
									       	DATE_FORMAT.format(album.getLastDate().getTime()) );
							}
						}
					}
					break;
				case PHOTO_ADD:
					if(command.length == 4) {
						// Check if album exists
						if(!controller.hasAlbum(command[3])) {
							System.out.println("Album " + command[3] + " does not exist");
						}

						IPhoto photo;
						try {
							photo = new Photo(command[1], command[2]);

							List<IAlbum> containingAlbums = controller.getAlbumsContaining(photo);
							if(!containingAlbums.isEmpty()) {
								// Get a reference to this photo
								photo = controller.getPhoto(photo.getFileName(), containingAlbums.get(0).getName());
								photo.setCaption(command[2]);
								System.out.println("Updated photo caption");
							}
						} catch (FileNotFoundException e) {
							System.out.println("File " + command[1] + " does not exist");
							break;
						}

						if(controller.addPhotoToAlbum(photo, command[3])) {
							System.out.println("Added photo " + command[1] + ":\n" + command[2] + " - Album: " + command[3]);
						} else {
							System.out.println("Photo " + command[1] + " already exists in ablum " + command[3]);
						}
					} else {
						printInvalidCommand();
					}
					break;
				case PHOTO_LIST:
					if(command.length == 2) {
						List<IPhoto> photos = controller.getPhotos(command[1]);
						if(photos == null) {
							System.out.println("Album " + command[1] + " does not exist");
						} else {
							if(photos.isEmpty()) {
								System.out.println("Album " + command[1] + " has no photos");
							} else {
								System.out.println("Photos for album " + command[1] + ":");
								for(IPhoto photo : photos) {
									System.out.println(photo.getFileName() + " - " + DATE_FORMAT.format(photo.getDateTime().getTime()));
								}
							}
						}
					} else {
						printInvalidCommand();
					}
					break;
				case PHOTO_REMOVE:
					if(command.length == 3) {
						if(controller.hasAlbum(command[2])) {
							if(controller.removePhotoFromAlbum(command[1], command[2])) {
								System.out.println("Removed photo:\n" + command[1] + " - From album " + command[2]);
							} else {
								System.out.println("Photo " + command[1] + " does not exist in album " + command[2]);

							}
						} else {
							System.out.println("Album " + command[2] + " does not exist");
						}
					} else {
						printInvalidCommand();
					}
					break;
				case PHOTO_MOVE:
					if(command.length == 4) {
						if(!controller.hasAlbum(command[2])) {
							System.out.println("Album " + command[2] + " does not exist");
							break;
						}

						if(!controller.hasAlbum(command[3])) {
							System.out.println("Album " + command[3] + " does not exist");
							break;
						}

						IPhoto photo = controller.getPhoto(command[1], command[2]);
						if(photo == null) {
							System.out.println("Photo " + command[1] + " does not exist in " + command[2]);
							break;
						}

						if(!controller.removePhotoFromAlbum(command[1], command[2])) {
							System.out.println("Couldn't remove photo from album");
							break;
						}

						if(!controller.addPhotoToAlbum(photo, command[3])) {
							System.out.println("Couldn't add photo to album");
							break;
						}

						System.out.println("Moved photo " + command[1] + ":\n" + command[1] + 
									" - From album " + command[2] + " to album " + command[3]);
					} else {
						printInvalidCommand();
					}
					break;
				case PHOTO_LIST_INFO:
					if(command.length == 2) {
						IPhoto photo;
						       
						try {
							photo = new Photo(command[1], "");
						} catch (Exception e) {
							System.out.println("Photo " + command[1] + " does not exist");
							break;
						}

						List<IAlbum> containingAlbums = controller.getAlbumsContaining(photo);

						if(containingAlbums.isEmpty()) {
							System.out.println("Photo " + command[1] + " does not exist");
							break;
						}

						// Get the photo from one of the albums
						photo = controller.getPhoto(command[1], containingAlbums.get(0).getName());

						System.out.println("Photo file name: " + photo.getFileName());

						System.out.print("Album: ");
						for(int i = 0; i < containingAlbums.size(); i++) {
							System.out.print(containingAlbums.get(i).getName());
							if(i < containingAlbums.size() - 1) {
								System.out.print(", ");
							}
						}
						System.out.println();

						System.out.println("Date: " + DATE_FORMAT.format(photo.getDateTime().getTime()));
						System.out.println("Caption: " + photo.getCaption());

						List<String> tags = photo.getTagStrings();
						if(!tags.isEmpty()) {
							System.out.println("Tags:");
							for(String t : tags) {
								System.out.println(t);
							}
						}
					} else {
						printInvalidCommand();
					}
					break;
				case TAG_ADD:
					if(command.length == 4) {
						Photo photo;

						try {
							photo = new Photo(command[1], "");
							if(controller.getAlbumsContaining(photo).isEmpty()) {
								System.out.println("Photo " + command[1] + " does not exist");
								break;
							}
						} catch (Exception e) {
							System.out.println("Photo " + command[1] + " does not exist");
							break;
						}

						if(controller.addTag(photo, command[2], command[3])) {
							System.out.println("Added tag:\n" + command[1] + " " + 
										command[2] + ":\"" + command[3] + "\"");
						} else {
							System.out.println("Tag already exists for " + command[1] + " " +
										command[2] + ":\"" + command[3] + "\"");
						}
					} else {
						printInvalidCommand();
					}
					break;
				case TAG_DELETE:
					if(command.length == 4) {
						Photo photo;

						try {
						photo = new Photo(command[1], "");
							if(controller.getAlbumsContaining(photo).isEmpty()) {
								System.out.println("Photo " + command[1] + " does not exist");
								break;
							}
						} catch (Exception e) {
							System.out.println("Photo " + command[1] + " does not exist");
							break;
						}

						if(controller.deleteTag(photo, command[2], command[3])) {
							System.out.println("Deleted tag:\n" + command[1] + " " + 
										command[2] + ":\"" + command[3] + "\"");
						} else {
							System.out.println("Tag does not exist for " + command[1] + " " +
										command[2] + ":\"" + command[3] + "\"");
						}
					} else {
						printInvalidCommand();
					}
					break;
				case GET_PHOTOS_BY_DATE:
					if(command.length == 7) {
						Calendar start, end;

						try {
							start = Calendar.getInstance();
							start.setTime(DATE_FORMAT.parse(command[1] + ":" + command[2] + ":" + command[3]));
							start.set(Calendar.MILLISECOND, 0);

							end = Calendar.getInstance();
							end.setTime(DATE_FORMAT.parse(command[4] + ":" + command[5] + ":" + command[6]));
							end.set(Calendar.MILLISECOND, 0);
						} catch(ParseException e) {
							System.out.println("Dates need to be in the form MM/DD/YYY-hh:mm:ss");
							break;
						}

						List<IPhoto> photos = controller.getPhotosByDate(start, end);
						if(photos.isEmpty()) {
							System.out.println("No photos exist between " + DATE_FORMAT.format(start.getTime()) + " to " + DATE_FORMAT.format(end.getTime()));
						} else {
							System.out.println("Photos for user " + user.getUserId() + 
										" in date range "  + 
										DATE_FORMAT.format(start.getTime()) +
										" to " + DATE_FORMAT.format(end.getTime()));
							for(IPhoto p : photos) {
								String albumString = "";

								List<IAlbum> containingAlbums = controller.getAlbumsContaining(p);
								for(int i = 0; i < containingAlbums.size(); i++) {
									albumString += containingAlbums.get(i).getName();
									if(i < containingAlbums.size() - 1) {
										albumString += ", ";
									}
								}
								
								System.out.println(p.getCaption() + " - Album: " + albumString + 
											" - Date: " + 
											DATE_FORMAT.format(p.getDateTime().getTime()));
							}
						}

					} else {
						printInvalidCommand();
						System.out.println("Dates need to be in the form MM/DD/YYY-hh:mm:ss");
					}
					break;
				case GET_PHOTOS_BY_TAG:
					if(command.length > 1) {
						commandLine = commandLine.replace(GET_PHOTOS_BY_TAG, "").trim();
						List<IPhoto> photos =  controller.getPhotosByTagString(commandLine);

						if(photos.isEmpty()) {
							System.out.println("No photos exist with tag " + 
										command[1]+ ":\"" + command[2] + "\"");
						} else {
							System.out.println("Photos for user " + user.getUserId() + 
											" with tags " + commandLine);
							for(IPhoto p : photos) {
								String albumString = "";

								List<IAlbum> containingAlbums = controller.getAlbumsContaining(p);
								for(int i = 0; i < containingAlbums.size(); i++) {
									albumString += containingAlbums.get(i).getName();
									if(i < containingAlbums.size() - 1) {
										albumString += ", ";
									}
								}
								
								System.out.println(p.getCaption() + " - Album: " + albumString + 
											" - Date: " + 
											DATE_FORMAT.format(p.getDateTime().getTime()));
							}
						}

					} else {
						printInvalidCommand();
					}
					break;
				default:
					printInvalidCommand();
					break;
			}
		}
	}

	private void printInvalidCommand() {
		System.out.println("Invalid command");
	}

	/**
	 * Return tokens of the command
	 * in a String array
	 */
	private String[] getCommand() {
		commandLine = stdin.nextLine();

		ArrayList<String> tokens = new ArrayList<String>(5);
		Matcher matcher = inputRegex.matcher(commandLine);
		while(matcher.find()) {
			tokens.add(matcher.group(1).replace("\"", ""));
		}

		return tokens.toArray(new String[0]);
	}

}
=======
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
	
	

>>>>>>> other
