package EntidadesPersistencia;

import java.time.Instant;
import java.util.Date;

public class Alumno {
    private int id;
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;


    public Alumno(int id, String nombre, String apellidos, Date fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Alumno() {
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.fechaNacimiento = Date.from(Instant.now());
    }

    @Override
    public String toString() {
        return String.format("""
                ========================================
                id = %d
                
                nombre = %s
                
                apellidos = %s
                
                fechaNacimiento = %s
                ========================================""", id, nombre, apellidos, fechaNacimiento.toString());

    }
}
