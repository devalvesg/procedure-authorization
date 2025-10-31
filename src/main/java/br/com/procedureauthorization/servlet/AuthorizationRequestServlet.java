package br.com.procedureauthorization.servlet;

import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.config.contract.IDatabaseConfig;
import br.com.procedureauthorization.dao.AuthorizationRequestDAO;
import br.com.procedureauthorization.dao.contract.IAuthorizationRequestDAO;
import br.com.procedureauthorization.models.AuthorizationRequest;
import br.com.procedureauthorization.services.AuthorizationService;
import br.com.procedureauthorization.services.contract.IAuthorizationService;
import jakarta.servlet.ServletException;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AuthorizationRequestServlet", urlPatterns = {"/authorizations"})
public class AuthorizationRequestServlet extends HttpServlet {

    private IAuthorizationService authorizationService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        IDatabaseConfig dbConfig = new DatabaseConfig();
        IAuthorizationRequestDAO authorizationRequestDAO = new AuthorizationRequestDAO(dbConfig);
        this.authorizationService = new AuthorizationService(authorizationRequestDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        String idParam = req.getParameter("id");
        String action = req.getParameter("action");

        try {
            if ("new".equals(action)) {
                req.getRequestDispatcher("/pages/formAuthorization.jsp").forward(req, resp);
                return;
            }

            if (idParam != null && !idParam.isEmpty()) {
                Integer id = Integer.parseInt(idParam);
                AuthorizationRequest authorization = authorizationService.findById(id);

                req.setAttribute("authorization", authorization);
                req.getRequestDispatcher("/pages/viewAuthorizationResult.jsp").forward(req, resp);
                return;
            }

            List<AuthorizationRequest> authorizations = authorizationService.listAll();
            req.setAttribute("authorizations", authorizations);

            req.getRequestDispatcher("/pages/listAuthorizations.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            AuthorizationRequest authorization = extractAuthorizationFromJson(req);

            authorizationService.createAuthorization(authorization);

            String jsonResponse = gson.toJson(authorization);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private AuthorizationRequest extractAuthorizationFromJson(HttpServletRequest req) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return gson.fromJson(json, AuthorizationRequest.class);
        }
    }

    private Integer parseAge(String ageStr) {
        if (ageStr == null || ageStr.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}