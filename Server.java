package prac_mul_tcp_s;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends Thread{
	JButton button = new JButton("send");
	String iD="6009650571",ipString;
	
	int port;
	JFrame frame = new JFrame();
	private BufferedReader inReader;
	private PrintWriter out ;
	JTextField txtf = new JTextField(30);
	JTextArea txtArea = new JTextArea(20,30);
	JTextArea txtArea1 = new JTextArea(20,30);
	BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port,1,InetAddress.getByName(ipString));
		
			System.out.println("waiting client....");
			txtArea.append("waiting for client....\n");
			Socket socket = serverSocket.accept();
			out = new PrintWriter(socket.getOutputStream(),true);
			inReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String clientIDString = inReader.readLine();
			System.out.println(clientIDString+" client connected");
			txtArea.append(clientIDString+" client connected \n");
			System.out.println(serverSocket.getLocalPort());
			while (true) {
				String str= inReader.readLine();
				txtArea1.append(str+"\n");
				if(str.contains("/BYE")||str.contains("/bye")) {
					txtArea.append(clientIDString+" disconnected\n");
					System.out.println(clientIDString+" disconnected");
					out.println("/BYE");
					bReader.close();
					out.close();
					inReader.close();
					socket.close();
					serverSocket.close();
					break;
				}
			}
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Server(int port,String ip,String ID) throws IOException {
		// TODO Auto-generated constructor stub
		this.iD=ID;
		this.port = port;
		this.ipString=ip;
		String string = "xxxxx";
			frame.setLayout(new BorderLayout());
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String srt = txtf.getText();
					System.out.println(srt);
					txtArea1.append("Me : "+srt+"\n");
					out.println("from "+ID+" : "+srt);
					txtf.setText("");
				}
			});
			JPanel panelCenter = new JPanel();
//			txtArea.setText("waiting client\n");
			txtArea.setEditable(false);
			txtArea1.setEditable(false);
			JScrollPane scrollPane=new JScrollPane(txtArea);
			panelCenter.add(scrollPane);
			JScrollPane scrollPane2=new JScrollPane(txtArea1);
			panelCenter.add(scrollPane2);
			
			JPanel panelBot = new JPanel();
			panelBot.add(txtf);
			panelBot.add(button);
			frame.add(panelCenter,BorderLayout.CENTER);
			frame.add(panelBot,BorderLayout.SOUTH);
			frame.setTitle("Your("+ID+") server start at - "+"ip: "+ip + " port : "+port);
			frame.setSize(800,420);
			frame.setLocation(800,420);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
			

	}	
	
}
