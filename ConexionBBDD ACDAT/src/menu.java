import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class menu {
    private BusinessLogic bl;

    public void iniciarMenu() {

    }

    private static int seleccionarOpcion(Scanner sc) {
        int eleccion = 0;
        String opcion = sc.next();

        try {
            eleccion = Integer.parseInt(opcion);
        } catch (NumberFormatException e) {
            System.out.println("No se pudo registrar esa opcion");
        }

        return eleccion;
    }

    public void opciones(Scanner sc) {
        var menu = format("""
                =========================================
                                
                1 = insertar registro
                                
                2 = modificar registro
                                
                3 = eliminar registro
                                
                4 = listar registros de tabla
                                
                5 = obtener registro por número de identificación
                                
                ========================================""");
        System.out.println(menu);
        var eleccion = seleccionarOpcion(sc);
        switch (eleccion) {
            case 1 -> {
                menuInsertarRegistro(sc);
            }
            case 2 -> {
                menuModificarRegistro(sc);
            }
            case 3 -> {
                eliminarRegistro(sc);
            }
            case 4 -> {
                menuListarRegistrosDeTabla(sc);
            }
            case 5 -> {
                
            }

        }
    }

    
    public void menuObtenerRegistroPorNumeroDeIdentificacion(Scanner sc){
        var menu = format("""
                =========================================
                                
                0 = salir
                                
                1 = buscar Profesor
                                
                2 = buscar Alumno
                                
                3 = buscar Matrícula
                                
                ========================================""");
        System.out.println(menu);
        var salir =false;
        while(!salir){
            try{
                var eleccion = seleccionarOpcion(sc);
                switch(eleccion){
                    case 1 -> {
                        System.out.println(bl.getProfesorById(eleccion));
                        salir = true;
                    }
                    case 2 -> {
                        System.out.println(bl.getAlumnoById(eleccion));
                        salir = true;
                    }
                    case 3 -> {
                        System.out.println(bl.getMatriculaById(eleccion));
                        salir = true;
                    }
                    case 0 -> {
                        salir = true;
                    }
                }
            }catch(NumberFormatException e){
                System.out.println("Debe introducir un número");
            }catch(ClassNotFoundException s){
                System.out.println("No se pudo obtener ningun dato");
            }
        }
        
    }
    
    
    private int getEnteroMenuBuscarPorId(Scanner sc, int eleccion){
        switch(eleccion){
            case 1 -> {

            }
            case 2 -> {
                
            }
            case 3 -> {
                
            }erwt
        }
        System.out.println("");
        var id = seleccionarOpcion(sc);
    }
    

    public void menuListarRegistrosDeTabla(Scanner sc) {
        var menu = format("""
                =========================================
                                
                0 = salir
                                
                1 = listar Profesores
                                
                2 = listar Alumnos
                                
                3 = listar Matrículas
                                
                ========================================""");
        System.out.println(menu);
        var salir = false;

        while (!salir) {
            var eleccion = seleccionarOpcion(sc);
            if (eleccion >= 0 && eleccion <= 3) {
                try {
                    switch (eleccion) {
                        case 1 -> {
                            bl.listarTabla("Profesores");
                            salir = true;
                        }
                        case 2 -> {
                            bl.listarTabla("Alumnos");
                            salir = true;
                        }
                        case 3 -> {
                            bl.listarTabla("Matriculas");
                            salir = true;
                        }
                        case 0 -> {
                            salir = true;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("No se pudo acceder a la base de datos");
                } catch (ClassNotFoundException f) {
                    System.out.println("No se pudieron recoger los datos");
                }

            }else{
                System.out.println("Debe introducir un número correspondiente a una de las elecciones posibles");
            }

        }

    }


    
    
    private void eliminarRegistro(Scanner sc) {
        System.out.println("inserta el nombre de la tabla en la que se encuentra el registro que desea eliminar");
        var nombreTabla = sc.nextLine();

        if (bl.gestion.testIfExists(nombreTabla)) {
            try {
                int id = Integer.parseInt(sc.nextLine());
                bl.gestion.delete(nombreTabla, id);
            } catch (NumberFormatException e) {
                System.out.println("El identificador debe ser un numero entero, por lo ue no se eliminó ningun registro de la base de datos");
            }

        }
        System.out.println("inserta el número de identificacion del registro que desea eliminar");
    }


    
    
    
    private void menuModificarRegistro(Scanner sc) {
        var menu = format("""
                =========================================
                                
                1 = modifiar Profesor
                                
                2 = modificar Alumno
                                
                3 = modificar Matrícula
                                
                ========================================""");
        System.out.println(menu);

        var eleccion = seleccionarOpcion(sc);
        switch (eleccion) {
            case 1 -> {
                bl.editProfesor(getDatosProfesorCambiar(sc));
            }
            case 2 -> {
                bl.editAlumno(getDatosAlumnoCambiar(sc));
            }
            case 3 -> {
                bl.editMatricula(getDatosMatriculaNuevo(sc));
            }

        }
    }


    private void menuInsertarRegistro(Scanner sc) {
        var menu = format("""
                =========================================
                                
                1 = insertar Profesor
                                
                2 = insertar Alumno
                                
                3 = insertar Matrícula
                                
                ========================================""");
        System.out.println(menu);
        var eleccion = seleccionarOpcion(sc);
        switch (eleccion) {
            case 1 -> {
                bl.insertProfesor(getDatosProfesorNuevo(sc));
            }
            case 2 -> {
                bl.insertAlumno(getDatosAlumnoNuevo(sc));
            }
            case 3 -> {
                bl.insertMatricula(getDatosMatriculaNuevo(sc));
            }

        }
    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Profesor
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private String[] getDatosMatriculaNuevo(Scanner sc) {
        String[] datos = new String[4];
        System.out.println("introduzca el id del Profesor");
        datos[0] = sc.nextLine();
        System.out.println("introduzca el id del Alumno");
        datos[1] = sc.nextLine();
        System.out.println("introduzca el nombre de la asignatura");
        datos[2] = sc.nextLine();
        System.out.println("introduzca el numero del curso");
        datos[3] = sc.nextLine();

        return datos;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Alumno
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private String[] getDatosAlumnoNuevo(Scanner sc) {
        String[] datos = new String[3];
        System.out.println("introduzca el nombre del Alumno");
        datos[0] = sc.nextLine();
        System.out.println("introduzca los apellidos del Alumno");
        datos[1] = sc.nextLine();
        System.out.println("introduzca la fecha de nacimiento del alumno en formato dd-mm-yyyy");
        datos[2] = sc.nextLine();

        return datos;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de una nueva Matricula
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private String[] getDatosProfesorNuevo(Scanner sc) {
        String[] datos = new String[4];
        System.out.println("introduzca el nombre del Profesor");
        datos[0] = sc.nextLine();
        System.out.println("introduzca los apellidos del Profesor");
        datos[1] = sc.nextLine();
        System.out.println("introduzca la fecha de nacimiento del alumno en formato dd-mm-yyyy");
        datos[2] = sc.nextLine();
        System.out.println("introduzca los años de antiüedad en la docencia");
        datos[3] = sc.nextLine();

        return datos;
    }


    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosProfesorCambiar(Scanner sc) {
        List<String> _datos = new ArrayList<>();

        System.out.println("introduce el número de identificación del Profesor");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getProfesorById(id));
            var datosProfesor = getDatosProfesorNuevo(sc);
            for (String dato : datosProfesor) {
                _datos.add(dato);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return (String[]) _datos.toArray();
    }

    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosAlumnoCambiar(Scanner sc) {
        List<String> _datos = new ArrayList<>();

        System.out.println("introduce el número de identificación del ALumno");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getAlumnoById(id));
            var datosAlumno = getDatosAlumnoNuevo(sc);
            for (String dato : datosAlumno) {
                _datos.add(dato);
            }
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return (String[]) _datos.toArray();
    }

    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosMatriculaCambiar(Scanner sc) {
        List<String> _datos = new ArrayList<>();

        System.out.println("introduce el número de identificación del Profesor");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getMatriculaById(id));
            var datosMatricula = getDatosMatriculaNuevo(sc);
            for (String dato : datosMatricula) {
                _datos.add(dato);
            }
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return (String[]) _datos.toArray();
    }

}
