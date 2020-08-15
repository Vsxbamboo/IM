package test;

import java.net.*;
import javax.xml.namespace.*;
import javax.xml.ws.*;

public class PostAndGet {
	public static QName qname=new QName(
			"http://test/",
			"IMServerImplService"
			);
	public static String getMsg() throws Exception {
		// TODO Auto-generated method stub
		URL url=new URL("http://127.0.0.1:5335/service?wsdl");
		Service service=Service.create(url,qname);
		IMServer srs=service.getPort(IMServer.class);
		
		String a=new String(srs.getMsg(),"UTF-8");
		return a;
	}
	public static void postMsg(String msg) throws Exception {
		URL url=new URL("http://127.0.0.1:5335/service?wsdl");
		Service service=Service.create(url,qname);
		IMServer srs=service.getPort(IMServer.class);
		srs.addMsg(msg);
	}
}
