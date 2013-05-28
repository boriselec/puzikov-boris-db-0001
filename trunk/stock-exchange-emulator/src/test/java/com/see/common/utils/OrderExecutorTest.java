package com.see.common.utils;

import java.io.IOException;

import org.junit.Test;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.DisconnectException;

public class OrderExecutorTest {

	private OrderExecutor executor = new OrderExecutor(null);

	@Test(expected = BadOrderException.class)
	public void shouldThrowExceptionWhenUnknowOrderExecuted()
			throws DisconnectException, IOException, CancelOrderException,
			BadOrderException {
		executor.execute("test", new String("Unknown order"));
	}
}
