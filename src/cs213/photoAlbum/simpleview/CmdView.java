package cs213.photoAlbum.simpleview;

import java.util.List;

import cs213.photoAlbum.control.UserController;
import cs213.photoAlbum.model.IUser;

public class CmdView {

	public static void main(String[] argv) {
		UserController controller = new UserController();

		// Determine possible commands by arguments
		switch(argv.length) {
			case 0:
				printUsage();
				break;
			case 1:
				if("listusers".equals(argv[0])) {
					List<IUser> users = controller.listUsers();

					if(users.isEmpty()) {
						System.out.println("no users exist");
					}

					for(IUser user : users) {
						System.out.println(user.getUserId());
					}
				} else {
					printUsage();
				}
				break;
			case 2:
				if("deleteuser".equals(argv[0])) {
					if(controller.deleteUser(argv[1])) {
						System.out.println("deleted user " + argv[1]);
					} else {
						System.out.println("user " + argv[1] + " does not exist");
					}
				}  else if ("login".equals(argv[1])) {
					// TODO launch interactive mode
				}
				break;
			case 3:
				if("adduser".equals(argv[0])) {
					if(controller.addUser(argv[1], argv[2])) {
						System.out.println("created user " + argv[1] + " with name " + argv[2]);
					} else {
						System.out.println("user " + argv[1] + " already exists with name " + controller.getUser(argv[1]).getFullName());
					}
				} else {
					printUsage();
				}
				break;
			default:
				printUsage();
				break;
		}	
	}


	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("java cs213.photAlbum.simpleview.CmdView listusers");
		System.out.println("java cs213.photAlbum.simpleview.CmdView adduser <user id> <user's name>");
		System.out.println("java cs213.photAlbum.simpleview.CmdView deleteuser <user id>");
		System.out.println("java cs213.photAlbum.simpleview.CmdView login <user id>");
	}

}
