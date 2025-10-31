package br.com.procedureauthorization.dao;

import br.com.procedureauthorization.config.contract.IDatabaseConfig;
import br.com.procedureauthorization.models.ProcedureRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcedureRulesDAOTest {

    @Mock
    private IDatabaseConfig dbConfig;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ProcedureRulesDAO procedureRulesDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfProcedureRules_WhenResultSetHasData() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("procedure_code")).thenReturn("1234");
        when(resultSet.getInt("age")).thenReturn(25);
        when(resultSet.getString("gender")).thenReturn("M");
        when(resultSet.getBoolean("is_authorized")).thenReturn(true);

        List<ProcedureRules> result = procedureRulesDAO.findAll();

        assertEquals(1, result.size());
        ProcedureRules p = result.get(0);
        assertEquals("1234", p.getCode());
        assertTrue(p.getIsAuthorized());
    }

    @Test
    void insert_ShouldSetGeneratedId_WhenInsertSuccessful() throws Exception {
        ProcedureRules rule = new ProcedureRules();
        rule.setCode("9999");
        rule.setAge(40);
        rule.setGender("M");
        rule.setIsAuthorized(true);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getInt(1)).thenReturn(100);

        procedureRulesDAO.insert(rule);

        verify(preparedStatement, times(1)).executeUpdate();
        assertEquals(100, rule.getId());
    }

    @Test
    void findById_ShouldReturnProcedure_WhenFound() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(5);
        when(resultSet.getString("procedure_code")).thenReturn("1234");
        when(resultSet.getInt("age")).thenReturn(30);
        when(resultSet.getString("gender")).thenReturn("F");
        when(resultSet.getBoolean("is_authorized")).thenReturn(false);

        ProcedureRules result = procedureRulesDAO.findById(5);

        assertNotNull(result);
        assertEquals("1234", result.getCode());
        assertEquals("F", result.getGender());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() throws Exception {
        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        ProcedureRules result = procedureRulesDAO.findById(99);

        assertNull(result);
    }

    @Test
    void existsProcedure_ShouldReturnTrue_WhenRecordExists() throws Exception {
        ProcedureRules rule = new ProcedureRules();
        rule.setCode("1234");
        rule.setAge(20);
        rule.setGender("M");

        when(dbConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        boolean exists = procedureRulesDAO.existsProcedure(rule);

        assertTrue(exists);
    }

    @Test
    void existsProcedure_ShouldReturnFalse_WhenSQLExceptionOccurs() throws Exception {
        ProcedureRules rule = new ProcedureRules();
        rule.setCode("1234");
        rule.setAge(20);
        rule.setGender("M");

        when(dbConfig.getConnection()).thenThrow(new SQLException("DB error"));

        boolean exists = procedureRulesDAO.existsProcedure(rule);

        assertFalse(exists);
    }
}
