package net.main;

import net.socket.SocketCtrler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Main {
    public static void main(String argv[]) {
        BeanFactory beanFactory = new XmlBeanFactory(
                new ClassPathResource("ApplicationContext.xml"));
        SocketCtrler socketCtrler = (SocketCtrler)beanFactory.getBean("socketCtrler");
        socketCtrler.start();
    }
}
