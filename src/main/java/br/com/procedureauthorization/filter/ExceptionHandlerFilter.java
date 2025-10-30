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
        } catch (BusinessException e) {
            handleBusinessException(httpRequest, httpResponse, e);
        } catch (Exception e) {
            handleUnexpectedException(httpRequest, httpResponse, e);
        }
    }

    private void handleBusinessException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         BusinessException e) throws ServletException, IOException {

        LOGGER.log(Level.WARNING, "Erro de validação: {0}", e.getMessage());

        request.setAttribute("errorType", "business");
        request.setAttribute("errorTitle", "Atenção");
        request.setAttribute("errorMessage", e.getMessage());
        request.setAttribute("errorIcon", "exclamation-triangle");
        request.setAttribute("showBackButton", true);

        request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
    }

    private void handleUnexpectedException(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Exception e) throws ServletException, IOException {

        String errorId = generateErrorId();

        LOGGER.log(Level.SEVERE,
                String.format("ERRO INESPERADO [ID: %s] - URI: %s", errorId, request.getRequestURI()),
                e);

        request.setAttribute("errorType", "unexpected");
        request.setAttribute("errorTitle", "Erro Inesperado");
        request.setAttribute("errorMessage", "Um erro inesperado ocorreu. Por favor, contate o suporte informando o código: " + errorId);
        request.setAttribute("errorId", errorId);
        request.setAttribute("errorIcon", "x-circle");
        request.setAttribute("showBackButton", true);

        request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
    }

    private String generateErrorId() {
        return "ERR-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("ExceptionHandlerFilter inicializado");
    }

    @Override
    public void destroy() {
        LOGGER.info("ExceptionHandlerFilter destruído");
    }
}