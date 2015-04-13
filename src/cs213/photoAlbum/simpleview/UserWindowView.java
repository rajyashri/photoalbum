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
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.IUserController;
import cs213.photoAlbum.control.UserDataController;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IPhoto;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.model.Photo;

public class UserWindowView extends JFrame implements ActionListener {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");

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
	private List<IPhoto> photoList;
	private IPhoto photoSelected = null;
	private int photoSelectedIndex = -1;
	private boolean showSaveAlbum = false;
	private boolean noShowMoveDelete = false;

	/**
	 * Shows a UserWindowView
	 * @param userController the user controller used by LoginView
	 * @param user the user whose details this UserWindowView should show
	 */
	public static void show(IUserController userController, IUser user) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		UserWindowView view = new UserWindowView(userController, user);
		view.setSize(900, 700);
		view.setMinimumSize(new Dimension(800, 700));
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		view.pack();
		view.setVisible(true);
	}

	private UserWindowView(IUserController userController, IUser user) {
		super(user.getFullName() + "'s Albums");

		this.user = user;
		this.userController = userController;
		this.controller = new UserDataController(user);

		setupMenuBar();
		setupGeneralLayout();
		setupAlbumListPanel();
	}

	private void resizeGrid() {
		if(photoGrid != null) {
			int columns = (getContentPane().getWidth() - 500) / 110;
			int rows = 0; // any number of rows
			
			photoGrid.setLayout(new GridLayout(rows, columns));
		}
	}

	/**
	 * Sets up the three panes for 
	 * listing albums, showing pictures,
	 * and showing pictured details
	 */
	private void setupGeneralLayout() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				userController.saveUser(user);
				UserWindowView.this.dispose();

				// Show login view
				LoginView.showWindow();
			}
		});

		getContentPane().addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent ce) {
				photoGridPanel.setPreferredSize(
						new Dimension( 4000, getContentPane().getHeight() - 10));
				resizeGrid();
			}

			public void componentMoved(ComponentEvent ce) { }
			public void componentShown(ComponentEvent ce) { }
			public void componentHidden(ComponentEvent ce) { }
		});

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

	/**
	 * Sets up the Search and Logout
	 * items in the MenuBar
	 */
	private void setupMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu search = new JMenu("Search");
		menubar.add(search);
		JMenuItem logout = new JMenuItem("Logout");
		JMenuItem searchByDate = new JMenuItem("By Date");
		JMenuItem searchByTag = new JMenuItem("By Tag");
		search.add(searchByDate);
		search.add(searchByTag);
		menubar.add(logout);
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userController.saveUser(user);
				UserWindowView.this.dispose();

				// Show login view
				LoginView.showWindow();
			}
		});
		searchByDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar start, end;

				String startString = JOptionPane.showInputDialog(getContentPane(), 
						"Enter start date (in the form MM/DD/YYYY-hh:mm:ss):",
						"Search by Dates", 
						JOptionPane.PLAIN_MESSAGE);

				if(startString == null) {
					// Cancel search
				} else {
					try{
						start = Calendar.getInstance();
						start.setTime(DATE_FORMAT.parse(startString.trim()));
						start.set(Calendar.MILLISECOND, 0);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(getContentPane(), 
							"Dates need to be entered in the format MM/DD/YYYY-hh:mm:ss",
							"Invalid Date",
							JOptionPane.ERROR_MESSAGE);
						return;
					}

					String endString = JOptionPane.showInputDialog(getContentPane(), 
						"Enter end date (in the form MM/DD/YYYY-hh:mm:ss):",
						"Search by Dates", 
						JOptionPane.PLAIN_MESSAGE);

					try{
						end = Calendar.getInstance();
						end.setTime(DATE_FORMAT.parse(endString.trim()));
						end.set(Calendar.MILLISECOND, 0);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(getContentPane(), 
							"Dates need to be entered in the format MM/DD/YYYY-hh:mm:ss",
							"Invalid Date",
							JOptionPane.ERROR_MESSAGE);
						return;
					}

					
					List<IPhoto> photos = controller.getPhotosByDate(start, end);
					IAlbum album = new Album("Dates: " + startString + " to " + endString, user);
					for(IPhoto photo : photos) {
						album.addPhoto(photo);
					}
					albumSelected = album;
					photoSelected = null;
					showSaveAlbum = true;
					noShowMoveDelete = true;

					setupPhotoGridPanel();
					setupPhotoDetailPanel();
				}
			}
		});
		searchByTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tagString = JOptionPane.showInputDialog(getContentPane(), 
						"Enter tag query\n(in form [<tagType:>]\"<tagValue>\" [,[<tagType:>]\"<tagValue>\"]):",
						"Search by Tags", 
						JOptionPane.PLAIN_MESSAGE);
				
				if(tagString == null) {
					// Do nothing on user cancel
				} else if(tagString.isEmpty()) {
					JOptionPane.showMessageDialog(photoAdd, 
							"Tag query cannot be empty",
							"Blank Search",
							JOptionPane.ERROR_MESSAGE);
				} else {
					List<IPhoto> photos =  controller.getPhotosByTagString(tagString);
					IAlbum album = new Album("Tags: " + tagString, user);
					for(IPhoto photo : photos) {
						album.addPhoto(photo);
					}
					albumSelected = album;
					photoSelected = null;
					showSaveAlbum = true;
					noShowMoveDelete = true;

					setupPhotoGridPanel();
					setupPhotoDetailPanel();
				}
			}
		});
		setJMenuBar(menubar);
	}

	/**
	 * Populates the AlbumListPanel, can also be used
	 * to refresh the album list
	 */
	private void setupAlbumListPanel() {
		while(albumListPanel.getComponentCount() > 0) {
			albumListPanel.remove(0);
		}

		albumListPanel.setLayout(new BoxLayout(albumListPanel, BoxLayout.Y_AXIS));

		final List<IAlbum> list = controller.listAlbums();
		String[] albums = new String[list.size()];
		for(int i = 0; i < albums.length; i++) {
			if(list.get(i).getName().length() < 30) {
				albums[i] = list.get(i).getName();
			} else {
				albums[i] = list.get(i).getName().substring(0, 25) + "...";
			}
		}

		albumList = new JList<String>(albums);
		albumList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		albumList.setLayoutOrientation(JList.VERTICAL);
		albumList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(albumList);
		
		albumList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(albumList.getSelectedIndex() >= 0) {
					if(list.get(albumList.getSelectedIndex()) != albumSelected) {
						showSaveAlbum = false;
						noShowMoveDelete = false;
						albumSelected = list.get(albumList.getSelectedIndex());
						photoSelected = null;
						albumRename.setEnabled(true);
						albumDelete.setEnabled(true);
					}
				} else {
					showSaveAlbum = false;
					noShowMoveDelete = false;
					albumSelected = null;
					albumRename.setEnabled(false);
					albumDelete.setEnabled(false);
				}
				setupPhotoGridPanel();
				setupPhotoDetailPanel();
			}
		});
		albumList.setAlignmentX(Component.LEFT_ALIGNMENT);

		albumListPanel.add(albumList);	
		albumListPanel.add(new JLabel(" "));

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

		albumListPanel.add(albumRename);
		albumListPanel.add(albumDelete);
		albumListPanel.add(new JLabel(" "));
		albumListPanel.add(albumAdd);

		albumListPanel.setVisible(true);
		albumListPanel.revalidate();
		albumListPanel.repaint();
	}

	/**
	 * Shows the grid of photos based on the currently
	 * selected album, or by the current search
	 */
	private void setupPhotoGridPanel() {
		while(photoGridPanel.getComponentCount() > 0) {
			photoGridPanel.remove(0);
		}

		photoGridPanel.setLayout(new BoxLayout(photoGridPanel, BoxLayout.Y_AXIS));
		
		if(albumSelected == null) {
			// Show empty view
			photoGridPanel.setBorder(BorderFactory.createTitledBorder(""));
		} else {
			photoGridPanel.setBorder(BorderFactory.createTitledBorder(albumSelected.getName() ));

			JLabel photoLabel = new JLabel(albumSelected.getPhotoCount() + " photos");
			photoGridPanel.add(photoLabel);

			if(albumSelected.getPhotoCount() != 0) {
				JLabel dateLabelFrom = new JLabel("From " + DATE_FORMAT.format(albumSelected.getFirstDate().getTime()));
				JLabel dateLabelTo = new JLabel("     to " + DATE_FORMAT.format(albumSelected.getLastDate().getTime()));
				photoGridPanel.add(dateLabelFrom);
				photoGridPanel.add(dateLabelTo);
			}

			// Setup photo grid
			photoGrid = new JPanel(new GridLayout(0, 3));
			photoList  = albumSelected.getPhotos();
			for(int i = 0; i < photoList.size(); i++) {
				final IPhoto photo = photoList.get(i);
				ImageIcon icon = new ImageIcon(photo.getFileName());
				JButton button = new JButton(new ImageIcon(
							(icon.getImage())
								.getScaledInstance(100, 100 * icon.getImage().getHeight(this) / 
									icon.getImage().getWidth(this), java.awt.Image.SCALE_FAST), 
							photo.getCaption()));
				final int index = i;
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						photoSelected = photo;
						photoSelectedIndex = index;
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

						if(!file.exists()) {
							JOptionPane.showMessageDialog(photoAdd, 
									"The selected file doesn't exist",
									"Cannot Add Photo",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

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
							// Should never happen
						}
					}
				}
			});
			photoGridPanel.add(photoAdd);

			if(showSaveAlbum) {
				// Show a button to save albumSelected as a new album.
				// This is used for saving searches

				JButton saveAlbum = new JButton("Save as Album");
				saveAlbum.setAlignmentX(Component.LEFT_ALIGNMENT);
				saveAlbum.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(controller.hasAlbum(albumSelected.getName())) {
								JOptionPane.showMessageDialog(photoAdd, 
										"An album with this name alreay exists",
										"Duplicate Album",
										JOptionPane.ERROR_MESSAGE);
						} else {
							controller.addAlbum(albumSelected);
							setupAlbumListPanel();
							albumSelected = null;
							photoSelected = null;
							setupPhotoGridPanel();
							setupPhotoDetailPanel();
						}
					}
				});
				photoGridPanel.add(saveAlbum);
			}
		}

		photoGridPanel.setVisible(true);
		photoGridPanel.revalidate();
		photoGridPanel.repaint();
	}

	/**
	 * Show's the details of the current photo,
	 * allows editing of tags, updating of caption, etc.
	 */
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
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			JButton previousButton = new JButton("<<");
			previousButton.addActionListener(this);
			photoDetailPanel.add(previousButton, cnts);

			cnts.gridx = 1;
			cnts.gridy = 0;
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			JButton nextButton = new JButton(">>");
			nextButton.addActionListener(this);
			photoDetailPanel.add(nextButton, cnts);

			cnts.gridy++;
			photoDetailPanel.add(new JLabel(" "), cnts);

			cnts.gridx = 0;
			cnts.gridy++;
			cnts.gridwidth = 3;
			cnts.gridheight = 1;
			ImageIcon icon = new ImageIcon(photoSelected.getFileName());
			JLabel iconLabel = new JLabel(new ImageIcon(
						(icon.getImage())
							.getScaledInstance(200, 200 * icon.getImage().getHeight(this) / 
								icon.getImage().getWidth(this), java.awt.Image.SCALE_FAST)));
			photoDetailPanel.add(iconLabel, cnts);

			cnts.gridy++;
			photoDetailPanel.add(new JLabel(" "), cnts);


			cnts.gridx = 0;
			cnts.gridy++;
			cnts.gridwidth = 3;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			cnts.ipady = 10;
			JTextArea captionLabel = new JTextArea("Caption: " + photoSelected.getCaption());
			captionLabel.setMaximumSize(new Dimension(300, 500));
			photoDetailPanel.add(captionLabel, cnts);

			cnts.gridx = 0;
			cnts.gridy++;
			cnts.gridwidth = 3;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			cnts.ipady = 10;
			JTextArea dateLabel = new JTextArea("Date: " + DATE_FORMAT.format(photoSelected.getDateTime().getTime()));
			photoDetailPanel.add(dateLabel, cnts);


			cnts.gridy++;
			cnts.gridwidth = 3;
			List<String> tags = photoSelected.getTagStrings();
			if(tags.isEmpty()) {
				photoDetailPanel.add(new JTextArea("No tags set"), cnts);
			} else {
				photoDetailPanel.add(new JTextArea("Tags: "), cnts);

				cnts.gridy++;
				String[] tagArray = new String[tags.size()];
				for(int i = 0; i < tagArray.length; i++) {
					tagArray[i] = tags.get(i);
				}
				tagList = new JList<String>(tagArray);
				tagList.setSelectionModel(new NoSelectionModel());
				JScrollPane listScroller = new JScrollPane(tagList);
				photoDetailPanel.add(tagList, cnts);
			}

			cnts.gridy++;
			photoDetailPanel.add(new JLabel(" "), cnts);
			
			JButton editCaption = new JButton("Edit Caption");
			cnts.gridx = 0;
			cnts.gridy++;
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			editCaption.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					String caption = JOptionPane.showInputDialog(UserWindowView.this,
						       	"Enter new caption:", "Edit Caption", JOptionPane.PLAIN_MESSAGE);

					if(caption == null) {
						// Do nothing
					} else if(caption.isEmpty()) {
						JOptionPane.showMessageDialog(UserWindowView.this,
							       	"Caption can't be blank", "Invalid Caption", JOptionPane.ERROR_MESSAGE);
					} else {
						IPhoto photo;
						try {
							photo = new Photo(photoSelected.getFileName(), photoSelected.getCaption());

							List<IAlbum> containingAlbums = controller.getAlbumsContaining(photo);
							if(!containingAlbums.isEmpty()) {
								// Get a reference to this photo
								photo = controller.getPhoto(photo.getFileName(), containingAlbums.get(0).getName());
								photo.setCaption(caption);
							}
						} catch (FileNotFoundException e) {
						}

						setupPhotoDetailPanel();
					}
				}
			});
			photoDetailPanel.add(editCaption, cnts);

			cnts.gridx = 1;
			cnts.gridwidth = 1;
			cnts.gridheight = 1;
			cnts.ipadx = 10;
			JButton editTags = new JButton("Edit Tags");
			editTags.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					EditTagsDialog diag = new EditTagsDialog(UserWindowView.this,
									photoSelected, controller, 
									new EditTagsDialog.Callbacks() {
										@Override
										public void editTagsDialogClosed() {
											// Refresh photo detail view 
											// in case tags were updated
											setupPhotoDetailPanel();
										}
									});
				}
			});
			photoDetailPanel.add(editTags, cnts);

			if(!noShowMoveDelete) {
				cnts.gridx = 0;
				cnts.gridy++;
				cnts.gridwidth = 1;
				cnts.gridheight = 1;
				cnts.ipadx = 10;
				JButton deletePhoto = new JButton("Delete Photo");
				deletePhoto.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						if(photoSelected != null) {
							int answer = JOptionPane.showConfirmDialog(
									UserWindowView.this,
									"Are you sure you want to delete this photo?",
									"Delete Photo",
									JOptionPane.YES_NO_OPTION);
							if(answer == JOptionPane.YES_OPTION) {
								controller.removePhotoFromAlbum(
										photoSelected.getFileName(), 
										albumSelected.getName());
								photoSelected = null;
								setupPhotoDetailPanel();
								setupPhotoGridPanel();
							}
						}
					}
				});
				photoDetailPanel.add(deletePhoto, cnts);


				cnts.gridx = 1;
				cnts.gridwidth = 1;
				cnts.gridheight = 1;
				cnts.ipadx = 10;
				JButton movePhoto = new JButton("Move Photo");
				movePhoto.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						List<IAlbum> albumList = controller.listAlbums();
						final String[] albumArray = new String[albumList.size() - 1];
						int i = 0;
						for(IAlbum album : albumList) {
							if(!album.getName().equals(albumSelected.getName())) {
								albumArray[i] = album.getName();
								i++;
							}
						}
						JList<String> list = new JList<String>(albumArray);
						list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
						JOptionPane.showMessageDialog(UserWindowView.this, list,
										"Move to Album", JOptionPane.PLAIN_MESSAGE);

						if(controller.getPhoto(photoSelected.getFileName(), list.getSelectedValue()) != null) {
							JOptionPane.showMessageDialog(UserWindowView.this, 
									"Album \"" + list.getSelectedValue() + "\" already contains this photo", 
									"Cannot Move", JOptionPane.ERROR_MESSAGE);
						} else {
							controller.removePhotoFromAlbum(photoSelected.getFileName(), albumSelected.getName());
							controller.addPhotoToAlbum(photoSelected, list.getSelectedValue());
							photoSelected = null;
							setupPhotoDetailPanel();
							setupPhotoGridPanel();
						}
					}
				});
				photoDetailPanel.add(movePhoto, cnts);
			}
		}

		photoGridPanel.setVisible(true);
		photoGridPanel.revalidate();
		photoGridPanel.repaint();

		resizeGrid();
	}

	// ActionListener methods
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == albumAdd) {
			// Show dialog to add album
			String name = JOptionPane.showInputDialog(this, "Enter name for new album:", "Create Album", JOptionPane.PLAIN_MESSAGE);
			if(name == null) {
				// Do nothing
			} else	if(name.isEmpty()) {
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
		} else if(event.getSource() instanceof JButton && ((JButton)event.getSource()).getText().equals("<<")) {
			photoSelectedIndex--;
			if(photoSelectedIndex < 0) {
				photoSelectedIndex = photoList.size() - 1;
			}
			photoSelected = photoList.get(photoSelectedIndex);
			setupPhotoDetailPanel();
		} else if(event.getSource() instanceof JButton && ((JButton)event.getSource()).getText().equals(">>")) {
			photoSelectedIndex++;
			if(photoSelectedIndex >= photoList.size()) {
				photoSelectedIndex = 0;
			}
			photoSelected = photoList.get(photoSelectedIndex);
			setupPhotoDetailPanel();
		}
	}

	// Used to not show selections for the Tag list
	class NoSelectionModel extends DefaultListSelectionModel {
		@Override
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1, -1);
		}
	}
}
