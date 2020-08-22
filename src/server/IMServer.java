package server;

import java.math.BigInteger;
import javax.jws.*;
import javax.jws.soap.*;
import javax.jws.soap.SOAPBinding.*;

import message.Message;

@WebService
@SOAPBinding(style=Style.RPC)

public interface IMServer {
	@WebMethod public boolean postMsg(Message msg);
	@WebMethod public Message getMsg(int count,BigInteger postid);
	@WebMethod public BigInteger[] getPubckey(String nick,BigInteger[] mypubkey);
}
