<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultado da Autorização</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/result.css">
</head>
<body>
<div class="container">
    <div class="result-card">
        <div class="result-header ${authorization.status == 'APROVADO' ? 'approved' : 'denied'}">
            <div class="result-icon">
                <c:choose>
                    <c:when test="${authorization.status == 'APROVADO'}">
                        <i class="bi bi-check-circle-fill"></i>
                    </c:when>
                    <c:otherwise>
                        <i class="bi bi-x-circle-fill"></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="result-title">
                <c:choose>
                    <c:when test="${authorization.status == 'APROVADO'}">
                        AUTORIZAÇÃO APROVADA
                    </c:when>
                    <c:otherwise>
                        AUTORIZAÇÃO NEGADA
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="result-subtitle">
                Protocolo: <span class="protocol-number">#${authorization.id}</span>
            </div>
        </div>

        <div class="card-body p-4">
            <div class="text-center mb-4">
                <small class="text-muted">
                    <i class="bi bi-calendar-event me-2"></i>
                    Solicitado em:
                    <fmt:formatDate value="${authorization.requestDate}" pattern="dd/MM/yyyy 'às' HH:mm" />
                </small>
            </div>

            <h5 class="mb-3">
                <i class="bi bi-person-fill me-2 text-primary"></i>
                Dados do Paciente
            </h5>
            <div class="info-section">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="info-label">Nome do Paciente</div>
                        <div class="info-value">${authorization.patientName}</div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="info-label">Idade</div>
                        <div class="info-value">
                            <i class="bi bi-calendar me-1"></i>${authorization.patientAge} anos
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="info-label">Sexo</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${authorization.patientGender == 'M'}">
                                    <i class="bi bi-gender-male me-1"></i>Masculino
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-gender-female me-1"></i>Feminino
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <h5 class="mb-3 mt-4">
                <i class="bi bi-clipboard-pulse me-2 text-primary"></i>
                Procedimento Solicitado
            </h5>
            <div class="info-section">
                <div class="row">
                    <div class="col-md-12 mb-3">
                        <div class="info-label">Código do Procedimento</div>
                        <div class="info-value">
                                <span class="badge bg-primary" style="font-size: 1.1rem; padding: 0.5rem 1rem;">
                                    ${authorization.procedureCode}
                                </span>
                        </div>
                    </div>
                    <c:if test="${not empty authorization.justification}">
                        <div class="col-md-12">
                            <div class="info-label">Observações</div>
                            <div class="info-value">${authorization.justification}</div>
                        </div>
                    </c:if>
                </div>
            </div>

            <c:if test="${authorization.status == 'NEGADO'}">
                <h5 class="mb-3 mt-4">
                    <i class="bi bi-exclamation-triangle me-2 text-danger"></i>
                    Motivo da Negação
                </h5>
                <div class="justification-box error">
                    <strong>
                        <i class="bi bi-info-circle me-2"></i>
                        Procedimento não autorizado
                    </strong>
                    <p class="mb-0 mt-2">
                        O procedimento <strong>${authorization.procedureCode}</strong> não é permitido
                        para paciente com <strong>${authorization.patientAge} anos</strong>
                        do sexo <strong>${authorization.patientGender == 'M' ? 'masculino' : 'feminino'}</strong>
                        de acordo com as regras cadastradas no sistema.
                    </p>
                </div>
            </c:if>

            <c:if test="${authorization.status == 'APROVADO'}">
                <h5 class="mb-3 mt-4">
                    <i class="bi bi-check-circle me-2 text-success"></i>
                    Confirmação
                </h5>
                <div class="justification-box success">
                    <strong>
                        <i class="bi bi-check2-circle me-2"></i>
                        Procedimento autorizado com sucesso
                    </strong>
                    <p class="mb-0 mt-2">
                        O procedimento <strong>${authorization.procedureCode}</strong> está autorizado
                        para paciente com <strong>${authorization.patientAge} anos</strong>
                        do sexo <strong>${authorization.patientGender == 'M' ? 'masculino' : 'feminino'}</strong>.
                    </p>
                </div>
            </c:if>

            <h5 class="mb-3 mt-4">
                <i class="bi bi-list-check me-2 text-primary"></i>
                Processo de Análise
            </h5>
            <div class="timeline">
                <div class="timeline-item">
                    <div class="timeline-icon">
                        <i class="bi bi-1-circle"></i>
                    </div>
                    <div>
                        <strong>Solicitação recebida</strong>
                        <br>
                        <small class="text-muted">Dados do paciente e procedimento registrados</small>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="timeline-icon">
                        <i class="bi bi-2-circle"></i>
                    </div>
                    <div>
                        <strong>Validação automática</strong>
                        <br>
                        <small class="text-muted">Verificação das regras de autorização</small>
                    </div>
                </div>
                <div class="timeline-item">
                    <div class="timeline-icon">
                        <i class="bi bi-3-circle"></i>
                    </div>
                    <div>
                        <strong>Resultado gerado</strong>
                        <br>
                        <small class="text-muted">
                            Status:
                            <strong class="${authorization.status == 'APROVADO' ? 'text-success' : 'text-danger'}">
                                ${authorization.status}
                            </strong>
                        </small>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-between align-items-center mt-4 pt-3 border-top">
                <a href="${pageContext.request.contextPath}/procedures" class="btn btn-outline-secondary btn-action">
                    <i class="bi bi-list-ul me-2"></i>Ver Regras
                </a>
                <div>
                    <button onclick="window.print()" class="btn btn-outline-primary btn-action me-2">
                        <i class="bi bi-printer me-2"></i>Imprimir
                    </button>
                    <a href="${pageContext.request.contextPath}/authorizations?action=new" class="btn btn-primary btn-action">
                        <i class="bi bi-plus-circle me-2"></i>Nova Solicitação
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="card mt-4" style="max-width: 800px; margin: 1rem auto; border-radius: 15px;">
        <div class="card-body">
            <h6 class="mb-3">
                <i class="bi bi-info-circle me-2 text-primary"></i>
                Próximos Passos
            </h6>
            <c:choose>
                <c:when test="${authorization.status == 'APROVADO'}">
                    <ul class="mb-0">
                        <li>Anote o número do protocolo: <strong>#${authorization.id}</strong></li>
                        <li>Imprima este documento para apresentar ao prestador de serviço</li>
                        <li>O procedimento pode ser realizado conforme autorização</li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <ul class="mb-0">
                        <li>Verifique as regras cadastradas no sistema</li>
                        <li>Entre em contato com a operadora para mais informações</li>
                        <li>Caso necessário, solicite revisão da regra</li>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<button onclick="window.print()" class="btn btn-primary btn-lg print-button shadow">
    <i class="bi bi-printer-fill"></i>
</button>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>