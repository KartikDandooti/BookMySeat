package com.valtech.bookmyseat.exception;

import java.io.IOException;

import org.springframework.mail.MailException;

import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;

public class EmailException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmailException(String message) {
		super(message);
	}

	public EmailException(String message, TemplateNotFoundException e) {
		super(message, e);
	}

	public EmailException(String message, IOException e) {
		super(message, e);
	}

	public EmailException(String message, TemplateException e) {
		super(message, e);
	}

	public EmailException(String message, MessagingException e) {
		super(message, e);
	}

	public EmailException(String message, MailException e) {
		super(message, e);
	}
}
