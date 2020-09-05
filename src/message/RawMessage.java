package message;

import javax.swing.ImageIcon;
import java.io.Serializable;
//所谓生消息，即化简了的消息，除去了不必要的加密与id，可以序列化的消息
public class RawMessage implements Serializable{
	public String nick="";
	public int type = 0;
	public String text="";
	public ImageIcon pic;
	public RawMessage(String nick,String text) {
		this.nick=nick;
		this.type=1;
		this.text=text;
	}
	public RawMessage(String nick,ImageIcon pic) {
		this.nick=nick;                                                            
		this.type=2;
		this.pic=pic;
	}
	
}
