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
              //  verificarSesion();
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