package com.acme.bank;


public class Bank {
	public final int MAX_CLIENTS = 10;
	private Client[] clients = new Client[MAX_CLIENTS];
	private int numOfClients = 0;
	
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
	
	public void printBalance() {
		System.out.print(this.toString());
	}
	
	public String toString() {
		String toPrint = "";
		if (numOfClients == 0){
			toPrint += "Bank is empty\n";
		}
		else {
			toPrint += "Bank\n";
			for (Client client : clients){
				if (client == null)
					continue;
				else {
					toPrint = toPrint + client.getSalutation() + ": " + 
							client.getAccounts().getBalance() + '\n';
				}
			}
		}
			toPrint += "\n";
		return toPrint;
		
	}
	

}
