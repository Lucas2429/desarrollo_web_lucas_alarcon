const validateRegion = (region) => {
    if(!region) return false;
    return true;
}

const validateComuna = (comuna) => {
    if(!comuna) return false;
    return true;
}

const validateSector = (sector) => {
    if(!sector) return false;
    let lenghtValid = sector.trim().length <= 100;
    return lenghtValid;
}

const validateNombre = (nombre) => {
    if(!nombre) return false;
    let lenghtValid = nombre.trim().length <= 200;
    return lenghtValid;
}

const validateEmail = (email) => {
    if(!email) return false;
    let lenghtValid = email.length > 15;

    let regex = /^[\w.]+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
    let formatValid = regex.test(email);
    return lenghtValid && formatValid;
}

const validateTelefono = (telefono) => {
    if(!telefono) return false;
    let lenghtValid = telefono.length >= 8;

    let regex = /^[0-9]+$/;
    let formatValid = regex.test(telefono);
    return lenghtValid && formatValid;
}

const validateContacto = (contacto) => {
    if(!contacto) return false;
    let lenghtValid = contacto.trim().length >= 4 && contacto.trim().length <= 50;
    return lenghtValid;
}

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
}

const validateTema = (tema) => {
    if(!tema) return false;
    if(tema=== "10"){
        let temaOtro = document.getElementById("temaOtro").value;
        if(!temaOtro) return false;
        let lenghtValid = temaOtro.trim().length >= 3 && temaOtro.trim().length <= 15;
        return lenghtValid;
    }
    return true;
}

const validateFoto = (foto) => {
    if(!foto) return false;
    return true;
}

const validateFormulario = () => {
    let region = document.getElementById("region").value;
    let comuna = document.getElementById("comuna").value;
    let sector = document.getElementById("sector").value;
    let nombre = document.getElementById("nombre").value;
    let email = document.getElementById("email").value;
    let telefono = document.getElementById("telefono").value;
    let contacto = document.getElementById("contacto").value;
    let inicio = document.getElementById("inicio").value;
    let termino = document.getElementById("termino").value;
    let descripcion = document.getElementById("descripcion").value;
    let tema = document.getElementById("tema").value;

    const fotos = document.querySelectorAll('[id^="foto"]');
    
    if(!validateRegion(region)) return false;
    if(!validateComuna(comuna)) return false;
    if(!validateSector(sector)) return false;
    if(!validateNombre(nombre)) return false;
    if(!validateEmail(email)) return false;
    if(!validateTelefono(telefono)) return false;
    if(!validateContacto(contacto)) return false;
    if(!validateInicio(inicio)) return false;
    if(!validateTermino(termino)) return false;
    if(!validateDescripcion(descripcion)) return false;    
    if(!validateTema(tema)) return false;

    fotos.forEach(foto => {
        if(!validateFoto(foto)) return false;
    });        

    return true;
}
