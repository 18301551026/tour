package com.lxs.security.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.lxs.com")
public interface IUserServiceWs {

	public String doLoginWs(
			@WebParam(name = "xmppUserName") String xmppUserName,
			@WebParam(name = "userName") String userName,
			@WebParam(name = "password") String password);

}
