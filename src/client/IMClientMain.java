package client;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import encryption.*;
import message.Message;


public class IMClientMain implements MouseListener,KeyListener,Runnable{
	int count=0;//总消息数
	boolean ready=false;//是否选择了昵称
	boolean ctrl=false;//是否按了ctrl
	BigInteger id,originid;//原始id，以及变化id，每次一发送消息后都会对id进行修改改
	BigInteger[] pubkey=new BigInteger[3];//接受服务器返回的公钥数组，0是n，1是公钥，2是原始id
	BigInteger[] mypubkey=new BigInteger[2];//记载自己产生的公钥数组，0是n，1是公钥
	public static GUI gui;//图形用户界面对象
	private static Encryption encryption;//RSA非称加密对象
	public static Thread refresh;//消息刷新线程 
	public IMClientMain() throws Exception{
		encryption=new Encryption();//初始化，产生自己的公私钥
		//将公私钥导入mypubkey
		mypubkey[0]=encryption.n;
		mypubkey[1]=encryption.e;
		gui=new GUI();//图形用户界面初始化
		//添加事件监听器
		gui.nickconfirm.addMouseListener(this);
		gui.send.addMouseListener(this);
		gui.sendBox.addKeyListener(this);
		gui.post.addMouseListener(this);
		gui.emoji.addMouseListener(this);
		
		
		refresh=new Thread(this);//刷新进程初始化
	}
	public static void main(String[] args) throws Exception {
		new IMClientMain();
		
	}
	//发送文本方法
	public void postText() {
		//对id进行RSA加密
		BigInteger postid=encryption.encode(id, pubkey[0], pubkey[1]);
		if (!gui.sendBox.getText().equals("")) {//sendBox不为空
			try {
				//对消息进行对称加密
				BigInteger[] etext=Transform.encryptText(gui.sendBox.getText(),id);
				if (
						PostAndGet.postMsg(new Message(gui.nick.getText(),postid,etext))
						//消息发送成功
						) {
					//对id进行变换
					id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
					//调试功能：输出变换后id
					System.out.println("id="+id);
					//sendBox清空
					gui.sendBox.setText("");
				} else {
					//发送失败，显示在sendBox
					gui.sendBox.setText("fail to send msg");
				}
			
		} catch (Exception exc) {
				System.out.println(
						//PostAndGet.postMsg报错
						"Error in  sendClicked"
						);
			}	
		} else {
			//空的sendBox,不处理的话会出现空指针nullPointer
			gui.sendBox.setText("nullmsg");
		}
	}
	//发送图片方法
	public void postPic(Image pic) {
		//对id进行RSA加密
		BigInteger postid=encryption.encode(id, pubkey[0], pubkey[1]);
		try {
			if (
				PostAndGet.postMsg(new Message(gui.nick.getText(),postid,pic))
				//发送成功
					) {
					//id变换
					id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
					
			} else {
				//发送失败
				gui.sendBox.setText("fail to send msg");
				
			}
		} catch (Exception exc) {
			//PostAndGet.postMsg报错
			System.out.println(
					"Error in postPic"
					);
		}
	}
	@Override
	//消息刷新线程
	public void run() {
		boolean needRevalidate=false;//是否需要刷新消息框
		while(refresh!=null){//refresh=null用于手动停止
			try {
				Thread.sleep(500);//常规延时
			} catch (InterruptedException ie) {
				System.out.println("unable to normal Thread.sleep");
			}
			try {
				//保存此时id,避免此线程和主线程同时运行时，id被主线程修改导致解密错误
				BigInteger idbackup=id;
				//加密现有id
				BigInteger postid=encryption.encode(idbackup, pubkey[0], pubkey[1]);
				//取消息
				Message msg = PostAndGet.getMsg(count,postid);
				if (msg.type!=-1) {//空消息的type为-1
					gui.setTitle("IM loading");//窗口标题提示加载中
					JPanel dialog = new JPanel(new GridLayout(2,1));//显示消息的JPanel对象,盒式布局
					dialog.setAlignmentY(0);
					JLabel nick = new JLabel (msg.nick+":");//用于nick显示的标签
					if (msg.type==1) {//消息类型1，文本消息
						//显示文本消息的文本区域，先要解密
						JTextArea text = new JTextArea (Transform.decryptText(msg.etext, idbackup));
						text.setLineWrap(true);//自动换行
						text.setEditable(false);//不可编辑
						nick.setMaximumSize(nick.getPreferredSize());
						text.setMaximumSize(text.getPreferredSize());
						nick.setAlignmentX(0);
						text.setAlignmentX(0);
						gui.msgBox.add(nick);
						gui.msgBox.add(text);
						needRevalidate=true;//需要刷新消息框
						count++;//消息数加一
					} else if (msg.type==2) {//图片消息type=2
						JLabel pic = new JLabel (
								new ImageIcon (msg.pic),JLabel.LEFT
								);//用新标签对象装图片,并且设置左对齐
						nick.setMaximumSize(nick.getPreferredSize());
						pic.setMaximumSize(pic.getPreferredSize());
						nick.setAlignmentX(0);
						pic.setAlignmentX(0);
						gui.msgBox.add(nick);
						gui.msgBox.add(pic);
						needRevalidate=true;//需要刷新消息框
						count++;//消息数加一
					} 
				} else {
					//是否要刷新消息框
					if (needRevalidate) {//当收到空消息时表示已经没有新消息了
						
						gui.msgBox.revalidate();
						gui.mBSB.setValue(gui.mBSB.getMaximum());
						needRevalidate=false;
					}
					//这里是初始化的时候会加载一次消息，加载后才允许发消息
					gui.sendBox.setEnabled(true);
					gui.send.setEnabled(true);
					gui.post.setEnabled(true);
					gui.emoji.setEnabled(true);
					gui.setTitle("IM");//消息框刷新完毕后改回窗口标题
				}
			} catch (Exception exc) {
				try {
					//一般是因为服务器关了,所以显示太多遍没用
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
				}
				System.out.print(
						"Error in thread run or unable to connect to server"
						);
			}
			
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//confirm被单击
		if (e.getSource()==gui.nickconfirm && gui.nickconfirm.isEnabled()) {
			//锁定nick,防止双击
			gui.nick.setEditable(false);
			gui.nickconfirm.setEnabled(false);
			try {//取服务器公钥
				pubkey=PostAndGet.getPubkey(gui.nick.getText(), mypubkey);
				if (pubkey[0].equals(BigInteger.ZERO)) {//昵称存在则这样返回
					gui.nick.setText("nick exists");
					gui.nick.setEditable(true);
					gui.nickconfirm.setEnabled(true);
					
					} else {
					originid=encryption.decode(pubkey[2]);//RSA解密得到的原始id
					id=originid;
					
					
					ready=true;
					refresh.start();//开始接收消息
					}
			} catch (Exception e1) {
				//PostAndGet报错
				System.out.println("unable to connect server");
				gui.nick.setText("Error");
				gui.nick.setEditable(true);
				gui.nickconfirm.setEnabled(true);
			}
				
		} else if (ready) {//昵称是否已经选择
			Object source=e.getSource();
			//send被单击
			if (source==gui.send && gui.send.isEnabled()){
				postText();
			} else if(source==gui.post && gui.post.isEnabled()){//post被单击
				//打开文件选择框
				int result=gui.chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){//文件被选择
					ImageIcon icon = new ImageIcon(
							gui.chooser.getSelectedFile().getPath()//取文件带目录名称
									);
					postPic(icon.getImage());
				}
			} else if (source==gui.emoji && gui.emoji.isEnabled()) {
				if (!gui.emojiInit) {
					gui.Emoji=new JEmoji();
					gui.emojiInit=true;
				}
				for(int i=0;i<32;i++) {
					gui.Emoji.emojis[i].addMouseListener(this);
				}
				gui.Emoji.setLocation((int)(gui.getLocation().getX()+gui.getWidth()),(int)gui.getLocation().getY());
				gui.Emoji.setVisible(true);//显示Emoji窗口
			} else if (gui.emojiInit && gui.Emoji.isVisible()) {//switch不能用
				for (int i=0;i<32;i++) {
					if (source==gui.Emoji.emojis[i]) {
						postPic(gui.Emoji.icons[i].getImage());
						break;
					}
				}
			}
				
			
		} else {
			//没有填写适当nick昵称
			gui.sendBox.setText("choose a proper nick");
			
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
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(gui.sendBox.isEnabled()) {
			if(e.getKeyCode()==KeyEvent.VK_CONTROL)
				ctrl=true;
			if(e.getKeyCode()==KeyEvent.VK_ENTER && ctrl)
				postText();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(gui.sendBox.isEnabled()) {
			if(e.getKeyCode()==KeyEvent.VK_CONTROL)
				ctrl=false;
		}
	}

}
