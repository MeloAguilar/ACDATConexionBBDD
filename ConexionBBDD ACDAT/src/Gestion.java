import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

public class Gestion {


    //Atributos
    private MiConexion conexion;
    public static final String BBDD = "jdbc:mysql://dns11036.phdns11.es";
    public static final String USER = "caguilar";
    public static final String PASS = "caguilar";

    //End Atributos

    //Constructores
    public Gestion(String nameBBDD, String user, String password)
    {
        conexion = new MiConexion(nameBBDD, user, password);
    }

    public Gestion(){
        conexion = new MiConexion(BBDD, USER, PASS);

    }

    //End Constructores
    /**
     * Método que se encarga de crear una tabla dentro de la base de datos
     */
    public void crearTabla(String nombreTabla, String[] tabla){

        StringBuilder peticion = new StringBuilder("CREATE TABLE ad2223_caguilar." + nombreTabla + " (");

        for (int i = 0; i < tabla.length; i++) {
            peticion.append(tabla[i]);
            if(i< tabla.length-1){
                peticion.append(",");
            }
        }
        peticion.append(");");
        try {
            conexion.abrirConexion();
            Statement sttmt = this.conexion.getConexion().createStatement();
            sttmt.executeUpdate(peticion.toString());
            System.out.println("Se generó la tabla "+ nombreTabla);
            System.out.println("Se creó la tabla "+nombreTabla+" con los datos ");
        } catch (SQLException e) {
            System.out.println("No se pudo insertar la tabla");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            conexion.cerrarConexion();
        }


    }


