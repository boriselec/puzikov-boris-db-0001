package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.Client;;


public interface ClientRegistrationListener {
	public void onClientAdded(Client client);
}
