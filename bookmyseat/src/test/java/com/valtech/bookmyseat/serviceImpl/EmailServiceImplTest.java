package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserModifyBooking;
import com.valtech.bookmyseat.serviceimpl.EmailServiceImpl;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private Configuration configuration;

	@Mock
	private Template template;

	@Mock
	private MimeMessageHelper mimeMessageHelper;

	@Mock
	private MimeMessage mimeMessage;

	@Mock
	private JavaMailSenderImpl javaMailSenderImpl;

	@InjectMocks
	private EmailServiceImpl emailServiceImpl;

	@Test
	void testGetEmailContentAdmin() throws Exception {
		User user = new User();
		user.setUserId(1);
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put("USER_ID", String.valueOf(user.getUserId()));
		when(configuration.getTemplate("registrationStatus.ftlh")).thenReturn(template);
		doNothing().when(template).process(any(), any(StringWriter.class));
		String result = emailServiceImpl.getEmailContentAdmin(user);
		assertEquals("", result);
		assertNotNull(stringWriter);
		verify(configuration).getTemplate("registrationStatus.ftlh");
	}

	@Test
	void testGetEmailContentAdmin_TemplateNotFound() throws Exception {
		User user = new User();
		user.setUserId(1);
		when(configuration.getTemplate("registrationStatus.ftlh")).thenThrow(TemplateNotFoundException.class);
		assertThrows(EmailException.class, () -> emailServiceImpl.getEmailContentAdmin(user));
	}

	@Test
	void testGetEmailContentAdmin_IOException() throws Exception {
		User user = new User();
		user.setUserId(1);
		when(configuration.getTemplate("registrationStatus.ftlh")).thenReturn(template);
		doThrow(IOException.class).when(template).process(any(), any(StringWriter.class));
		assertThrows(EmailException.class, () -> emailServiceImpl.getEmailContentAdmin(user));
	}

	@Test
	void testGetEmailContentAdmin_TemplateException() throws Exception {
		User user = new User();
		user.setUserId(1);
		when(configuration.getTemplate("registrationStatus.ftlh")).thenReturn(template);
		doThrow(TemplateException.class).when(template).process(any(), any(StringWriter.class));
		assertThrows(EmailException.class, () -> emailServiceImpl.getEmailContentAdmin(user));
	}

	@Test
	void testSendApprovalEmailToAdmin() throws Exception {
		User user = new User();
		user.setUserId(1);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(configuration.getTemplate("registrationStatus.ftlh")).thenReturn(template);
		emailServiceImpl.sendApprovalEmailToAdmin(user);
		verify(mailSender).createMimeMessage();
		verify(mailSender).send(mimeMessage);
	}

	@Test
	void testSendApprovalEmailToAdmin_MailException() throws Exception {
		User user = new User();
		when(mailSender.createMimeMessage()).thenThrow(new MailException("Error while sending email") {
			private static final long serialVersionUID = 1L;
		});
		assertThrows(EmailException.class, () -> emailServiceImpl.sendApprovalEmailToAdmin(user));
	}

	@Test
	void testGetUserContent_TemplateNotFoundException() throws Exception {
		UserModel userModel = new UserModel();
		when(configuration.getTemplate("userCredentails.ftlh")).thenThrow(TemplateNotFoundException.class);
		EmailException exception = assertThrows(EmailException.class, () -> emailServiceImpl.getUserContent(userModel));
		assertEquals("Template not found", exception.getMessage());
		assertTrue(exception.getCause() instanceof TemplateNotFoundException);
	}

	@Test
	void testGetUserContent_IOException() throws Exception {
		UserModel userModel = new UserModel();
		when(configuration.getTemplate("userCredentails.ftlh")).thenThrow(IOException.class);
		EmailException exception = assertThrows(EmailException.class, () -> emailServiceImpl.getUserContent(userModel));
		assertEquals("IO Exception", exception.getMessage());
		assertTrue(exception.getCause() instanceof IOException);
	}

	@Test
	void testGetUserContent_TemplateException() throws Exception {
		UserModel userModel = new UserModel();
		when(configuration.getTemplate("userCredentails.ftlh")).thenReturn(template);
		doThrow(TemplateException.class).when(template).process(any(), any(StringWriter.class));
		EmailException exception = assertThrows(EmailException.class, () -> emailServiceImpl.getUserContent(userModel));
		assertEquals("Template Exception", exception.getMessage());
		assertTrue(exception.getCause() instanceof TemplateException);
	}

	@Test
	void sendEmailToUser_Successfully() throws Exception {
		UserModel userModel = new UserModel();
		userModel.setEmailId("test@example.com");
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(mailSender).send(mimeMessage);
		when(configuration.getTemplate("userCredentails.ftlh")).thenReturn(mock(Template.class));
		boolean result = emailServiceImpl.sendEmailToUser(userModel);
		assertTrue(result);
		verify(mailSender, Mockito.times(1)).send(mimeMessage);
	}

	@Test
	void sendEmailToUser_MailException() throws Exception {
		UserModel userModel = new UserModel();
		when(mailSender.createMimeMessage()).thenThrow(new MailException("Error while sending email") {
			private static final long serialVersionUID = 1L;
		});
		assertThrows(EmailException.class, () -> emailServiceImpl.sendEmailToUser(userModel));
	}

	@Test
	void testGetApprovalEmailContent() throws Exception {
		User user = new User();
		user.setUserId(123);
		when(configuration.getTemplate("approvalMail.ftlh")).thenReturn(template);
		StringWriter stringWriter = new StringWriter();
		String emailContent = emailServiceImpl.getApprovalEmailContent(user);
		verify(configuration).getTemplate("approvalMail.ftlh");
		verify(template).process(anyMap(), any(StringWriter.class));
		assertNotNull(emailContent);
		assertNotNull(stringWriter);
	}

	@Test
	void testTemplateNotFoundException() throws Exception {
		User user = new User();
		when(configuration.getTemplate("approvalMail.ftlh")).thenThrow(TemplateNotFoundException.class);
		EmailException exception = assertThrows(EmailException.class,
				() -> emailServiceImpl.getApprovalEmailContent(user));
		assertEquals("Template not found", exception.getMessage());
		assertEquals(TemplateNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	void testIOException() throws Exception {
		User user = new User();
		when(configuration.getTemplate("approvalMail.ftlh")).thenThrow(IOException.class);
		EmailException exception = assertThrows(EmailException.class,
				() -> emailServiceImpl.getApprovalEmailContent(user));
		assertEquals("IO Exception", exception.getMessage());
		assertEquals(IOException.class, exception.getCause().getClass());
	}

	@Test
	void testTemplateException() throws Exception {
		User user = new User();
		when(configuration.getTemplate("approvalMail.ftlh")).thenReturn(template);
		doThrow(TemplateException.class).when(template).process(any(), any(StringWriter.class));
		EmailException exception = assertThrows(EmailException.class,
				() -> emailServiceImpl.getApprovalEmailContent(user));
		assertEquals("Template Exception", exception.getMessage());
		assertEquals(TemplateException.class, exception.getCause().getClass());
	}

	@Test
	void sendApprovalEmailToUser_Success() throws Exception {
		User user = new User();
		user.setEmailId("test@example.com");
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(configuration.getTemplate("approvalMail.ftlh")).thenReturn(template);
		emailServiceImpl.sendApprovalEmailToUser(user);
		verify(mailSender).send(mimeMessage);
	}

	@Test
	void sendApprovalEmailToUser_MailException() throws Exception {
		User user = new User();
		user.setEmailId("test@example.com");
		doThrow(new MailException("Test Mail Exception") {
			private static final long serialVersionUID = 1L;
		}).when(mailSender).createMimeMessage();
		assertThrows(EmailException.class, () -> emailServiceImpl.sendApprovalEmailToUser(user));
	}

	@Test
	void testGetRejectionEmailContent() throws Exception {
		User user = new User();
		StringWriter stringWriter = new StringWriter();
		when(configuration.getTemplate("rejectionMail.ftlh")).thenReturn(template);
		emailServiceImpl.getRejectionEmailContent(user);
		verify(configuration).getTemplate("rejectionMail.ftlh");
		verify(template).process(anyMap(), any(StringWriter.class));
		assertNotNull(stringWriter);
	}

	@Test
	void testGetRejectionEmailContent_TemplateNotFoundException() throws Exception {
		User user = new User();
		user.setUserId(123);
		when(configuration.getTemplate(Mockito.anyString())).thenThrow(TemplateNotFoundException.class);
		assertThrows(EmailException.class, () -> emailServiceImpl.getRejectionEmailContent(user));
	}

	@Test
	void testGetRejectionEmailContent_IOException() throws Exception {
		User user = new User();
		user.setUserId(123);
		Mockito.when(configuration.getTemplate(Mockito.anyString())).thenThrow(new IOException("IO Exception"));
		assertThrows(EmailException.class, () -> emailServiceImpl.getRejectionEmailContent(user));
	}

	@Test
	void testGetRejectionEmailContent_TemplateException() throws Exception {
		User user = new User();
		user.setUserId(123);
		when(configuration.getTemplate("rejectionMail.ftlh")).thenReturn(template);
		doThrow(TemplateException.class).when(template).process(any(), any(StringWriter.class));
		assertThrows(EmailException.class, () -> emailServiceImpl.getRejectionEmailContent(user));
	}

	@Test
	void sendRejectionEmailToUser_Success() throws Exception {
		User user = new User();
		user.setEmailId("test@example.com");
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(configuration.getTemplate("rejectionMail.ftlh")).thenReturn(template);
		emailServiceImpl.sendRejectionEmailToUser(user);
		verify(mailSender).send(mimeMessage);
	}

	@Test
	void sendRejectionEmailToUser_MailException() throws Exception {
		User user = new User();
		user.setEmailId("test@example.com");
		doThrow(new MailException("Simulated mail sending exception") {
			private static final long serialVersionUID = 1L;
		}).when(mailSender).createMimeMessage();
		assertThrows(EmailException.class, () -> emailServiceImpl.sendRejectionEmailToUser(user));
	}

	@Test
	void testSendUpdateSeatEmailToUser() throws EmailException, MessagingException, TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		userModifyBooking.setUserEmail("test@example.com");
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(configuration.getTemplate("seatModifyMail.ftlh")).thenReturn(template);
		emailServiceImpl.sendUpdateSeatEmailToUser(userModifyBooking);
		verify(mailSender).createMimeMessage();
		verify(mailSender).send(mimeMessage);
	}

	@Test
	void sendUpdateSeatEmailToUser_MailException() throws Exception {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		userModifyBooking.setUserEmail("test@example.com");
		doThrow(new MailException("Simulated mail sending exception") {
			private static final long serialVersionUID = 1L;
		}).when(mailSender).createMimeMessage();
		assertThrows(EmailException.class, () -> emailServiceImpl.sendUpdateSeatEmailToUser(userModifyBooking));
	}

	@Test
	void getEmailContentForUpdateSeat_TemplateNotFoundException() throws Exception {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		when(configuration.getTemplate("seatModifyMail.ftlh")).thenThrow(TemplateNotFoundException.class);
		EmailException exception = assertThrows(EmailException.class,
				() -> emailServiceImpl.getEmailContentForUpdateSeat(userModifyBooking));
		assertEquals("Template not found", exception.getMessage());
		assertTrue(exception.getCause() instanceof TemplateNotFoundException);
	}

	@Test
	void getEmailContentForUpdateSeat_IOException() throws Exception {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		when(configuration.getTemplate("seatModifyMail.ftlh")).thenThrow(IOException.class);
		EmailException exception = assertThrows(EmailException.class,
				() -> emailServiceImpl.getEmailContentForUpdateSeat(userModifyBooking));
		assertEquals("IO Exception", exception.getMessage());
		assertTrue(exception.getCause() instanceof IOException);
	}

	@Test
	void getEmailContentForUpdateSeat_TemplateException() throws Exception {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		when(configuration.getTemplate("seatModifyMail.ftlh")).thenReturn(template);
		doThrow(TemplateException.class).when(template).process(any(), any(StringWriter.class));
		assertThrows(EmailException.class, () -> emailServiceImpl.getEmailContentForUpdateSeat(userModifyBooking));
	}

	@Test
	void testGetUserOtpDetails() throws IOException, TemplateException {
		User user = new User();
		when(configuration.getTemplate("userOtp.ftlh")).thenReturn(template);
		String otpValue = "123456";
		String result = emailServiceImpl.getUserOtpDetails(user, otpValue);
		Map<String, Object> expectedModel = new HashMap<>();
		expectedModel.put("user", user);
		expectedModel.put("otpValue", otpValue);
		assertNotNull(expectedModel);
		assertNotNull(result);
	}

	@Test
	void testSendOtpMailToUser() throws MessagingException, IOException, TemplateException {
		User user = new User();
		user.setUserId(123);
		user.setEmailId("user@example.com");
		String otpValue = "123456";
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(configuration.getTemplate("userOtp.ftlh")).thenReturn(template);
		emailServiceImpl.sendOtpMailToUser(user, otpValue);
		verify(mailSender, times(1)).send(any(MimeMessage.class));
	}
}