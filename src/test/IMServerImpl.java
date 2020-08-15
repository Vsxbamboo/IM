package test;

import javax.swing.ImageIcon;
import java.awt.Image;
import javax.jws.*;

@WebService(endpointInterface="test.IMServer")

public class IMServerImpl implements IMServer{
	public static Image imageBufferred=new ImageIcon("icon.png").getImage();
	@Override
	public void addMsg(String msg) {
		try {
		IOProcess.writeFile(msg);
		} catch (Exception e) {
			System.out.println("Error,IMServerImpl addMsg iop.output");
		}
	}
	@Override
	public byte[] getMsg() {
		try {
			return IOProcess.readFile().getBytes("UTF-8");
		} catch (Exception e) {
			System.out.println("Error,IMServerImpl getMsg iop.input");
		}
		return new byte[]{1,2};
	}
	@Override
	public void addImage(Image icon) {
		imageBufferred=icon;
	}
	@Override
	public Image getImage() {
		return imageBufferred;
	}
}
