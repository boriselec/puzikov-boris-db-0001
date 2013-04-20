package com.acme.bankapp.domain.bank;

import com.acme.bankapp.service.bank.ClientRegistrationListener;


public class Bank {
	public final int MAX_CLIENTS = 10;
	private Client[] clients = new Client[MAX_CLIENTS];
	public final int MAX_LISTENERS = 100;
	private ClientRegistrationListener[] listeners = 
				new ClientRegistrationListener[MAX_LISTENERS];
	private int numOfClients = 0;
	private int numOfListeners = 0;
	
    public Bank(ClientRegistrationListener... listeners){
        this.listeners = listeners;
}

	
	public int getNumOfClients() {
		return numOfClients;
	}

	public Client[] getClients() {
		return clients;
	}
	
	public void addClient(Client client){
		if (numOfClients < MAX_CLIENTS){
			clients[numOfClients++] = client;
			
            if (listeners != null)
            for (ClientRegistrationListener listener : listeners){
                    if (listener != null)
                            listener.onClientAdded(client);
            }

		}
		
	}
	
    public void addListener(ClientRegistrationListener listener){
		if (numOfListeners < MAX_LISTENERS) {
			listeners[numOfListeners++] = listener;
		}
	}

}
