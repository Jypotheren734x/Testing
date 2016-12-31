import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by komar on 12/20/2016.
 */
public class RecieveFromServer {
    public static void main(String[] args) {
        String host = "192.168.0.61";
        int port = 22;
        String user = "jypotheren";
        String pass = "Ki5vmrv4aaet84";
        String pwd = "/home/jypotheren/testing";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(pass);
            UserInfo ui = new SendToServer.MyUserInfo(){
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
            File f = new File("Output.graph");
            channelSftp.get(f.getName(), new FileOutputStream(f));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
