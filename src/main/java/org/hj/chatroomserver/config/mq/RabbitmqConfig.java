package org.hj.chatroomserver.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String EXCHANGE_EMAIL = "exchange";
    public static final String KEY = "email";

    @Bean(EXCHANGE_EMAIL)
    public Exchange EXCHANGE_EMAIL(){
        return ExchangeBuilder.fanoutExchange(EXCHANGE_EMAIL).durable(true).build();
    }

    @Bean(QUEUE_INFORM_EMAIL)
    public Queue queue_inform_email(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    @Bean
    public Binding binding(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue, @Qualifier(EXCHANGE_EMAIL) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(KEY).noargs();
    }
}
