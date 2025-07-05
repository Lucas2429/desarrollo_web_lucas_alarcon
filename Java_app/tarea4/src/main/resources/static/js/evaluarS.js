function evaluar(actividadId) {
    let nota = prompt("Ingresa una nota entre 1 y 7:");

    if (!nota) return; // cancelado
    nota = parseInt(nota);

    if (isNaN(nota) || nota < 1 || nota > 7) {
        alert("La nota debe ser un número entero entre 1 y 7.");
        return;
    }

    // Cambia la celda correspondiente
    document.getElementById("nota-" + actividadId).innerText = nota;
}