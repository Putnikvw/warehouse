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

function updateIndices() {
    const rows = document.querySelectorAll('#product-items > .product-item');

    rows.forEach((row, index) => {
        // loop through all inputs in the row
        row.querySelectorAll('input').forEach(input => {
            if (input.name) {
                // always rebuild the name based on the field
                const field = input.name.substring(input.name.lastIndexOf('.') + 1);
                input.name = `productItems[${index}].${field}`;
            }
        });

        // also give each row a unique id for removing
        row.id = `row-${index}`;
    });
}

function resetProductItems() {
    document.getElementById('product-items').innerHTML = '';
}