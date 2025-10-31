package br.com.procedureauthorization.dao.contract;

import br.com.procedureauthorization.models.AuthorizationRequest;

import java.sql.SQLException;
import java.util.List;

public interface IAuthorizationRequestDAO {

    void insert(AuthorizationRequest authorizationRequest) throws SQLException;
    List<AuthorizationRequest> findAll() throws SQLException;
    AuthorizationRequest findById(Integer id) throws SQLException;
    AuthorizationRequest validateRules(AuthorizationRequest authorizationRequest) throws SQLException;
}
