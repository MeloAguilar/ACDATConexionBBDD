import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class menu {
    private BusinessLogic bl;

    public void iniciarMenu(Scanner sc) {
        bl = new BusinessLogic();
        start(sc);
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

    /**
     * Método que se encarga de iniciar el programa.
     * Muestra ciertas opciones, pide al usuario
     * una eleccion entre ellas y llama al método necesario para
     * proseguir el menu.
     *
     * @param sc
     */
    public void start(Scanner sc) {
        var menu = """
                =========================================
                                
                1 = insertar registro
                                
                2 = modificar registro
                                
                3 = eliminar registro
                                
                4 = listar registros de tabla
                                
                5 = obtener registro por número de identificación
                                
                0 = Salir
                                
                ========================================""";

        var salir = false;
        while (!salir) {
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
                    menuObtenerRegistroPorNumeroDeIdentificacion(sc);
                }
                case 0 -> {
                    salir = true;
                }

            }
        }
    }


    /**
     * Método que se encarga de mostrar el menu para obtener un registro de una tabla.
     * Se muestran las opciones y el usuario deberá elegir introduciendo un número entre los
     * que representan a las tablas de la base de datos.
     * Después se pedirá que introduzca un nuevo entero que representará el campo id
     * de la tabla que se haya elegido en inicio del menu
     *
     * @param sc
     */
    private void menuObtenerRegistroPorNumeroDeIdentificacion(Scanner sc) {
        var menu = """
                =========================================
                                
                0 = salir
                                
                1 = buscar Profesor
                                
                2 = buscar Alumno
                                
                3 = buscar Matrícula
                                
                ========================================""";
        var salir = false;
        while (!salir) {

            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);
            try {


                switch (eleccion) {
                    case 1 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                        System.out.println(bl.getProfesorById(id));

                    }
                    case 2 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                        System.out.println(bl.getAlumnoById(id));

                    }
                    case 3 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                        System.out.println(bl.getMatriculaById(id));

                    }
                    case 0 -> {
                        salir = true;
                    }
                }
                sc.next();
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número");
            } catch (ClassNotFoundException s) {
                System.out.println("No se pudo obtener ningun dato");
            }

        }

    }


    /**
     * Método que se encarga de, dado un entero correspondiente a una de las tres tablas que se encuentran
     * en la base de datos, pide otro entero que representará el identificador de un registro que se
     * encuentre en esta.
     *
     * @param sc
     * @param eleccion
     * @return
     */
    private int getEnteroMenuBuscarPorId(Scanner sc, int eleccion) {
        var nombreTabla = "";
        var salir = false;
        while (!salir) {


            switch (eleccion) {
                case 1 -> {
                    nombreTabla = "el Profesor";
                }
                case 2 -> {
                    nombreTabla = "el Alumno";

                }
                case 3 -> {
                    nombreTabla = "la Matricula";
                }
            }
            if (eleccion >= 1 && eleccion <= 3) {
                salir = true;
            }
        }
        System.out.println("Escriba el numero de identificacion de " + nombreTabla);
        var id = seleccionarOpcion(sc);

        return id;
    }


    /**
     * Método que se encarga de pedir al usuario un número entero, haciendo que represente una de las tres
     * tablas de la base de datos
     *
     * @param sc
     */
    private void menuListarRegistrosDeTabla(Scanner sc) {
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
                            for (Profesor profesor : (ArrayList<Profesor>) bl.listarTabla("Profesores")) {
                                System.out.println(profesor);
                            }
                        }
                        case 2 -> {
                            for (Alumno alumno : (ArrayList<Alumno>) bl.listarTabla("Alumnos")) {
                                System.out.println(alumno);
                            }
                        }
                        case 3 -> {
                            for (Matricula matricula : (ArrayList<Matricula>) bl.listarTabla("Matriculas")) {
                                System.out.println(matricula);
                            }

                        }
                        case 0 -> {
                            salir = true;
                        }
                    }
                } catch (ClassNotFoundException f) {
                    System.out.println("No se pudieron recoger los datos");
                }

            } else {
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
        var filas = 0;
        var eleccion = seleccionarOpcion(sc);
        switch (eleccion) {
            case 1 -> {
                filas = bl.insertProfesor(getDatosProfesorNuevo(sc));
                if(filas > 0){
                    System.out.println("Se insertaron los datos con éxito");
                }
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
        System.out.println("introduzca el id del Profesor que imparte la asignatura");
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
        System.out.println("introduzca la fecha de nacimiento del alumno en formato dd/mm/yyyy");
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
        sc.nextLine();
        String[] datos = new String[4];
        System.out.println("introduzca el nombre del Profesor");
        datos[0] = sc.nextLine();
        System.out.println("introduzca los apellidos del Profesor");
        datos[1] = sc.nextLine();
        System.out.println("introduzca la fecha de nacimiento del profesor en formato dd/mm/yyyy");
        datos[2] = sc.nextLine();
        System.out.println("introduzca los años de antigüedad en la docencia");
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
