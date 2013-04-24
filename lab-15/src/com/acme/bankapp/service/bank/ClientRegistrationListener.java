package com.acme.bankapp.service.bank;
import java.io.Serializable;

import com.acme.domain.email.Client;

public abstract class ClientRegistrationListener implements Serializable{
	public abstract void onClientAdded(Client client);
	
}

