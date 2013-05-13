package com.stockexchangeemulator.domain;

import java.util.LinkedList;

public class ResponseManager {

	public Response createCancelingResponse(TradeOrder order) {
		return new Response(order, Status.CANCELED);

	}

	public Response createErrorResponse(CancelOrder cancelOrder, Status error) {
		return new Response(cancelOrder,
				"Unable to cancel order. No such order in stock exchange");
	}

	public Response createFilledResponse(TradeOrder order, Status status) {
		return new Response(order, status);
	}

	public void spliceResponcesWithSameOrderID(LinkedList<Response> responses,
			LinkedList<Response> dealResponses) {
		l: for (Response response : dealResponses) {
			for (Response oldResponse : responses) {
				boolean isSameClient = oldResponse.getLogin().equals(
						response.getLogin());
				boolean isSameOrderID = oldResponse.getOrderID() == response
						.getOrderID();
				if (isSameOrderID && isSameClient) {
					oldResponse.splice(response);
					continue l;
				}
			}
			responses.add(response);
		}
	}
}
