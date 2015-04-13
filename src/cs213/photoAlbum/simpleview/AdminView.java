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
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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
import javax.swing.event.ListDataListener;
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
	private JLabel userIdLabel;
	private JTextField userIdText;
	private JLabel userExistLabel;
	private JScrollPane scrollPane;
	private IUser user;
	private IUserController controller;
	
	private IUser userSelected = null;
	private List<IUser> list;
	String[] users ;
    private DefaultListModel <String>listModel;
	private GridBagUtility mylayout;
	
	public AdminView(IUserController controller){
			
		super("Admin Session");
		
		this.controller = controller;
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		populateUserArray();
		setupLayout();
		
			
	}


	
	private void setupLayout() {
	        mylayout = new GridBagUtility();
		
			leftPanel = new JPanel();
			rightPanel = new JPanel();
		
			
			userList = new JList(listModel);
			userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			userList.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
					if(userList.isSelectionEmpty())
						userDeleteButton.setEnabled(false);
					else
						userDeleteButton.setEnabled(true);
					
				}
			});
		
			
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
			userIdLabel = new JLabel ("User ID");
			userIdText = new JTextField(10);
			userIdText.addFocusListener(new FocusAdapter() {
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
			mylayout.appendComponentInGridRow(userIdLabel, rightPanel);
			mylayout.addFinalFieldInGridRow(userIdText, rightPanel);
			
			
			mylayout.addFinalFieldInGridRow(new JLabel(""), rightPanel);
			mylayout.addFinalFieldInGridRow(new JLabel(""), rightPanel);
			
			mylayout.appendComponentInGridRow(addButton, rightPanel);
			addButton.addActionListener(this);
				
			mylayout.addFinalFieldInGridRow(userExistLabel, rightPanel);
			
			setLayout(new FlowLayout());
						
			getContentPane().add(leftPanel);
			getContentPane().add(rightPanel);
	
			leftPanel.setVisible(true);
			leftPanel.repaint();
			leftPanel.revalidate();

		
	}



		@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource()==userDeleteButton)
		{
			int index = userList.getSelectedIndex();
			userSelected = list.get(index);
			System.out.println(userSelected.getFullName());
			controller.deleteUser(userSelected.getUserId());
			listModel.remove(index);
			userDeleteButton.setEnabled(false);
			
		}
		else if(e.getSource()==addButton)
		{
			String userName = text.getText();
			String userId = userIdText.getText();
			boolean flag = false;
		

			for(int index =0 ; index < users.length; index++)
			{
				if(userName.equalsIgnoreCase(users[index]) || controller.userExists(userId)){
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
				controller.addUser(userIdText.getText(),text.getText());
				System.out.println(text.getText());
				listModel.addElement(userName);
				
				System.out.println("Added user");
				
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
		
		listModel = new DefaultListModel<String>();
		list = controller.listUsers();
		users = new String[list.size()];
		
		for(int index=0;index<users.length;index++)
		{
			users[index]= list.get(index).getFullName();
			listModel.addElement(users[index]);
			
		
		}
		
				
	}

}



