package br.com.procedureauthorization.dao;

import br.com.procedureauthorization.config.DatabaseConfig;
import br.com.procedureauthorization.models.AuthorizationRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationRequestDAO {

    private final DatabaseConfig dbConfig;

    public AuthorizationRequestDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void insert(AuthorizationRequest authorizationRequest) throws SQLException {
        String sql = "INSERT INTO authorization_request(procedure_code, patient_name, patient_age, patient_gender, status, justification) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, authorizationRequest.getProcedureCode());
            ps.setString(2, authorizationRequest.getPatientName());
            ps.setInt(3, authorizationRequest.getPatientAge() != null ? authorizationRequest.getPatientAge() : 0);
            ps.setString(4, authorizationRequest.getPatientGender() != null ? authorizationRequest.getPatientGender() : "");
            ps.setString(5, authorizationRequest.getStatus() != null ? authorizationRequest.getStatus() : "PENDING");
            ps.setString(6, authorizationRequest.getJustification());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    authorizationRequest.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<AuthorizationRequest> findAll() throws SQLException {
        List<AuthorizationRequest> requests = new ArrayList<>();
        String sql = "SELECT id, procedure_code, patient_name, patient_age, patient_gender, status, justification, request_date FROM authorization_request ORDER BY request_date DESC";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                requests.add(mapResultSetToAuthorizationRequest(rs));
            }
        }

        return requests;
    }

    public AuthorizationRequest findById(Integer id) throws SQLException {
        String sql = "SELECT id, procedure_code, patient_name, patient_age, patient_gender, status, justification, request_date FROM authorization_request WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAuthorizationRequest(rs);
                }
            }
        }

        return null;
    }

    public AuthorizationRequest validateRules(AuthorizationRequest authorizationRequest) throws SQLException {
        String sql = "SELECT * FROM rule_procedure WHERE procedure_code = ? AND age = ? AND gender = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, authorizationRequest.getProcedureCode());
            ps.setInt(2, authorizationRequest.getPatientAge());
            ps.setString(3, authorizationRequest.getPatientGender());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    var teste = rs.getString("is_authorized");
                    String status = Boolean.parseBoolean(rs.getString("is_authorized")) ? "APROVADO" : "NEGADO";
                    authorizationRequest.setStatus(status);
                    return authorizationRequest;
                }
            }
        }

        return null;
    }

    private AuthorizationRequest mapResultSetToAuthorizationRequest(ResultSet rs) throws SQLException {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setId(rs.getInt("id"));
        request.setProcedureCode(rs.getString("procedure_code"));
        request.setPatientName(rs.getString("patient_name"));
        request.setPatientAge(rs.getInt("patient_age"));
        request.setPatientGender(rs.getString("patient_gender"));
        request.setStatus(rs.getString("status"));
        request.setJustification(rs.getString("justification"));
        request.setRequestDate(rs.getDate("request_date"));
        return request;
    }
}