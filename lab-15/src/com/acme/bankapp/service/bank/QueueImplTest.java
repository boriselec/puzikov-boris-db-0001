package com.acme.bankapp.service.bank;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.acme.bankapp.domain.bank.Gender;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.SavingAccount;
import com.acme.domain.email.Client;
import com.acme.domain.email.impl.EmailImpl;
import com.acme.domain.email.impl.QueueImpl;

public class QueueImplTest {

	public EmailImpl initEmail() {
		EmailImpl email = new EmailImpl();
		try {
			email.setClient(new Client("qw", Gender.FEMALE, new SavingAccount(
					1, 4)));
		} catch (NegativeArgumentException e) {
			e.printStackTrace();
		}
		email.setFrom("Bank");
		email.setTo("listener");
		email.setSubject("New Client");
		email.setMessage("New Client Added");
		return email;

	}

	@Test
	public void mustEndWhenAddThehGet() {
		final QueueImpl<EmailImpl> queue = new QueueImpl<>(10);
		new Thread(new Runnable() {

			@Override
			public void run() {
				EmailImpl email = initEmail();
				queue.addEmail(email);

				new Thread(new Runnable() {

					@Override
					public void run() {
						EmailImpl email = (EmailImpl) queue.getEmail();
					}
				}).start();
			}
		}).start();
		assertEquals(0, queue.getSize());
	}

	@Test
	public void mustEndWhenGetThenAdd() {
		final QueueImpl<EmailImpl> queue = new QueueImpl<>(10);
		new Thread(new Runnable() {

			@Override
			public void run() {

				new Thread(new Runnable() {

					@Override
					public void run() {
						EmailImpl email = initEmail();
						queue.addEmail(email);
					}
				}).start();
				EmailImpl email = (EmailImpl) queue.getEmail();
			}
		}).start();
		assertEquals(0, queue.getSize());
	}

	@Test
	public void elementExistWhenAdded() {
		final QueueImpl<EmailImpl> queue = new QueueImpl<>(10);
		EmailImpl email = initEmail();
		queue.addEmail(email);
		assertEquals(1, queue.getSize());
	}

}
