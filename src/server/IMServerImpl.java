package server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.jws.*;
import encryption.Encryption;
import encryption.Transform;
import message.Message;

@WebService(endpointInterface="server.IMServer")

public class IMServerImpl implements IMServer{
	private Encryption encryption;
	private BigInteger[] pubkey=new BigInteger[2];
	public ArrayList<Message> dialogs = new ArrayList<Message>(50);
	private HashMap<String,BigInteger[]> ids = new HashMap<String,BigInteger[]>(10,0.7F);
	private Date now=new Date();
	private int nicked=0;
	public IMServerImpl() {
		encryption = new Encryption();
		//转存到pubkey中
		pubkey[0] = encryption.n;
		pubkey[1] = encryption.e;
	}
	@Override
	public boolean postMsg(Message msg) {
		BigInteger[] id=ids.get(msg.nick);//取出nick对应id
		if(encryption.decode(msg.mid).equals(id[1])) {
			if(msg.type==1) {
				//解密化简后存储
				msg=new Message(msg.nick,Transform.decryptText(msg.etext, id[1]));
				//服务端输出
				System.out.print(now.toString());
				System.out.println("|nick used counts:"+nicked);
				System.out.println("\t"+msg.nick+":");
				System.out.println("\t"+polish(msg.text));
			} else if(msg.type==2) {
				//化简存储
				msg=new Message(msg.nick,msg.pic);
				//服务端输出
				System.out.print(now.toString());
				System.out.println("|nick used counts:"+nicked);
				System.out.println("\t"+msg.nick+" post a picture");
			}
			dialogs.add(msg);//存入dialogs
			//id自变换
			id[1]=id[1].add(BigInteger.valueOf(1000)).mod(id[0]).add(BigInteger.valueOf(123)).mod(id[0]);
			
			ids.put(msg.nick,id);
			System.out.println("msg processed!");//处理完毕
			System.out.println();
			return true;//成功
		} else {
			return false;//失败
		}
	}
	@Override
	public Message getMsg(int count,BigInteger postid) {
		if (count < dialogs.size()) {//客户端消息数小于现有消息数
			Message msg=dialogs.get(count);//取出消息
			if(msg.type==1) {//如果是文字，要加密
			msg=new Message(msg.nick,msg.mid,Transform.encryptText(msg.text, encryption.decode(postid)));
			}
			return msg;
		} else {
			Message nullmsg=new Message();//空消息
			return nullmsg;
		}
	}
	@Override
	public BigInteger[] getPubckey(String nick,BigInteger[] mypubkey) {
		BigInteger[] toreturn=new BigInteger[3];
		toreturn[0]=new BigInteger("0");
		toreturn[1]=new BigInteger("0");
		toreturn[2]=new BigInteger("0");
		if(!ids.containsKey(nick) && nick!="" && nick!=null) {
			BigInteger[] id=new BigInteger[2];
			
			id[0]=idRandom();//生成原始id
			id[1]=id[0];
			toreturn[0]=pubkey[0];
			toreturn[1]=pubkey[1];
			toreturn[2]=encryption.encode(id[0], mypubkey[0], mypubkey[1]);
			ids.put(nick, id);//存入ids
			nicked++;
			System.out.print(now.toString());
			System.out.println("|nick used counts:"+nicked);
			System.out.println("nick \""+nick+"\" used");
			System.out.println();
		} 
			return toreturn;
	}
	
	private BigInteger idRandom() {//产生三位随机数的方法
		int bitid;
		do {
			bitid=(int)(Math.random()*1000);
		} while (bitid<100);
		return BigInteger.valueOf(bitid);
	}
	
	private String polish(String text) {
		char textchars[]=text.toCharArray();
		StringBuilder polishedtext=new StringBuilder();
		for(int i=0;i<textchars.length;i++) {
			polishedtext.append(textchars[i]);
			if(textchars[i]=='\n') {
				polishedtext.append('\t');
			}
			
		}
		return polishedtext.toString();
	}
}
