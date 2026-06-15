package ni.edu.uam.api;
package ni.edu.uam.modelo.Evaluacion;

import org.openxava.jpa.XPersistence;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EvaluacionRestController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CONFIGURACIÓN DE CORS (CRÍTICO: Permite que VS Code (ej. puerto 5500) le hable a IntelliJ (puerto 8080))
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Iniciar la transacción con la base de datos
            XPersistence.getManager().getTransaction().begin();

            // 2. Capturar los datos enviados desde el HTML de VS Code
            String cedula = request.getParameter("identificacion");
            String nombre = request.getParameter("nombre");

            // 3. Crear y guardar el Candidato
            Candidato candidato = new Candidato();
            candidato.setIdentificacion(cedula);
            candidato.setNombre(nombre);
            XPersistence.getManager().persist(candidato);

            // 4. Crear la Evaluación vinculada a ese candidato
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setCandidato(candidato);
            evaluacion.iniciarEvaluacion(); // Estado: EN_PROGRESO
            XPersistence.getManager().persist(evaluacion);

            // 5. Procesar de forma iterativa las respuestas (Ejemplo para las primeras preguntas)
            // Tu JavaScript en VS Code debe enviar parámetros como: p1=A, p2=B, etc.
            for (int i = 1; i <= 20; i++) {
                String rtaSeleccionada = request.getParameter("p" + i);
                if (rtaSeleccionada != null) {
                    RespuestaDetalle detalle = new RespuestaDetalle();
                    detalle.setEvaluacion(evaluacion);

                    // Buscar la pregunta correspondiente en el catálogo
                    Pregunta pregunta = XPersistence.getManager().find(Pregunta.class, (long) i);
                    detalle.setPregunta(pregunta);
                    detalle.registrarRespuesta(rtaSeleccionada);

                    XPersistence.getManager().persist(detalle);
                }
            }

            // 6. Consolidar cambios en PostgreSQL
            XPersistence.commit();

            // Respuesta de éxito para VS Code
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\": \"success\", \"message\": \"Examen guardado. Listo para calificación.\"}");

        } catch (Exception e) {
            XPersistence.rollback(); // Cancela todo si hay un error de base de datos
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Soporte para solicitudes de pre-vuelo de navegadores (CORS Preflight)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    }
