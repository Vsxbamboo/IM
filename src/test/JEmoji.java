package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class JEmoji extends JFrame implements MouseListener{
	public JButton[] emojis=new JButton[32];
	public ImageIcon[] icons=new ImageIcon[32];
	public JEmoji() {
		super("Emoji");
		setLocation(700,200);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new GridLayout(6,6));
		for(int i=1;i<=32;i++) {
			icons[i-1]=new ImageIcon(".\\emoji\\"+i+".jpg");
			emojis[i-1]=new JButton(icons[i-1]);
			emojis[i-1].addMouseListener(this);
			add(emojis[i-1]);
		}
		pack();
		setVisible(true);
	}
//	public static  void main(String[] args) {
//		new JEmoji();
//	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		for (int i=0;i<32;i++) {
//			if(e.getSource()==emojis[i]) {
//				try {
//					if (
//						PostAndGet.postMsg(new Message(NewGUI.nick.getText(),postid,icon.getImage()))
//						) {
//							Thread.sleep(100);
//							id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
//							
//					} else {
//						sendBox.setText("fail to send msg");
//					}
//				} catch (Exception exc) {
//					System.out.println(
//							"Error in NewGUI chooseClicked"
//							);
//				}
//			}
//
//		}
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
}
