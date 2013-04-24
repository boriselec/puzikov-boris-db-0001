package com.acme.domain.email.impl;

import java.util.ArrayList;
import java.util.List;

import com.acme.domain.email.Email;
import com.acme.domain.email.Queue;

//TODO: implement the queue which is list-based.
public class QueueImpl implements Queue {
	private final int MAX_ELELMENT;
	private final Object monitor = new Object();

	public QueueImpl(int max) {
		MAX_ELELMENT = max;
	}

	private List<Email> mails = new ArrayList<Email>();

	public void addEmail(Email email) {
			synchronized (monitor) {
				
				System.out.println("added");
				while (mails.size() == MAX_ELELMENT) {
				System.out.println("while add");
					try {
						monitor.wait();
					} catch (InterruptedException e) {}
				}
				monitor.notify();
				mails.add(email);
			}
		}
	public void close() {
		
	}

	public Email getEmail() {
			synchronized (monitor) {

			while (mails.isEmpty()) {
				System.out.println("while get");
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
			monitor.notify();
			return mails.remove(0);
		}
		}
	}
	
