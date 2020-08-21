package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
//用于选择Emoji表情的窗口
public class JEmoji extends JFrame {
	public JButton[] emojis=new JButton[32];//32个按钮
	public ImageIcon[] icons=new ImageIcon[32];//32个图片
	public JEmoji() {
		super("Emoji");
		setLocation(700,200);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//不能让它关了整个程序
		setLayout(new GridLayout(6,6));
		for(int i=1;i<=32;i++) {//把emoji目录下的图片添加进来
			icons[i-1]=new ImageIcon(".\\emoji\\"+i+".jpg");
			emojis[i-1]=new JButton(icons[i-1]);
			add(emojis[i-1]);
		}
		
		pack();//自动确定大小
		setVisible(true);
	}
}