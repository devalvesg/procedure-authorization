package br.com.procedureauthorization.config.contract;

import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.AuthorizationRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseConfig {
    Connection getConnection() throws SQLException;
}
