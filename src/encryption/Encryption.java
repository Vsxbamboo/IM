package encryption;

import java.math.BigInteger;

public class Encryption {
	int[] primes;
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
		n = p.multiply(q);
		fn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
//		System.out.print("p:"+p+" q:"+q+" n:"+n+" fn:"+fn);
		} while (n.compareTo(new BigInteger("1000")) < 0);
		
		do {
			Fraction fraction = new Fraction(Math.random());
			e = fn.subtract(BigInteger.ONE
					).multiply(
					BigInteger.valueOf(fraction.nume)
					).divide(
							BigInteger.valueOf(fraction.deno)
							);
		} while (primed(e,fn));
		
		for (BigInteger i = BigInteger.ONE;;i = i.add(BigInteger.ONE)) {
			if (i.multiply(fn).add(BigInteger.ONE).mod(e).equals(BigInteger.ZERO)) {
				d=i.multiply(fn).add(BigInteger.ONE).divide(e);
				break;
			}
		}
//		System.out.print(" e:"+e+" d:"+d);
	}
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
		if (b.equals(BigInteger.ONE))
			return false;
		else
			return true;
	}
	public BigInteger encode(BigInteger m,BigInteger n,BigInteger e) {
		return m.modPow(e,n);
	}
	public BigInteger decode(BigInteger c) {
		return c.modPow(d,n);
	}
}
