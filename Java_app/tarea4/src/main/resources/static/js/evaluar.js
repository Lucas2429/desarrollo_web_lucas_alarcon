function evaluarActividad(actividadId) {
    const nota = prompt("Ingrese una nota entre 1 y 7:");
    if (nota === null) return;

    const valor = parseInt(nota);
    if (isNaN(valor) || valor < 1 || valor > 7) {
        alert("La nota debe ser un número entero entre 1 y 7.");
        return;
    }

    fetch('/api/evaluar/' + actividadId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nota: valor })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al guardar la nota");
        }
        return response.json();
    })
    .then(data => {
        // Actualizar nota en la tabla
        const fila = document.querySelector(`#actividad-${actividadId}`);
        if (fila) {
            fila.querySelector('.nota').innerText = data.promedio;
        }
    })
    .catch(error => {
        console.error(error);
        alert("Hubo un error al evaluar la actividad.");
    });
}

