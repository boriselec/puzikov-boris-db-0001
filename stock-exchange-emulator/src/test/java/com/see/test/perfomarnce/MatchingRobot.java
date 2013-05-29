package com.see.test.perfomarnce;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.see.client.network.Client;
import com.see.client.network.DefaultClient;
import com.see.common.domain.OrderType;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.LoginException;
import com.see.common.message.OrderRequest;

public class MatchingRobot implements Robot {

	private CountDownLatch finishLatch;
	private CyclicBarrier startBarrier;

	public MatchingRobot(CountDownLatch finishLatch,
			CyclicBarrier startBarrier, String clientName) {
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
			client.login(clientName);
			System.out.println(clientName + " connected");

			final OrderRequest buyOrder = new OrderRequest(clientName, "IBM",
					1, 1, OrderType.BUY);
			final OrderRequest sellOrder = new OrderRequest(clientName, "IBM",
					1, 1, OrderType.SELL);
			int count = 0;
			while (true) {
				client.sendOrder(buyOrder);
				client.sendOrder(sellOrder);
				System.out.println(count++);
			}
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (BadOrderException e) {
			e.printStackTrace();
		}
	}
}
