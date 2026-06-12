package ni.edu.uam.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.modelo.Evaluacion;
import ni.edu.uam.modelo.RespuestaDetalle;

public class CalcularNotaFinalAction extends ViewBaseAction {

    // Variables
    private String estadoProceso;

    @Override
    public void execute() throws Exception {
        this.estadoProceso = "INICIANDO_CALCULO";

        // 1. Obtener la evaluación actual que está en pantalla
        Evaluacion evaluacion = (Evaluacion) getView().getEntity();

        if (evaluacion == null || evaluacion.getRespuestas() == null) {
            addError("No hay datos de evaluación para calcular.");
            return;
        }

        // 2. Ejecutar baremación (lógica del UML)
        double puntajeTotal = ejecutarBaremacion(evaluacion);

        // 3. Actualizar la entidad
        evaluacion.setNotaFinal(puntajeTotal);
        evaluacion.finalizarEvaluacion(); // Cambia estado a "FINALIZADA"

        // 4. Guardar en la base de datos (PostgreSQL/H-SQL)
        XPersistence.getManager().merge(evaluacion);

        // 5. Refrescar la vista en el navegador web
        getView().setValue("notaFinal", puntajeTotal);
        getView().setValue("estado", evaluacion.getEstado());

        this.estadoProceso = "CALCULO_FINALIZADO";
        addMessage("Baremación automatizada completada. Puntaje Final: " + puntajeTotal);
    }

    // Método definido en tu UML: ejecutarBaremacion(long)
    // Aquí se pasa el objeto para evitar múltiples queries a la BD
    public double ejecutarBaremacion(Evaluacion evaluacion) {
        double acumulado = 0.0;

        for (RespuestaDetalle detalle : evaluacion.getRespuestas()) {
            if (detalle.getPregunta() != null && detalle.getRespuestaSeleccionada() != null) {

                // Verifica usando el método de Pregunta
                boolean correcta = detalle.getPregunta().verificarRespuestaCorrecta(detalle.getRespuestaSeleccionada());

                // Marca el detalle y suma puntaje
                detalle.marcarComoCorrecta(correcta);
                if (correcta) {
                    acumulado += detalle.getPregunta().getPuntajeBase();
                }
            }
        }
        return acumulado;
    }
}
