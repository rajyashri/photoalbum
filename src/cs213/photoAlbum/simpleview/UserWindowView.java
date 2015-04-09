package cs213.photoAlbum.simpleview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.IUserController;
import cs213.photoAlbum.control.UserController;
import cs213.photoAlbum.control.UserDataController;
import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IPhoto;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.Photo;

public class UserWindowView extends JFrame implements ActionListener {

	// General layout and three panes
	private GridBagLayout layout;
	private JPanel albumListPanel;
	private JPanel photoGridPanel;
	private JPanel photoDetailPanel;

	// albumListPanel fields
	private JLabel albumListTitle;
	private JList albumList;
	private JButton albumAdd;
	private JButton albumRename;
	private JButton albumDelete;

	// photoGridPanel fields
	private JLabel albumTitle;
	// TODO private GridLayout photoGrid;
	private JPanel photoGrid;
	private JButton photoAdd;

	// photoDetailPanel fields
	private JLabel photoTitle;
	private GridLayout detailGrid;
	private JLabel photoCaption;
	private JList tagList;
	private JButton tagEdit;
	private JButton photoDelete;
	private JButton photoMove;


	// Controller members
	private IUser user;
	private IUserController userController;
	private UserDataController controller;

	// Session state
	private IAlbum albumSelected = null;
	private IPhoto photoSelected = null;

	public static void main(String[] args) {
		// TODO hardcoding usercontroller / user for now 
		// until login screen is done
		show(new UserController(), 
				new UserController().getUser("victor"));
	}

	public static void show(IUserController userController, IUser user) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		UserWindowView view = new UserWindowView(userController, user);
		view.setVisible(true);
		view.setSize(900, 600);
		view.setMinimumSize(new Dimension(800, 600));
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public UserWindowView(IUserController userController, IUser user) {
		super(user.getFullName() + "'s Albums");

		this.user = user;
		this.userController = userController;
		this.controller = new UserDataController(user);

		setupGeneralLayout();
		setupAlbumListPanel();
	}

