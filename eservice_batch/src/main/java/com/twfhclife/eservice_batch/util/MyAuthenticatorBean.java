package com.twfhclife.eservice_batch.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticatorBean extends Authenticator{

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
