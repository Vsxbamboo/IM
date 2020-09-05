package encryption;
//用来取质数表的类
public class PQchooser {
	public int count=1;//记录数组arr已填写的个数
	public int[] choose() {
		int n=2;
		int[] arr=new int[10000];
		arr[0]=2;
		for(;n<10000;n++) {
			boolean check=true;//记录是否被整除过一次，是则不是质数
			
			for(int i=0;i<count;i++) {
				if (n%arr[i]==0) {//取余前面的质数
					check=false;//被整除
					break;
				}
				
			}
			if (check) {//没有一次被整除
				arr[count]=n;//写入arr数组
				count++;
			}
		}
		return arr;//返回有很多质数的数组
	}
}
