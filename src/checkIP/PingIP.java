package checkIP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class PingIP {
	/**
	 * 功能：判断IP是否可以ping通
	 * @author zsg@gwhk.com.cn
	 * @date 2016-1-12
	 */
	 static Logger logger = Logger.getLogger(PingIP.class);
		
	 public static void main(String[] args) {
		    PropertyConfigurator.configure("config/log4j.properties");
			// 读取IP地址
		    InputStreamReader read = readIpAddrs();
			if (read == null) return;
	        BufferedReader bufferedReader = new BufferedReader(read); 
	        String ipAddr =null;

			try {
				while((ipAddr = bufferedReader.readLine()) != null){
					if(ipAddr.trim().startsWith("#")) continue;
					if (isOKPing(ipAddr)){
				    	logger.info("成功：" + ipAddr);
				    }else{
				    	logger.error("失败：" + ipAddr);
				    }
				}
			read.close();//关闭用户资料
		} catch(Exception e) {
			logger.error("IO异常："+e.toString());
			return;
		}
		} 
	 /**
	    * 判断IP地址是否可以ping通
	    * @param s_ip
	    * 邮件服务器的IP地址
	    * @return boolean
	    * true 表示能连接
	    * false 表示不能连接
	    */
	   static boolean isOKPing(String s_ip){
		   Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
		   Process process = null; // 声明处理类对象
		   String line = null; // 返回行信息
		   InputStream is = null; // 输入流
		   InputStreamReader isr = null; // 字节流
		   BufferedReader br = null;
		   boolean res = false;// 结果
		   try {
		    process = runtime.exec("ping " + s_ip); // PING
		    is = process.getInputStream(); // 实例化输入流
		    isr = new InputStreamReader(is);// 把输入流转换成字节流
		    br = new BufferedReader(isr);// 从字节中读取文本
		    while ((line = br.readLine()) != null) {
		     if (line.contains("TTL")) {
		      res = true;
		      break;
		     }
		    }
		    is.close();
		    isr.close();
		    br.close();
		   } catch (IOException e) {
		    logger.error("Ping 异常：" + e.toString());
		    runtime.exit(1);
		   }
		   return res;
	   }
	   
	   /**
	    * 读取IP地址 config/ipAddrs.txt
	    * @return InputStreamReader
	    */
	    static InputStreamReader readIpAddrs(){
			String filePath = "config/ipAddrs.txt";
	        File file=new File(filePath);
	        InputStreamReader read = null;
	        if(file.isFile() && file.exists()){ //判断文件是否存在
				try {
					read = new InputStreamReader(new FileInputStream(file));
				} catch (FileNotFoundException e1) {
					logger.error("File not found:"+file.toString());
				}
	        }else{
	        	logger.error("失败：查找文件 "+filePath);
	        }
	        return read;
	    }
}
