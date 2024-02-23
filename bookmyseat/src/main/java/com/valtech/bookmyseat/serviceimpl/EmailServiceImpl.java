package com.valtech.bookmyseat.serviceimpl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserModifyBooking;
import com.valtech.bookmyseat.service.EmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
	private static final String TEMPLATE_NOT_FOUND = "Template not found";
	private static final String TEMPLATE_EXCEPTION = "Template Exception";
	private static final String IO_EXCEPTION = "IO Exception";
	private static final String ERROR_WHILE_SENDING_EMAIL = "Error while sending email";
	private static final String USER_ID = "userId";
	private static final String ERROR_WHILE_OR_CREATING_SENDING_EMAIL = "Error while creating or sending email message";

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration configuration;

	@Override
	public String getEmailContentAdmin(User user) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put(USER_ID, String.valueOf(user.getUserId()));
		try {
			Template template = configuration.getTemplate("registrationStatus.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.getBuffer().toString();
	}

	@Async
	@Override
	public void sendApprovalEmailToAdmin(User user) throws EmailException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Approval - Status");
			helper.setTo("kartik.dandooti@valtech.com");
			String emailContent = getEmailContentAdmin(user);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("Sending email to Admin with user details for approval");
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
	}

	@Override
	public boolean sendEmailToUser(UserModel userModel) throws EmailException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("User - Credentials");
			helper.setTo(userModel.getEmailId());
			String emailContent = getUserContent(userModel);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("sending email to user with his crendtails");

			return true;
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
	}

	@Override
	public String getUserContent(UserModel userModel) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", userModel);
		try {
			Template template = configuration.getTemplate("userCredentails.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.toString();
	}

	@Override
	public String getApprovalEmailContent(User user) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put(USER_ID, String.valueOf(user.getUserId()));
		try {
			Template template = configuration.getTemplate("approvalMail.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.getBuffer().toString();
	}

	@Async
	@Override
	public void sendApprovalEmailToUser(User user) throws EmailException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Approval - Status");
			helper.setTo(user.getEmailId());
			String emailContent = getApprovalEmailContent(user);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("Sending email to user his crendtails after the admin approves the registration");
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
	}

	@Override
	public String getRejectionEmailContent(User user) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put(USER_ID, String.valueOf(user.getUserId()));
		try {
			Template template = configuration.getTemplate("rejectionMail.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.getBuffer().toString();
	}

	@Async
	@Override
	public void sendRejectionEmailToUser(User user) throws EmailException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Rejection - Status");
			helper.setTo(user.getEmailId());
			String emailContent = getRejectionEmailContent(user);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("Sending email to user after the user has been rejected");
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
	}

	@Async
	@Override
	public void sendUpdateSeatEmailToUser(UserModifyBooking userModifyBooking) throws EmailException {
		LOGGER.info("Sending email to user after the user seat has been modified");
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Seat - Update");
			helper.setTo(userModifyBooking.getUserEmail());
			String emailContent = getEmailContentForUpdateSeat(userModifyBooking);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("Mail sent to user");
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
	}

	@Override
	public String getEmailContentForUpdateSeat(UserModifyBooking userModifyBooking) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("userModifyBooking", userModifyBooking);
		model.put(USER_ID, String.valueOf(userModifyBooking.getUserId()));
		try {
			Template template = configuration.getTemplate("seatModifyMail.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.getBuffer().toString();
	}

	@Override
	public String getUserOtpDetails(User user, String otpValue) throws IOException, TemplateException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put("otpValue", otpValue);
		configuration.getTemplate("userOtp.ftlh").process(model, stringWriter);
		LOGGER.info("Fetching OTP email template for user: {}", user.getFirstName());

		return stringWriter.getBuffer().toString();
	}

	@Async
	@Override
	public void sendOtpMailToUser(User user, String otpValue)
			throws MessagingException, IOException, TemplateException {
		LOGGER.info("Sending OTP email to the user {}", user.getUserId());
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(user.getEmailId());
		helper.setSubject("OTP for Password Reset");
		String emailContent = getUserOtpDetails(user, otpValue);
		helper.setText(emailContent, true);
		mailSender.send(mimeMessage);

		LOGGER.debug("OTP email sent successfully to {}", user.getEmailId());
	}

	@Async
	@Override
	public void sendCancelSeatEmailToUser(UserModifyBooking userModifyBooking) throws EmailException {
		LOGGER.info("Sending email to user after the user seat has been canceled");
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Seat - Update");
			helper.setTo(userModifyBooking.getUserEmail());
			String emailContent = getEmailContentForCancelSeat(userModifyBooking);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
			LOGGER.info("Mail Sent to user");
		} catch (MessagingException e) {
			throw new EmailException(ERROR_WHILE_OR_CREATING_SENDING_EMAIL, e);
		} catch (MailException e) {
			throw new EmailException(ERROR_WHILE_SENDING_EMAIL, e);
		}
		
	}

	@Override
	public String getEmailContentForCancelSeat(UserModifyBooking userModifyBooking) throws EmailException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("userModifyBooking", userModifyBooking);
		model.put(USER_ID, String.valueOf(userModifyBooking.getUserId()));
		try {
			Template template = configuration.getTemplate("seatCancelMail.ftlh");
			template.process(model, stringWriter);
		} catch (TemplateNotFoundException e) {
			throw new EmailException(TEMPLATE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new EmailException(IO_EXCEPTION, e);
		} catch (TemplateException e) {
			throw new EmailException(TEMPLATE_EXCEPTION, e);
		}
		return stringWriter.getBuffer().toString();
	}
}