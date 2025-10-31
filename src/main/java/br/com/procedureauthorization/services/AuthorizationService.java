package br.com.procedureauthorization.services;

import br.com.procedureauthorization.dao.AuthorizationRequestDAO;
import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.AuthorizationRequest;

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

    public void createAuthorization(AuthorizationRequest authorization) throws SQLException, BusinessException {
        validateAuthorization(authorization);
        authorization.setStatus(validateAuthorizationRulesStatus(authorization));
        authorizationRequestDAO.insert(authorization);
    }

    public AuthorizationRequest findById(Integer id) throws SQLException, BusinessException {
        var authorization = authorizationRequestDAO.findById(id);

        if (authorization == null) {
            throw new BusinessException("Nenhuma autorização foi encontrada");
        }

        return authorization;
    }

    public String validateAuthorizationRulesStatus(AuthorizationRequest authorization) throws SQLException, BusinessException {
        if (authorizationRequestDAO.validateRules(authorization) == null) {
            throw new BusinessException("Procedimento não autorizado: Regra de Procedimento não encontrada");
        };

       return authorization.getStatus();
    }

    private void validateAuthorization(AuthorizationRequest authorization) throws BusinessException {
        if (authorization.getProcedureCode() == null || authorization.getProcedureCode().isBlank()) {
            throw new BusinessException("O código do procedimento é obrigatório");
        }

        if (authorization.getProcedureCode().length() > 4) {
            throw new BusinessException("O código do procedimento não pode ter mais de 4 caracteres");
        }

        if (authorization.getPatientAge() != null && authorization.getPatientAge() < 0) {
            throw new BusinessException("A idade do paciente não pode ser negativa");
        }

        if (authorization.getPatientGender() != null && !authorization.getPatientGender().isEmpty()) {
            String gender = authorization.getPatientGender().toUpperCase();
            if (!gender.equals("M") && !gender.equals("F")) {
                throw new BusinessException("Sexo do paciente deve ser M ou F");
            }
        }
    }
}
