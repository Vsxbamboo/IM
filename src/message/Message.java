package message;

import java.awt.Image;
import java.math.BigInteger;

public class Message {
	
	public String nick ="";
	public BigInteger mid;
	public int type = 0;
	public String text="";
	public BigInteger[] etext;
	public Image pic;
	public Message() {
		this.type=-1;
	}
	public Message(String nick,String text){
		this.nick=nick;
		this.type=1;
		this.text=text;
	}
	public Message(String nick,Image pic){
		this.nick=nick;
		this.type=2;
		this.pic=pic;
	}
	public Message(String nick,BigInteger mid,BigInteger[] etext){
		this.nick=nick;
		this.mid=mid;
		this.type=1;
		this.etext=etext;
	}
	
	public Message(String nick,BigInteger id,Image pic){
		this.nick=nick;
		this.mid=id;
		this.type=2;
		this.pic=pic;
	}
	
}
