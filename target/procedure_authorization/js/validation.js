document.addEventListener('DOMContentLoaded', function() {

    const authorizationForm = document.getElementById('authorizationForm');
    if (authorizationForm) {
        authorizationForm.addEventListener('submit', validateAuthorizationForm);

        const procedureCodeInput = document.getElementById('procedureCode');
        if (procedureCodeInput) {
            procedureCodeInput.addEventListener('input', formatProcedureCode);
        }

        const patientAgeInput = document.getElementById('patientAge');
        if (patientAgeInput) {
            patientAgeInput.addEventListener('blur', validateAge);
        }
    }
});

function validateAuthorizationForm(event) {
    let isValid = true;
    const form = event.target;

    clearValidationErrors(form);

    const ageInput = document.getElementById('patientAge');
    const age = parseInt(ageInput.value);

    if (isNaN(age) || age < 0 || age > 150) {
        showError(ageInput, 'Por favor, informe uma idade válida entre 0 e 150 anos.');
        isValid = false;
    }

    const codeInput = document.getElementById('procedureCode');
    const code = codeInput.value.trim();

    if (code.length !== 4) {
        showError(codeInput, 'O código do procedimento deve ter exatamente 4 caracteres.');
        isValid = false;
    }

    const nameInput = document.getElementById('patientName');
    const name = nameInput.value.trim();

    if (name.length < 3) {
        showError(nameInput, 'O nome do paciente deve ter pelo menos 3 caracteres.');
        isValid = false;
    }

    const genderInput = document.getElementById('patientGender');
    if (!genderInput.value) {
        showError(genderInput, 'Por favor, selecione o sexo do paciente.');
        isValid = false;
    }

    if (!isValid) {
        event.preventDefault();

        const firstError = form.querySelector('.is-invalid');
        if (firstError) {
            firstError.focus();
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }

    return isValid;
}

function formatProcedureCode(event) {
    const input = event.target;
    input.value = input.value.toUpperCase();

    input.value = input.value.replace(/[^A-Z0-9]/g, '');
}

function validateAge(event) {
    const input = event.target;
    const age = parseInt(input.value);

    clearError(input);

    if (isNaN(age) || age < 0) {
        showError(input, 'A idade não pode ser negativa.');
    } else if (age > 150) {
        showError(input, 'Por favor, informe uma idade válida.');
    } else {
        showSuccess(input);
    }
}

function showError(input, message) {
    input.classList.add('is-invalid');
    input.classList.remove('is-valid');

    const existingFeedback = input.parentElement.querySelector('.invalid-feedback');
    if (existingFeedback) {
        existingFeedback.remove();
    }

    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = message;
    input.parentElement.appendChild(feedback);
}

function showSuccess(input) {
    input.classList.add('is-valid');
    input.classList.remove('is-invalid');

    const existingFeedback = input.parentElement.querySelector('.invalid-feedback');
    if (existingFeedback) {
        existingFeedback.remove();
    }
}

function clearError(input) {
    input.classList.remove('is-invalid', 'is-valid');

    const feedback = input.parentElement.querySelector('.invalid-feedback');
    if (feedback) {
        feedback.remove();
    }
}

function clearValidationErrors(form) {
    const invalidInputs = form.querySelectorAll('.is-invalid');
    invalidInputs.forEach(input => {
        input.classList.remove('is-invalid');
    });

    const feedbacks = form.querySelectorAll('.invalid-feedback');
    feedbacks.forEach(feedback => {
        feedback.remove();
    });
}

function numericMask(event) {
    const input = event.target;
    input.value = input.value.replace(/\D/g, '');
}

function confirmAction(message) {
    return confirm(message || 'Tem certeza que deseja continuar?');
}