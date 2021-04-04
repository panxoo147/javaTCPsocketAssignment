package prac_mul_tcp_s;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import project342.client;

public class Client extends Thread {
	String ID, PASS, IP;
	int PORT;
	JButton button = new JButton("send");
	private ArrayList<String> friendList = new ArrayList<String>();
	JFrame frame = new JFrame();
	private BufferedReader inReader;
	private PrintWriter out;
	JTextField txtf = new JTextField(15);
	JTextArea txtArea = new JTextArea(15, 45);
	JLabel lbLabel = new JLabel("Input(IP:PORT) friend that u want to chat");

	JTextArea txtArea1 = new JTextArea(30, 30);
	BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	Timer timer;
	String ServerIP;
	int ServerPort;

	@Override
	public void run() {
		Socket socket;
		Path path = Paths.get("src\\server.config");
		String s = path.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		File f = new File(s);
		Scanner sc;
		try {
			sc = new Scanner(f);
			ServerIP = sc.nextLine();
			ServerPort = Integer.valueOf(sc.nextLine());
			System.out.println("connect to Central Server : " + ServerIP + " " + ServerPort);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			socket = new Socket(ServerIP, ServerPort);
			out = new PrintWriter(socket.getOutputStream(), true);
			inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println("USER:" + ID);
			out.println("PASS:" + PASS);
			out.println("IP:" + IP);
			out.println("PORT:" + PORT);

			String authStatus = "";
			authStatus = inReader.readLine();
			System.out.println(authStatus);
			if (authStatus.equalsIgnoreCase("200 SUCCESS")) {
				String str;
				int count1 = 0;
				readFriendList();
				String sentence = "", input;
				heartbeatChanelHandle();

			} else {
				socket.close();
				System.out.println("socket closed");
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Client(String ID, String PASS, String IP, int port) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub

		this.ID = ID;
		this.PASS = PASS;
		this.IP = IP;
		this.PORT = port;

		ActionListener task = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				UpdateTXT();
			}

		};
		timer = new Timer(100, task);
		frame.setLayout(new BorderLayout());

		JPanel panelCenter = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(txtArea);
		panelCenter.add(scrollPane);

		JPanel panelBot = new JPanel();
		panelBot.add(lbLabel);
		panelBot.add(txtf);

		JPanel panelEast = new JPanel();

		JPanel panelWest = new JPanel();
		JButton btnStartChatWithButton = new JButton("Chat With");

		btnStartChatWithButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String ipString = IP, port = txtf.getText();
				try {
					String string[] = txtf.getText().split(":");
					String ip, portString;
					ip = string[0];
					port = string[1];
					System.out.println(ip + " " + port);
					Thread t1 = new Thread(new chat(ip, port, ID));
					t1.start();
				} catch (Exception e1) {

				}
			}
		});
		panelBot.add(btnStartChatWithButton);
		frame.add(panelEast, BorderLayout.EAST);
		frame.add(panelWest, BorderLayout.WEST);
		frame.add(panelCenter, BorderLayout.CENTER);
		frame.add(panelBot, BorderLayout.SOUTH);
		frame.setSize(600, 350);
		frame.setTitle("Friend list");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

	}

	public void readFriendList() throws IOException {
		int count = 0;
		String str = readEOF();

		txtArea.setText("\tID\tIP\t\tPORT\n\t==========================================\n");
		while (!str.equalsIgnoreCase("END")) {
			String newstr = str.replaceAll("([A-Z])\\w+ \\d{10}", "");
			System.out.println(count + ". " + newstr);
			String string[] = newstr.split(":");
			System.out.println(string[1]);
			if (!newstr.contains("-1")) {
				if (string[1].length() < 15) {
					txtArea.append("\t" + string[0] + "\t" + string[1] + "\t\t" + string[2] + "\n");
				} else
					txtArea.append("\t" + string[0] + "\t" + string[1] + "\t" + string[2] + "\n");
			}
			friendList.add(newstr);
			str = inReader.readLine();
			count++;
		}
		System.out.println(count + ". " + str);
	}

	public String readEOF() throws IOException {
		String str = inReader.readLine();
		return str;
	}

	int count = 0;

	public void heartbeatChanelHandle() throws IOException {
		String sentence = "", input;
		int count1 = 0;
		Scanner scanner = new Scanner(System.in);

		while (true) {
			friendList.clear();
			System.out.println("------------" + "" + count1 + ". Waiting...friendlist-----------");
			out.println("Hello Server");
			readFriendList();
			count1++;
		}
	}

	public void Update(ArrayList<String> friendList) {
		this.friendList.clear();
		for (String string : friendList) {
			this.friendList.add(string);
		}
		timer.start();
	}

	public void UpdateTXT() {
		txtArea.setText("");
		for (String string : friendList) {
			txtArea.append(string + "\n");
		}
		timer.stop();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
//		new Client();
	}
}
