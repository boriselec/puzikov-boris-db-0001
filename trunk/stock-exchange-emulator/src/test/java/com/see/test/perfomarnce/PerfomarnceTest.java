package com.see.test.perfomarnce;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

public class PerfomarnceTest {
	private final int CLIENTS_NUMBER = 1;
	ExecutorService loadPool = Executors.newFixedThreadPool(CLIENTS_NUMBER);

	private CyclicBarrier startBarrier1 = new CyclicBarrier(CLIENTS_NUMBER);
	private CountDownLatch finishLatch1 = new CountDownLatch(CLIENTS_NUMBER);

	private final int loadSize = 100000;

	@Before
	public void init() {
	}

	@Test
	public void testPerfomance() {

		Integer i = 0;
		for (; i < CLIENTS_NUMBER; i++) {
			Robot client = new SendOrderRobot(finishLatch1, startBarrier1, "id"
					+ i.toString(), loadSize / CLIENTS_NUMBER);

			loadPool.execute(client);
		}

		try {
			finishLatch1.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
