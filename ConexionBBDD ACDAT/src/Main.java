import EntidadesPersistencia.Profesor;

import EntidadesPersistencia.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {

    static BusinessLogic bl = new BusinessLogic();

    public static void main(String[] args) {


        //System.out.println("Se elminiaron " + gestion.deleteTables() + " tablas");
        List lista;
       bl.crearTablas();
        try {
            lista = bl.listarTabla("Alumnos");
            for (Object objecto : lista) {
                System.out.println(objecto.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }





    }


}

