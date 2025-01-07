package info.ashutosh.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SessionConfiguration {

    @Bean
    ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {

        return new ServletListenerRegistrationBean<>(new HttpSessionListener() {

            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Session Created: " + se.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                System.out.println("Session Destroyed: " + se.getSession().getId());
            }
        });
    }
}
