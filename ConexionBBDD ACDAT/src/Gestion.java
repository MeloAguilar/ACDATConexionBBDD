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
    public static String BBDD = "jdbc:mysql://dns11036.phdns11.es/ad2223_caguilar";
    public static String USER = "ad2223_caguilar";
    public static String PASS = "Patatitasexy69";

    //End Atributos
    public MiConexion getConexion() {
        return conexion;
    }

    //Constructores
    public Gestion(String nameBBDD, String user, String password) {
        BBDD = nameBBDD;
        USER = user;
        PASS = password;
        conexion = new MiConexion(nameBBDD, user, password);
    }

    public Gestion() {
        conexion = new MiConexion(BBDD, PASS, USER);

    }

    //End Constructores

    /**
     * Método que, dado el nombre de una propiedades de la base de propiedades y un Array de Strings
     * que coincidan en número con los attributos de la propiedades de la que se ha aportado el nombre,
     * insertará dichos propiedades en la propiedades correspondiente al nombre dado
     *
     * <pre>
     *     -nombreTabla debe coincidir con el nombre de una propiedades de la base de propiedades a la que hagamos referencia.
     *     -propiedades debe contener una longitud igual al numero de propiedades de la propiedades a la que se hace
     *     referencia en nombreTabla.
     * </pre>
     *
     * @param nombreTabla
     * @param propiedades
     */
    public void crearTabla(String nombreTabla, String[] propiedades) {


        StringBuilder peticion = new StringBuilder("CREATE TABLE ");
        peticion.append(USER );
        peticion.append(".");
        peticion.append(nombreTabla);
        peticion.append(" (");
        for (int i = 0; i < propiedades.length; i++) {
            peticion.append(propiedades[i]);
            if (i < propiedades.length - 1) {
                peticion.append(",");
            }
        }
        peticion.append(");");
        try {
            conexion.abrirConexion();
            Statement sttmt = this.conexion.getConexion().createStatement();
            sttmt.executeUpdate(peticion.toString());
            System.out.println("Se generó la propiedades " + nombreTabla);
            System.out.println("Se creó la propiedades " + nombreTabla + " con los propiedades ");
        } catch (SQLException e) {
            System.out.println("No se pudo insertar la propiedades");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            conexion.cerrarConexion();
        }


    }


    /**
     * Método que se encarga de enviar una instrucción select al servidor de la base de datos
     * y devuelve un ResultSet
     *
     * <pre>la petición debe ser una cadena con estilo sql que haga referencia a una tabla de la base de datos
     * que exista</pre>
     * <post>Devolverá un Array de Strings que contendrán todos los datos recibidos de la BBDD</post>
     *
     * @param peticion
     * @return ResultSet -> result
     */
    public ResultSet select(String peticion) throws SQLException, ClassNotFoundException {

        var statement = this.conexion.abrirConexion().createStatement();


        return statement.executeQuery(peticion);
    }


    /**
     * nMétodo que se encarga de, dado un string, introducir los datos que contiene en la tabla correspondiente
     * de la base de datos.
     *
     * <pre>
     *     datos debe tener el siguiente formato. 'NombreTabla values(valor, valor, valor...)'
     *          donde NombreTabla debe coincidir con el nombre de alguna de las tablas de la base de datos
     *          y dentro de los parentesis debe haber tantos valores como propedades tenga la tabla.
     * </pre>
     *
     * @param datos
     * @return filasAfectadas -> será el número de inserciones que se realizaron en la base de datos
     */
    public int insertString(String datos) {
        int filasAfectadas = 0;
        try {
            Statement statement = conexion.abrirConexion().createStatement();
            filasAfectadas = statement.executeUpdate(datos);
        } catch (SQLException e) {
            System.out.println("No se pudo establecer la conexin con el servidor");
        } catch (ClassNotFoundException e) {
            System.out.println("Los datos que se intentaron introducir no coinciden con la base de datos");
        } finally {
            conexion.cerrarConexion();
        }

        return filasAfectadas;
    }


    /**
     * @param peticion
     * @param id
     * @return
     */
    public int updateString(String peticion) {
        int filasAfectadas = 0;
        try {

            Statement statement = conexion.abrirConexion().createStatement();
            filasAfectadas = statement.executeUpdate(peticion);
        } catch (SQLException e) {
            System.out.println("No se pudo establecer la conexin con el servidor");
        } catch (ClassNotFoundException e) {
            System.out.println("Los datos que se intentaron introducir no coinciden con la base de datos");
        } finally {
            conexion.cerrarConexion();
        }

        return filasAfectadas;
    }


    /**
     * Método que se encarga de comprobar si una tabla existe o no dentro de la base de datos
     *
     *
     * @param nombreTabla
     * @return
     */
    public boolean testIfExists(String nombreTabla){
        var exito = false;
       try(var result = conexion.getConexion().getMetaData().getTables(null,null,nombreTabla,null)) {
           while(result.next()){
               String tName = result.getString("TABLE_NAME");
               if(tName != null && tName.equals(nombreTabla)){
                   exito = true;
                   break;
               }
           }


       }catch (SQLException e){
           System.out.println("No se pudo acceder a la base de datos");
       }

        return exito;
    }


    public void deleteTable(String tabla) throws SQLException, ClassNotFoundException {
        String tablaString = "DROP TABLE " + Gestion.USER + "." + tabla;
        var stmnt = conexion.abrirConexion().createStatement();
        stmnt.executeUpdate(String.valueOf(tablaString));
    }


    /**
     * @param sql
     */
    public void delete(String nombreTabla, int id) {
        try {
            var datos = new StringBuilder("Delete From ");
            datos.append(USER);
            datos.append(".");
            datos.append(nombreTabla);
            datos.append(" Where id = ");
            datos.append(id);
            var stmnt = conexion.abrirConexion().createStatement();
            stmnt.executeUpdate(String.valueOf(datos));
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el dato");
        } catch (ClassNotFoundException e) {
            System.out.println("El registro o tabla que intenta elimiar no existe");
        }

    }

    /**
     * Método que se encarga de borrar una tabla de la base de datos
     *
     * @param tabla
     */







public PreparedStatement getPreparedStatement(String query) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = null;

        statement = conexion.abrirConexion().prepareStatement(query);
        return statement;
}



}
