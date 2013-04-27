package com.acme.service.email;

import com.acme.domain.email.impl.EmailImpl;
import com.acme.domain.email.impl.QueueImpl;

public class EmailService {

	private QueueImpl<EmailImpl> queue = new QueueImpl<EmailImpl>(10);

	private static EmailService instance = new EmailService();

	public static EmailService getEmailService() {
		if (instance == null)
			synchronized (EmailService.class) {
				if (instance == null)
					instance = new EmailService();
			}

		return instance;
	}

	public void sendNotificationEmail(EmailImpl email) {
		queue.addEmail(email);
	}

}
