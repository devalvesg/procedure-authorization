package br.com.procedureauthorization.dao;
import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.models.Procedure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProcedureDAO {
    private final DatabaseConfig dbConfig;

    public ProcedureDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public List<Procedure> findAll() throws SQLException {
        List<Procedure> procedures = new ArrayList<>();
        String sql = "SELECT id, code, age, gender, isAuthorized FROM procedure ORDER BY id";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                procedures.add(mapResultSetToProcedure(rs));
            }
        }

        return procedures;
    }
    
    public void insert(Procedure procedure) throws SQLException {
        String sql = "INSERT INTO procedure(code, age, gender, isAuthorized) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, procedure.getCode());
            ps.setInt(2, procedure.getAge() != null ? procedure.getAge() : 0);
            ps.setString(3, procedure.getGender() != null ? procedure.getGender() : "");
            ps.setBoolean(4, procedure.getIsAuthorized() != null ? procedure.getIsAuthorized() : false);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    procedure.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Procedure findById(Integer id) throws SQLException {
        String sql = "SELECT id, code, age, gender, isAuthorized FROM procedure WHERE id = ?";

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

    private Procedure mapResultSetToProcedure(ResultSet rs) throws SQLException {
        Procedure p = new Procedure();
        p.setId(rs.getInt("id"));
        p.setCode(rs.getString("code"));
        p.setAge(rs.getInt("age"));
        p.setGender(rs.getString("gender"));
        p.setIsAuthorized(rs.getBoolean("isAuthorized"));
        return p;
    }
}
