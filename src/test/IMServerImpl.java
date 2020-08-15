package test;

import java.util.*;
import javax.jws.*;

@WebService(endpointInterface="test.IMServer")
public class IMServerImpl implements IMServer{
	
	public void addMsg(String msg) {
		try {
		IOProcess.writeFile(msg);
		} catch (Exception e) {
			System.out.println("Error,IMServerImpl addMsg iop.output");
		}
	}
	public byte[] getMsg() {
		try {
			return IOProcess.readFile().getBytes("UTF-8");
		} catch (Exception e) {
			System.out.println("Error,IMServerImpl getMsg iop.input");
		}
		return new byte[]{1,2};
	}
	public String getTime(){
		Date now=new Date();
		return now.toString();
	}
}
