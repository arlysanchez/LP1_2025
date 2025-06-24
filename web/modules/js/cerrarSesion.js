/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
$(document).ready(function () {
 $(document).on("click", "#cerrarSesion", function () {
    console.log("Clic en cerrar sesión");  // ← Agrega esto
    CerrarSesion();
});
function CerrarSesion() {
    $.ajax({
        url: config.baseUrl + "/AuthController?action=Salir",
        type: "GET",
        dataType: "json",
         success: function (response) {
              sessionStorage.removeItem("userSession");
                Swal.fire({
                    title: "Success",
                    text: "Sesion cerrada exitosamente.",
                    icon: "success",
                    timer: 2000, // Muestra el mensaje por 2 segundos
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = "login.html";
                });
            },
        error: function (xhr, status, error) {
            console.error("Error al obtener el producto:", error);
            Swal.fire({
                title: "¡Error!",
                text: "Error al obtener el producto.",
                icon: "error"
            });
        }
    });
}
});