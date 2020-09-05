package encryption;

import java.math.BigInteger;
//RSA加密类

public class Encryption {
	int[] primes;//质数数组
	public BigInteger n;
	private BigInteger fn;
	public BigInteger e;
	private BigInteger d;
	public Encryption() {
		do {
		PQchooser chooser = new PQchooser();
		primes = chooser.choose();
		BigInteger p = BigInteger.valueOf(primes[(int)(Math.random()*chooser.count)]);
		BigInteger q = BigInteger.valueOf(primes[(int)(Math.random()*chooser.count)]);
		n = p.multiply(q);//n=p*q
		//fn=(p-1)*(q-1)
		fn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
//		System.out.print("p:"+p+" q:"+q+" n:"+n+" fn:"+fn);//调试输出
		} while (n.compareTo(new BigInteger("1000")) < 0);//要求n大于1000，因为n决定能加密数字大小
		
		do {
			Fraction fraction = new Fraction(Math.random());
			e = fn.subtract(BigInteger.ONE//公钥e在1到fn-1中取
					).multiply(
					BigInteger.valueOf(fraction.nume)
					).divide(
							BigInteger.valueOf(fraction.deno)
							);
		} while (primed(e,fn));//e与fn必须互质，不然求d会出现死循环
		
		for (BigInteger i = BigInteger.ONE;;i = i.add(BigInteger.ONE)) {
			if (i.multiply(fn).add(BigInteger.ONE).mod(e).equals(BigInteger.ZERO)) {
				d=i.multiply(fn).add(BigInteger.ONE).divide(e);
				//d满足e*d%fn=1
				break;
			}
		}
//		System.out.print(" e:"+e+" d:"+d);//调试输出
	}
	//辗转相除法判断是否互质
	
	private boolean primed(BigInteger a,BigInteger b) {
		if(a.compareTo(b) < 0){
			BigInteger temp=a;
			a=b;
			b=temp;
		}
		
		for(BigInteger remain=b.mod(a);remain.compareTo(BigInteger.ZERO) > 0;){
			remain=b.mod(a);
			b=a;
			a=remain;
		}
		if (b.equals(BigInteger.ONE))//b是最大公因数
			return false;
		else
			return true;
	}
	
	public BigInteger encode(BigInteger m,BigInteger n,BigInteger e) {
		return m.modPow(e,n);//m^e%n
	}
	
	public BigInteger decode(BigInteger c) {
		return c.modPow(d,n);//c^e%n
	}
}
