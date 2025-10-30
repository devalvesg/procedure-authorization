package br.com.procedureauthorization.filter;

import br.com.procedureauthorization.exception.BusinessException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
public class ExceptionHandlerFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);

        } catch (ServletException e) {
            Throwable rootCause = e.getRootCause() != null ? e.getRootCause() : e.getCause();

            if (rootCause instanceof BusinessException) {
                handleBusinessException(httpRequest, httpResponse, (BusinessException) rootCause);
            } else if (rootCause != null) {
                handleUnexpectedException(httpRequest, httpResponse, rootCause);
            } else {
                handleUnexpectedException(httpRequest, httpResponse, e);
            }

        } catch (Exception e) {
            handleUnexpectedException(httpRequest, httpResponse, e);
        }
    }

    private void handleBusinessException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         BusinessException e) throws ServletException, IOException {

        LOGGER.log(Level.WARNING, "⚠️ Erro de validação: {0} - URI: {1} - Method: {2}",
                new Object[]{e.getMessage(), request.getRequestURI(), request.getMethod()});

        request.setAttribute("errorMessage", e.getMessage());

        String targetPage = determineTargetPage(request);
        LOGGER.log(Level.INFO, "📄 Redirecionando para: {0}", targetPage);

        request.getRequestDispatcher(targetPage).forward(request, response);
    }

    private void handleUnexpectedException(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Throwable e) throws ServletException, IOException {

        String errorId = generateErrorId();

        LOGGER.log(Level.SEVERE,
                String.format("❌ ERRO INESPERADO [ID: %s] - URI: %s - Method: %s - User: %s",
                        errorId,
                        request.getRequestURI(),
                        request.getMethod(),
                        request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous"),
                e);

        request.setAttribute("unexpectedError",
                "Um erro inesperado ocorreu. Por favor, contate o suporte informando o código: " + errorId);
        request.setAttribute("errorId", errorId);

        String targetPage = determineTargetPage(request);
        LOGGER.log(Level.INFO, "📄 Redirecionando para: {0}", targetPage);

        request.getRequestDispatcher(targetPage).forward(request, response);
    }

    private String determineTargetPage(HttpServletRequest request) {
        String errorPage = (String) request.getAttribute("errorPage");
        if (errorPage != null && !errorPage.isEmpty()) {
            LOGGER.log(Level.FINE, "✅ Usando errorPage definido pelo servlet: {0}", errorPage);
            return errorPage;
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            try {
                String contextPath = request.getContextPath();
                int contextIndex = referer.indexOf(contextPath);

                if (contextIndex != -1) {
                    String path = referer.substring(contextIndex + contextPath.length());

                    int queryIndex = path.indexOf('?');
                    if (queryIndex != -1) {
                        path = path.substring(0, queryIndex);
                    }

                    if (path.endsWith(".jsp")) {
                        LOGGER.log(Level.FINE, "✅ Usando Referer: {0}", path);
                        return path;
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erro ao processar referer: " + referer, e);
            }
        }

        String servletPath = request.getServletPath();
        String method = request.getMethod();

        LOGGER.log(Level.FINE, "🔍 ServletPath: {0}, Method: {1}", new Object[]{servletPath, method});

        if (servletPath.startsWith("/procedures")) {
            return "POST".equalsIgnoreCase(method)
                    ? "/pages/formProcedureRule.jsp"
                    : "/pages/listProcedureRules.jsp";
        }

        if (servletPath.startsWith("/authorizations")) {
            return "/pages/formAuthorization.jsp";
        }

        if (servletPath.endsWith(".jsp")) {
            LOGGER.log(Level.FINE, "✅ Mantendo página JSP atual: {0}", servletPath);
            return servletPath;
        }

        LOGGER.log(Level.WARNING, "Nenhuma rota reconhecida, redirecionando para index");
        return "/index.jsp";
    }

    private String generateErrorId() {
        return String.format("ERR-%d-%04d",
                System.currentTimeMillis(),
                (int) (Math.random() * 10000));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("ExceptionHandlerFilter inicializado - Sistema de tratamento de exceções ativo");
    }

    @Override
    public void destroy() {
        LOGGER.info("ExceptionHandlerFilter finalizado");
    }
}