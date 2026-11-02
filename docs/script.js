document.querySelectorAll('[data-copy]').forEach(button => {
  button.addEventListener('click', async () => {
    const field = document.getElementById(button.dataset.copy);
    const status = button.nextElementSibling;
    try { await navigator.clipboard.writeText(field.value); status.textContent = 'Copied.'; }
    catch { field.focus(); field.select(); status.textContent = 'Selected. Press Ctrl+C or use Copy.'; }
  });
});