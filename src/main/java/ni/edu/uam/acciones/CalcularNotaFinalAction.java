package ni.edu.uam.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.modelo.*;

public class CalcularNotaFinalAction extends ViewBaseAction {

    private String estadoProceso;

    @Override
    public void execute() throws Exception {
        this.estadoProceso = "INICIANDO_CALCULO";

        // 1. Obtener la evaluación actual de la pantalla
        Evaluacion evaluacion = (Evaluacion) getView().getEntity();

        if (evaluacion == null || evaluacion.getRespuestas() == null) {
            addError("No hay datos de evaluación para calcular.");
            return;
        }

        // 2. Ejecutar la lógica de calificación dinámica
        int puntajeCalculado = ejecutarBaremacion(evaluacion);

        // 3. Actualizar la entidad con las variables reales de María Celeste
        evaluacion.setPuntajeTotal(puntajeCalculado);

        // 4. Guardar los cambios de manera limpia en PostgreSQL
        XPersistence.getManager().merge(evaluacion);

        // 5. Refrescar la vista del navegador web automáticamente
        getView().setValue("puntajeTotal", puntajeCalculado);

        this.estadoProceso = "CALCULO_FINALIZADO";
        addMessage("Baremación automatizada completada. Puntaje Final: " + puntajeCalculado);
    }

    // Método que recorre las respuestas, compara con la correcta y califica
    public int ejecutarBaremacion(Evaluacion evaluacion) {
        int acumulado = 0;

        for (RespuestaDetalle detalle : evaluacion.getRespuestas()) {
            if (detalle.getPregunta() != null && detalle.getLetraSeleccionada() != null) {

                // Extraemos el valor del enum como texto (.name()) para compararlo
                String correcta = detalle.getPregunta().getRespuestaCorrecta() != null
                        ? detalle.getPregunta().getRespuestaCorrecta().name()
                        : null;

                boolean esCorrecta = correcta != null && correcta.equalsIgnoreCase(detalle.getLetraSeleccionada());

                // Guarda el resultado (vuelve el check VERDE si coincide)
                detalle.setEsCorrecta(esCorrecta);

                if (esCorrecta) {
                    acumulado += 1;
                }
            }
        }
        return acumulado;
    }
}
