package com.asiainfo.service;

import com.asiainfo.busi.CustGroupCheckBusi;

import javax.jws.WebService;
import java.io.IOException;
@WebService(serviceName = "Custgroupcheck")
public class CustGroupCheckImpl implements CustGroupCheck {

	public String checkExistsNo(String jsonParam)  {
		// TODO Auto-generated method stub
		CustGroupCheckBusi busi=new CustGroupCheckBusi();

		try {
			String rt= busi.queryExist(jsonParam);
			return rt;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}catch (Exception e2)
		{
			e2.printStackTrace();
			return "";
		}
	}

	public String getCustGroupList(String jsonParam)  {
		// TODO Auto-generated method stub
		CustGroupCheckBusi busi=new CustGroupCheckBusi();
		try {
			String rt= busi.queryInfo(jsonParam);
			return rt;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}catch (Exception e2)
		{
			e2.printStackTrace();
			return "";
		}
	}

}
