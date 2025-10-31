package br.com.procedureauthorization.dao.contract;

import br.com.procedureauthorization.models.AuthorizationRequest;
import br.com.procedureauthorization.models.ProcedureRules;

import java.sql.SQLException;
import java.util.List;

public interface IProcedureRulesDAO {

    void insert(ProcedureRules procedureRules) throws SQLException;
    List<ProcedureRules> findAll() throws SQLException;
    ProcedureRules findById(Integer id) throws SQLException;
    boolean existsProcedure(ProcedureRules procedureRule);
}
