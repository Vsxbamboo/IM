package server;

import javax.swing.ImageIcon;

import message.Message;
import message.RawMessage;

import java.io.*;
import java.util.ArrayList;

public class BackupAndLoad {
	//存储消息的方法，序列化
	public static void enser(ArrayList<Message> dialogs) {
		//定义一个生消息的数组列表，生消息表示简化后的消息
		ArrayList<RawMessage> dialogsser=new ArrayList<RawMessage>(dialogs.size());
		for(Message msg:dialogs) {//用循环赋值dialogs给dialogsser
			if(msg.type==1) {
				dialogsser.add(new RawMessage(msg.nick,msg.text));
			} else if(msg.type==2) {
				dialogsser.add(new RawMessage(msg.nick,new ImageIcon(msg.pic)));
			}
		}
		try{
			FileOutputStream fileOut =new FileOutputStream("dialogs.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dialogsser);
			out.close();
			fileOut.close();
			System.out.println("Serialized dialogs is saved in dialogs.ser");
	    }catch(IOException i){
	    	System.out.println("Fail to serialize the dialogs");
	    }
		
	}
	//提取消息的方法，反序列化
	@SuppressWarnings("unchecked")
	public static ArrayList<Message> deser() {
		File file=new File("dialogs.ser");
		if (file.exists()) {//是否存在文件
			ArrayList<RawMessage> dialogsser=null;
			try{
				FileInputStream fileIn=new FileInputStream("dialogs.ser");
	        	ObjectInputStream in=new ObjectInputStream(fileIn);
				dialogsser=(ArrayList<RawMessage>) in.readObject();
	        	in.close();
	        	fileIn.close();
			}catch(IOException i){
				System.out.println("unable to read file");
			}catch(ClassNotFoundException c){
				System.out.println("unable to find RawMessage class");
			}
			ArrayList<Message> dialogs=new ArrayList<Message>(dialogsser.size());
			for(RawMessage rmsg:dialogsser) {//转成Message
				if(rmsg.type==1) {
					dialogs.add(new Message(rmsg.nick,rmsg.text));
				} else if(rmsg.type==2) {
					dialogs.add(new Message(rmsg.nick,rmsg.pic.getImage()));
				}
				}
			return dialogs;
		} else {
			return new ArrayList<Message>(50);
		}
	}
}
