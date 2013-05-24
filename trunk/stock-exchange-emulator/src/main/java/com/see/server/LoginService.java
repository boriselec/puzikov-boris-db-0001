package com.see.server;

import java.io.IOException;

import com.see.common.exception.NoLoginException;
import com.see.server.network.TradingMessager;

public class LoginService {

	public String getLogin(TradingMessager tradingMessager)
			throws NoLoginException {
		String clientLogin;
		try {
			clientLogin = tradingMessager.readLogin();
		} catch (IOException e) {
			throw new NoLoginException("Unable to read client login name");
		}
		return clientLogin;
	}

}
