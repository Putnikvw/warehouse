// app.js
document.addEventListener('htmx:afterSwap', (e) => {
    // Open modal after HTMX loads it into the page
    if (e.target.id === 'modal-container') {
        const modalEl = document.getElementById('productModal');
        if (modalEl) {
            const modal = new bootstrap.Modal(modalEl, {
                backdrop: true,
                keyboard: true // ESC key closes modal
            });
            modal.show();
        }
    }
});

// Listen for custom event from server to close modal
document.body.addEventListener('closeModal', () => {
    const modalEl = document.getElementById('productModal');
    if (modalEl) {
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) {
            modal.hide();
        }
    }
});
