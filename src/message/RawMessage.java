package message;

import javax.swing.ImageIcon;
import java.io.Serializable;

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
