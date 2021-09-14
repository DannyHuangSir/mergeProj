package com.twfhclife.eservice.web.model;

import java.io.Serializable;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticatorBean extends Authenticator implements Serializable {
	
	private static final long serialVersionUID = 1L;

	String name;
    String password;
     
    public MyAuthenticatorBean(String name,String password){
        this.name = name;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(name,password);
    }
    
}
