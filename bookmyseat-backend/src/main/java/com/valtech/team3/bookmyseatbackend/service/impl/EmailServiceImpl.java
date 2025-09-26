package com.valtech.team3.bookmyseatbackend.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.exceptions.EmailSendingException;
import com.valtech.team3.bookmyseatbackend.service.EmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EmailServiceImpl implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private Configuration freemarkerConfig;
	@Autowired
	private UserDAO userDAO;

	@Async
	@Override
	public void sendEmailToAdmins(User user) throws MessagingException {
		List<User> admins = userDAO.getAdminUsers();
		for (User admin : admins) {
			try {
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setTo(admin.getEmail());
				helper.setSubject("New User Registration");
				Map<String, Object> model = new HashMap<>();
				model.put("user", user);
				String content = processFreemarkerTemplate("new_user_notification.ftlh", model);
				helper.setText(content, true);
				javaMailSender.send(mimeMessage);
				LOGGER.info("Email sent to admin");
			} catch (IOException | TemplateException e) {
				throw new MessagingException("Error sending email to Admin !", e);
			}
		}
	}

	@Override
	public void sendApprovalEmail(User user) throws MessagingException {
		LOGGER.info("Sending Approval mail to user");
		LOGGER.debug("Sending Approval mail to user: {}", user);
		sendEmail(user, "approval_email.ftlh");
	}

	@Override
	public void sendRejectionEmail(User user) throws MessagingException {
		LOGGER.info("Sending Rejection mail to user");
		LOGGER.debug("Sending Rejection mail to user: {}", user);
		sendEmail(user, "rejection_email.ftlh");
	}

	public void sendEmail(User user, String templateName) throws MessagingException {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(user.getEmail());
			helper.setSubject("Registration Status");
			Map<String, Object> model = new HashMap<>();
			model.put("user", user);
			String content = processFreemarkerTemplate(templateName, model);
			helper.setText(content, true);
			javaMailSender.send(mimeMessage);
			LOGGER.info("Email sent to User");
		} catch (IOException | TemplateException e) {
			throw new MessagingException("Error sending email", e);
		}
	}

	public String processFreemarkerTemplate(String templateName, Map<String, Object> model) throws IOException, TemplateException {
		Template template = freemarkerConfig.getTemplate(templateName);
		try (StringWriter stringWriter = new StringWriter()) {
			template.process(model, stringWriter);
			LOGGER.info("Processing email template");

			return stringWriter.toString();
		}
	}

	@Override
	public String sendChangePasswordEmail(User user, String token) {
		try {
			String url = "http://localhost:3000/bookmyseat/resetpassword/" + token;
			String htmlBody = "<p style='display: inline;'>Hello, <h3 style='display: inline;'>" + user.getFirstName() + "</h3></p>" + "<p>Please click on the link below to reset your password:</p>"
			      + "<a href=\"" + url + "\">Reset Password</a>" + "<p>Regards,<br>BookMySeat</p>";

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("bookmyseat2024@gmail.com");
			helper.setTo(user.getEmail());
			helper.setSubject("Password Reset");
			helper.setText(htmlBody, true);
			javaMailSender.send(message);

			return token;
		} catch (MessagingException | MailException e) {
			LOGGER.error("Failed to send Mail", e);
			throw new EmailSendingException("Error sending email to User !");
		}
	}

	public void sendEmailAfterSeatEdit(User user, String templateName) throws MessagingException {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(user.getEmail());
			helper.setSubject("Seat Modification Notification");
			Map<String, Object> model = new HashMap<>();
			model.put("user", user);
			String content = processFreemarkerTemplate(templateName, model);
			helper.setText(content, true);
			javaMailSender.send(mimeMessage);
			LOGGER.info("Email sent to User");
		} catch (IOException | TemplateException e) {
			throw new MessagingException("Error sending email to User !", e);
		}
	}

	@Async
	@Override
	public void sendSeatModificationMail(User user) throws MessagingException {
		LOGGER.info("Sending Seat modification mail to user");
		LOGGER.debug("Sending Seat modification mail to user: {}", user);
		sendEmailAfterSeatEdit(user, "edited_seat_notification.ftlh");
	}

	@Async
	@Override
	public void sendSeatCanceledMail(User user) throws MessagingException {
		LOGGER.info("Sending Seat canceled mail to user");
		LOGGER.debug("Sending Seat canceled mail to user: {}", user);
		sendEmailAfterSeatEdit(user, "cancled_seat_notification.ftlh");
	}

	@Override
	public void sendWelcomeEmail(User user) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(user.getEmail());
			helper.setSubject("Welcome to Our Platform");
			Map<String, Object> model = new HashMap<>();
			model.put("user", user);
			String content = processFreemarkerTemplate("welcome_email.ftlh", model);
			helper.setText(content, true);
			javaMailSender.send(mimeMessage);
			LOGGER.info("Welcome email sent to User");
		} catch (IOException | TemplateException | MessagingException e) {
			LOGGER.error("Failed to send welcome email to User", e);
		}
	}
}