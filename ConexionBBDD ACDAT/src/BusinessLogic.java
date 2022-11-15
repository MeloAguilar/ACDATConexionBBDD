import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessLogic {

    public Gestion gestion;

    public BusinessLogic() {
        gestion = new Gestion();
    }

    public BusinessLogic(String bbdd, String user, String pass) {
        gestion = new Gestion(bbdd, user, pass);

    }

    /**
     * Método que se encarga de eliminar todas las tablas de la base de datos
     *
     * @return
     */
    public int deleteTables() {
        int filasAfectadas = 0;
        deleteTable("Matriculas");
        filasAfectadas += 1;
        deleteTable("Profesores");
        filasAfectadas += 1;
        deleteTable("Alumnos");
        filasAfectadas += 1;
        return filasAfectadas;
    }

    /**
     * Método que se encarga de insertar un dato dentro de una tabla de la base de datos
     * dado un archivo .sql.
     *
     * <pre>archivo debe ser un archivo .sql con el nombre de la
     * tabla igual a uno que se encuentre en la base de datos a la qu queremos atacar</pre>
     *
     * @param archivo
     * @return
     */
    public int insertArchivoCompleto(File archivo) {
        String linea;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(archivo));

            while ((linea = br.readLine()) != null) {
                gestion.insertString(linea);

            }

            System.out.println("datos insertados correctamente");
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se encontró");
        } catch (IOException e) {
            System.out.println("Errror de entrada o salida de datos");
        } finally {
            gestion.getConexion().cerrarConexion();
        }
        return 0;
    }

    /**
     * Método que se encarga de eliminar una tabla de la base de datos
     * a la q2ue está asociada esta clase
     *
     * @param tabla
     */
    public void deleteTable(String tabla) {

        try {
            gestion.deleteTable(tabla);
        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("La tabla no existe");
        }
    }

    /**
     * @param nombreTabla
     * @return
     */
    public List listarTabla(String nombreTabla) throws ClassNotFoundException {


        //Genero la cadena de la select
        StringBuilder datos = new StringBuilder("Select * From ");
        datos.append(gestion.USER);
        datos.append(".");
        datos.append(nombreTabla);
        List lista = null;
        try {
            //Gracias al método de la clase gestion lanzo la peticion y recojo el resultSet
            ResultSet result = gestion.select(String.valueOf(datos));


            //Si el nomrbe de la tabla es Profesores
            if (nombreTabla.equals("Profesores")) {
                lista = new ArrayList<Profesor>();
                //Rellenop el arrayList de profesores
                while (result.next()) {
                    lista.add(getProfesorById(result.getInt("id")));
                }

                //Si el nombre de la tabla es Alumnos
            } else if (nombreTabla.equals("Alumnos")) {
                lista = new ArrayList<Alumno>();
                //Genero el arrayList de Alumnos
                while (result.next()) {
                    lista.add(getAlumnoById(result.getInt("id")));
                }

                //Si el nombre de la tabla es Matriculas
            } else if (nombreTabla.equals("Matriculas")) {
                lista = new ArrayList<Matricula>();
                //Genero el arrayList de Matriculas
                while (result.next())
                    lista.add(getMatriculaById(result.getInt("id")));
            }
        }catch(SQLException s){
            System.out.println("No se pudo acceder a la base de datos");
        }

        //Devuelvo la lista
        return lista;
    }


    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     *
     * @param id id correspondiente a un registro de la tabla Profesores
     * @return
     */
    public Profesor getProfesorById(int id) throws ClassNotFoundException {
        Profesor prof = null;
        String nombre;
        String apellidos;
        Date fechaNacimiento;
        int antiguedad;
        ResultSet result;
        //Le pido el resultSet de la query a la clase gestion
        try {
            result = gestion.select("Select * From ad2223_caguilar.Profesores where id = " + id);
            //Establezco los datos que contendra el objeto Profesor
            nombre = result.getNString("nombre");
            apellidos = result.getNString("apellidos");
            fechaNacimiento = result.getDate("fechaNacimiento");
            antiguedad = result.getInt("antiguedad");
            prof = new Profesor(id, nombre, apellidos, fechaNacimiento, antiguedad);
        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la base de datos");
        }
        //Instancio el profesor

        return prof;
    }


    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return
     */
    public boolean editAlumno(String[] datos) {
        var exito = false;
        if ((getAlumnoById(Integer.parseInt(datos[0]))) != null) {
            var datosReales = new StringBuilder("Update Alumnos set nombre = ");
            datosReales.append(datos[1]);
            datosReales.append(" apellidos = ");
            datosReales.append(datos[2]);
            datosReales.append(" fechaNacimiento = ");
            datosReales.append(datos[3]);
            datosReales.append(" Where id = ");
            datosReales.append(datos[0]);
            gestion.updateString(String.valueOf(datosReales));
            exito = true;
        } else {
            System.out.println("El registro no se encontró en la base de datos");
        }

        return exito;
    }


    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return
     */
    public boolean editMatricula(String[] datos) {
        var exito = false;

        if ((getMatriculaById(Integer.parseInt(datos[0]))) != null) {
            var datosReales = new StringBuilder("Update Matriculas set idProfesor = ");
            datosReales.append(datos[1]);
            datosReales.append(" idAlumno = ");
            datosReales.append(datos[2]);
            datosReales.append(" asignatura = ");
            datosReales.append(datos[3]);
            datosReales.append(" curso = ");
            datosReales.append(datos[4]);

            datosReales.append(" Where id = ");
            datosReales.append(datos[0]);
            gestion.updateString(String.valueOf(datosReales));
            exito = true;
        } else {
            System.out.println("El registro no se encontró en la base de datos");
        }


        return exito;
    }


    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return
     */
    public boolean editProfesor(String[] datos) {
        var exito = false;
        try {
            if ((getProfesorById(Integer.parseInt(datos[0]))) != null) {
                var datosReales = new StringBuilder("Update Profesores set nombre = ");
                datosReales.append(datos[1]);
                datosReales.append(" apellidos = ");
                datosReales.append(datos[2]);
                datosReales.append(" fechaNacimiento = ");
                datosReales.append(datos[3]);
                datosReales.append(" antiguedad = ");
                datosReales.append(datos[4]);
                datosReales.append(" Where id = ");
                datosReales.append(datos[0]);
                gestion.updateString(String.valueOf(datosReales));
                exito = true;
            } else {
                System.out.println("El registro no se encontró en la base de datos");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return exito;
    }

    /**
     * Método que se encarga de buscar un listado de objetos
     * profesor segun el nombre de la asignatura que imparte
     *
     * @param nombreAsignatura
     * @return
     */
    public Profesor getProfesoresPorAsignatura(String nombreAsignatura) {
        Profesor profesor = new Profesor();
        int id;
        PreparedStatement sttmnt = null;
        try {
            sttmnt = gestion.getPreparedStatement("(Select idProfesor From ad2223_caguilar.Matriculas Where asignatura = ?)");
            sttmnt.setString(1, nombreAsignatura);
            var result = sttmnt.executeQuery();

            while (result.next()) {

                id = result.getInt("idProfesor");


                profesor = getProfesorById(id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return profesor;
    }


    /**
     * Método que, dado un String que coincida con el nombre d un alumno de la base de datos
     * instancia un objeto de tipo Alumno y lo devuelve.
     */
    public Alumno getAlumnoByNombre(String _nombre) {
        Alumno alumno = null;

        try {
            ResultSet result = gestion.select("Select * From ad2223_caguilar.Alumnos Where Nombre = " + _nombre);
            alumno = new Alumno(result.getInt("id"), result.getString("nombre"), result.getString("apellidos"), result.getDate("fechaNacimiento").toString());
        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("Los datos introducidos no son correctos");
        }

        return alumno;
    }

    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     *
     * @param result
     * @return
     */
    public Alumno getAlumnoById(int id) {
        Alumno alumno = null;
        String nombre;
        String apellidos;
        String fechaNacimiento;
        StringBuilder datos = new StringBuilder("Select * From ");
        datos.append(gestion.USER);
        datos.append(".Alumnos Where id=");
        datos.append(id);
        ResultSet result;
        try {
            result = gestion.select(String.valueOf(datos));
            nombre = result.getNString("nombre");
            apellidos = result.getNString("apellidos");
            fechaNacimiento = result.getDate("fechaNacimiento").toString();
            alumno = new Alumno(id, nombre, apellidos, fechaNacimiento);

            System.out.println(alumno);
        } catch (SQLException e) {
            System.out.println("Columna no válida");
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo obtener informacion de los datos introducidos");
        }

        return alumno;
    }


    /**
     * Método que se encarga de, dado un entero que sea igual al id de una matricula
     * que se encuentre en la base de datos, devuelve un objeto Matricula
     *
     * @param id
     * @return
     */
    public Matricula getMatriculaById(int id) {
        Matricula matricula = null;
        int idProf;
        int idAlumno;
        String asignatura;
        int curso;
        var datos = new StringBuilder("Select * From ");
        datos.append(gestion.USER);
        datos.append(".Matriculas Where id = ");
        datos.append(id);
        ResultSet result;
        try {
            result = gestion.select(String.valueOf(datos));
            idProf = result.getInt("idProfesor");
            idAlumno = result.getInt("idAlumno");
            asignatura = result.getString("asignatura");
            curso = result.getInt("curso");
            matricula = new Matricula(id, idProf, idAlumno, asignatura, curso);
            System.out.println(matricula);
        } catch (SQLException e) {
            System.out.println("Columna no válida");
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo obtener informacion de los datos introducidos");
        }

        return matricula;
    }


    public int updatePropiedadAlumno(String propiedad, String dato, int id) {
        int filasAfectadas = 0;
        var datos = new StringBuilder("Update ");
        datos.append(gestion.USER);
        datos.append(".Alumnos set ");
        datos.append(propiedad);
        datos.append("=");
        datos.append(dato);
        datos.append("Where id = ");
        datos.append(id);

        filasAfectadas = gestion.updateString(String.valueOf(datos));
        return filasAfectadas;
    }

    /**
     * Método que se encarga de generar las tablas de la base de datos
     *
     * @return
     */
    public int crearTablas() {
        //Este método solo se encarga de llamar al método que genera la tabla para hacer el
        int filasAfectadas = 0;
        gestion.crearTabla("Profesores", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date", "antiguedad int"});
        gestion.crearTabla("Alumnos", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date"});
        gestion.crearTabla("Matriculas", new String[]{"id int Primary Key AUTO_INCREMENT", "idProfesor int", "idAlumno int", "asignatura varChar(50)", "curso int", "Foreign Key (idProfesor) References Profesores(id) On delete Cascade on Update Cascade", "Foreign Key (idAlumno) References Alumnos(id) On Delete Cascade on Update Cascade"});
        return filasAfectadas;
    }


    public void insertProfesor(String[] datos) {
        StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Profesores(nombre, apellidos, fechaNacimiento, antiguedad) values (");
        dat.append(datos[0]);
        dat.append(", ");
        dat.append(datos[1]);
        dat.append(", ");
        dat.append(datos[2]);
        dat.append(", ");
        dat.append(datos[3]);
        dat.append(")");
        gestion.insertString(String.valueOf(dat));
    }

    public void insertAlumno(String[] datos) {
        StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Alumnos(nombre, apellidos, fechaNacimiento) values (");
        dat.append(datos[0]);
        dat.append(", ");
        dat.append(datos[1]);
        dat.append(", ");
        dat.append(datos[2]);
        dat.append(")");
        gestion.insertString(String.valueOf(dat));
    }


    /**
     * Método que, dado un array de Strings, introduce los datos en la tabla Matriculas de la base de datos
     *
     * @param datos
     */
    public void insertMatricula(String[] datos) {
        try {
            var profesr = getProfesorById(Integer.parseInt(datos[0]));
            var alumno = getAlumnoById(Integer.parseInt(datos[1]));
            if (profesr != null && alumno != null) {
                StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Matriculas(idProfesor, idAlumno, asignatura, curso) values (");
                dat.append(datos[0]);
                dat.append(", ");
                dat.append(datos[1]);
                dat.append(", ");
                dat.append(datos[2]);
                dat.append(", ");
                dat.append(datos[3]);
                dat.append(")");
                gestion.insertString(String.valueOf(dat));
            } else {
                System.out.println("idProfesor e idAlumno deben coincidir con un registro de la base de datos.");
            }
        } catch (NumberFormatException e) {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("isProfesor e idAlumnos deben ser números enteros");
        }

    }


}
