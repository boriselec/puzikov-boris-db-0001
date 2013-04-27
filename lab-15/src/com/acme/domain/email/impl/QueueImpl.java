package com.acme.domain.email.impl;

import java.util.ArrayList;
import java.util.List;

public class QueueImpl<K> {
	private final int MAX_ELELMENT;
	private final Object monitor = new Object();
	private List<K> mails = new ArrayList<K>();
	private boolean isClose = false;

	public QueueImpl(int max) {
		MAX_ELELMENT = max;
	}

	public int getSize() {
		return mails.size();
	}

	public void addEmail(K email) {
		synchronized (monitor) {

			while (mails.size() == MAX_ELELMENT && (isClose == false)) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
			if (isClose == true) {
				throw new IllegalStateException();

			} else {
				monitor.notify();
				mails.add(email);
			}
		}
	}

	public K getEmail() {
		synchronized (monitor) {

			while (mails.isEmpty() && (isClose == false)) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
			if (isClose == false) {
				monitor.notify();
				return mails.remove(0);
			} else {
				return null;
			}
		}
	}

	public void close() {
		synchronized (monitor) {
			isClose = true;
			monitor.notifyAll();
		}
	}

}
