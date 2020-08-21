package test;

import java.math.BigInteger;
import java.net.*;
import javax.xml.namespace.*;
import javax.xml.ws.*;

public class PostAndGet {
	public static Message getMsg(int count,BigInteger postid) throws Exception {
		QName qname=new QName(
				"http://test/",
				"IMServerImplService"
				);
		URL url=new URL("http://127.0.0.1:5335/service?wsdl");
		Service service=Service.create(url,qname);
		IMServer srs=service.getPort(IMServer.class);
		return srs.getMsg(count,postid);
	}
	
	public static boolean postMsg(Message msg) throws Exception {
		
		QName qname=new QName(
				"http://test/",
				"IMServerImplService"
				);
		URL url=new URL("http://127.0.0.1:5335/service?wsdl");
		Service service=Service.create(url,qname);
		IMServer srs=service.getPort(IMServer.class);
		return srs.postMsg(msg);
	}
	public static BigInteger[] getPubkey(String nick,BigInteger[] mypubkey) throws Exception {
		QName qname=new QName(
				"http://test/",
				"IMServerImplService"
				);
		URL url=new URL("http://127.0.0.1:5335/service?wsdl");
		Service service=Service.create(url,qname);
		IMServer srs=service.getPort(IMServer.class);
		return srs.getPubckey(nick, mypubkey);
	}
	
}