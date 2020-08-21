package encryption;

import java.math.BigInteger;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;;

public class Transform {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigInteger id=idRandom();
		JWindow jw=new JWindow();
		jw.setLocation(200,300);
		jw.setSize(200,200);
		ImageIcon icon=new ImageIcon("icon.png");
		Image image=icon.getImage();
		System.out.print(image.toString());
		
//		JLabel jl=new JLabel(icon);
//		jw.add(jl);
//		jw.setVisible(true);
	}
	public static BigInteger[] encryptText(String m,BigInteger id) {
		byte[] mBytes=m.getBytes();
		BigInteger[] mBigIntegers=new BigInteger[mBytes.length];
		for (int i=0;i<mBytes.length;i++) {
			mBigIntegers[i]=BigInteger.valueOf(mBytes[i]);
		}
		for (int i=0;i<mBytes.length;i++) {
			mBigIntegers[i]=mBigIntegers[i].add(id).multiply(id);
		}
		return mBigIntegers;
	}
	public static String decryptText(BigInteger[] mBigIntegers,BigInteger id) {
		for (int i=0;i<mBigIntegers.length;i++) {
			mBigIntegers[i]=mBigIntegers[i].divide(id).subtract(id);
		}
		byte[] mBytes=new byte[mBigIntegers.length];
		for (int i=0;i<mBytes.length;i++) {
			mBytes[i]=mBigIntegers[i].byteValue();
		}
		String re=new String(mBytes);
		return re;
	}
	public static BigInteger idRandom() {
		int bitid;
		do {
			bitid=(int)(Math.random()*1000);
		} while (bitid<100);
		return BigInteger.valueOf(bitid);
	}
}
