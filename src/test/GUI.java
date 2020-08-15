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
	
	ImageIcon icon=new ImageIcon();
	JLabel image=new JLabel(icon);
	JButton choose=new JButton("choose");
	JPanel imagePanel=new JPanel();
	JFileChooser chooser=new JFileChooser();
	
	Thread refresh= new Thread(this);
	public GUI(){
		super("IM");
		setLocation(200,200);
		setSize(500,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new  GridLayout(2,2));
		msgBox.setLineWrap(true);
		add(msgBoxScrollPane);
		send.addMouseListener(this);
		sendPanel.add(sendBox);
		sendPanel.add(send);
		add(sendPanel);
		
		add(image);
		imagePanel.setLayout(new FlowLayout());
		choose.addMouseListener(this);
		imagePanel.add(choose);
		add(imagePanel);
		
		setVisible(true);
		refresh.start();
	}
	public void run(){
		while(refresh==Thread.currentThread()){
			try {
				Thread.sleep(1000);
				msgBox.setText(PostAndGet.getMsg());
				image.setIcon(new ImageIcon(PostAndGet.getImage()));
			} catch (Exception ie) {
				System.out.println("Error in GUI run");
				msgBox.setText("unable to connect the server");
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		if(me.getSource()==send){
			try {
				if (!sendBox.getText().equals("")){
					PostAndGet.postMsg("\r\n"+sendBox.getText());
					sendBox.setText("");
				}
			} catch (Exception exc) {	
				System.out.println("Error in mouseClicked");
			}
		}
		if(me.getSource()==choose){
			int result=chooser.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
				String name = chooser.getSelectedFile().getPath();
				try {
					PostAndGet.postImage(new ImageIcon(name).getImage());
				} catch (Exception e) {
					System.out.println("Error in GUI choose Clicked");
				}
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui=new GUI();
	}
}
