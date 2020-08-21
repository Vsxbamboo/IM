package test;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import encryption.Encryption;
import encryption.Transform;

public class NewGUI extends JFrame implements MouseListener,Runnable {
	int count=0;
	boolean ready=false;
	BigInteger id,originid;
	BigInteger[] pubkey=new BigInteger[3];
	BigInteger[] mypubkey=new BigInteger[2];
	JPanel msgBox=new JPanel();
	JScrollPane mBS=new JScrollPane(msgBox);
	JLabel nicktip=new JLabel("your nick:");
	public JTextField nick=new JTextField(6);
	JButton nickconfirm=new JButton("confirm");
	JPanel leftPanel=new JPanel();
	JPanel nickPanel=new JPanel();
	JTextField sendBox=new JTextField(20);
	JButton send=new JButton("send");
	JButton post=new JButton("post");
	JButton emoji=new JButton("emoji");
	JFileChooser chooser=new JFileChooser();
	JPanel sendPanel=new JPanel();  
	Thread refresh=new Thread(this);
	Encryption encryption;
	PostAndGet pg;
	public NewGUI() throws Exception {
		super("IM");
		setLocation(200,200);
		setSize(500,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1,2));
		msgBox.setLayout(new BoxLayout(msgBox,BoxLayout.Y_AXIS));
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(mBS,BorderLayout.CENTER);
		nickPanel.add(nicktip);
		nickPanel.add(nick);
		nickconfirm.addMouseListener(this);
		nickPanel.add(nickconfirm);
		leftPanel.add(nickPanel,BorderLayout.SOUTH);
		add(leftPanel);
		sendBox.setEnabled(false);
		sendPanel.add(sendBox);
		send.addMouseListener(this);
		send.setEnabled(false);
		sendPanel.add(send);
		post.addMouseListener(this);
		post.setEnabled(false);
		sendPanel.add(post);
		emoji.addMouseListener(this);
//		emoji.setEnabled(false);
		sendPanel.add(emoji);
		add(sendPanel);
		
		setVisible(true);
		encryption=new Encryption();
		mypubkey[0]=encryption.n;
		mypubkey[1]=encryption.e;
		pg=new PostAndGet();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource()==emoji) {
			JEmoji emojiChooseFrame=new JEmoji();
		}
		if (e.getSource()==nickconfirm) {
			
			try {
				pubkey=PostAndGet.getPubkey(nick.getText(), mypubkey);
			} catch (Exception e1) {
				System.out.println("Error in confirm");
				nick.setText("Error");
			}
			if (pubkey[0].equals(BigInteger.ZERO)) {
				nick.setText("nick exists");
				nick.setEditable(true);
				
			} else {
				originid=encryption.decode(pubkey[2]);
				id=originid;
				
				nick.setEditable(false);
				nickconfirm.setEnabled(false);
				ready=true;
				refresh.start();
			}
		}
		
		if (ready) {
			BigInteger postid=encryption.encode(id, pubkey[0], pubkey[1]);
			if (e.getSource()==send){
				if (!sendBox.getText().equals("")) {
					try {
						BigInteger[] etext=Transform.encryptText(sendBox.getText(), id);
						if (
								PostAndGet.postMsg(new Message(nick.getText(),postid,etext))
								) {
							Thread.sleep(100);
							id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
							System.out.println("id="+id);	
							sendBox.setText("");
						} else {
							sendBox.setText("fail to send msg");
						}
					
				} catch (Exception exc) {
						System.out.println(
								"Error in NewGUI sendClicked"
								);
						exc.printStackTrace();
					}	
				} else {
					sendBox.setText("nullmsg");
					System.out.println("nullmsg");
				}
			}
			if(e.getSource()==post){
				
				int result=chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					ImageIcon icon = new ImageIcon(
							chooser.getSelectedFile().getPath()
									);
					try {
						if (
							PostAndGet.postMsg(new Message(nick.getText(),postid,icon.getImage()))
							) {
								Thread.sleep(100);
								id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
								
						} else {
							sendBox.setText("fail to send msg");
						}
					} catch (Exception exc) {
						System.out.println(
								"Error in NewGUI chooseClicked"
								);
					}
				}
			}
			
		}else{
			sendBox.setText("choose a proper nick");
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
	@Override
	public void run() {
		boolean needRevalidate=false;
		while(refresh!=null){
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				System.out.println("unable to normal Thread.sleep");
			}
			try {
				BigInteger postid=encryption.encode(id, pubkey[0], pubkey[1]);
				Message msg = PostAndGet.getMsg(count,postid);
				if (msg.type!=-1) {
					setTitle("IM loading");
					JPanel dialog = new JPanel();
					dialog.setLayout(new BoxLayout(dialog,BoxLayout.X_AXIS));
					JLabel nick = new JLabel (msg.nick+":");
					if (msg.type==1) {
						JLabel text = new JLabel (Transform.decryptText(msg.etext, id));
						dialog.add(nick,FlowLayout.LEFT);
						dialog.add(text);
						dialog.setAlignmentX(0);
						msgBox.add(dialog);
						needRevalidate=true;
						count++;
					} else if (msg.type==2) {
						JLabel pic = new JLabel (
								new ImageIcon (msg.pic)
								);
						msgBox.add(nick);
						msgBox.add(pic);
						needRevalidate=true;
						count++;
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.out.println("Fail to Thread.sleep");
							refresh=null;
						}
						
					}
				} else {
					if (needRevalidate) {
						msgBox.revalidate();
					}
					sendBox.setEnabled(true);
					send.setEnabled(true);
					post.setEnabled(true);
					setTitle("IM");
				}
			} catch (Exception exc) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
				}
				System.out.print("Error in thread run");
			}
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		new NewGUI();
		
	}
}