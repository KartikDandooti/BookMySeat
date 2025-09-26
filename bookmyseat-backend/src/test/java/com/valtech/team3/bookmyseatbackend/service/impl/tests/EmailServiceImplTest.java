package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.service.impl.EmailServiceImpl;

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
	private JavaMailSender javaMailSender;
	@Mock
	private Configuration freemarkerConfig;
	@Mock
	private UserDAO userDAO;
	@Mock
	FreeMarkerTemplateUtils freeMarkerTemplateUtils;
	@InjectMocks
	private EmailServiceImpl emailService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSendEmailToAdmins() throws IOException, TemplateException, MessagingException {
		User user = new User();
		user.setEmail("gagana.cm@valtech.com");

		List<User> admins = new ArrayList<>();
		User admin = new User();
		admin.setEmail("poojitha.sp@valtech.com");
		admins.add(admin);

		when(userDAO.getAdminUsers()).thenReturn(admins);
		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(Template.class));

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);

		emailService.sendEmailToAdmins(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendApprovalEmail() throws IOException, TemplateException, MessagingException {
		User user = new User();
		user.setEmail("poojitha.sp@valtech.com");

		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(Template.class));

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);

		emailService.sendApprovalEmail(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendRejectionEmail() throws IOException, TemplateException, MessagingException {
		User user = new User();
		user.setEmail("poojitha.sp@valtech.com");

		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(Template.class));

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);

		emailService.sendRejectionEmail(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendEmail() throws IOException, TemplateException, MessagingException {
		User user = new User();
		user.setEmail("poojitha.sp@valtech.com");

		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(Template.class));

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);

		emailService.sendEmail(user, "approval_email.ftlh");

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testProcessFreemarkerTemplate_Success() throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("user", new User());

		Template templateMock = mock(Template.class);
		when(freemarkerConfig.getTemplate(anyString())).thenReturn(templateMock);

		emailService.processFreemarkerTemplate("templateName", model);

		verify(freemarkerConfig).getTemplate(anyString());
		verify(templateMock).process(any(), any());
	}

	@Test
	void testProcessFreemarkerTemplate_Exception() throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("user", new User());

		when(freemarkerConfig.getTemplate(anyString())).thenThrow(new IOException("Template not found"));

		assertThrows(IOException.class, () -> emailService.processFreemarkerTemplate("templateName", model));
	}

	@Test
	void testSendWelcomeEmail() throws IOException, TemplateException, MessagingException {
		User user = new User();
		user.setEmail("poojitha.sp@valtech.com");

		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(Template.class));

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);

		emailService.sendWelcomeEmail(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendChangePasswordEmail() throws MessagingException {
		User user = new User();
		user.setEmail("murali.kr@valtech.com");
		String token = "randomToken";

		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(mimeMessage);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

		assertDoesNotThrow(() -> emailService.sendChangePasswordEmail(user, token));

		verify(javaMailSender, times(1)).send(mimeMessage);
	}

	@Test
	void testSendEmailAfterSeatEdit() throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		User user = new User();
		user.setEmail("laxman.kuddemmi@valtech.com");
		String templateName = "edited_seat_notification.ftlh";

		MimeMessage mimeMessage = mock(MimeMessage.class);
		doNothing().when(javaMailSender).send(mimeMessage);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(freemarker.template.Template.class));

		emailService.sendEmailAfterSeatEdit(user, templateName);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendSeatModificationMail() throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		User user = new User();
		user.setEmail("kruthik.b@valtech.com");

		MimeMessage mimeMessage = mock(MimeMessage.class);
		doNothing().when(javaMailSender).send(mimeMessage);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(freemarker.template.Template.class));

		emailService.sendSeatModificationMail(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}

	@Test
	void testSendSeatCanceledMail() throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		User user = new User();
		user.setEmail("sandeep.kumar@valtech.com");

		MimeMessage mimeMessage = mock(MimeMessage.class);
		doNothing().when(javaMailSender).send(mimeMessage);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(freemarker.template.Template.class));

		emailService.sendSeatCanceledMail(user);

		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}
}