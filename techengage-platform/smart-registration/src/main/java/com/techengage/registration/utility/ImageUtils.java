package com.techengage.registration.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.openimaj.image.DisplayUtilities.ImageComponent;

import com.techengage.idm.dataobject.UserProfile;
import com.techengage.registration.idm.IdmConnectionManager;
import com.techengage.registration.idm.IdmConnectionManagerImpl;
import com.techengage.registration.idm.IdmRuntimeException;

public class ImageUtils {

    private static final JFrame frame;
    private static JPanel plForm;
    private static JLabel lbUserID;
    private static JLabel lbPassword;
    private static JLabel lbFirstName;
    private static JLabel lbLastName;
    private static JLabel lbEmail;

    private static JTextField txtFirstName;
    private static JTextField txtLastName;
    private static JTextField txtEmail;

    private static JTextField txtUserID;
    private static JPasswordField txtPassword;
    private static JButton btnSubmit;

    static {
	frame = new JFrame("Registration");
	frame.setSize(340, 480);
	frame.setLocationRelativeTo(null);
	plForm = new JPanel();

	lbUserID = new JLabel("User ID");
	lbPassword = new JLabel("Password");
	lbFirstName = new JLabel("First Name");
	lbLastName = new JLabel("Last Name");
	lbEmail = new JLabel("Email ID");

	txtUserID = new JTextField(10);
	txtPassword = new JPasswordField(10);
	txtFirstName = new JTextField(10);
	txtLastName = new JTextField(10);
	txtEmail = new JTextField(10);

	btnSubmit = new JButton("Submit");
	// Create the layout
	GroupLayout layout = new GroupLayout(plForm);
	plForm.setLayout(layout);
	plForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	// Horizontally, we want to align the labels and the text fields along the left (LEADING) edge
	layout.setAutoCreateGaps(true);
	layout.setHorizontalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lbUserID).addComponent(lbPassword)
			.addComponent(lbFirstName).addComponent(lbLastName).addComponent(lbEmail))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(txtUserID).addComponent(txtPassword)
			.addComponent(txtFirstName).addComponent(txtLastName).addComponent(txtEmail).addComponent(btnSubmit)));

	// Vertically, we want to align each label with his text fields on the baseline of the components
	layout.setVerticalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbUserID).addComponent(txtUserID))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbPassword).addComponent(txtPassword))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbFirstName).addComponent(txtFirstName))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbLastName).addComponent(txtLastName))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbEmail).addComponent(txtEmail))
		.addComponent(btnSubmit));

	btnSubmit.addActionListener(new ActionListener() {
	    @SuppressWarnings("deprecation")
	    public void actionPerformed(ActionEvent arg0) {

		IdmConnectionManager idmConnectionManager = IdmConnectionManagerImpl.getInstance();
		try {

		    UserProfile profile = new UserProfile();

		    profile.setUserName(txtUserID.getText());
		    profile.setPassword(txtPassword.getText());
		    profile.setFirstName(txtFirstName.getText());
		    profile.setLastName(txtLastName.getText());
		    profile.setEmail(txtEmail.getText());

		    profile = idmConnectionManager.registrationInitiation(profile);

		    String file = IConstants.REGISTRATION_TRIGGER_LOCATION + "/" + profile.getUserName() + "-" + System.currentTimeMillis() + ".txt";

		    FileWriter filewriter = new FileWriter(file, true);
		    filewriter.write(profile.getUserName() + ";10");
		    filewriter.close();
		    System.out.println(
			    "Trigger file created and placed successfully - " + profile.getUserName() + "-" + System.currentTimeMillis() + ".txt");
		} catch (IOException e) {
		    showAlert(e.getMessage());
		} catch (IdmRuntimeException idmException) {
		    showAlert(idmException.getMessage());
		}
	    }
	});
    }

    public static void display(final BufferedImage image) {

	if (frame.getContentPane().getComponentCount() > 0) {
	    final JPanel component = (JPanel) frame.getContentPane().getComponent(1);
	    if (component.getComponent(0) instanceof ImageComponent) {
		final ImageComponent cmp = ((ImageComponent) component.getComponent(0));
		cmp.setImage(image);
	    } else {
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());
		plForm.setBackground(new Color(233, 233, 233));
		frame.add(plForm, BorderLayout.SOUTH);
		final ImageComponent icImage = new ImageComponent(image);
		final JPanel imgPanel = new JPanel();
		imgPanel.setBackground(new Color(233, 233, 233));
		imgPanel.add(icImage);
		frame.add(imgPanel, BorderLayout.CENTER);
		frame.setVisible(true);
	    }
	} else {
	    frame.setLayout(new BorderLayout());
	    plForm.setBackground(new Color(233, 233, 233));
	    frame.add(plForm, BorderLayout.SOUTH);
	    final ImageComponent icImage = new ImageComponent(image);
	    final JPanel imgPanel = new JPanel();
	    imgPanel.setBackground(new Color(233, 233, 233));
	    imgPanel.add(icImage);
	    frame.add(imgPanel, BorderLayout.CENTER);
	    frame.setVisible(true);
	}
    }

    public static void showAlert(String message) {
	JOptionPane.showMessageDialog(null, message);
    }

}
