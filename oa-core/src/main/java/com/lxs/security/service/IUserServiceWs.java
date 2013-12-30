package com.lxs.security.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.lxs.com")
public interface IUserServiceWs {
	
	public String loginWs(@WebParam(name = "userName") String userName, @WebParam(name = "password") String password);

}
