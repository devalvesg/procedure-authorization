package br.com.procedureauthorization.services;

import br.com.procedureauthorization.dao.AuthorizationRequestDAO;
import br.com.procedureauthorization.dao.ProcedureRulesDAO;
import br.com.procedureauthorization.models.AuthorizationRequest;
import br.com.procedureauthorization.models.ProcedureRules;

import java.sql.SQLException;
import java.util.List;

public class AuthorizationService {

    private final AuthorizationRequestDAO authorizationRequestDAO;

    public AuthorizationService(AuthorizationRequestDAO authorizationRequestDAO) {
        this.authorizationRequestDAO = authorizationRequestDAO;
    }

    public List<AuthorizationRequest> listAll() throws SQLException {
        return authorizationRequestDAO.findAll();
    }

    public void createAuthorization(AuthorizationRequest authorization) throws SQLException, ValidationException {
        validateAuthorization(authorization);
        authorization.setStatus(validateAuthorizationRulesStatus(authorization));
        authorizationRequestDAO.insert(authorization);
    }

    public AuthorizationRequest findById(Integer id) throws SQLException {
        return authorizationRequestDAO.findById(id);
    }

    public String validateAuthorizationRulesStatus(AuthorizationRequest authorization) throws SQLException, ValidationException {
        if (authorizationRequestDAO.validateRules(authorization) == null) {
            throw new ValidationException("Procedimento não autorizado: Regra de Procedimento não encontrada");
        };

       return authorization.getStatus();
    }

    private void validateAuthorization(AuthorizationRequest authorization) throws ValidationException {
        if (authorization.getProcedureCode() == null || authorization.getProcedureCode().isBlank()) {
            throw new ValidationException("O código do procedimento é obrigatório");
        }

        if (authorization.getProcedureCode().length() > 4) {
            throw new ValidationException("O código do procedimento não pode ter mais de 4 caracteres");
        }

        if (authorization.getPatientAge() != null && authorization.getPatientAge() < 0) {
            throw new ValidationException("A idade do paciente não pode ser negativa");
        }

        if (authorization.getPatientGender() != null && !authorization.getPatientGender().isEmpty()) {
            String gender = authorization.getPatientGender().toUpperCase();
            if (!gender.equals("M") && !gender.equals("F")) {
                throw new ValidationException("Sexo do paciente deve ser M ou F");
            }
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
