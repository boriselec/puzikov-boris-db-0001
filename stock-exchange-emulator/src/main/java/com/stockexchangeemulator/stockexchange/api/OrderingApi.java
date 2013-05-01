package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.WrappedOrder;

public interface OrderingApi {

	public void sendOrder(WrappedOrder order);

	public void addObserver(FilledObserver observer);

	public void removeObserver(FilledObserver observer);

	public void notifyObservers(Response response);

}
