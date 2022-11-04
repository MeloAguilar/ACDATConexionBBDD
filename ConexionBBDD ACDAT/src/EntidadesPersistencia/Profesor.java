package EntidadesPersistencia;

import javax.print.DocFlavor;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Profesor {

    //Atributos
    private int id;
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;
    private int antiguedad;
    //End Atributos

    //Constructores
    public Profesor(int _id, String _nombre, String _apellidos, Date _fechaNac, int _antiguedad){
        this.id = _id;
        this.nombre = _nombre;
        this.apellidos = _apellidos;
        this.fechaNacimiento = _fechaNac;
        this.antiguedad = _antiguedad;
    }

    public Profesor() {
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.fechaNacimiento = Date.from(Instant.now());
        this.antiguedad = 0;
    }
//End Constructores


    @Override
    public String toString() {
        return String.format("""
                ========================================
                id = %d
                
                nombre = %s
                
                apellidos = %s
                
                fechaNacimiento = %s
                
                antigüedad = %d
                ========================================""", id, nombre, apellidos, fechaNacimiento.toString(), antiguedad);
    }
}
