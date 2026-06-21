const banco = [
    { n: 51, img: "51.png", opts: [18, 10, 13, 15, 12], ans: "E" },
    { n: 52, img: "52.png", opts: [19, 18, 20, 22, 14], ans: "B" },
    { n: 53, img: "53.png", opts: [24, 21, 17, 19, 23], ans: "A" },
    { n: 54, img: "54.png", opts: [20, 18, 17, 22, 19], ans: "E" },
    { n: 55, img: "55.png", opts: [15, 20, 14, 17, 18], ans: "B" },
    { n: 56, img: "56.png", opts: [16, 18, 13, 16, 23], ans: "D" },
    { n: 57, img: "57.png", opts: [13, 19, 12, 18, 15], ans: "C" },
    { n: 58, img: "58.png", opts: [24, 14, 13, 18, 17], ans: "E" },
    { n: 59, img: "59.png", opts: [21, 22, 19, 25, 15], ans: "B" },
    { n: 60, img: "60.png", opts: [18, 21, 23, 19, 20], ans: "A" },
    { n: 61, img: "61.png", opts: [14, 12, 13, 11, 15], ans: "C" },
    { n: 62, img: "62.png", opts: [19, 15, 13, 16, 17], ans: "E" },
    { n: 63, img: "63.png", opts: [14, 20, 22, 24, 19], ans: "A" },
    { n: 64, img: "64.png", opts: [34, 28, 30, 36, 32], ans: "C" },
    { n: 65, img: "65.png", opts: [15, 41, 20, 35, 38], ans: "D" }
];

let index = 0;
let tiempo = 210; 
let timer;
let respuestasUsuario = new Array(banco.length).fill("");
let evaluacionTerminada = false;
let terminoPrueba = false;

// Control del Switch de Modo Claro / Oscuro moderno
document.getElementById('theme-checkbox').addEventListener('change', (e) => {
    document.documentElement.setAttribute('data-theme', e.target.checked ? 'dark' : 'light');
});

// Enviar Registro actualizando las credenciales visuales superiores e inferiores
document.getElementById('form-registro').onsubmit = (e) => {
    e.preventDefault();

    const nombreCap = document.getElementById('nombre').value;
    const cifCap = document.getElementById('cif').value;
    
    if(nombreCap === "" || cifCap === ""){

        Swal.fire({

            icon: 'warning',

            title: 'Datos incompletos',

            text: 'Por favor ingrese su nombre completo y su identificación.',

            confirmButtonColor: '#26C3D7'

        });
        return;
    }
    // Actualizar información del usuario
    document.getElementById('sb-user-name').innerText = nombreCap;
    document.getElementById('profile-name').innerText = nombreCap;
    document.getElementById('profile-id').innerText = cifCap;

    Swal.fire({

        icon: 'success',

        title: `¡Bienvenido, ${nombreCap}!`,

        text: 'Sus datos fueron ingresados correctamente. Está listo para iniciar la evaluación psicotécnica.',

        confirmButtonText: 'Continuar',

        confirmButtonColor: '#26C3D7',

        timer: 4000,

        timerProgressBar: true

    }).then(() => {


        cambiarPantalla(
            'screen-login',
            'screen-instructions'
        );
    });

};

// Empezar Test Oficial
document.getElementById('btn-start-test').onclick = () => {
    cambiarPantalla('screen-instructions', 'screen-test');
    inicializarMatrizNavegacion();
    cargarPregunta();
    startTimer();
};

function cambiarPantalla(sale, entra) {
    const s = document.getElementById(sale);
    const e = document.getElementById(entra);
    s.classList.remove('active');
    s.classList.add('hidden');
    e.classList.remove('hidden');
    e.classList.add('active');

    // Sincronizar el estado del menú lateral izquierdo
    actualizarSidebarActivo(entra);
}

// Navegación directa por barra lateral (Control de seguridad)
function navegarA(pantallaId) {
    if(
        pantallaId === 'screen-results' &&
        !terminoPrueba
    ){

        alert(
        "Aún no has completado la evaluación."
        );

        return;

    }
    // Si está haciendo el test, bloquear salidas accidentales desde la barra lateral
    if(!document.getElementById('screen-test').classList.contains('hidden') && pantallaId !== 'screen-test') {
        if(!confirm("¿Seguro que deseas abandonar la prueba en curso? Perderás tu progreso.")) return;
        clearInterval(timer);
    }
    
    const pantallas = ['screen-login', 'screen-instructions', 'screen-test', 'screen-results'];
    pantallas.forEach(p => {
        document.getElementById(p).classList.remove('active');
        document.getElementById(p).classList.add('hidden');
    });
    
    document.getElementById(pantallaId).classList.remove('hidden');
    document.getElementById(pantallaId).classList.add('active');
    actualizarSidebarActivo(pantallaId);
}

function actualizarSidebarActivo(idActivo) {
    document.querySelectorAll('.sidebar-item').forEach(btn => btn.classList.remove('active'));
    if (idActivo === 'screen-login' || idActivo === 'screen-instructions' || idActivo === 'screen-test') {
        document.querySelectorAll('.sidebar-item')[0].classList.add('active'); // Inicio
    } else if (idActivo === 'screen-results') {
        document.getElementById('sidebar-lnk-resultados').classList.add('active'); // Resultados
    }
}

