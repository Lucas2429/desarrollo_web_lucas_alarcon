document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".eliminar-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            const id = btn.getAttribute("data-id");
            const wrapper = document.getElementById("motivo-wrapper-" + id);
            wrapper.style.display = "inline"; 
            btn.style.display = "none";
        });
    });
});

