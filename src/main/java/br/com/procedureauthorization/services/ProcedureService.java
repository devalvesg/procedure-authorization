package br.com.procedureauthorization.services;

import br.com.procedureauthorization.dao.ProcedureRulesDAO;
import br.com.procedureauthorization.dao.contract.IProcedureRulesDAO;
import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.ProcedureRules;
import br.com.procedureauthorization.services.contract.IProcedureService;

import java.sql.SQLException;
import java.util.List;

public class ProcedureService implements IProcedureService {

    private final IProcedureRulesDAO procedureRulesDAO;

    public ProcedureService(IProcedureRulesDAO procedureRulesDAO) {
        this.procedureRulesDAO = procedureRulesDAO;
    }

    public List<ProcedureRules> listAll() throws SQLException {
        return procedureRulesDAO.findAll();
    }

    public void createProcedure(ProcedureRules procedureRule) throws SQLException, BusinessException {
        validateProcedure(procedureRule);
        procedureRulesDAO.insert(procedureRule);
    }

    public ProcedureRules findById(Integer id) throws SQLException, BusinessException {
        var procedure = procedureRulesDAO.findById(id);

        if (procedure == null) {
            throw new BusinessException("Nenhuma regra encontrada");
        }
        
        return procedure;
    }

    private void validateProcedure(ProcedureRules procedureRule) throws BusinessException {
        if (procedureRule.getCode() == null || procedureRule.getCode().isBlank()) {
            throw new BusinessException("O código do procedimento é obrigatório");
        }

        if (procedureRule.getCode().length() > 4) {
            throw new BusinessException("O código do procedimento não pode ter mais de 4 caracteres");
        }

        if (procedureRule.getAge() != null && procedureRule.getAge() < 0) {
            throw new BusinessException("A idade não pode ser negativa");
        }

        if (procedureRule.getGender() != null && !procedureRule.getGender().isEmpty()) {
            String gender = procedureRule.getGender().toUpperCase();
            if (!gender.equals("M") && !gender.equals("F")) {
                throw new BusinessException("Sexo deve ser M ou F");
            }
        }

        if (procedureRulesDAO.existsProcedure(procedureRule)) {
            throw new BusinessException("Regra de procedimento já existente no banco de dados");
        }
    }
}
