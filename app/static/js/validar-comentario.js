const validateNombre = (nombre) => {
    if(!nombre) return false;
    let lenghtValid = nombre.trim().length >= 3 && nombre.trim().length <= 80;
    return lenghtValid;
};

const validateComentario = (comentario) => {
    if(!comentario) return false;
    let lenghtValid =  comentario.trim().length >= 5;
    return lenghtValid;
};

const validateComentarioForm = async (actividad) => {
    console.log("Actividad:", actividad);
    let formulario = document.getElementById(`formulario-comentario${actividad}`);
    let nombre = document.getElementById(`usuario${actividad}`).value;
    console.log("nombre:", nombre);
    let comentario = document.getElementById(`comentario${actividad}`).value; 
    console.log("comentario:", comentario);  
    let actividadId = document.getElementById(`actividad_id${actividad}`).value;
    console.log("id:", actividadId);


    let invalidInputs = [];
    let isValid = true;

    const setInvalidInput = (input) => {
        invalidInputs.push(input);
        isValid &&= false;
    }

    if (!validateNombre(nombre)) {
        setInvalidInput("nombre");
    }

    if (!validateComentario(comentario)) {
        console.log(comentario);
        setInvalidInput("comentario");
    }

    let validationBox = document.getElementById(`val-box${actividad}`);
    let validationMessageElem = document.getElementById(`val-msg${actividad}`);
    let validationListElem = document.getElementById(`val-list${actividad}`);

    if (!isValid) {
        validationListElem.textContent = "";
        // agregar elementos inválidos al elemento val-list.
        for (const input of invalidInputs) {
          let listElement = document.createElement("li");
          listElement.innerText = input;
          validationListElem.append(listElement);
        }
        // establecer val-msg
        validationMessageElem.innerText = "Los siguientes campos son inválidos:";
    
        // aplicar estilos de error
        validationBox.style.backgroundColor = "#ffdddd";
        validationBox.style.borderLeftColor = "#f44336";
    
        // hacer visible el mensaje de validación
        validationBox.hidden = false;
        return;

    } 

    const formData = new FormData();
    formData.append("usuario", nombre);
    formData.append("comentario", comentario);
    formData.append("actividad_id", actividadId);

    try {
        const response = await fetch("/post_comentario", {
            method: "POST",
            body: formData
        });

        const result = await response.json();
        console.log("respuesta:", response);
        console.log("result:", result);

        if (response.ok) {
            validationMessageElem.innerText = result.message;
            validationBox.style.backgroundColor = "#ddffdd";
            validationBox.style.borderLeftColor = "#4CAF50";
            validationBox.hidden = false;
            validationListElem.innerHTML = "";

            // Opcional: limpiar el formulario
            formulario.reset();
        } else {
            validationMessageElem.innerText = result.error || "Error desconocido";
            validationBox.style.backgroundColor = "#ffdddd";
            validationBox.style.borderLeftColor = "#f44336";
            validationBox.hidden = false;
            validationListElem.innerHTML = "";
        }
    } catch (error) {
        validationMessageElem.innerText = "Error al conectar con el servidor.";
        validationBox.style.backgroundColor = "#ffdddd";
        validationBox.style.borderLeftColor = "#f44336";
        validationBox.hidden = false;
        validationListElem.innerHTML = "";
    }
};