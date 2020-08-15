package test;

import java.io.*;

public class IOProcess {
	public static String readFile()throws Exception{
		File file=new File("msg.txt");
		BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        StringBuilder allString=new StringBuilder();
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
             allString.append(tempString+"\r\n");
            }
        reader.close();
		return allString.toString();
	}
	public static void writeFile(String write)throws Exception{
		File file=new File("msg.txt");
		FileOutputStream outStream=new FileOutputStream(file,true);
		byte[] writeByte=write.getBytes("UTF-8");
		outStream.write(writeByte);
		outStream.close();
	}
}
