document.addEventListener('DOMContentLoaded', () => {
    const FALLBACK_SVG =
        'data:image/svg+xml,' +
        encodeURIComponent(
            '<svg xmlns="http://www.w3.org/2000/svg" width="300" height="200">' +
            '<rect width="100%" height="100%" fill="#161629"/>' +
            '<text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" ' +
            'fill="#00e5ff" font-family="sans-serif" font-size="18">Foto Gamer</text></svg>'
        );

    // 1. Imagen de respaldo sin handlers inline (cumple CSP script-src 'self')
    document.querySelectorAll('img.js-foto-fallback').forEach((img) => {
        img.addEventListener('error', () => {
            if (img.dataset.fallbackApplied) {
                return;
            }
            img.dataset.fallbackApplied = 'true';
            img.src = FALLBACK_SVG;
        });
    });

    // 2. Copiar enlace del evento al portapapeles
    const btnCopiar = document.getElementById('btn-copiar-enlace');
    const feedbackCopiar = document.getElementById('copiar-feedback');
    if (btnCopiar) {
        btnCopiar.addEventListener('click', async () => {
            const url = btnCopiar.dataset.url || window.location.href;
            try {
                await navigator.clipboard.writeText(url);
                if (feedbackCopiar) {
                    feedbackCopiar.textContent = 'Enlace copiado al portapapeles.';
                    feedbackCopiar.classList.remove('oculto');
                }
            } catch {
                if (feedbackCopiar) {
                    feedbackCopiar.textContent = 'Copia manualmente este enlace: ' + url;
                    feedbackCopiar.classList.remove('oculto');
                }
            }
        });
    }

    // 3. Pestañas para agregar multimedia (URL vs Archivo)
    const tabBtns = document.querySelectorAll('.media-tab-btn');
    const panels = document.querySelectorAll('.media-tab-panel');
    tabBtns.forEach((btn) => {
        btn.addEventListener('click', () => {
            const targetId = btn.dataset.target;
            tabBtns.forEach((b) => b.classList.toggle('activo', b === btn));
            panels.forEach((p) => p.classList.toggle('oculto', p.id !== targetId));
        });
    });
});
