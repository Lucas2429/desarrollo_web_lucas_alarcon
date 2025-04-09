const mostrarActividad = (id) => {
    let actividad = document.getElementById(id);
    if (actividad.style.display === "none") {
        actividad.style.display = "block";
    } else {
        actividad.style.display = "none";
    }
}

const mostrarImagen = (src) => {
    let mainContainer = document.getElementById("containerActividades");
    let showImageDiv = document.getElementById("mostrarImagenDiv");
    let image = document.getElementById("image");

    image.src = src;
    showImageDiv.style.display  = "block";
    mainContainer.style.display = "none";

}
const cerrarImagen = () => {
    let mainContainer = document.getElementById("containerActividades");
    let showImageDiv = document.getElementById("mostrarImagenDiv");
    let image = document.getElementById("image");

    image.src = "";
    showImageDiv.style.display  = "none";
    mainContainer.style.display = "block";
}

window.onload = () => {
    let showImageDiv = document.getElementById("mostrarImagenDiv");
    showImageDiv.style.display  = "none";
}