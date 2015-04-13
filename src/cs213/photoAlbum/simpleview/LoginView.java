package cs213.photoAlbum.simpleview;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cs213.photoAlbum.control.IUserController;
import cs213.photoAlbum.control.UserController;

public class LoginView extends JFrame implements ActionListener, DocumentListener {

	//General Layout
	
	private GridBagLayout grid;
	private JLabel userID;
	private JLabel invalidID;
	private JTextField text;
	private JButton loginButton;
	
	IUserController controller;
	
	public LoginView(){
		super("Photo Album Login");
		setupLayout();
	}

	public static void showWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		LoginView view = new LoginView();
		view.setVisible(true);
		view.setSize(300, 200);
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void setupLayout() {
		grid = new GridBagLayout();
		setLayout(grid);
			
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = 10;
		constraints.ipady = 10;
		
		userID = new JLabel("UserID");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		add(userID,constraints);
		
		invalidID = new JLabel("<html><font color='red'>Invalid User ID</font></html>");
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(invalidID,constraints);
		invalidID.setVisible(false);
			
		text = new JTextField(15);
		text.addActionListener(this);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(text,constraints);
		
		loginButton = new JButton("Login");
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		add(loginButton,constraints);
		loginButton.setEnabled(false);
		
		this.text.getDocument().addDocumentListener(this);
	}


	public static void main(String[] args) {
		showWindow();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String id = text.getText();
				String admin = "admin";
				controller = new UserController();
				
				if(id.equals(admin)){
					//Invoke AdminView
					System.out.println("Admin user");
					AdminView.showWindow(controller);
					invalidID.setVisible(false);
				} else if(controller.userExists(id)){
					invalidID.setVisible(false);
					LoginView.this.dispose();
					UserWindowView.show(controller, controller.getUser(id));
				} else {
					invalidID.setVisible(true);
				}
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
