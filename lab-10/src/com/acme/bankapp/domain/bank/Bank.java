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
		if (numOfClients >= MAX_CLIENTS){
			//throw
		}
		else {
			clients[numOfClients++] = client;
		}
		
	}
	
	public void modifyBank(){
		if (clients[0] != null)
			clients[0].getAccounts().deposit(1.0);
		if (clients[1] != null)
			clients[1].getAccounts().withdraw(1.0);
	}
	
	
	

}
