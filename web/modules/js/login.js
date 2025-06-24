/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



$(document).ready(function () {
   
    
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        let formData = $("#loginForm").serialize(); // En lugar de FormData
        formData += "&action=validar";

        $.ajax({
            url: config.baseUrl + "/AuthController",
            type: "POST",
            data: formData,
            contentType: "application/x-www-form-urlencoded",
            processData: false,
            dataType: "json", // Asegura que la respuesta se trate como JSON
            success: function (response) {
                console.log("Respuesta del servidor:", response);

                if (response.success) {
                    let {id_usuario, persona, ...filteredData} = response.userData;
                    let {id_persona, ...filteredPersona} = persona; // Elimina id_persona

                    // Asignar la persona sin el id_persona
                    filteredData.persona = filteredPersona;

                    sessionStorage.setItem("userSession", JSON.stringify(filteredData));
                    window.location.href = "index.html";
                } else {
                    Swal.fire({
                        title: "¡Error!",
                        text: response.message || "Usuario o contraseña incorrectos.",
                        icon: "error"
                    });
                }
            },
            error: function (xhr) {
                console.error("Error en la petición:", xhr.responseText);
                Swal.fire({
                    title: "¡Error!",
                    text: "No se pudo conectar con el servidor.",
                    icon: "error"
                });
            }
        });
    });

    $("#formPerson").submit(function (event) {
        event.preventDefault();

        let formData = $("#formPerson").serialize(); // En lugar de FormData
        formData += "&action=register";

        $.ajax({
            url: config.baseUrl + "/AuthController",
            type: "POST",
            data: formData,
            contentType: "application/x-www-form-urlencoded",
            processData: false,
            dataType: "json",
            success: function (response) {
                Swal.fire({
                    title: "Success",
                    text: "Registro guardado exitosamente.",
                    icon: "success",
                    timer: 2000, // Muestra el mensaje por 2 segundos
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = "login.html";
                });
            },
            error: function (xhr) {
                console.error("Error en la petición:", xhr.responseText);
                Swal.fire({
                    title: "¡Error!",
                    text: "No se pudo conectar con el servidor.",
                    icon: "error"
                });
            }
        });
    });
    
    
});