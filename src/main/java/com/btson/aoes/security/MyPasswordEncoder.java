package com.btson.aoes.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder{

    //对明文密码进行加密后返回密文
	@Override
	public String encode(CharSequence arg0) {
		return arg0.toString();
	}

	//判断密码正确与否
	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		return arg1.equals(arg0.toString());
	}

}
