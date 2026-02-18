let EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

window.onload = function () {

  let form = document.querySelector(".cmp-form-usuario form");
  let emailError = document.querySelector('[data-error="email"]');

  // Cuando se envía el formulario
  form.addEventListener("submit", function (event) {
    event.preventDefault(); // Evita que se recargue la página

    // Cogemos los valores del formulario
    let nombre = form.elements.nombre.value.trim();
    let apellidos = form.elements.apellidos.value.trim();
    let email = form.elements.email.value.trim();

    // Validar nombre
    if (nombre === "") {
      alert("El nombre es obligatorio");
      return;
    }

    // Validar apellidos
    if (apellidos === "") {
      alert("Los apellidos son obligatorios");
      return;
    }

    // Validar email con la expresión regular
    if (!EMAIL_REGEX.test(email)) {
      emailError.textContent = "Por favor, introduce un email válido.";
      return;
    }

    // Si todo es correcto, limpiamos el mensaje de error
    emailError.textContent = "";

    // Mostramos los datos en consola
    console.log("Datos del formulario:", {
      nombre: nombre,
      apellidos: apellidos,
      email: email
    });
  });
};