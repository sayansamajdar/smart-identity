package com.techengage.verification.utility;

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
import com.techengage.verification.idm.IdmConnectionManager;
import com.techengage.verification.idm.IdmConnectionManagerImpl;
import com.techengage.verification.idm.IdmRuntimeException;

public class LoginForm {

    private static final JFrame frame;
    private static JPanel plForm;
    private static JLabel lbUserID;
    private static JLabel lbPassword;

    private static JTextField txtUserID;
    private static JPasswordField txtPassword;
    private static JButton btnSubmit;

    static {
	frame = new JFrame("Authentication");
	frame.setSize(340, 380);
	frame.setLocationRelativeTo(null);
	plForm = new JPanel();

	lbUserID = new JLabel("User ID");
	lbPassword = new JLabel("Password");

	txtUserID = new JTextField(10);
	txtPassword = new JPasswordField(10);

	btnSubmit = new JButton("Submit");
	// Create the layout
	GroupLayout layout = new GroupLayout(plForm);
	plForm.setLayout(layout);
	plForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	// Horizontally, we want to align the labels and the text fields along the left (LEADING) edge
	layout.setAutoCreateGaps(true);
	layout.setHorizontalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lbUserID).addComponent(lbPassword))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(txtUserID).addComponent(txtPassword)
			.addComponent(btnSubmit)));

	// Vertically, we want to align each label with his text fields on the baseline of the components
	layout.setVerticalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbUserID).addComponent(txtUserID))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbPassword).addComponent(txtPassword))
		.addComponent(btnSubmit));

	btnSubmit.addActionListener(new ActionListener() {
	    @SuppressWarnings("deprecation")
	    public void actionPerformed(ActionEvent arg0) {

		IdmConnectionManager idmConnectionManager = new IdmConnectionManagerImpl();
		try {
		    UserProfile userProfile = new UserProfile(txtUserID.getText(), txtPassword.getText());

		    userProfile = idmConnectionManager.login(userProfile);

		    String file = IConstants.FACE_VERIFICATION_TRIGGER + "/" + txtUserID.getText() + "-" + System.currentTimeMillis() + ".txt";

		    FileWriter filewriter = new FileWriter(file, true);
		    filewriter.write(userProfile.getFaceCode() + ";" + userProfile.getUserName());
		    filewriter.close();

		} catch (IOException e) {
		    e.printStackTrace();
		} catch (IdmRuntimeException e1) {
		    e1.printStackTrace();
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
