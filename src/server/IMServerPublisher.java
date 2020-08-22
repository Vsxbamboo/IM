package server;

import java.util.Scanner;
import javax.xml.ws.*;

public class IMServerPublisher {

	public static void main(String[] args) {
		IMServerImpl srsi=new IMServerImpl();
		srsi.dialogs=BackupAndLoad.deser();
		Endpoint.publish("http://127.0.0.1:5335/service",srsi);
		System.out.println("ready");
		Scanner sc=new Scanner(System.in);
		if (sc.nextLine().equals("shutdown")) {
			BackupAndLoad.enser(srsi.dialogs);
			System.out.println("Backup and exit.");
			System.exit(0);
		}
	}

}
