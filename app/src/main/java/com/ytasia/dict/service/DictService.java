/**
 * 
 */
package com.ytasia.dict.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.ytasia.dict.ddp.DBBasic;
import com.ytasia.dict.util.DictCache;

/**
 * @author truongnguyen
 *
 */
public class DictService {
	private static String httpUrl = "http://192.168.20.37:8080/MemdictServer";
	public String getLoginInfo(String user,String pwd) throws IOException{
		
		URL obj = new URL(httpUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//con.connect();
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "username="+user+"&uuid="+pwd+"&type=f&f=a";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		//GET RESULT
		String uuid=in.readLine();
		String dbInfo=in.readLine();
		in.close();
		//SAVE TO CACHE
		DictCache.server_ddp=dbInfo;
		DictCache.username=user;
		DictCache.uuid=uuid;	
		if(uuid!=null&&uuid.equals(pwd)){
			System.out.println("Register DDP");
			DictCache.isRegister=true;	
			DBBasic.getInstance().registerDB(user, pwd);
		}
			
		System.out.println("server_ddp="+DictCache.server_ddp);
		return uuid;
	}
	

}
