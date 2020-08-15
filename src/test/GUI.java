package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame implements MouseListener,Runnable {
	
	JTextArea msgBox=new JTextArea();
	JScrollPane msgBoxScrollPane=new JScrollPane(msgBox);
	JTextField sendBox=new JTextField(10);
	JButton send=new JButton("send");
	JPanel sendPanel=new JPanel();
	Thread tick= new Thread(this);
	public GUI(){
		super("IM");
		setLocation(200,200);
		setSize(500,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new  GridLayout());
		msgBox.setLineWrap(true);
		add(msgBoxScrollPane);
		send.addMouseListener(this);
		sendPanel.add(sendBox);
		sendPanel.add(send);
		add(sendPanel);
		setVisible(true);
		tick.start();
	}
	public void run(){
		while(tick==Thread.currentThread()){
			try {
				Thread.sleep(1300);
				msgBox.setText(PostAndGet.getMsg());
			} catch (Exception ie) {
				System.out.println("Error in GUI run");
				msgBox.setText("unable to connect the server");
			}
		}
	}
		
	
	@Override
	public void mouseClicked(MouseEvent me) {
		try {
			if (!sendBox.getText().equals("")){
				PostAndGet.postMsg("\r\n"+sendBox.getText());
				sendBox.setText("");
			}
		} catch (Exception exc) {
			System.out.println("Error in mouseClicked");
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	public static void main(String[] args) {
		GUI gui=new GUI();
	}
}
