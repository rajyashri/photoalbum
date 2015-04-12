package cs213.photoAlbum.simpleview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.IUserController;
import cs213.photoAlbum.model.IAlbum;
import cs213.photoAlbum.model.IUser;
import cs213.photoAlbum.util.GridBagUtility;

public class AdminView extends JFrame implements ActionListener  {
	
	//General Layout
		
	private GridBagLayout grid;
	private JPanel leftPanel; // userList Panel
	private JPanel rightPanel; // newUser Panel
	
	//userListPanel
		
	private JList userList;
	private JButton userDeleteButton;
	
	//newUserPanel
	private JButton addButton;
	private JLabel newUserLabel;
	private JTextField text;
	private JLabel userExistLabel;
	private JScrollPane scrollPane;
	private IUser user;
	private IUserController controller;
	
	private IUser userSelected = null;
	private List<IUser> list;
	String[] users ;

	private GridBagUtility mylayout;
	
	//public AdminView(IUserController controller){
	public AdminView(IUserController controller){
			
		super("Admin Session");
		
	this.controller = controller;
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		populateUserArray();
		setNewUserPanel();
		//setupUserList();
			
	}


	
	private void setNewUserPanel() {
	        mylayout = new GridBagUtility();
		
			leftPanel = new JPanel();
			rightPanel = new JPanel();
			
			userList = new JList(users);
			userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(userList);

			userDeleteButton = new JButton("Delete");
			addButton = new JButton("Add");
			userExistLabel = new JLabel("User Exist");
			userExistLabel.setVisible(false);
			newUserLabel = new JLabel("New User");
			text = new JTextField(10);
			text.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent fe)
				{
					userExistLabel.setVisible(false);
				}
			});
			leftPanel.setLayout(new GridBagLayout());
			rightPanel.setLayout(new GridBagLayout());
			
		    mylayout.addFinalFieldInGridRow(scrollPane,leftPanel);
		    
		    
			mylayout.addFinalFieldInGridRow(userDeleteButton, leftPanel);
			userDeleteButton.addActionListener(this);
			
			
			
			mylayout.appendComponentInGridRow(newUserLabel, rightPanel);
			mylayout.addFinalFieldInGridRow(text, rightPanel);
			
			
			mylayout.addFinalFieldInGridRow(new JLabel(""), rightPanel);
			mylayout.addFinalFieldInGridRow(new JLabel(""), rightPanel);
			
			mylayout.appendComponentInGridRow(addButton, rightPanel);
			addButton.addActionListener(this);
				
			mylayout.addFinalFieldInGridRow(userExistLabel, rightPanel);
			
			setLayout(new FlowLayout());
						
			getContentPane().add(leftPanel);
			getContentPane().add(rightPanel);
	

		
	}



		@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource()==userDeleteButton)
		{
			int index = userList.getSelectedIndex();
			controller.deleteUser(getName());
			userList.remove(index);
			//populateUserArray();
			
		}
		else if(e.getSource()==addButton)
		{
			String user = text.getText();
			boolean flag = false;
		

			for(int index =0 ; index < users.length; index++)
			{
				if(user.equalsIgnoreCase(users[index])){
					flag = true;
					break;
				}
				else
					flag = false;
			}
			
			if(flag)
				userExistLabel.setVisible(flag);
			else
			{
				//controller.addUser(userid, name)
				System.out.println("Add user here");
				//populateUserArray();
			}
		}
	}
	
	public static void showWindow(IUserController controller) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		AdminView view = new AdminView(controller);
		view.setSize(300, 350);
		view.setMinimumSize(new Dimension(300, 350));
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.pack();
		view.setVisible(true);
	
	
	}

	public void populateUserArray()
	{
		
		list = controller.listUsers();
		users = new String[list.size()];
		
		for(int index=0;index<users.length;index++)
		{
			users[index]= list.get(index).getFullName();
		}
	}

	
	

}



/*

private void setupUserList() {
// TODO Auto-generated method stub
while(leftPanel.getComponentCount() > 0) {
	leftPanel.remove(0);
}

leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));



userList = new JList<String>(users);
userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
userList.setLayoutOrientation(JList.VERTICAL);
userList.setVisibleRowCount(-1);

JScrollPane listScroller = new JScrollPane(userList);

userList.addListSelectionListener(new ListSelectionListener() {
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(userList.getSelectedIndex() >= 0) {
			if(list.get(userList.getSelectedIndex()) != userSelected) {
				userSelected = list.get(userList.getSelectedIndex());
				userDeleteButton.setEnabled(true);
			}
		} else {
			userSelected = null;
			userDeleteButton.setEnabled(false);
		}
		
	}
});
userList.setAlignmentX(Component.LEFT_ALIGNMENT);

leftPanel.add(userList);	
leftPanel.add(new JLabel(" "));

userDeleteButton = new JButton("Delete User");
leftPanel.add(userDeleteButton);
userDeleteButton.setEnabled(false);

}

*/