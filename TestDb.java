import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDb {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_proyecto_final", "root", "Pikachu_21");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.usuario, r.nombre FROM perfil p JOIN rol r ON p.rol_id = r.rol_id");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("usuario") + ", Role: " + rs.getString("nombre"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
