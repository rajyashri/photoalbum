package cs213.photoAlbum.simpleview;

public class CmdView {

	public static void main(String[] argv) {
		// Determine possible commands by arguments
		switch(argv.length) {
			case 0:
				printUsage();
				break;
			case 1:
				if("listusers".equals(argv[0])) {

				} else if ("login".equals(argv[1])) {

				} else {
					printUsage();
				}
				break;
			case 2:
				if("adduser".equals(argv[0])) {
					
				} else {
					printUsage();
				}
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
