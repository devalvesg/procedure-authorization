package br.com.procedureauthorization.service;

import br.com.procedureauthorization.dao.contract.IAuthorizationRequestDAO;
import br.com.procedureauthorization.exception.BusinessException;
import br.com.procedureauthorization.models.AuthorizationRequest;
import br.com.procedureauthorization.services.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @Mock
    private IAuthorizationRequestDAO authorizationRequestDAO;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listAll_ShouldReturnList_WhenDAOFindAllReturnsData() throws SQLException {
        var mockList = List.of(new AuthorizationRequest());
        when(authorizationRequestDAO.findAll()).thenReturn(mockList);

        var result = authorizationService.listAll();

        assertEquals(mockList, result);
        verify(authorizationRequestDAO, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnAuthorization_WhenFound() throws SQLException, BusinessException {
        AuthorizationRequest mockAuth = new AuthorizationRequest();
        mockAuth.setProcedureCode("1234");

        when(authorizationRequestDAO.findById(1)).thenReturn(mockAuth);

        var result = authorizationService.findById(1);

        assertEquals(mockAuth, result);
        verify(authorizationRequestDAO).findById(1);
    }

    @Test
    void findById_ShouldThrowBusinessException_WhenNotFound() throws SQLException {
        when(authorizationRequestDAO.findById(1)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.findById(1));

        assertEquals("Nenhuma autorização foi encontrada", ex.getMessage());
    }

    @Test
    void createAuthorization_ShouldInsert_WhenValidAuthorization() throws SQLException, BusinessException {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setProcedureCode("1234");
        auth.setPatientAge(25);
        auth.setPatientGender("M");

        when(authorizationRequestDAO.validateRules(any())).thenReturn(auth);
        doNothing().when(authorizationRequestDAO).insert(any());

        authorizationService.createAuthorization(auth);

        verify(authorizationRequestDAO).insert(auth);
    }

    @Test
    void createAuthorization_ShouldThrowException_WhenProcedureCodeMissing() {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setProcedureCode("");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.createAuthorization(auth));

        assertEquals("O código do procedimento é obrigatório", ex.getMessage());
    }

    @Test
    void createAuthorization_ShouldThrowException_WhenProcedureCodeTooLong() {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setProcedureCode("ABCDE");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.createAuthorization(auth));

        assertEquals("O código do procedimento não pode ter mais de 4 caracteres", ex.getMessage());
    }

    @Test
    void createAuthorization_ShouldThrowException_WhenNegativeAge() {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setProcedureCode("1234");
        auth.setPatientAge(-10);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.createAuthorization(auth));

        assertEquals("A idade do paciente não pode ser negativa", ex.getMessage());
    }

    @Test
    void createAuthorization_ShouldThrowException_WhenInvalidGender() {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setProcedureCode("1234");
        auth.setPatientGender("X");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.createAuthorization(auth));

        assertEquals("Sexo do paciente deve ser M ou F", ex.getMessage());
    }

    @Test
    void validateAuthorizationRulesStatus_ShouldReturnStatus_WhenRulesValid() throws SQLException, BusinessException {
        AuthorizationRequest auth = new AuthorizationRequest();
        auth.setStatus("APPROVED");

        when(authorizationRequestDAO.validateRules(auth)).thenReturn(auth);

        String result = authorizationService.validateAuthorizationRulesStatus(auth);

        assertEquals("APPROVED", result);
    }

    @Test
    void validateAuthorizationRulesStatus_ShouldThrowBusinessException_WhenRuleNotFound() throws SQLException {
        AuthorizationRequest auth = new AuthorizationRequest();

        when(authorizationRequestDAO.validateRules(auth)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authorizationService.validateAuthorizationRulesStatus(auth));

        assertEquals("Procedimento não autorizado: Regra de Procedimento não encontrada", ex.getMessage());
    }
}
