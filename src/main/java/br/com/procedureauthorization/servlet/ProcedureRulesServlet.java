package br.com.procedureauthorization.servlet;

import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.config.contract.IDatabaseConfig;
import br.com.procedureauthorization.dao.ProcedureRulesDAO;
import br.com.procedureauthorization.dao.contract.IProcedureRulesDAO;
import br.com.procedureauthorization.models.ProcedureRules;
import br.com.procedureauthorization.services.ProcedureService;
import br.com.procedureauthorization.services.contract.IProcedureService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet(name = "ProcedureRulesServlet", urlPatterns = {"/procedures"})
public class ProcedureRulesServlet extends HttpServlet {

    private IProcedureService procedureService;

    @Override
    public void init() throws ServletException {
        super.init();
        IDatabaseConfig dbConfig = new DatabaseConfig();
        IProcedureRulesDAO procedureRulesDAO = new ProcedureRulesDAO(dbConfig);
        this.procedureService = new ProcedureService(procedureRulesDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        String idParam = req.getParameter("id");

        try {
            if (idParam != null && !idParam.isEmpty()) {
                Integer id = Integer.parseInt(idParam);
                ProcedureRules rule = procedureService.findById(id);

                req.setAttribute("procedureRule", rule);
                req.getRequestDispatcher("/pages/listProcedure.jsp").forward(req, resp);

                return;
            }

            List<ProcedureRules> rules = procedureService.listAll();
            req.setAttribute("procedureRules", rules);
            req.getRequestDispatcher("/pages/listProcedure.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            ProcedureRules rule = extractProcedureRuleFromRequest(req);
            procedureService.createProcedure(rule);

            req.getSession().setAttribute("successMessage", "Regra de procedimento criada com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/procedures");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private ProcedureRules extractProcedureRuleFromRequest(HttpServletRequest req) {
        String code = req.getParameter("code");
        String ageStr = req.getParameter("age");
        String gender = req.getParameter("gender");
        String isAuthorizedStr = req.getParameter("isAuthorized");

        Integer age = parseAge(ageStr);
        Boolean isAuthorized = parseBoolean(isAuthorizedStr);

        ProcedureRules rule = new ProcedureRules();
        rule.setCode(code);
        rule.setAge(age);
        rule.setGender(gender != null ? gender.toUpperCase() : null);
        rule.setIsAuthorized(isAuthorized);

        return rule;
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

    private Boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }
        return "on".equalsIgnoreCase(value) ||
                "true".equalsIgnoreCase(value) ||
                "1".equals(value) ||
                "yes".equalsIgnoreCase(value);
    }
}