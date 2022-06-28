package com.changer;

import com.changer.session.Session;
import com.changer.session.SessionFactory;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.TimeUnit;

/**
 * Encapsulated servlet configuration and used to inject dependencies into servlets
 */
@WebListener
public class ServletConfig implements ServletContextListener {

    private static final long SESSION_MAX_AGE_MILLIS = TimeUnit.MINUTES.toMillis(1);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        servletContext.addServlet(EveningServlet.class.getSimpleName(), eveningServlet())
                      .addMapping("/evening");
    }

    private Servlet eveningServlet() {
        return new EveningServlet(session());
    }

    private Session session() {
        return new SessionFactory().createSession(SESSION_MAX_AGE_MILLIS);
    }
}
