<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Autorizações de Procedimentos - Sistema de Autorizações</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<div class="container">
    <jsp:include page="../components/toast.jsp" />

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>
                ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card">
        <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h3 class="mb-0">
                        <i class="bi bi-file-medical me-2"></i>
                        Autorizações de Procedimentos
                    </h3>
                    <small>Histórico de solicitações de autorização</small>
                </div>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/procedures" class="btn btn-outline-light">
                        <i class="bi bi-list-ul me-2"></i>
                        Ver Regras
                    </a>
                    <a href="${pageContext.request.contextPath}/authorizations?action=new" class="btn btn-light">
                        <i class="bi bi-plus-circle me-2"></i>
                        Nova Solicitação
                    </a>
                </div>
            </div>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty authorizations}">
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <h4>Nenhuma autorização encontrada</h4>
                        <p class="text-muted">Não há solicitações de autorização no sistema.</p>
                        <a href="${pageContext.request.contextPath}/authorizations?action=new" class="btn btn-primary mt-3">
                            <i class="bi bi-plus-circle me-2"></i>
                            Criar Primeira Solicitação
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                            <tr>
                                <th>Protocolo</th>
                                <th>Paciente</th>
                                <th class="text-center">Procedimento</th>
                                <th class="text-center">Idade</th>
                                <th class="text-center">Sexo</th>
                                <th class="text-center">Status</th>
                                <th class="text-center">Data</th>
                                <th class="text-center">Ações</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="auth" items="${authorizations}">
                                <tr>
                                    <td><strong>#${auth.id}</strong></td>
                                    <td>
                                        <i class="bi bi-person-fill me-1 text-primary"></i>
                                            ${auth.patientName}
                                    </td>
                                    <td class="text-center">
                                        <span class="badge bg-primary">${auth.procedureCode}</span>
                                    </td>
                                    <td class="text-center">
                                        <i class="bi bi-calendar me-1"></i>${auth.patientAge} anos
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${auth.patientGender == 'M'}">
                                                <span class="rounded-pill badge badge-cyan">
                                                    <i class="bi bi-gender-male"></i> M
                                                </span>
                                            </c:when>
                                            <c:when test="${auth.patientGender == 'F'}">
                                                <span class="rounded-pill badge badge-pink">
                                                    <i class="bi bi-gender-female"></i> F
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${auth.status == 'APPROVED'}">
                                                <span class="badge badge-approved">
                                                    <i class="bi bi-check-circle me-1"></i>APROVADO
                                                </span>
                                            </c:when>
                                            <c:when test="${auth.status == 'DENIED'}">
                                                <span class="badge badge-denied">
                                                    <i class="bi bi-x-circle me-1"></i>NEGADO
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">
                                                    <i class="bi bi-clock me-1"></i>PENDENTE
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <small class="text-muted">
                                            <i class="bi bi-calendar-event me-1"></i>
                                            <fmt:formatDate value="${auth.requestDate}" pattern="dd/MM/yyyy" />
                                            <br>
                                            <fmt:formatDate value="${auth.requestDate}" pattern="HH:mm" />
                                        </small>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/authorizations?id=${auth.id}"
                                           class="btn btn-sm btn-primary"
                                           title="Ver detalhes">
                                            <i class="bi bi-eye"></i>
                                            Detalhes
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="mt-3 d-flex justify-content-between align-items-center">
                        <small class="text-muted">
                            <i class="bi bi-info-circle me-1"></i>
                            Total de autorizações: <strong>${authorizations.size()}</strong>
                        </small>

                        <div class="d-flex gap-2">
                            <c:set var="approved" value="0" />
                            <c:set var="denied" value="0" />
                            <c:forEach var="auth" items="${authorizations}">
                                <c:if test="${auth.status == 'APPROVED'}">
                                    <c:set var="approved" value="${approved + 1}" />
                                </c:if>
                                <c:if test="${auth.status == 'DENIED'}">
                                    <c:set var="denied" value="${denied + 1}" />
                                </c:if>
                            </c:forEach>

                            <small class="text-muted">
                                <span class="badge badge-approved">${approved}</span> Aprovadas
                            </small>
                            <small class="text-muted">
                                <span class="badge badge-denied">${denied}</span> Negadas
                            </small>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-4">
        <div class="card">
            <div class="card-body">
                <h6 class="mb-3"><i class="bi bi-info-circle me-2"></i>Legendas</h6>
                <div class="row">
                    <div class="col-md-4">
                        <p class="mb-2">
                            <span class="badge badge-approved me-2">APROVADO</span>
                            Procedimento autorizado conforme regras
                        </p>
                    </div>
                    <div class="col-md-4">
                        <p class="mb-2">
                            <span class="badge badge-denied me-2">NEGADO</span>
                            Procedimento não autorizado
                        </p>
                    </div>
                    <div class="col-md-4">
                        <p class="mb-0">
                            <span class="badge bg-warning me-2">PENDENTE</span>
                            Aguardando processamento
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>