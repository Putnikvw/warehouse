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
    if (e.target.id === 'product-table') {
        const currentPage = e.detail.xhr.getResponseHeader('X-Current-Page');
        const addBtn = document.getElementById('addProductBtn');

        // Enable Add button only if table is loaded AND current page is 1
        if (currentPage === '0') {
            addBtn.disabled = false;
            addBtn.style.display = 'inline-block';
        } else {
            addBtn.disabled = true;
            addBtn.style.display = 'none';
        }
    }

    //generate by AI for make code more flexible and remove duplicates

    // if (e.target.id === 'product-table') {
    //     const currentPage = e.detail.xhr.getResponseHeader('X-Current-Page');
    //     const addBtn = document.getElementById('addProductBtn');
    //     const srhBtn = document.getElementById('searchProductBtn');
    //
    //     const isFirstPage = currentPage === '0';
    //     const displayStyle = isFirstPage ? 'inline-block' : 'none';
    //
    //     [addBtn, srhBtn].forEach(btn => {
    //         btn.disabled = !isFirstPage;
    //         btn.style.display = displayStyle;
    //     });
    // }
});

document.body.addEventListener('htmx:afterRequest', function(event) {
    if (event.detail.successful) {
        bootstrap.Modal.getInstance(document.getElementById('productModal')).hide();
    }
});

// Show Alerts
document.body.addEventListener('showError', function(event) {
    // Create Bootstrap alert
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3';
    alertDiv.style.zIndex = '9999';
    alertDiv.innerHTML = `
            ${event.detail.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
    document.body.appendChild(alertDiv);

    // Auto remove after 5 seconds
    setTimeout(() => alertDiv.remove(), 5000);
});