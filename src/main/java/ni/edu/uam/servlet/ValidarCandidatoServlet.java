package ni.edu.uam.servlet;

import org.openxava.util.DataSourceConnectionProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/api/validar-candidato")
public class ValidarCandidatoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cabeceras de seguridad y formato
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String cif = request.getParameter("cif");
        boolean existe = false;

        PrintWriter out = response.getWriter();

        if (nombre != null && cif != null) {

            // 1. Forzar la carga del driver de PostgreSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se encontró el Driver de PostgreSQL: " + e.getMessage() + "\"}");
                return;
            }

            // 2. Datos de conexión nativa (Ajusta el nombre de tu base de datos, usuario y contraseña)
            // Reemplaza 'tu_base_de_datos' por el nombre real en tu pgAdmin (Ej. 'proyectofinal' o 'openxava_db')
            String urlConexion = "jdbc:postgresql://localhost:5432/ProyectoFinalIIICorteDB";
            String usuarioDB = "postgres";
            String contrasenaDB = "1234"; // Pon aquí la contraseña que usas para entrar a pgAdmin

            // SQL adaptado a tu tabla y columnas en minúsculas
            String sql = "SELECT COUNT(*) FROM candidato WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?)) AND TRIM(identificacion) = TRIM(?)";

            // 3. Conexión clásica directa
            try (Connection con = java.sql.DriverManager.getConnection(urlConexion, usuarioDB, contrasenaDB);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, nombre);
                ps.setString(2, cif);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        existe = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error en conexión nativa JDBC: " + e.getMessage() + "\"}");
                return;
            }
        }

        // Enviar respuesta final en JSON limpio
        out.print("{\"registrado\": " + existe + "}");
        out.flush();
    }
}
