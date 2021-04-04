package prac_mul_tcp_s;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class chat extends Thread{
	JButton button = new JButton("send");
	String ID ,ip,port;
	JFrame frame = new JFrame();
	Socket socket;
	private BufferedReader inReader;
	private PrintWriter out ;
	JTextField txtf = new JTextField(24);
	JTextArea txtArea = new JTextArea(30,30);
	JTextArea txtArea1 = new JTextArea(30,30);
	BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	 JTextPane textPane = new JTextPane();

     StyledDocument doc = textPane.getStyledDocument();

     SimpleAttributeSet left = new SimpleAttributeSet();


     SimpleAttributeSet right = new SimpleAttributeSet();

	@Override
	public void run() {
	String string;
		// TODO Auto-generated method stub
		
		try {
			socket = new Socket(ip,Integer.parseInt(port));
			out = new PrintWriter(socket.getOutputStream(),true);
			inReader = new BufferedReader(
					   new InputStreamReader(socket.getInputStream()));
			out.println(ID);
			while(true) {
				System.out.println("xxxxx");
				string = inReader.readLine();
				System.out.println(string);
				
				txtArea.append(string+"\n");
				
				if(string.contains("/BYE")||string.contains("/bye")) {
					inReader.close();
					out.close();
					socket.close();
					frame.setVisible(false);
					break;
				}
			}
		} catch (UnknownHostException e) {e.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();}
		
	}
	
	public chat(String ip,String port,String ID) throws NumberFormatException, UnknownHostException, IOException {
		
			this.ip = ip;
			this.port = port;
			this.ID = ID;
			 StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		     StyleConstants.setForeground(left, Color.RED);

		     StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		     StyleConstants.setForeground(right, Color.BLUE);
		     
				frame.setLayout(new BorderLayout());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String srt = txtf.getText();
						System.out.println(srt);
						txtArea.append("ME : "+srt+"\n");
						
						out.println("from "+ID+" : "+srt);
						txtf.setText("");
						if(srt=="/BYE") {
								System.out.println("disconnect");
							}
						}
				});
				JPanel panelCenter = new JPanel();
				JScrollPane scrollPane=new JScrollPane(txtArea);
				panelCenter.add(scrollPane);
				JPanel topJPanel=new JPanel();
				topJPanel.add(new JLabel("chat input '/bye' to exit."));
				frame.add(topJPanel,BorderLayout.NORTH);
				JPanel panelBot = new JPanel();
				panelBot.add(txtf);
				panelBot.add(button);
	
				frame.add(panelCenter,BorderLayout.CENTER);
				frame.add(panelBot,BorderLayout.SOUTH);
				frame.setTitle("client - "+ID);
				frame.setSize(380,600);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
				
		
	}
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
//		new chat("","");
	}
}
