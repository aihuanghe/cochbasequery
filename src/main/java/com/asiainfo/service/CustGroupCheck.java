package com.asiainfo.service;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;

@WebService
public interface CustGroupCheck {
	public  String checkExistsNo(@WebParam(name = "jsonParam") String jsonParam) ;
	public  String getCustGroupList(@WebParam(name = "jsonParam") String jsonParam) ;

}