	private void setupGeneralLayout() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				userController.saveUser(user);
			}
		});

		getContentPane().addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent ce) {
				photoGridPanel.setPreferredSize(
						new Dimension( 4000, getContentPane().getHeight() - 10));
				if(photoGrid != null) {
					int columns = (getContentPane().getWidth() - 500) / 120;
					photoGrid.setLayout(new GridLayout(0, columns));
				}
			}

			public void componentMoved(ComponentEvent ce) { }
			public void componentShown(ComponentEvent ce) { }
			public void componentHidden(ComponentEvent ce) { }
		});

		// getContentPane().setLayout(new GridLayout(1, 3));
		SpringLayout layout = new SpringLayout();
		getContentPane().setLayout(layout);

		// Album List Panel
		albumListPanel = new JPanel();
		albumListPanel.setBorder(BorderFactory.createTitledBorder("Albums"));
		albumListPanel.setMaximumSize(new Dimension(250, 4000));
		albumListPanel.setMinimumSize(new Dimension(250, 500));
		getContentPane().add(albumListPanel);

		photoGridPanel = new JPanel();
		photoGridPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		getContentPane().add(photoGridPanel);

		photoDetailPanel = new JPanel();;
		photoDetailPanel.setMaximumSize(new Dimension(250, 4000));
		photoDetailPanel.setMinimumSize(new Dimension(250, 500));
		getContentPane().add(photoDetailPanel);

		// Album list is left pane
		layout.putConstraint(SpringLayout.WEST, albumListPanel, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, albumListPanel, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, albumListPanel, 5, SpringLayout.SOUTH, getContentPane());

		// Photo grid panel is middle
		layout.putConstraint(SpringLayout.WEST, photoGridPanel, 5, SpringLayout.EAST, albumListPanel);
		layout.putConstraint(SpringLayout.EAST, photoGridPanel, 5, SpringLayout.WEST, photoDetailPanel);
		layout.putConstraint(SpringLayout.NORTH, albumListPanel, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, albumListPanel, 5, SpringLayout.SOUTH, getContentPane());

		// Photo detail panel is right pane
		layout.putConstraint(SpringLayout.EAST, photoDetailPanel, 5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, photoDetailPanel, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, photoDetailPanel, 5, SpringLayout.SOUTH, getContentPane());

	}

	private void setupAlbumListPanel() {
		while(albumListPanel.getComponentCount() > 0) {
			albumListPanel.remove(0);
		}

		albumListPanel.setLayout(new BoxLayout(albumListPanel, BoxLayout.Y_AXIS));

		final List<IAlbum> list = controller.listAlbums();
		String[] albums = new String[list.size()];
		for(int i = 0; i < albums.length; i++) {
			albums[i] = list.get(i).getName();
		}

		System.out.println("There are " + albums.length + " albums");

		albumList = new JList<String>(albums);
		albumList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		albumList.setLayoutOrientation(JList.VERTICAL);
		albumList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(albumList);
		
		albumList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(albumList.getSelectedIndex() >= 0) {
					albumSelected = list.get(albumList.getSelectedIndex());
					albumRename.setEnabled(true);
					albumDelete.setEnabled(true);
				} else {
					albumSelected = null;
					albumRename.setEnabled(false);
					albumDelete.setEnabled(false);
				}
				setupPhotoGridPanel();
			}
		});
		albumList.setAlignmentX(Component.LEFT_ALIGNMENT);

		albumListPanel.add(albumList);	

		albumAdd = new JButton("Add Album");
		albumRename = new JButton("Rename Album");
		albumDelete = new JButton("Delete Album");

		albumAdd.addActionListener(this);
		albumRename.addActionListener(this);
		albumDelete.addActionListener(this);

		albumAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
		albumRename.setAlignmentX(Component.LEFT_ALIGNMENT);
		albumDelete.setAlignmentX(Component.LEFT_ALIGNMENT);

		albumRename.setEnabled(false);
		albumDelete.setEnabled(false);

		albumListPanel.add(albumAdd);
		albumListPanel.add(albumRename);
		albumListPanel.add(albumDelete);

		albumListPanel.setVisible(true);
		albumListPanel.revalidate();
		albumListPanel.repaint();
	}

	private void setupPhotoGridPanel() {
		while(photoGridPanel.getComponentCount() > 0) {
			photoGridPanel.remove(0);
		}

		photoGridPanel.setLayout(new BoxLayout(photoGridPanel, BoxLayout.Y_AXIS));
		
		if(albumSelected == null) {
			// Show empty view
			photoGridPanel.setBorder(BorderFactory.createTitledBorder(""));
		} else {
			photoGridPanel.setBorder(BorderFactory.createTitledBorder(albumSelected.getName()));

			// Setup photo grid
			photoGrid = new JPanel(new GridLayout(0, 3));
			final List<IPhoto> photoList  = controller.getPhotos(albumSelected.getName());
			for(int i = 0; i < photoList.size(); i++) {
				final IPhoto photo = photoList.get(i);
				ImageIcon icon = new ImageIcon(photo.getFileName());
				JButton button = new JButton(new ImageIcon(
							(icon.getImage())
								.getScaledInstance(100, 100 * icon.getImage().getHeight(this) / 
									icon.getImage().getWidth(this), java.awt.Image.SCALE_FAST), 
							photo.getCaption()));
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						photoSelected = photo;
						setupPhotoDetailPanel();
					}
				});
				photoGrid.add(button);
			}

			photoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
			photoGridPanel.add(new JScrollPane(photoGrid, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

			// Spacing
			photoGridPanel.add(new JLabel(" "));

			photoAdd = new JButton("Add Photo");
			photoAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
			photoAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Open a file chooser
					final JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Pick an Image");
					int value = chooser.showOpenDialog(photoAdd);

					if(value == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();

						// Make sure there isn't already a photo with 
						// this file path
						try {
						IPhoto photo = new Photo(file.getCanonicalPath());
							// Get a caption for the photo
							String caption= 
								JOptionPane.showInputDialog(photoAdd, 
										"Enter caption for photo:",
										"Add Photo", 
										JOptionPane.PLAIN_MESSAGE);
							if(caption.isEmpty()) {
								JOptionPane.showMessageDialog(photoAdd, 
										"Caption can't be blank",
										"Invalid Caption",
										JOptionPane.ERROR_MESSAGE);
							} else {
								photo.setCaption(caption);
								if(controller.addPhotoToAlbum(photo, albumSelected.getName())) {
									photoSelected = photo;
									setupPhotoGridPanel();
									setupPhotoDetailPanel();
								} else {
									JOptionPane.showMessageDialog(photoAdd, 
											"This album already contains this photo",
											"Duplicate Photo",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			photoGridPanel.add(photoAdd);
		}

		photoGridPanel.setVisible(true);
		photoGridPanel.revalidate();
		photoGridPanel.repaint();
	}

	private void setupPhotoDetailPanel() {
		while(photoDetailPanel.getComponentCount() > 0) {
			photoDetailPanel.remove(0);
		}


		if(photoSelected == null) {
			photoDetailPanel.setBorder(BorderFactory.createTitledBorder(""));
		} else {
			photoDetailPanel.setBorder(BorderFactory.createTitledBorder("Photo Details"));

			photoDetailPanel.setLayout(new GridBagLayout());
			GridBagConstraints cnts = new GridBagConstraints();
			cnts.fill = GridBagConstraints.BOTH;

			cnts.gridx = 0;
			cnts.gridy = 0;
			cnts.gridwidth = 3;
			cnts.gridheight = 1;

			ImageIcon icon = new ImageIcon(photoSelected.getFileName());
			JLabel iconLabel = new JLabel(new ImageIcon(
						(icon.getImage())
							.getScaledInstance(200, 200 * icon.getImage().getHeight(this) / 
								icon.getImage().getWidth(this), java.awt.Image.SCALE_FAST)));
			photoDetailPanel.add(iconLabel, cnts);


			cnts.gridx = 0;
			cnts.gridy = 1;
			cnts.gridwidth = 3;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			cnts.ipady = 10;

			JLabel label = new JLabel("Caption: " + photoSelected.getCaption());
			photoDetailPanel.add(label, cnts);

			cnts.gridx = 0;
			cnts.gridy = 2;
			cnts.gridwidth = 2;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			JButton editTags = new JButton("Edit Tags");
			photoDetailPanel.add(editTags, cnts);

			cnts.gridx = 1;
			cnts.gridy = 3;
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			JButton deletePhoto = new JButton("Delete Photo");
			photoDetailPanel.add(deletePhoto, cnts);

			cnts.gridx = 2;
			cnts.gridy = 3;
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			JButton movePhoto = new JButton("Move Photo");
			photoDetailPanel.add(movePhoto, cnts);
		}

		photoGridPanel.setVisible(true);
		photoGridPanel.revalidate();
		photoGridPanel.repaint();
	}

	// ActionListener method
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == albumAdd) {
			// Show dialog to add album
			String name = JOptionPane.showInputDialog(this, "Enter name for new album:", "Create Album", JOptionPane.PLAIN_MESSAGE);
			if(name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Album name can't be blank", "Invalid Album Name", JOptionPane.ERROR_MESSAGE);
			} else if(controller.hasAlbum(name)) {
				JOptionPane.showMessageDialog(this, "There is already an album with name \"" + name + "\"", "Duplicate Album Name", JOptionPane.ERROR_MESSAGE);
			} else {
				// Add the album
				controller.addAlbum(name);
				setupAlbumListPanel();
			}
		} else if(event.getSource() == albumRename) {
			if(albumSelected != null) {
				String name = JOptionPane.showInputDialog(this, "Enter new name for \"" + albumSelected.getName() + "\"",
										"Rename Album", JOptionPane.PLAIN_MESSAGE);
				if(name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Album name can't be blank", "Invalid Album Name", JOptionPane.ERROR_MESSAGE);
				} else if(name.equals(albumSelected.getName())) {
					// Nothing to do 
				} else {
					if(controller.hasAlbum(name)) {
						JOptionPane.showMessageDialog(this, "There is already an album with name \"" + name + "\"", "Duplicate Album Name", JOptionPane.ERROR_MESSAGE);
					} else {
						albumSelected.setName(name);
						setupAlbumListPanel();
					}
				}
			}
		} else if(event.getSource() == albumDelete) {
			int answer = JOptionPane.showConfirmDialog(
					this,
					"Are you sure you want to delete album " + 
						albumSelected.getName() + "?",
					"Delete Album",
					JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION) {
				// Do delete
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						controller.removeAlbum(albumSelected.getName());
						setupAlbumListPanel();
					}
				});
			}
		}
	}

}
