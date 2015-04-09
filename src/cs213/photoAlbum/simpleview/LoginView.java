package cs213.photoAlbum.simpleview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cs213.photoAlbum.control.IUserController;
import cs213.photoAlbum.control.UserController;
import cs213.photoAlbum.model.User;

public class LoginView extends JFrame implements ActionListener, DocumentListener {

	//General Layout
	
	private GridBagLayout grid;
	private JLabel userID;
	private JLabel invalidID;
	private JTextField text;
	private JButton loginButton;
	
	IUserController controller;
	
	public LoginView(){
		super("Login");
		setupLayout();
	}
	
	
	private void setupLayout() {
		// TODO Auto-generated method stub
		
		grid = new GridBagLayout();
		setLayout(grid);
			
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = 10;
		
		userID = new JLabel("UserID");
		constraints.weightx = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(userID,constraints);
		
		invalidID = new JLabel("Invalid User ID");
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(invalidID,constraints);
		invalidID.setVisible(false);
		
		text = new JTextField(15);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(text,constraints);
		
		loginButton = new JButton("Login");
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(loginButton,constraints);
		loginButton.setEnabled(false);
		
		this.text.getDocument().addDocumentListener(this);
		
		loginButton.addActionListener(new ActionListener() {
            
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String id = text.getText();
				String admin = "admin";
				controller = new UserController();
				
				if(id.equals(admin)){
					//Invoke AdminView
					System.out.println("Admin user");
					invalidID.setVisible(false);
					
				}
				else if(controller.userExists(id)){
					invalidID.setVisible(false);
					UserWindowView userView = new UserWindowView(controller, controller.getUser(id));
					
					//System.out.println("User Exists");
					
				}
				else
					invalidID.setVisible(true);
			}
			
		});
		
	}


	public static void main(String[] args) {
		
		LoginView view = new LoginView();
		
		view.setVisible(true);
		view.setSize(500,400);
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		updateButtonEnabledStatus(loginButton,text);
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		updateButtonEnabledStatus(loginButton,text);
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		updateButtonEnabledStatus(loginButton,text);
		
	}

	private void updateButtonEnabledStatus(JButton loginButton,JTextField field) {
		// TODO Auto-generated method stub
		boolean enabled = true;
		
			if(field.getText().length()== 0) {
				enabled = false;		
			}
		loginButton.setEnabled(enabled);
	}
    
	

	

}
