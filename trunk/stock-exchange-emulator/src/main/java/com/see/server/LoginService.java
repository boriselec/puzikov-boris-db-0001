package com.see.server;

import java.io.IOException;

import com.see.common.exception.LoginException;
import com.see.server.network.TradingMessager;

public class LoginService {

	public String getLogin(TradingMessager tradingMessager)
			throws LoginException {
		String clientLogin;
		try {
			clientLogin = tradingMessager.readLogin();
		} catch (IOException e) {
			throw new LoginException("Unable to read client login name");
		}
		return clientLogin;
	}

}
