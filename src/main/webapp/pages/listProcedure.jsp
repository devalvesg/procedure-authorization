<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Regras de Procedimentos - Sistema de Autorizações</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<div class="container">
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
                        <i class="bi bi-clipboard-check me-2"></i>
                        Regras de Procedimentos
                    </h3>
                    <small>Critérios de autorização por procedimento, idade e sexo</small>
                </div>
                <a href="${pageContext.request.contextPath}/authorizations" class="btn btn-light">
                    <i class="bi bi-plus-circle me-2"></i>
                    Nova Solicitação
                </a>
            </div>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty procedureRules}">
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <h4>Nenhuma regra cadastrada</h4>
                        <p class="text-muted">Não há regras de procedimentos no sistema.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Código do Procedimento</th>
                                <th class="text-center">Idade</th>
                                <th class="text-center">Sexo</th>
                                <th class="text-center">Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="rule" items="${procedureRules}">
                                <tr>
                                    <td><strong>#${rule.id}</strong></td>
                                    <td>
                                        <span class="badge bg-primary">${rule.code}</span>
                                    </td>
                                    <td class="text-center">
                                        <i class="bi bi-calendar me-1"></i>${rule.age} anos
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${rule.gender == 'M'}">
                                                        <span class="badge bg-info">
                                                            <i class="bi bi-gender-male"></i> Masculino
                                                        </span>
                                            </c:when>
                                            <c:when test="${rule.gender == 'F'}">
                                                        <span class="badge bg-warning">
                                                            <i class="bi bi-gender-female"></i> Feminino
                                                        </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${rule.isAuthorized}">
                                                        <span class="badge badge-approved">
                                                            <i class="bi bi-check-circle me-1"></i>PERMITIDO
                                                        </span>
                                            </c:when>
                                            <c:otherwise>
                                                        <span class="badge badge-denied">
                                                            <i class="bi bi-x-circle me-1"></i>NÃO PERMITIDO
                                                        </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="mt-3">
                        <small class="text-muted">
                            <i class="bi bi-info-circle me-1"></i>
                            Total de regras cadastradas: <strong>${procedureRules.size()}</strong>
                        </small>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-4">
        <div class="card">
            <div class="card-body">
                <h6 class="mb-3"><i class="bi bi-info-circle me-2"></i>Informações</h6>
                <div class="row">
                    <div class="col-md-6">
                        <p class="mb-2">
                            <span class="badge badge-approved me-2">PERMITIDO</span>
                            Procedimento autorizado para esta combinação
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p class="mb-0">
                            <span class="badge badge-denied me-2">NÃO PERMITIDO</span>
                            Procedimento não autorizado para esta combinação
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