const validateRegion = (region) => {
    if(!region) return false;
    return true;
};

const validateComuna = (comuna) => {
    if(!comuna) return false;
    return true;
};

const validateSector = (sector) => {
    if(!sector) return false;
    let lenghtValid = sector.trim().length <= 100;
    return lenghtValid;
};

const validateNombre = (nombre) => {
    if(!nombre) return false;
    let lenghtValid = nombre.trim().length <= 200;
    return lenghtValid;
};

const validateEmail = (email) => {
    if(!email) return false;
    let lenghtValid = email.length > 15;

    let regex = /^[\w.]+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
    let formatValid = regex.test(email);
    return lenghtValid && formatValid;
};

const validateTelefono = (telefono) => {
    if(!telefono) return false;
    let lenghtValid = telefono.length >= 8;

    let regex = /^[0-9]+$/;
    let formatValid = regex.test(telefono);
    return lenghtValid && formatValid;
};

const validateContacto = (contacto) => {
    if(!contacto) return false;
    let lenghtValid = contacto.trim().length >= 4 && contacto.trim().length <= 50;
    return lenghtValid;
};

const validateInicio = (inicio) => {
    if (!inicio) return false;

    // Expresión regular para validar el formato YYYY-MM-DDTHH:mm
    const regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;
    const formatValid = regex.test(inicio);

    return formatValid;
};

const validateTermino = (termino) => {
    if (!termino) return false;

    // Expresión regular para validar el formato YYYY-MM-DDTHH:mm
    const regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;
    const formatValid = regex.test(termino);

    let inicio = document.getElementById("inicio").value;

    const timeValid = inicio < termino; // Validar que la fecha de término sea mayor que la de inicio

    return formatValid && timeValid;
};

const validateDescripcion = (descripcion) => {
    if(!descripcion) return false;
    let lenghtValid = descripcion.length <= 500;
    return lenghtValid;
};

const validateTema = (tema) => {
    if(!tema) return false;
    if(tema=== "10"){
        let otroTema = document.getElementById("otroTema").value;
        if(!otroTema) return false;
        let lenghtValid = otroTema.trim().length >= 3 && otroTema.trim().length <= 15;
        return lenghtValid;
    }
    return true;
};

const validateFotos = (fotos) => {
    if (!fotos) return false;
  
    // validación del número de archivos
    let lengthValid = 1 <= fotos.length && fotos.length <= 5;
  
    // validación del tipo de archivo
    let typeValid = true;
  
    for (const foto of fotos) {
      // el tipo de archivo debe ser "image/<foo>"
      let fileFamily = foto.files[0].type.split("/")[0];
      typeValid &&= fileFamily == "image" || foto.files[0].type == "application/pdf";
    }
  
    // devolvemos la lógica AND de las validaciones.
    return lengthValid && typeValid;
};

const validateFormulario = () => {
    let formulario = document.forms["formularioActividad"];
    let region = formulario["region"].value;
    let comuna = formulario["comuna"].value;
    let sector = formulario["sector"].value;
    let nombre = formulario["nombre"].value;
    let email = formulario["email"].value;
    let telefono = formulario["telefono"].value;
    let contacto = formulario["contacto"].value;
    let inicio = formulario["inicio"].value;
    let termino = formulario["termino"].value;
    let descripcion = formulario["descripcion"].value;
    let tema = formulario["tema"].value;

    let fotos = document.querySelectorAll('[id^="foto"]');
    let foto = document.getElementById("foto").files[0];

    let myForm = document.getElementById("myForm");
    
    let invalidInputs = [];
    let isValid = true;

    const setInvalidInput = (input) => {
        invalidInputs.push(input);
        isValid &&= false;
    }

    if(!validateRegion(region)) {
        console.log(region);
        setInvalidInput("region");
    }
    if(!validateComuna(comuna)) {
        setInvalidInput("comuna");
    }
    if(!validateSector(sector)) {
        setInvalidInput("sector");
    }
    if(!validateNombre(nombre)) {
        setInvalidInput("nombre");
    }
    if(!validateEmail(email)) {
        setInvalidInput("email");
    }  
    if(!validateTelefono(telefono)) {
        setInvalidInput("telefono");
    }
    if(!validateContacto(contacto)) {
        setInvalidInput("contacto");
    }
    if(!validateInicio(inicio)) {
        setInvalidInput("inicio");
    }
    if(!validateTermino(termino)) {
        setInvalidInput("termino");
    }
    if(!validateDescripcion(descripcion)) {
        setInvalidInput("descripcion");
    }
    if(!validateTema(tema)) {
        setInvalidInput("tema");
    }

    if(!validateFotos(fotos)) {
        setInvalidInput("fotos");
    }   
    
    // finalmente mostrar la validación
    let validationBox = document.getElementById("val-box");
    let validationMessageElem = document.getElementById("val-msg");
    let validationListElem = document.getElementById("val-list");
    let botonAgregar = document.getElementById("submit-btn");
    
    if (!isValid) {
        console.log("Formulario inválido");
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
    } else {
        console.log("Formulario válido");
        // Ocultar el formulario
        formulario.style.display = "none";
        botonAgregar.style.display = "none";
    
        // establecer mensaje de éxito
        validationMessageElem.innerText = "¡Formulario válido! ¿Deseas enviarlo o volver?";
        validationListElem.textContent = "";
    
        // aplicar estilos de éxito
        validationBox.style.backgroundColor = "#ddffdd";
        validationBox.style.borderLeftColor = "#4CAF50";
    
        // Agregar botones para enviar el formulario o volver
        let submitButton = document.createElement("button");
        submitButton.innerText = "Enviar";
        submitButton.style.marginRight = "10px";
        submitButton.type = "submit";
        submitButton.addEventListener("click", () => {
          myForm.submit();
        });
    
        let backButton = document.createElement("button");
        backButton.innerText = "Volver";
        backButton.addEventListener("click", () => {
          // Mostrar el formulario nuevamente
          formulario.style.display = "block";
          botonAgregar.style.display = "block";
          validationBox.hidden = true;
        });
    
        validationListElem.appendChild(submitButton);
        validationListElem.appendChild(backButton);
    
        // hacer visible el mensaje de validación
        validationBox.hidden = false;
    }  
};

let submitButton = document.getElementById("submit-btn");
submitButton.addEventListener("click", validateFormulario);


