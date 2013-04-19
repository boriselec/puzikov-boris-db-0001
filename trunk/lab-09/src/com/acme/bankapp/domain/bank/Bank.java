package com.acme.bankapp.domain.bank;


public class Bank {
	public final int MAX_CLIENTS = 10;
	private Client[] clients = new Client[MAX_CLIENTS];
	private int numOfClients = 0;
	
	public int getNumOfClients() {
		return numOfClients;
	}

	public Client[] getClients() {
		return clients;
	}
	
	public void addClient(Client client){
		if (numOfClients < MAX_CLIENTS){
			clients[numOfClients++] = client;
		}
		
	}
	
	
	
	

}
