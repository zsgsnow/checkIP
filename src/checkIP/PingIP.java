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
	 * ���ܣ��ж�IP�Ƿ����pingͨ
	 * @author zsg@gwhk.com.cn
	 * @date 2016-1-12
	 */
	 static Logger logger = Logger.getLogger(PingIP.class);
		
	 public static void main(String[] args) {
		    PropertyConfigurator.configure("config/log4j.properties");
			// ��ȡIP��ַ
		    InputStreamReader read = readIpAddrs();
			if (read == null) return;
	        BufferedReader bufferedReader = new BufferedReader(read); 
	        String ipAddr =null;

			try {
				while((ipAddr = bufferedReader.readLine()) != null){
					if(ipAddr.trim().startsWith("#")) continue;
					if (isOKPing(ipAddr)){
				    	logger.info("�ɹ���" + ipAddr);
				    }else{
				    	logger.error("ʧ�ܣ�" + ipAddr);
				    }
				}
			read.close();//�ر��û�����
		} catch(Exception e) {
			logger.error("IO�쳣��"+e.toString());
			return;
		}
		} 
	 /**
	    * �ж�IP��ַ�Ƿ����pingͨ
	    * @param s_ip
	    * �ʼ���������IP��ַ
	    * @return boolean
	    * true ��ʾ������
	    * false ��ʾ��������
	    */
	   static boolean isOKPing(String s_ip){
		   Runtime runtime = Runtime.getRuntime(); // ��ȡ��ǰ��������н�����
		   Process process = null; // �������������
		   String line = null; // ��������Ϣ
		   InputStream is = null; // ������
		   InputStreamReader isr = null; // �ֽ���
		   BufferedReader br = null;
		   boolean res = false;// ���
		   try {
		    process = runtime.exec("ping " + s_ip); // PING
		    is = process.getInputStream(); // ʵ����������
		    isr = new InputStreamReader(is);// ��������ת�����ֽ���
		    br = new BufferedReader(isr);// ���ֽ��ж�ȡ�ı�
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
		    logger.error("Ping �쳣��" + e.toString());
		    runtime.exit(1);
		   }
		   return res;
	   }
	   
	   /**
	    * ��ȡIP��ַ config/ipAddrs.txt
	    * @return InputStreamReader
	    */
	    static InputStreamReader readIpAddrs(){
			String filePath = "config/ipAddrs.txt";
	        File file=new File(filePath);
	        InputStreamReader read = null;
	        if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
				try {
					read = new InputStreamReader(new FileInputStream(file));
				} catch (FileNotFoundException e1) {
					logger.error("File not found:"+file.toString());
				}
	        }else{
	        	logger.error("ʧ�ܣ������ļ� "+filePath);
	        }
	        return read;
	    }
}
