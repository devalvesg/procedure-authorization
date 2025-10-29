package br.com.procedureauthorization.services;

import br.com.procedureauthorization.dao.ProcedureRulesDAO;
import br.com.procedureauthorization.models.ProcedureRules;

import java.sql.SQLException;
import java.util.List;

public class ProcedureService {

    private final ProcedureRulesDAO procedureRulesDAO;

    public ProcedureService(ProcedureRulesDAO procedureRulesDAO) {
        this.procedureRulesDAO = procedureRulesDAO;
    }

    public List<ProcedureRules> listAll() throws SQLException {
        return procedureRulesDAO.findAll();
    }

    public void createProcedure(ProcedureRules procedureRule) throws SQLException, ValidationException {
        validateProcedure(procedureRule);
        procedureRulesDAO.insert(procedureRule);
    }

    public ProcedureRules findById(Integer id) throws SQLException {
        return procedureRulesDAO.findById(id);
    }

    private void validateProcedure(ProcedureRules procedureRule) throws ValidationException {
        if (procedureRule.getCode() == null || procedureRule.getCode().isBlank()) {
            throw new ValidationException("O código do procedimento é obrigatório");
        }

        if (procedureRule.getCode().length() > 4) {
            throw new ValidationException("O código do procedimento não pode ter mais de 4 caracteres");
        }

        if (procedureRule.getAge() != null && procedureRule.getAge() < 0) {
            throw new ValidationException("A idade não pode ser negativa");
        }

        if (procedureRule.getGender() != null && !procedureRule.getGender().isEmpty()) {
            String gender = procedureRule.getGender().toUpperCase();
            if (!gender.equals("M") && !gender.equals("F")) {
                throw new ValidationException("Sexo deve ser M ou F");
            }
        }

        if(procedureRulesDAO.existsProcedure(procedureRule)){
            throw new ValidationException("Regra de procedimento já existente no banco de dados");
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
