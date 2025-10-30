<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Autorização de Procedimentos Médicos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
<div class="container">
    <div class="text-center text-white mb-5 animate-fade-in">
        <div class="logo-container">
            <i class="bi bi-hospital"></i>
        </div>
        <h1 class="display-4 fw-bold mb-3">Sistema de Autorização</h1>
        <p class="description lead mb-0">Plano de Saúde - Controle de Procedimentos Médicos</p>
    </div>

    <div class="row g-4 mb-5">
        <div class="col-md-6">
            <div class="action-card card-primary animate-slide-up" style="animation-delay: 0.1s">
                <div class="card-icon">
                    <i class="bi bi-file-medical"></i>
                </div>
                <h3 class="card-title">Nova Solicitação</h3>
                <p class="card-description">
                    Solicite autorização para um procedimento médico informando os dados do paciente.
                </p>
                <a href="${pageContext.request.contextPath}/authorizations?action=new" class="btn btn-card-action">
                    Criar Solicitação
                    <i class="bi bi-arrow-right ms-2"></i>
                </a>
            </div>
        </div>

        <div class="col-md-6">
            <div class="action-card card-secondary animate-slide-up" style="animation-delay: 0.2s">
                <div class="card-icon">
                    <i class="bi bi-clipboard-check"></i>
                </div>
                <h3 class="card-title">Regras de Procedimentos</h3>
                <p class="card-description">
                    Consulte as regras e critérios de autorização cadastrados no sistema.
                </p>
                <a href="${pageContext.request.contextPath}/procedures" class="btn btn-card-action">
                    Ver Regras
                    <i class="bi bi-arrow-right ms-2"></i>
                </a>
            </div>
        </div>
    </div>

    <div class="row g-4 mb-5">
        <div class="col-md-12">
            <div class="card info-card animate-slide-up" style="animation-delay: 0.3s">
                <div class="card-body p-4">
                    <h5 class="mb-4 text-center">
                        <i class="bi bi-info-circle me-2 text-primary"></i>
                        Como Funciona o Sistema
                    </h5>
                    <div class="row">
                        <div class="col-md-4 feature-item">
                            <div class="feature-icon">
                                <i class="bi bi-1-circle-fill"></i>
                            </div>
                            <h6>Solicite Autorização</h6>
                            <p class="text-muted small">
                                Informe o código do procedimento e os dados do paciente (idade e sexo).
                            </p>
                        </div>
                        <div class="col-md-4 feature-item">
                            <div class="feature-icon">
                                <i class="bi bi-2-circle-fill"></i>
                            </div>
                            <h6>Validação Automática</h6>
                            <p class="text-muted small">
                                O sistema verifica automaticamente se o procedimento é permitido.
                            </p>
                        </div>
                        <div class="col-md-4 feature-item">
                            <div class="feature-icon">
                                <i class="bi bi-3-circle-fill"></i>
                            </div>
                            <h6>Receba o Resultado</h6>
                            <p class="text-muted small">
                                Obtenha aprovação ou negação imediata com protocolo e justificativa.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center text-black mt-5 pt-4 border-top border-white-25 animate-fade-in" style="animation-delay: 0.7s">
        <p class="mb-2">
            <i class="bi bi-hospital me-2"></i>
            Sistema de Controle de Autorizações de Procedimentos Médicos
        </p>
        <p class="small mb-0">
            <i class="bi bi-gear me-2"></i>
            Versão 1.0 | Desenvolvido com tecnologia Java EE
        </p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, observerOptions);

    document.querySelectorAll('[class*="animate-"]').forEach(el => {
        observer.observe(el);
    });

    document.querySelectorAll('.action-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px) scale(1.02)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });
</script>
</body>
</html>