<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Solicitação de Autorização</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
</head>
<body>
<div class="container">
    <jsp:include page="../components/toast.jsp"/>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <strong>Erro!</strong> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card" style="max-width: 800px; margin: 0 auto;">
        <div class="card-header text-center">
            <h3 class="mb-2">
                <i class="bi bi-file-medical me-2"></i>
                Nova Solicitação de Autorização
            </h3>
            <p class="mb-0 opacity-75">Preencha os dados do paciente e do procedimento solicitado</p>
        </div>
        <div class="card-body p-4">
            <div class="info-box">
                <i class="bi bi-info-circle me-2"></i>
                <strong>Atenção:</strong> A autorização será validada automaticamente com base nas regras cadastradas
                (código do procedimento, idade e sexo do paciente).
            </div>

            <form method="POST" action="${pageContext.request.contextPath}/authorizations" id="authorizationForm">
                <h5 class="mb-3 text-primary">
                    <i class="bi bi-person me-2"></i>Dados do Paciente
                </h5>

                <div class="row mb-3">
                    <div class="col-md-12">
                        <label for="patientName" class="form-label required">Nome do Paciente</label>
                        <input type="text"
                               class="form-control"
                               id="patientName"
                               name="patientName"
                               placeholder="Digite o nome completo do paciente"
                               value="${authorization.patientName}"
                               required>
                    </div>
                </div>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <label for="patientAge" class="form-label required">Idade</label>
                        <input type="number"
                               class="form-control"
                               id="patientAge"
                               name="patientAge"
                               placeholder="Ex: 25"
                               value="${authorization.patientAge}"
                               min="0"
                               max="150"
                               required>
                    </div>
                    <div class="col-md-6">
                        <label for="patientGender" class="form-label required">Sexo</label>
                        <select class="form-select" id="patientGender" name="patientGender" required>
                            <option value="">Selecione...</option>
                            <option value="M" ${authorization.patientGender == 'M' ? 'selected' : ''}>
                                Masculino
                            </option>
                            <option value="F" ${authorization.patientGender == 'F' ? 'selected' : ''}>
                                Feminino
                            </option>
                        </select>
                    </div>
                </div>

                <hr class="my-4">

                <h5 class="mb-3 text-primary">
                    <i class="bi bi-clipboard-pulse me-2"></i>Dados do Procedimento
                </h5>

                <div class="row mb-3">
                    <div class="col-md-12">
                        <label for="procedureCode" class="form-label required">Código do Procedimento</label>
                        <input type="text"
                               class="form-control"
                               id="procedureCode"
                               name="procedureCode"
                               placeholder="Ex: 1234"
                               value="${authorization.procedureCode}"
                               maxlength="4"
                               required>
                        <small class="form-text">
                            <i class="bi bi-info-circle me-1"></i>
                            Digite o código de 4 dígitos do procedimento
                        </small>
                    </div>
                </div>

                <div class="row mb-4">
                    <div class="col-md-12">
                        <label for="justification" class="form-label">Justificativa / Observações</label>
                        <textarea class="form-control"
                                  id="justification"
                                  name="justification"
                                  rows="3"
                                  placeholder="Adicione informações adicionais sobre a solicitação (opcional)">${authorization.justification}</textarea>
                    </div>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-4">
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/procedures" class="btn btn-secondary">
                            <i class="bi bi-arrow-left me-2"></i>Ver Regras
                        </a>
                        <a href="${pageContext.request.contextPath}/authorizations" class="btn btn-secondary">
                            <i class="bi bi-card-list me-2"></i>Visualizar Solicitações
                        </a>
                    </div>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-circle me-2"></i>Solicitar Autorização
                    </button>
                </div>
            </form>
        </div>
    </div>

    <div class="card mt-4" style="max-width: 800px; margin: 1rem auto;">
        <div class="card-body">
            <h6 class="mb-3"><i class="bi bi-question-circle me-2"></i>Como funciona?</h6>
            <ol class="mb-0">
                <li class="mb-2">Preencha os dados do paciente (nome, idade e sexo)</li>
                <li class="mb-2">Informe o código do procedimento solicitado</li>
                <li class="mb-2">O sistema verificará automaticamente se o procedimento é permitido</li>
                <li>Você será redirecionado para a página com o resultado da análise</li>
            </ol>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/authorizationForm.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>