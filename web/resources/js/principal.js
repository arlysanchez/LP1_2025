/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
    // Cargar el header
    fetch("header.html")
            .then(response => response.text())
            .then(data => {
                document.getElementById("header").innerHTML = data;

                // Reagregar los estilos manualmente después de cargar el header
                let cssBootstrap = document.createElement("link");
                cssBootstrap.rel = "stylesheet";
                cssBootstrap.href = "resources/css/bootstrap.min.css";
                document.head.appendChild(cssBootstrap);

                let cssStyle = document.createElement("link");
                cssStyle.rel = "stylesheet";
                cssStyle.href = "resources/css/style.css";
                document.head.appendChild(cssStyle);

                let cssIcons = document.createElement("link");
                cssIcons.rel = "stylesheet";
                cssIcons.href = "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css";
                document.head.appendChild(cssIcons);
               verificarSesion();
            });

    // Cargar el footer
    fetch("footer.html")
            .then(response => response.text())
            .then(data => {
                document.getElementById("footer").innerHTML = data;

                // Reagregar los scripts después de cargar el footer
                let scriptBootstrap = document.createElement("script");
                scriptBootstrap.src = "resources/js/bootstrap.bundle.min.js";
                document.body.appendChild(scriptBootstrap);

                let scriptIcons = document.createElement("script");
                scriptIcons.src = "resources/js/iconos.js";
                document.body.appendChild(scriptIcons);

            });
});

function verificarSesion() {
    let userSession = sessionStorage.getItem("userSession");

    if (userSession) {
        let userData = JSON.parse(userSession);
        let loginButton = document.getElementById("loginBtn"); // Seleccionar el botón de login

        if (loginButton) {
            loginButton.style.display = "none"; // Ocultar el botón de iniciar sesión

            let authContainer = loginButton.parentElement; // Obtener el contenedor del botón

            // Crear el dropdown de usuario
            let userDropdown = document.createElement("div");
            userDropdown.classList.add("dropdown", "mx-2");
            userDropdown.innerHTML = `
                <button style="border: none;" class="nav-link dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                    ${userData.usuario}
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item text-center">
                        <img src="resources/img/avatar.jpg" alt="Avatar" width="60"/>
                    </a></li>
                    <li><a class="dropdown-item text-center">${userData.persona.nombre}</a></li>
                    <li><a class="dropdown-item text-center">${userData.rol}</a></li>
                    <div class="dropdown-divider"></div>
                    <li><a class="dropdown-item">Mis compras</a></li>
                    <li><a class="dropdown-item" id="logoutBtn"><i class="fa-solid fa-arrow-left"></i> Salir</a></li>
                </ul>
            `;

            // Insertar el dropdown al lado del botón de iniciar sesión
            authContainer.appendChild(userDropdown);

            // Agregar evento al botón de cerrar sesión
            document.getElementById("logoutBtn").addEventListener("click", cerrarSesion);
        }
    }
}
// ✅ Función para cerrar sesión
function cerrarSesion() {
    fetch("AuthController", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "action=Salir"
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            sessionStorage.removeItem("userSession"); // Eliminar sesión local
            window.location.href = "index.html"; // Redirigir a la página de inicio
        } else {
            alert("Error al cerrar sesión: " + data.message);
        }
    })
    .catch(error => console.error("Error en la solicitud de cierre de sesión:", error));
}