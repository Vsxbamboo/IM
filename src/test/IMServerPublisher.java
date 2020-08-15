package test;

import javax.xml.ws.*;

public class IMServerPublisher {

	public static void main(String[] args) {
		IMServerImpl srsi=new IMServerImpl();
		Endpoint.publish("http://127.0.0.1:5335/service",srsi);
	}

}
