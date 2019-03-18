package com.taotao.activemq;

		public class SpringActivemq {

//	@Test
//	public void testJmsTemplate(){
//		// 第一步：初始化一个spring容器
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
//		// 第二步：从容器中获得JMSTemplate对象。
//		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
//		// 第三步：从容器中获得一个Destination对象
//		Queue queue = (Queue) applicationContext.getBean("queueDestination");
//		// 第四步：使用JMSTemplate对象发送消息，需要知道Destination
//		jmsTemplate.send(queue, new MessageCreator() {
//
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				TextMessage textMessage = session.createTextMessage("spring activemq test new");
//				return textMessage;
//			}
//		});
//	}
}