package test;

import java.awt.Image;
import javax.jws.*;
import javax.jws.soap.*;
import javax.jws.soap.SOAPBinding.*;

@WebService
@SOAPBinding(style=Style.RPC)

public interface IMServer {
	@WebMethod public void addMsg(String msg);
	@WebMethod public byte[] getMsg();
	@WebMethod public void addImage(Image icon);
	@WebMethod public Image getImage();
}
