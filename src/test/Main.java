package test;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import encryption.*;


public class Main implements MouseListener,KeyListener,Runnable{
	int count=0;//总消息数
	boolean ready=false;
	BigInteger id,originid;//原始id，以及变化id，每次一发送消息后都会对id进行修改改
	BigInteger[] pubkey=new BigInteger[3];//接受服务器返回的公钥数组，0是n，1是公钥，2是原始id
	BigInteger[] mypubkey=new BigInteger[2];//记载自己产生的公钥数组，0是n，1是公钥
	public static GUI gui;//图形用户界面对象
	private static Encryption encryption;//RSA非称加密对象
	public static Thread refresh;//消息刷新线程 
	public Main() throws Exception{
		encryption=new Encryption();//初始化，产生自己的公私钥
		//将公私钥导入mypubkeyy
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
		new Main();
		
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
					Thread.sleep(100);//防止因id改变而造成的对收到的消息的解密错误
					//对id进行变换
					id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
					//调试功能：输出变换后id
					System.out.println("id="+id);
					//sendBox清空
					gui.sendBox.setText("");
				} else {
					//发送失败，显示在sendBox
					gui.sendBox.setText("fail to send msg");
					Thread.sleep(1000);
					gui.sendBox.setText("");
				}
			
		} catch (Exception exc) {
				System.out.println(
						//PostAndGet.postMsg报错
						"Error in  sendClicked"
						);
				exc.printStackTrace();
			}	
		} else {
			//空的sendBox,不处理的话会出现空指针nullPointer
			gui.sendBox.setText("nullmsg");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				System.out.println(
						"unable to Thread.sleep when sendBox is null"
						);
			}
			gui.sendBox.setText("");
			
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
					Thread.sleep(100);//同上，防解密消息错误
					//id变换
					id=id.add(BigInteger.valueOf(1000)).mod(originid).add(BigInteger.valueOf(123)).mod(originid);
					
			} else {
				//发送失败
				gui.sendBox.setText("fail to send msg");
				Thread.sleep(1000);
				gui.sendBox.setText("");
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
				//加密现有id
				BigInteger postid=encryption.encode(id, pubkey[0], pubkey[1]);
				//取消息
				Message msg = PostAndGet.getMsg(count,postid);
				if (msg.type!=-1) {//空消息的type为-1
					gui.setTitle("IM loading");//窗口标题提示加载中
					JPanel dialog = new JPanel(new FlowLayout());//显示消息的JPanel对象
					dialog.setLayout(new BoxLayout(dialog,BoxLayout.X_AXIS));//水平布局管理器，昵称在左，消息在右
					JLabel nick = new JLabel (msg.nick+":");//用于nick显示的标签
					if (msg.type==1) {//消息类型1，文本消息
						//显示文本消息的标签，先要解密
						JLabel text = new JLabel (Transform.decryptText(msg.etext, id));
						//添加nick标签，设置上面的dialog容器为左对齐的流式布局管理器，防止消息居中显示
						dialog.add(nick,FlowLayout.LEFT);
						//添加text标签
						dialog.add(text);
						//设置左边间隔为零，强行左对齐，上面的不起作用
						dialog.setAlignmentX(0);
						gui.msgBox.add(dialog);//把消息加到消息框中
						needRevalidate=true;//需要刷新消息框
						count++;//消息数加一
					} else if (msg.type==2) {//图片消息type=2
						JLabel pic = new JLabel (
								new ImageIcon (msg.pic)
								);//用新标签对象装图片
						//分别添加，以免布局出现消息居中状况
						gui.msgBox.add(nick);
						gui.msgBox.add(pic);
						needRevalidate=true;//需要刷新消息框
						count++;//消息数加一
					} 
				} else {
					//是否要刷新消息框
					if (needRevalidate) {//当收到空消息时表示已经没有新消息了
						gui.msgBox.revalidate();
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
		if (e.getSource()==gui.nickconfirm) {
			try {//取服务器公钥
				pubkey=PostAndGet.getPubkey(gui.nick.getText(), mypubkey);
			} catch (Exception e1) {
				//PostAndGet报错
				System.out.println("Error in confirm");
				gui.nick.setText("Error");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					System.out.println(
							"unable to Thread.sleep when Error in confirm"
							);
				}
				gui.nick.setText("");
			}
			if (pubkey[0].equals(BigInteger.ZERO)) {//昵称存在则这样返回
				gui.nick.setText("nick exists");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					System.out.println("unable to Thread.sleep when nick exists");
				}
				gui.nick.setText("");
				gui.nick.setEditable(true);
				
			} else {
				originid=encryption.decode(pubkey[2]);//RSA解密得到的原始id
				id=originid;
				//锁定nick
				gui.nick.setEditable(false);
				gui.nickconfirm.setEnabled(false);
				ready=true;
				refresh.start();//开始接收消息
			}
		} else if (ready) {//昵称是否已经选择
			Object source=e.getSource();
			//send被单击
			if (source==gui.send){
				postText();
			} else if(source==gui.post){//post被单击
				//打开文件选择框
				int result=gui.chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){//文件被选择
					ImageIcon icon = new ImageIcon(
							gui.chooser.getSelectedFile().getPath()//取文件带目录名称
									);
					postPic(icon.getImage());
				}
			} else if (e.getSource()==gui.emoji) {
				if (!gui.emojiInit) {
					gui.Emoji=new JEmoji();
					gui.emojiInit=true;
				}
				for(int i=0;i<32;i++) {
					gui.Emoji.emojis[i].addMouseListener(this);
				}
				gui.Emoji.setLocation((int)(gui.getLocation().getX()+gui.getWidth()),(int)gui.getLocation().getY());
				gui.Emoji.setVisible(true);//显示Emoji窗口
			} else {//switch不能用
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
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				System.out.println(
						"unable to Thread.sleep when nick hasn't been fill"
						);
			}
			gui.sendBox.setText("");
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
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			postText();
	}

}
