package br.com.procedureauthorization.service;

import br.com.procedureauthorization.dao.contract.IProcedureRulesDAO;
import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.ProcedureRules;
import br.com.procedureauthorization.services.ProcedureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcedureServiceTest {

    @Mock
    private IProcedureRulesDAO procedureRulesDAO;

    @InjectMocks
    private ProcedureService procedureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listAll_ShouldReturnList_WhenDAOFindAllReturnsData() throws SQLException {
        var mockList = List.of(new ProcedureRules());
        when(procedureRulesDAO.findAll()).thenReturn(mockList);

        var result = procedureService.listAll();

        assertEquals(mockList, result);
        verify(procedureRulesDAO, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnProcedure_WhenFound() throws SQLException, BusinessException {
        ProcedureRules mockProcedure = new ProcedureRules();
        mockProcedure.setCode("1234");

        when(procedureRulesDAO.findById(1)).thenReturn(mockProcedure);

        var result = procedureService.findById(1);

        assertEquals(mockProcedure, result);
        verify(procedureRulesDAO).findById(1);
    }

    @Test
    void findById_ShouldThrowBusinessException_WhenNotFound() throws SQLException {
        when(procedureRulesDAO.findById(1)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.findById(1));

        assertEquals("Nenhuma regra encontrada", ex.getMessage());
    }

    @Test
    void createProcedure_ShouldInsert_WhenValidProcedure() throws SQLException, BusinessException {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("1234");
        procedure.setAge(30);
        procedure.setGender("M");

        when(procedureRulesDAO.existsProcedure(any())).thenReturn(false);
        doNothing().when(procedureRulesDAO).insert(any());

        procedureService.createProcedure(procedure);

        verify(procedureRulesDAO).insert(procedure);
    }

    @Test
    void createProcedure_ShouldThrowException_WhenCodeMissing() {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.createProcedure(procedure));

        assertEquals("O código do procedimento é obrigatório", ex.getMessage());
    }

    @Test
    void createProcedure_ShouldThrowException_WhenCodeTooLong() {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("ABCDE");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.createProcedure(procedure));

        assertEquals("O código do procedimento não pode ter mais de 4 caracteres", ex.getMessage());
    }

    @Test
    void createProcedure_ShouldThrowException_WhenNegativeAge() {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("1234");
        procedure.setAge(-5);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.createProcedure(procedure));

        assertEquals("A idade não pode ser negativa", ex.getMessage());
    }

    @Test
    void createProcedure_ShouldThrowException_WhenInvalidGender() {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("1234");
        procedure.setGender("X");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.createProcedure(procedure));

        assertEquals("Sexo deve ser M ou F", ex.getMessage());
    }

    @Test
    void createProcedure_ShouldThrowException_WhenProcedureAlreadyExists() throws SQLException {
        ProcedureRules procedure = new ProcedureRules();
        procedure.setCode("1234");
        procedure.setGender("M");

        when(procedureRulesDAO.existsProcedure(any())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> procedureService.createProcedure(procedure));

        assertEquals("Regra de procedimento já existente no banco de dados", ex.getMessage());
    }
}
