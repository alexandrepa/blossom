package com.blossomproject.core.common.utils.mail;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import freemarker.template.*;

@RunWith(MockitoJUnitRunner.class)
public class MailSenderImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private MailSenderImpl mailSender;
  private JavaMailSender javaMailSender;
  private Configuration configuration;
  private MessageSource messageSource;
  private String from;
  private String basePath;
  private Set<String> filters;
  private Locale locale;
  private MailSenderImpl.MessageResolverMethod resolverMethod;

  @Before
  public void setUp() throws Exception {
    this.javaMailSender = mock(JavaMailSender.class);
    this.configuration = mock(Configuration.class);
    this.messageSource = mock(MessageSource.class);
    this.from = "blossom-test@test.fr";
    this.basePath = "basePath";
    this.filters = Sets.newHashSet("[a-z]+@test.fr");
    this.locale = Locale.ENGLISH;
    this.mailSender = new MailSenderImpl(this.javaMailSender, this.configuration, this.filters, this.messageSource,
        this.from, this.basePath, this.locale);
    this.resolverMethod = mailSender.new MessageResolverMethod(this.messageSource, this.locale);
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
        mock(MessageSource.class), "from", "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_javamailsender_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(null, mock(Configuration.class), Sets.newHashSet(), mock(MessageSource.class), "from",
        "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_configuration_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), null, Sets.newHashSet(), mock(MessageSource.class), "from",
        "basePath", this.locale);
  }

  @Test
  public void should_succeed_instanciate_when_filters_is_null() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), null, mock(MessageSource.class), "from",
        "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_messagesource_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(), null, "from",
        "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_from_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
        mock(MessageSource.class), null, "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_basepath_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
        mock(MessageSource.class), "from", null, this.locale);
  }

  @Test
  public void should_fail_instanciate_when_locale_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
        mock(MessageSource.class), "from", "basePath", null);
  }

  @Test
  public void should_not_send_mail_without_mailtos() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }

  @Test
  public void should_not_send_mail_without_context() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }

  @Test
  public void should_not_send_mail_without_subject() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = null;

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }

  @Test
  public void should_not_send_mail_without_template_found() throws Exception {
    thrown.expect(TemplateNotFoundException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    when(configuration.getTemplate(any())).thenThrow(new TemplateNotFoundException("test", null, null));

    this.mailSender.sendMail(htmlTemplate, parameters, subject, "test@test.fr");
  }

  @Test
  public void should_not_send_mail_with_address_filtered() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    Template template = mock(Template.class);
    when(configuration.getTemplate(any())).thenReturn(template);

    this.mailSender.sendMail(htmlTemplate, parameters, subject, "test@blossom-project.com");
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mails() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    Template template = mock(Template.class);
    when(configuration.getTemplate(any())).thenReturn(template);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("subject");

    this.mailSender.sendMail(htmlTemplate, parameters, subject, "test@test.fr", "testa@test.fr");
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mails_with_files() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";
    File file = spy(new File("test1"));
    File file2 = spy(new File("test2"));

    Template template = mock(Template.class);
    when(configuration.getTemplate(any())).thenReturn(template);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("subject");

    this.mailSender.sendMail(htmlTemplate, parameters, subject, Locale.ENGLISH, Lists.newArrayList(file, file2),
        "test@test.fr", "testa@test.fr");
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    verify(file, times(1)).getName();
    verify(file2, times(1)).getName();
  }

  @Test
  public void should_not_send_mail_with_address_filtered_and_files() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";
    File file = spy(new File("test1"));
    File file2 = spy(new File("test2"));

    Template template = mock(Template.class);
    when(configuration.getTemplate(any())).thenReturn(template);

    this.mailSender.sendMail(htmlTemplate, parameters, subject, Locale.ENGLISH, Lists.newArrayList(file, file2),
        "test@blossom-project.com");
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  public void should_not_exec_and_throw_exception_without_arguments() throws Exception {
    thrown.expect(NullPointerException.class);
    resolverMethod.exec(null);
  }

  @Test
  public void should_not_exec_and_throw_exception_with_multiple_arguments() throws Exception {
    thrown.expect(TemplateModelException.class);
    resolverMethod.exec(Lists.newArrayList("1", "2"));
  }

  @Test
  public void should_not_exec_and_throw_exception_with_incorrect_code() throws Exception {
    thrown.expect(TemplateModelException.class);
    resolverMethod.exec(Lists.newArrayList(new SimpleScalar("")));
  }

  @Test
  public void should_exec() throws Exception {
    String result = (String) resolverMethod.exec(Lists.newArrayList(new SimpleScalar("test")));
    verify(messageSource, times(1)).getMessage(any(), any(), any());
  }

  @Test
  public void should_enrich_context() {
    Map<String, Object> ctx = new HashMap<>();
    mailSender.enrichContext(ctx, Locale.ENGLISH);
    assertTrue(ctx.containsKey("basePath"));
    assertTrue(ctx.containsKey("message"));
    assertTrue(ctx.containsKey("lang"));
  }

  @Test
  public void should_not_filter_emails_without_filters() {
    this.filters = null;
    String[] mailto = new String[]{"test@test.fr", "testa@test.fr"};
    String[] response = mailSender.filterMails(mailto);
    assertEquals(2, response.length);
    this.filters = Sets.newHashSet("[a-z]+@test.fr");
  }

  @Test
  public void should_not_filter_emails_without_emails() {
    String[] response = mailSender.filterMails(null);
    assertNull(response);
  }

  @Test
  public void should_not_filter_emails_with_empty_filters() {
    this.filters = Sets.newHashSet();
    String[] mailto = new String[]{"test@test.fr", "testa@test.fr"};
    String[] response = mailSender.filterMails(mailto);
    assertEquals(2, response.length);
    this.filters = Sets.newHashSet("[a-z]+@test.fr");
  }

  @Test
  public void should_filter_emails() {
    String[] mailto = new String[]{"test@test.fr", "testa@test.fr", "test@blossom.fr"};
    String[] response = mailSender.filterMails(mailto);
    assertEquals(2, response.length);
  }

}
