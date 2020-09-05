package encryption;

import java.math.BigInteger;

public class Transform {
	//加密文本，文本变成byte数组，再加id乘id
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
	
	//解密文本，和加密操作相反
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
	
}
