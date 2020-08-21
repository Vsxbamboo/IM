package encryption;

public class PQchooser {
	public int count=1;
	public int[] choose() {
		int n=2;
		int[] arr=new int[10000];
		arr[0]=2;
		for(;n<10000;n++) {
			boolean check=true;
			
			for(int i=0;i<count;i++) {
				if (n%arr[i]==0) {
					check=false;
					break;
				}
				
			}
			if (check) {
				arr[count]=n;
				count++;
			}
		}
		return arr;
	}
}