    /**
     * Método que se encarga de generar las tablas de la base de datos
     * @return
     */
    public int crearTablas()
    {
        int filasAfectadas = 0;
        crearTabla("Profesores", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date", "antiguedad int"});
        crearTabla("Alumnos", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date"});
        crearTabla("Matriculas", new String[]{"id int Primary Key AUTO_INCREMENT", "idProfesor int", "idAlumno int", "asignatura varChar(50)", "curso int", "Foreign Key (idProfesor) References Profesores(id) On delete Cascade on Update Cascade", "Foreign Key (idAlumno) References Alumnos(id) On Delete Cascade on Update Cascade"});
        return filasAfectadas;
    }

    /**
     * Método que se encarga de enviar una instrucción select al servidor de la base de datos
     * y devuelve un ResultSet
     *
     * <pre>la petición debe ser una cadena con estilo sql que haga referencia a una tabla de la base de datos
     * que exista</pre>
     * <post>Devolverá un Array de Strings que contendrán todos los datos recibidos de la BBDD</post>
     * @param peticion
     * @return ResultSet -> result
     */
    public ResultSet select(String peticion){
        ResultSet result = null;

            //result = this.conexion.executeQuery(peticion);


        return result;
    }


    /**
     * Método que se encarga de insertar un dato dentro de una ttabla de la base de datos
     * @param archivo
     * @return
     */
    public int insert(File archivo){
        String linea;
        BufferedReader br;
        try {
            Statement statement = conexion.abrirConexion().createStatement();
            br = new BufferedReader(new FileReader(archivo));

            while((linea = br.readLine())!=null){
                statement.executeUpdate(linea);

            }

            System.out.println("datos insertados correctamente");
        } catch (SQLException e) {
            System.out.println("No se puede acceder a la base de datos");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se encontró");
        } catch (IOException e) {
            System.out.println("Errror de entrada o salida de datos");
        }
        finally {
            conexion.cerrarConexion();
        }
        return 0;
    }


    public int update(String peticion, int id){
        return 0;
    }

    /**
     * Método que se encarga, dado un string, de borrar un elemento de la base de datos
     * @param sql
     */
    public void delete(String sql){
        try {
            var stmnt = conexion.abrirConexion().createStatement();
           stmnt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el dato");
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo accede a la base de datos");
        }

    }

    /**
     * Método que se encarga de borrar una tabla de la base de datos
     * @param tabla
     */
    public void deleteTable(String tabla){
        String tablaString = "DROP TABLE ad2223_caguilar." + tabla;
        delete(tablaString);
    }

    /**
     * Método que se encarga de eliminar todas las tablas de la base de datos
     * @return
     */
    public int deleteTables(){
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
     *
     * @param nombreTabla
     * @return
     */
    public ResultSet listarTabla(String nombreTabla){
        ResultSet result = null;
        try {

            var sttmnt = conexion.abrirConexion().createStatement();
            result = sttmnt.executeQuery("Select * From ad2223_caguilar."+nombreTabla);

            if(nombreTabla.equals("Profesores")){

                int id = 1;
                while(result.next()){
                    getProfesorById(result, result.getInt("id"));
                }

            }else if(nombreTabla.equals("Alumnos")){
                while(result.next()){
                    getAlumnoById(result, result.getInt("id"));
                }

            }else if(nombreTabla.equals("Matriculas")){
                while(result.next())
                    getMatriculaById(result, result.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     * @param result
     * @return
     */
    public Profesor getProfesorById( int id){
        Profesor prof = null;
        String nombre;
        String apellidos;
        Date fechaNacimiento;
        int antiguedad;
        ResultSet result;
        try {
            nombre = result.getNString("nombre");
            apellidos = result.getNString("apellidos");
            fechaNacimiento = result.getDate("fechaNacimiento");
            antiguedad = result.getInt("antiguedad");
            prof = new Profesor(id, nombre, apellidos, fechaNacimiento, antiguedad);
            System.out.println(prof);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prof;
    }

    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     * @param result
     * @return
     */
    public Alumno getAlumnoById(ResultSet result, int id){
        Alumno alumno = null;
        String nombre;
        String apellidos;
        Date fechaNacimiento;
        try {
            nombre = result.getNString("nombre");
            apellidos = result.getNString("apellidos");
            fechaNacimiento = result.getDate("fechaNacimiento");
            alumno = new Alumno(id, nombre, apellidos, fechaNacimiento);

            System.out.println(alumno);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return alumno;
    }

    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Matricula que coincida con los
     * datos que recibe del resultset
     * @param result
     * @return
     */
    public Matricula getMatriculaById(ResultSet result, int id){
        Matricula matricula = null;
        int idProf;
        int idAlumno;
        String asignatura;
        int curso;
        try {

            idProf = result.getInt("idProfesor");
            idAlumno = result.getInt("idAlumno");
            asignatura = result.getString("asignatura");
            curso = result.getInt("curso");
            matricula = new Matricula(id, idProf, idAlumno, asignatura, curso);
            System.out.println(matricula);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matricula;
    }


    /**
     * Método que se encarga de buscar un profesor segun el id de la as
     * @param nombreAsignatura
     * @return
     */
    public HashMap<Integer, Profesor> getProfesorPorAsignatura(String nombreAsignatura){
        HashMap<Integer, Profesor> profes = new HashMap<>();
        int id;
        PreparedStatement sttmnt = null;
        PreparedStatement stm2 = null;
        try {
            sttmnt = conexion.abrirConexion().prepareStatement("(Select idProfesor From ad2223_caguilar.Matriculas Where asignatura = ?)");
            sttmnt.setString(1, nombreAsignatura);
            var result = sttmnt.executeQuery();

            while(result.next()){

                id =result.getInt("idProfesor");
                var conn = new MiConexion(BBDD, USER,PASS).abrirConexion();
                stm2 = conn.prepareStatement("Select * From ad2223_caguilar.Profesores Where id = ?");
                stm2.setInt(1, id);

                var result2 = stm2.executeQuery();
                int posicionHMap = 0;

                getProfesorById(result2, id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


return profes;
    }


    /**
     *
     */
    public Alumno getAlumnoByNombre(String _nombre){
        Alumno alumno = new Alumno();



        return alumno;
    }

}
