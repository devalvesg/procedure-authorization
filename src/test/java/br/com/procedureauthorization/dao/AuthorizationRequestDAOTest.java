package br.com.procedureauthorization.dao;

import br.com.procedureauthorization.config.contract.IDatabaseConfig;
import br.com.procedureauthorization.models.AuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationRequestDAOTest {

    @Mock
    private IDatabaseConfig dbConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AuthorizationRequestDAO authorizationRequestDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert_ShouldSetIdAndDate_WhenInsertSuccessful() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setProcedureCode("1234");
        request.setPatientName("John Doe");
        request.setPatientAge(30);
        request.setPatientGender("M");
        request.setJustification("Test insert");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);
        when(resultSet.getTimestamp(2)).thenReturn(timestamp);

        authorizationRequestDAO.insert(request);

        verify(preparedStatement, times(1)).executeQuery();
        assertEquals(10, request.getId());
        assertEquals(timestamp, request.getRequestDate());
    }

    @Test
    void findAll_ShouldReturnListOfRequests_WhenDataExists() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("procedure_code")).thenReturn("PROC1");
        when(resultSet.getString("patient_name")).thenReturn("Alice");
        when(resultSet.getInt("patient_age")).thenReturn(40);
        when(resultSet.getString("patient_gender")).thenReturn("F");
        when(resultSet.getString("status")).thenReturn("PENDING");
        when(resultSet.getString("justification")).thenReturn("Justification text");
        when(resultSet.getTimestamp("request_date")).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<AuthorizationRequest> result = authorizationRequestDAO.findAll();

        assertEquals(1, result.size());
        assertEquals("PROC1", result.get(0).getProcedureCode());
        assertEquals("Alice", result.get(0).getPatientName());
    }

    @Test
    void findById_ShouldReturnRequest_WhenFound() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(5);
        when(resultSet.getString("procedure_code")).thenReturn("PROC2");
        when(resultSet.getString("patient_name")).thenReturn("Bob");
        when(resultSet.getInt("patient_age")).thenReturn(50);
        when(resultSet.getString("patient_gender")).thenReturn("M");
        when(resultSet.getString("status")).thenReturn("APPROVED");
        when(resultSet.getString("justification")).thenReturn("Valid procedure");
        when(resultSet.getTimestamp("request_date")).thenReturn(new Timestamp(System.currentTimeMillis()));

        AuthorizationRequest result = authorizationRequestDAO.findById(5);

        assertNotNull(result);
        assertEquals("PROC2", result.getProcedureCode());
        assertEquals("Bob", result.getPatientName());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        AuthorizationRequest result = authorizationRequestDAO.findById(999);

        assertNull(result);
    }

    @Test
    void validateRules_ShouldReturnAuthorizationWithStatusApproved_WhenRuleMatchesAuthorized() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setProcedureCode("PROC3");
        request.setPatientAge(60);
        request.setPatientGender("M");

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean("is_authorized")).thenReturn(true);

        AuthorizationRequest result = authorizationRequestDAO.validateRules(request);

        assertNotNull(result);
        assertEquals("APROVADO", result.getStatus());
    }

    @Test
    void validateRules_ShouldReturnAuthorizationWithStatusDenied_WhenRuleMatchesNotAuthorized() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setProcedureCode("PROC4");
        request.setPatientAge(25);
        request.setPatientGender("F");

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean("is_authorized")).thenReturn(false);

        AuthorizationRequest result = authorizationRequestDAO.validateRules(request);

        assertNotNull(result);
        assertEquals("NEGADO", result.getStatus());
    }

    @Test
    void validateRules_ShouldReturnNull_WhenNoMatchFound() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setProcedureCode("PROC5");
        request.setPatientAge(18);
        request.setPatientGender("M");

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        AuthorizationRequest result = authorizationRequestDAO.validateRules(request);

        assertNull(result);
    }
}
