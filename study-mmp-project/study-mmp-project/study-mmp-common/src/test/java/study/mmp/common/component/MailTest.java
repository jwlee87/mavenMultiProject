package study.mmp.common.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import study.mmp.common.component.MailPublisher;
import study.mmp.common.component.TemplateMailHelper;
import study.mmp.common.component.mail.MailTemplate;
import study.mmp.common.configuration.RootWebConfig;

import com.google.common.collect.ImmutableMap;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootWebConfig.class})
public class MailTest {


	@Autowired MailPublisher publisher;
	@Autowired TemplateMailHelper mailHelper;
	
	
	@Test
	public void mailTest() {
		publisher.publish("테스트 메일", "안녕하세요..", "테스터<test-ssta@test.com>", new String[] {"sssm@test.com"});
	}
	
	public void templateMailTest() {
		TestMailTemplate testMail = new TestMailTemplate();
		
		mailHelper.send(testMail, ImmutableMap.of("name", "테스터"), new String[] {"sssm@test.com"});
	}
	
	
	static class TestMailTemplate extends MailTemplate {

		@Override
		public String subject() {
			return "테스트 메일";
		}

		@Override
		public String from() {
			return "테스터<test-ssta@test.com>";
		}
		
		@Override
		public String templateFileName() {
			return "testMail.mail";
		}
	}
}
