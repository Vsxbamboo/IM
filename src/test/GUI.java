package test;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import encryption.Encryption;
import encryption.Transform;

public class GUI extends JFrame{
	public boolean emojiInit=false;
	public JEmoji Emoji;//emoji窗口
	public JPanel msgBox=new JPanel();//消息框
	public JScrollPane mBS=new JScrollPane(msgBox);//消息框的外部滚动框
	public JLabel nicktip=new JLabel("your nick:");//nick左边的标签
	public JTextField nick=new JTextField(6);//nick文本框
	public JButton nickconfirm=new JButton("confirm");//confirm按钮
	public JPanel leftPanel=new JPanel();//左侧面板
	public JPanel nickPanel=new JPanel();//nick三个组件所在面板
	public JTextField sendBox=new JTextField(20);//发送文本框
	public JButton send=new JButton("send");//三个按钮
	public JButton post=new JButton("post");
	public JButton emoji=new JButton("emoji");
	public JFileChooser chooser=new JFileChooser();//图片文件选择器
	public JPanel sendPanel=new JPanel();  //发送面板或称右侧面板
	public GUI() throws Exception {
		super("IM");
		setLocation(200,200);
		setSize(500,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1,2));
		msgBox.setLayout(new BoxLayout(msgBox,BoxLayout.Y_AXIS));//消息竖直布局
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(mBS,BorderLayout.CENTER);
		nickPanel.add(nicktip);
		nickPanel.add(nick);
		nickPanel.add(nickconfirm);
		leftPanel.add(nickPanel,BorderLayout.SOUTH);
		add(leftPanel);
		sendBox.setEnabled(false);
		sendPanel.add(sendBox);
		send.setEnabled(false);
		sendPanel.add(send);
		post.setEnabled(false);
		sendPanel.add(post);
		emoji.setEnabled(false);
		sendPanel.add(emoji);
		add(sendPanel);
		setVisible(true);
	}
}