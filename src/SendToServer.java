import com.jcraft.jsch.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SendToServer {

	public SendToServer() {
		Stage stage = new Stage();
		GridPane root = new GridPane();
		TextField host = new TextField();
		host.setPromptText("Host");
		TextField user = new TextField();
		user.setPromptText("User");
		PasswordField pass = new PasswordField();
		pass.setPromptText("Pass");
		TextField pwd = new TextField();
		pwd.setPromptText("PWD");
		Button send = new Button("Send");
		send.setOnAction(e ->{
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(null);
			SendToServer sts = new SendToServer(host.getText(),user.getText(),pass.getText(),pwd.getText(), file);
		});
		root.add(host,0,0);
		root.add(user,0,1);
		root.add(pass,1,1);
		root.add(pwd,0,2);
		root.add(send,0,3);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	public SendToServer(String host, String user, String pwd, String password, File... files) {
		promptYesNo("Send to Server?");
		int port = 22;
		user = user;
		password = password;
		pwd = pwd;

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		ChannelExec channelExec = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			UserInfo ui = new MyUserInfo(){
				public void showMessage(String message){
					JOptionPane.showMessageDialog(null, message);
				}
				public boolean promptYesNo(String message){
					Object[] options={ "yes", "no" };
					int foo=JOptionPane.showOptionDialog(null,
							message,
							"Warning",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null, options, options[0]);
					return foo==0;
				}
			};
			session.setUserInfo(ui);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(pwd);
			for(File f : files) {
				channelSftp.put(new FileInputStream(f), f.getName());
			}
			JOptionPane.showMessageDialog(null, "Files Sent");
			channelSftp.disconnect();
			channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand("cd " + pwd +";javac -d classes *.java; java -cp classes Runner Output.graph;");
			channelExec.connect();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
	}
	public static boolean promptYesNo(String message){
		Object[] options={ "yes", "no" };
		int foo=JOptionPane.showOptionDialog(null,
				message,
				"Warning",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		return foo==0;
	}
	public static abstract class MyUserInfo
			implements UserInfo, UIKeyboardInteractive{
		public String getPassword(){ return null; }
		public boolean promptYesNo(String str){ return false; }
		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return false; }
		public boolean promptPassword(String message){ return false; }
		public void showMessage(String message){ }
		public String[] promptKeyboardInteractive(String destination,
												  String name,
												  String instruction,
												  String[] prompt,
												  boolean[] echo){
			return null;
		}
	}
}
