/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.simpleview;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.UserDataController;
import cs213.photoAlbum.model.IPhoto;


public class EditTagsDialog extends JDialog implements ListSelectionListener, ActionListener {

	private static final long serialVersionUUID = 119349494;

	/**
	 * A listener for when the EditTagsDialog is closed
	 */
	public static interface Callbacks {
		/**
		 * Called when the EditTagsDialog is closed,
		 * this would be a good time to refresh
		 * the tags displayed, in case there were updates
		 */
		public void editTagsDialogClosed();
	}

	private IPhoto photo;
	private UserDataController controller;
	private List<String> tagList = new ArrayList<String>();
	private JList<String> list;
	private JButton editTagButton;
	private JButton deleteTagButton;
	private JButton addTagButton;

	public EditTagsDialog(Frame frame, final IPhoto photo, final UserDataController controller, final Callbacks listener) {
		super(frame, "Edit Tags", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.photo = photo;
		this.controller = controller;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if(listener != null) {
					listener.editTagsDialogClosed();
				}
			}
		});

		setSize(200, 300);

		setupView();
	}

	/**
	 * Sets up the layout and content of the window
	 */
	private void setupView() {
		while(getContentPane().getComponentCount() > 0) {
			getContentPane().remove(0);
		}

		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cnts = new GridBagConstraints();

		// List of tags
		cnts.gridx = 0;
		cnts.gridy = 0;
		cnts.gridwidth = 2;
		cnts.ipadx = 10;
		cnts.ipady = 10;
		cnts.fill = GridBagConstraints.HORIZONTAL;
		tagList = photo.getTagStrings();
		String[] tagArray = new String[tagList.size()];
		for(int i = 0; i < tagArray.length; i++) {
			tagArray[i] = tagList.get(i);
		}
		list = new JList<String>(tagArray);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.addListSelectionListener(this);
		getContentPane().add(list, cnts);

		cnts.gridy++;
		cnts.gridx = 0;
		cnts.gridwidth = 1;
		cnts.fill = GridBagConstraints.HORIZONTAL;
		editTagButton = new JButton("Edit Tag");
		editTagButton.setEnabled(false);
		editTagButton.addActionListener(this);
		getContentPane().add(editTagButton, cnts);
		
		cnts.gridx = 1;
		cnts.gridwidth = 1;
		cnts.fill = GridBagConstraints.HORIZONTAL;
		deleteTagButton = new JButton("Delete Tag");
		deleteTagButton.setEnabled(false);
		deleteTagButton.addActionListener(this);
		getContentPane().add(deleteTagButton, cnts);

		cnts.gridy++;
		cnts.gridx = 0;
		cnts.gridwidth = 2;
		getContentPane().add(new JLabel(" "), cnts);

		cnts.gridy++;
		cnts.gridx = 0;
		cnts.gridwidth = 2;
		cnts.fill = GridBagConstraints.HORIZONTAL;
		addTagButton = new JButton("Add Tag");
		addTagButton.addActionListener(this);
		getContentPane().add(addTagButton, cnts);

		setVisible(true);
		revalidate();
		repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		editTagButton.setEnabled(true);
		deleteTagButton.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editTagButton) {
			String tag = list.getSelectedValue();
			String[] tagAndValue = tag.split(":");
			String value = JOptionPane.showInputDialog(getContentPane(), 
						"Enter new value for tag \"" + tagAndValue[0] + "\"",
						"Edit Tag", 
						JOptionPane.PLAIN_MESSAGE);

			if(value == null) {
				// User canceled, do nothing
			} else if(value.isEmpty()) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Tag's value can't be blank",
						"Invalid Tag Value",
						JOptionPane.ERROR_MESSAGE);
			} else {
				controller.deleteTag(photo, tagAndValue[0], tagAndValue[1]);
				if(controller.addTag(photo, tagAndValue[0], value)) {
					// Edited successfully!
				} else {
					// Re-add the old tag
					controller.addTag(photo, tagAndValue[0], tagAndValue[1]);
					JOptionPane.showMessageDialog(getContentPane(), 
						"Can't add a duplicate tag",
						"Duplicate Tag",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if(e.getSource() == deleteTagButton) {
			String tag = list.getSelectedValue();
			String[] tagAndValue = tag.split(":");
			controller.deleteTag(photo, tagAndValue[0], tagAndValue[1]);
		} else if(e.getSource() == addTagButton) {
			String key = JOptionPane.showInputDialog(getContentPane(), 
						"Enter type of tag: ",
						"Add Tag", 
						JOptionPane.PLAIN_MESSAGE);

			if(key == null) {
				// User canceled, do nothing
			} else if(key.isEmpty()) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Tag's type can't be blank",
						"Invalid Tag Type",
						JOptionPane.ERROR_MESSAGE);
			} else if(key.contains(":")) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Tag's type can't contain \":\"",
						"Invalid Tag Type",
						JOptionPane.ERROR_MESSAGE);
			} else {
				String value = JOptionPane.showInputDialog(getContentPane(), 
							"Enter value for tag \"" + key + "\": ",
							"Add Tag", 
							JOptionPane.PLAIN_MESSAGE);

				if(value == null) {
					// Do nothing
				} else if(value.isEmpty()) {
					JOptionPane.showMessageDialog(getContentPane(), 
						"Tag's value can't be blank",
						"Invalid Tag Value",
						JOptionPane.ERROR_MESSAGE);
				} else if(value.contains(":")) {
					JOptionPane.showMessageDialog(getContentPane(), 
						"Tag's value can't contain \":\"",
						"Invalid Tag Value",
						JOptionPane.ERROR_MESSAGE);
				} else if(controller.addTag(photo, key.trim().toLowerCase(), value.trim())) {
					// Added successfully!
				} else {
					// Show duplicate tag message
					JOptionPane.showMessageDialog(getContentPane(), 
						"Can't add a duplicate tag",
						"Duplicate Tag",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		setupView();
	}

}
