package com.christmo.app;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by christmo on 8/7/15.
 */
public class MainApp {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        try{

            ActiveMQComponent queue = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false");
            queue.setBrokerURL("tcp://localhost:61616");

            context.addComponent("activemq", queue);
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:queue:MessagesQueue").to("stream:out");
                }
            });

            ProducerTemplate template = context.createProducerTemplate();
            context.start();

            template.sendBody("activemq:queue:MessagesQueue", "Mensaje para la cola");

        } finally {
            context.stop();
        }

    }

}
