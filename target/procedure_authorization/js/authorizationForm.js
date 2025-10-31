document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('authorizationForm');
    if (!form) return;

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = Object.fromEntries(new FormData(form));

        try {
            console.log('JSON enviado:', JSON.stringify(formData, null, 2));
            const response = await fetch(form.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify(formData)
            });

            const data = await response.json();

            if (!response.ok) {
                showToast(data.error || data.message || 'Erro na solicitação.', 'warning');
            } else {
                showToast('Solicitação enviada com sucesso!', 'success');
                form.reset();
            }

        } catch (error) {
            console.error('Erro ao enviar solicitação:', error);
            showToast('Erro inesperado ao enviar a solicitação.', 'danger');
        }
    });

    function showToast(message, type) {
        const colors = {
            success: 'text-bg-success',
            warning: 'text-bg-warning',
            danger: 'text-bg-danger'
        };

        let container = document.querySelector('.toast-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'toast-container position-fixed top-0 end-0 p-3';
            container.style.zIndex = '9999';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast align-items-center ${colors[type] || 'text-bg-secondary'} border-0 show`;
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;

        container.appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();

        toast.addEventListener('hidden.bs.toast', () => toast.remove());
    }
});
