import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SendToServer {
	public SendToServer(){
		promptYesNo("Send to Server?");
		String SFTPHOST = "192.168.0.61";
		int SFTPPORT = 22;
		String SFTPUSER = "jypotheren";
		String SFTPPASS = "Ki5vmrv4aaet84";
		String SFTPWORKINGDIR = "/home/jypotheren/testing";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		ChannelExec channelExec = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
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
			channelSftp.cd(SFTPWORKINGDIR);
			File f = new File("src/Runner.java");
			File t = new File("src/Test.java");
			channelSftp.put(new FileInputStream(f), f.getName());
			channelSftp.put(new FileInputStream(t), t.getName());
			JOptionPane.showMessageDialog(null, "Files Sent");
			channelSftp.disconnect();
			channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand("cd " + SFTPWORKINGDIR +";javac -d classes *.java; java -cp classes Runner Output.graph;");
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
