
document.addEventListener('keyup', (e) => {
    if (e.key == 'PrintScreen') {
        navigator.clipboard.writeText('');
    }
});

document.addEventListener('keydown', (e) => {
    if (e.ctrlKey && e.key == 'p') {
        e.cancelBubble = true;
        e.preventDefault();
        e.stopImmediatePropagation();
    }
});