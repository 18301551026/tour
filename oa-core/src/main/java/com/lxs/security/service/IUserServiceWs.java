package com.lxs.security.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface IUserServiceWs {
	
	public String loginWs(@WebParam(name = "userName") String userName, @WebParam(name = "password") String password);

}
