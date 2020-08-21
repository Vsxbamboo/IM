package test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.jws.*;
import encryption.Encryption;
import encryption.Transform;

@WebService(endpointInterface="test.IMServer")

public class IMServerImpl implements IMServer{
	private Encryption encryption;
	private BigInteger[] pubkey=new BigInteger[2];
	private ArrayList<Message> dialogs = new ArrayList<Message>(50);
	private HashMap<String,BigInteger[]> ids = new HashMap<String,BigInteger[]>(10,0.7F);
	public IMServerImpl() {
		encryption = new Encryption();
		pubkey[0] = encryption.n;
		pubkey[1] = encryption.e;
		System.out.println("ready");
	}
	@Override
	public boolean postMsg(Message msg) {
		BigInteger[] id=ids.get(msg.nick);
		if(encryption.decode(msg.mid).equals(id[1])) {
			if(msg.type==1) {
			msg=new Message(msg.nick,msg.mid,Transform.decryptText(msg.etext, id[1]));
			}
			dialogs.add(msg);
			
			id[1]=id[1].add(BigInteger.valueOf(1000)).mod(id[0]).add(BigInteger.valueOf(123)).mod(id[0]);
			
			ids.put(msg.nick,id);
			System.out.println("处理完毕");
			return true;
		} else {
			return false;
		}
	}
	@Override
	public Message getMsg(int count,BigInteger postid) {
		if (count < dialogs.size()) {
			Message msg=dialogs.get(count);
			if(msg.type==1) {
			msg=new Message(msg.nick,msg.mid,Transform.encryptText(msg.text, encryption.decode(postid)));
			}
			return msg;
		} else {
			Message nullmsg=new Message();
			return nullmsg;
		}
	}
	@Override
	public BigInteger[] getPubckey(String nick,BigInteger[] mypubkey) {
		BigInteger[] toreturn=new BigInteger[3];
		toreturn[0]=new BigInteger("0");
		toreturn[1]=new BigInteger("0");
		toreturn[2]=new BigInteger("0");
		if(!ids.containsKey(nick)) {
			BigInteger[] id=new BigInteger[2];
			
			id[0]=idRandom();
			id[1]=id[0];
			toreturn[0]=pubkey[0];
			toreturn[1]=pubkey[1];
			toreturn[2]=encryption.encode(id[0], mypubkey[0], mypubkey[1]);
			ids.put(nick, id);
			return toreturn;
		} else {
			return toreturn;
		}

	}
	public BigInteger idRandom() {
		int bitid;
		do {
			bitid=(int)(Math.random()*1000);
		} while (bitid<100);
		return BigInteger.valueOf(bitid);
	}
}
