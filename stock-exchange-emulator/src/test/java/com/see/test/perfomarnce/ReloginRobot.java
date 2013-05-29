package com.see.test.perfomarnce;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.see.client.network.Client;
import com.see.client.network.DefaultClient;
import com.see.common.exception.LoginException;
import com.see.common.message.DisconnectRequest;

public class ReloginRobot implements Robot {

	private final int sleepTime = 1000; // ms
	private CountDownLatch finishLatch;
	private CyclicBarrier startBarrier;

	public ReloginRobot(CountDownLatch finishLatch, CyclicBarrier startBarrier,
			String clientName) {
		this.clientName = clientName;
		this.finishLatch = finishLatch;
		this.startBarrier = startBarrier;
	}

	private final String clientName;

	public String getClientName() {
		return clientName;
	}

	private final Client client = new DefaultClient();

	@Override
	public void run() {
		try {
			startBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			finishLatch.countDown();
			return;
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			finishLatch.countDown();
			return;

		}
		try {
			System.out.println(clientName + " connected");

			while (true) {
				client.login(clientName);
				client.sendDisconnect(new DisconnectRequest(true));
				Thread.sleep(1000);
			}
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
