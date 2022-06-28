package com.changer;

import com.changer.session.Session;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class EveningServlet extends HttpServlet {

    private static final String EVENING_GREETING_TEMPLATE = "Good evening, %s";
    private static final String NAME_QUERY_PARAM = "name";
    private static final String DEFAULT_NAME = "Buddy";

    private final transient Session session;

    public EveningServlet(Session session) {
        this.session = session;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object name = session.get(NAME_QUERY_PARAM, req).orElseGet(() -> getNameFromQuery(req, resp));
        PrintWriter writer = resp.getWriter();
        writer.format(EVENING_GREETING_TEMPLATE, name);
        writer.flush();
    }

    private String getNameFromQuery(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> nameParam = Optional.ofNullable(request.getParameter(NAME_QUERY_PARAM));
        nameParam.ifPresent(name -> session.set(NAME_QUERY_PARAM, name, request, response));
        return nameParam.orElse(DEFAULT_NAME);
    }
}
