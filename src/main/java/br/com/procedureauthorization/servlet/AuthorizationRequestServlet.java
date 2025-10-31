package br.com.procedureauthorization.servlet;

import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.dao.AuthorizationRequestDAO;
import br.com.procedureauthorization.models.AuthorizationRequest;
import br.com.procedureauthorization.services.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet(name = "AuthorizationRequestServlet", urlPatterns = {"/authorizations"})
public class AuthorizationRequestServlet extends HttpServlet {

    private AuthorizationService authorizationService;

    @Override
    public void init() throws ServletException {
        super.init();
        DatabaseConfig dbConfig = new DatabaseConfig();
        AuthorizationRequestDAO authorizationRequestDAO = new AuthorizationRequestDAO(dbConfig);
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
            AuthorizationRequest authorization = extractAuthorizationFromRequest(req);
            authorizationService.createAuthorization(authorization);

            req.setAttribute("authorization", authorization);
            req.getRequestDispatcher("/pages/viewAuthorizationResult.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private AuthorizationRequest extractAuthorizationFromRequest(HttpServletRequest req) {
        String procedureCode = req.getParameter("procedureCode");
        String patientName = req.getParameter("patientName");
        String patientAgeStr = req.getParameter("patientAge");
        String patientGender = req.getParameter("patientGender");
        String justification = req.getParameter("justification");

        Integer patientAge = parseAge(patientAgeStr);

        AuthorizationRequest authorization = new AuthorizationRequest();
        authorization.setProcedureCode(procedureCode);
        authorization.setPatientName(patientName);
        authorization.setPatientAge(patientAge);
        authorization.setPatientGender(patientGender != null ? patientGender.toUpperCase() : null);
        authorization.setJustification(justification);

        return authorization;
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