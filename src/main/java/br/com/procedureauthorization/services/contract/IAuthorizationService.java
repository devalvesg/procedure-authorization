package br.com.procedureauthorization.services.contract;

import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.AuthorizationRequest;

import java.sql.SQLException;
import java.util.List;

public interface IAuthorizationService {
    List<AuthorizationRequest> listAll() throws SQLException;
    void createAuthorization(AuthorizationRequest authorization) throws SQLException, BusinessException;
    AuthorizationRequest findById(Integer id) throws SQLException, BusinessException;
    String validateAuthorizationRulesStatus(AuthorizationRequest authorization) throws SQLException, BusinessException;
}
