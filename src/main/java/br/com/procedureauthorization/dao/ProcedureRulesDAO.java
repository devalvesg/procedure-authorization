package br.com.procedureauthorization.dao;

import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.models.ProcedureRules;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureRulesDAO {
    private final DatabaseConfig dbConfig;

    public ProcedureRulesDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public List<ProcedureRules> findAll() throws SQLException {
        List<ProcedureRules> procedureRules = new ArrayList<>();
        String sql = "SELECT id, code, age, gender, isAuthorized FROM rule_procedure ORDER BY id";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                procedureRules.add(mapResultSetToProcedure(rs));
            }
        }

        return procedureRules;
    }

    public void insert(ProcedureRules procedureRules) throws SQLException {
        String sql = "INSERT INTO rule_procedure(code, age, gender, isAuthorized) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, procedureRules.getCode());
            ps.setInt(2, procedureRules.getAge() != null ? procedureRules.getAge() : 0);
            ps.setString(3, procedureRules.getGender() != null ? procedureRules.getGender() : "");
            ps.setBoolean(4, procedureRules.getIsAuthorized() != null ? procedureRules.getIsAuthorized() : false);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    procedureRules.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public ProcedureRules findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM rule_procedure WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProcedure(rs);
                }
            }
        }

        return null;
    }

    public boolean existsProcedure(ProcedureRules procedureRule) {
        String sql = "SELECT 1 FROM rule_procedure WHERE procedure_code = ? AND age = ? AND gender = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, procedureRule.getCode());
            ps.setInt(2, procedureRule.getAge());
            ps.setString(3, procedureRule.getGender());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private ProcedureRules mapResultSetToProcedure(ResultSet rs) throws SQLException {
        ProcedureRules p = new ProcedureRules();
        p.setId(rs.getInt("id"));
        p.setCode(rs.getString("code"));
        p.setAge(rs.getInt("age"));
        p.setGender(rs.getString("gender"));
        p.setIsAuthorized(rs.getBoolean("isAuthorized"));
        return p;
    }
}
