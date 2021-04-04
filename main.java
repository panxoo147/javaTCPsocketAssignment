package prac_mul_tcp_s;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;


public class main extends JFrame{
	String ID,PASS,iP;
	int port;
	public main() throws IOException {

//		Scanner scanner = new Scanner(System.in);
//		System.out.print("ID : ");
//		ID=scanner.next();
//		System.out.print("PASS : ");
//		PASS = scanner.next();
//		System.out.print("IP : ");
//		iP=scanner.next();
//		System.out.print("PORT : ");
//		port = scanner.nextInt();
		ID="6009650570";
		PASS="0570";
		iP="10.5.50.214";
		port = 8456;
		
		Thread thread1 = new Thread(new Client(ID,PASS,iP,port));
		thread1.start();
		
		Thread thread2 = new Thread(new Server(port, iP,ID));
		thread2.start();
	}
	public static void main(String[] args) throws IOException {
		new main();
	}
}