// Ventana Modal de Perfil de usuario
function toggleProfileModal(){
    document
    .getElementById('profile-modal')
    .classList.toggle('hidden');
}

function cambiarAccesibilidad(size, boton) {

    document.body.classList.remove(
        'font-small',
        'font-medium',
        'font-large'
    );

    document.body.classList.add(`font-${size}`);

    document.querySelectorAll('.size-buttons button')
    .forEach(b => b.classList.remove('active'));

    boton.classList.add('active');
}

// Generar dinámicamente los botones del menú lateral derecho
function inicializarMatrizNavegacion() {
    const matrixContainer = document.getElementById('nav-matrix');
    matrixContainer.innerHTML = "";
    
    banco.forEach((q, i) => {
        const btn = document.createElement('button');
        btn.className = "nav-matrix-btn";
        btn.id = `nav-item-${i}`;
        btn.innerText = q.n;
        
        btn.onclick = () => {
            index = i;
            cargarPregunta();
        };
        matrixContainer.appendChild(btn);
    });
}

// Renderizado de Preguntas
function cargarPregunta() {
    const q = banco[index];
    document.getElementById('num-ejercicio').innerText = `Pregunta -${q.n}-`;
    document.getElementById('img-cubos').src = q.img;
    
    const box = document.getElementById('box-opciones');
    box.innerHTML = "";
    
    const letras = ['A', 'B', 'C', 'D', 'E'];
    letras.forEach((letra, i) => {
        const btn = document.createElement('button');
        btn.className = "opt-btn";
        btn.innerHTML = `<strong>${letra}</strong> <span>${q.opts[i]}</span>`;
        
        if (respuestasUsuario[index] === letra) {
            btn.classList.add('selected');
        }
        
        btn.onclick = () => {
            document.querySelectorAll('.opt-btn').forEach(b => b.classList.remove('selected'));
            btn.classList.add('selected');
            respuestasUsuario[index] = letra;
            actualizarInterfazNavegacion();
        };
        box.appendChild(btn);
    });

    actualizarInterfazNavegacion();
    actualizarBarraProgreso();
    
    if (index === banco.length - 1) {
        document.getElementById('btn-next').innerText = "FINALIZAR EVALUACIÓN";
    } else {
        document.getElementById('btn-next').innerText = "SIGUIENTE PREGUNTA";
    }

    const btnPrev = document.getElementById('btn-prev');
    if (btnPrev) {
        btnPrev.style.visibility = (index === 0) ? "hidden" : "visible";
    }
}

function actualizarInterfazNavegacion() {
    banco.forEach((_, i) => {
        const cell = document.getElementById(`nav-item-${i}`);
        if (!cell) return;
        
        cell.className = "nav-matrix-btn";
        if (respuestasUsuario[i] !== "") cell.classList.add('answered');
        if (i === index) cell.classList.add('current');
    });
}

function actualizarBarraProgreso() {
    const respondidas = respuestasUsuario.filter(r => r !== "").length;
    document.getElementById('progress-fill').style.width = `${(respondidas / banco.length) * 100}%`;
}

// Acciones de los botones inferiores
document.getElementById('btn-next').onclick = () => {
    if (index < banco.length - 1) {
        index++;
        cargarPregunta();
    } else {
        finalizar();
    }
};

document.getElementById('btn-prev').onclick = () => {
    if (index > 0) {
        index--;
        cargarPregunta();
    }
};

function startTimer() {
    timer = setInterval(() => {
        tiempo--;
        let m = Math.floor(tiempo / 60);
        let s = tiempo % 60;
        document.getElementById('cronometro').innerText = `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
        if (tiempo <= 0) finalizar();
    }, 1000);
}

function finalizar() {
    evaluacionTerminada = true;
    
    clearInterval(timer);
    cambiarPantalla('screen-test', 'screen-results');
    
    let aciertos = 0;
    banco.forEach((q, i) => {
        if (respuestasUsuario[i] === q.ans) aciertos++;
    });

    document.getElementById('res-nombre').innerText = document.getElementById('nombre').value;
    document.getElementById('puntos').innerText = aciertos;
    document.getElementById('total-q').innerText = `/ ${banco.length}`;
    
    let porcentaje = (aciertos / banco.length) * 100;
    let msg = "";
    if (porcentaje >= 80) msg = "Nivel superior en procesamiento visoespacial y análisis relacional abstracta.";
    else if (porcentaje >= 50) msg = "Rendimiento promedio óptimo. Estructuración métrica espacial adecuada.";
    else msg = "Desempeño bajo el promedio estándar. Se recomienda estimulación analítica bidimensional.";
    
    document.getElementById('mensaje-r').innerText = msg;
}

function toggleSidebar(){

    const sidebar=document.querySelector('.sidebar');
    const button=document.querySelector('.sidebar-toggle');

    sidebar.classList.toggle('collapsed');


    if(sidebar.classList.contains('collapsed')){

        button.style.left="70px";

    }else{

        button.style.left="250px";

    }

}

function irResultados(){

    if(!evaluacionTerminada){

        Swal.fire({

            icon: "info",

            title: "Resultados no disponibles",

            text: "Debes completar la evaluación psicotécnica antes de consultar tus resultados.",

            confirmButtonText: "Entendido",

            confirmButtonColor: "#26C3D7"

        });

        return;
    }

    navegarA('screen-results');
}