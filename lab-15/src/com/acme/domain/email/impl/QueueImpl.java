package com.acme.domain.email.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.acme.domain.email.Email;
import com.acme.domain.email.Queue;

public class QueueImpl<K> {
	private final int MAX_ELELMENT;
	private final Object monitor = new Object();

	public QueueImpl(int max) {
		MAX_ELELMENT = max;
	}
	
	public int getSize() {
		return mails.size();
	}
	
	private List<K> mails = new ArrayList<K>();

	public void addEmail(K email) {
			synchronized (monitor) {
				
				while (mails.size() == MAX_ELELMENT) {
					try {
						monitor.wait();
					} catch (InterruptedException e) {
						return;
					}
				}
				monitor.notify();
				mails.add(email);
			}
		}

	public K getEmail() {
			synchronized (monitor) {

			while (mails.isEmpty()) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					return null;
				}
			}
			monitor.notify();
			return mails.remove(0);
		}
	}
	public void close() {
		notify();
	}
	
}
