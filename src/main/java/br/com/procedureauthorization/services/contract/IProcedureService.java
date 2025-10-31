package br.com.procedureauthorization.services.contract;

import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.ProcedureRules;

import java.sql.SQLException;
import java.util.List;

public interface IProcedureService {
    List<ProcedureRules> listAll() throws SQLException;
    void createProcedure(ProcedureRules procedureRule) throws SQLException, BusinessException;
    ProcedureRules findById(Integer id) throws SQLException, BusinessException;
}
