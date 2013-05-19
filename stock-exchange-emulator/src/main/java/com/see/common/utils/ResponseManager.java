package com.see.common.utils;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.CancelResponse;
import com.see.common.domain.ClientResponse;
import com.see.common.domain.ErrorResponse;
import com.see.common.domain.OrderBookResponse;
import com.see.common.domain.Status;
import com.see.common.domain.Trade;
import com.see.common.domain.TradeOrder;

public class ResponseManager {

	private ClientResponse createCancelingResponse(TradeOrder order) {
		return new ClientResponse(order, Status.CANCELED);

	}

	private ClientResponse createErrorResponse(CancelOrder cancelOrder,
			String string) {
		return new ClientResponse(cancelOrder, string);
	}

	private ClientResponse createFilledResponse(TradeOrder order) {
		if (order.getSharesCount() == 0)
			return new ClientResponse(order, Status.FULLY_FILLED);
		else
			return new ClientResponse(order, Status.PARTIALLY_FILLED);
	}

	// private void spliceResponcesWithSameOrderID(
	// LinkedList<ClientResponse> responses,
	// LinkedList<ClientResponse> dealResponses) {
	// l: for (ClientResponse response : dealResponses) {
	// for (ClientResponse oldResponse : responses) {
	// boolean isSameClient = oldResponse.getLogin().equals(
	// response.getLogin());
	// boolean isSameOrderID = oldResponse.getOrderID() == response
	// .getOrderID();
	// if (isSameOrderID && isSameClient) {
	// oldResponse.splice(response);
	// continue l;
	// }
	// }
	// responses.add(response);
	// }
	// }

	public ClientResponse createResponse(OrderBookResponse orderBookResponse,
			String clientName) {
		if (orderBookResponse instanceof Trade) {
			String bidName = ((Trade) orderBookResponse).getBid().getLogin();
			if (bidName.equals(clientName))
				return createFilledResponse(((Trade) orderBookResponse)
						.getBid());
			else
				return createFilledResponse(((Trade) orderBookResponse)
						.getOffer());
		} else if (orderBookResponse instanceof CancelResponse) {
			return createCancelingResponse(((CancelResponse) orderBookResponse)
					.getOrder());
		} else if (orderBookResponse instanceof ErrorResponse)
			return createErrorResponse(null,
					((ErrorResponse) orderBookResponse).getMessage());
		return null;
	}
}
