import EntidadesPersistencia.Profesor;

import EntidadesPersistencia.*;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static menu menu = new menu();

    public static void main(String[] args) {

       Scanner sc  =new Scanner(System.in);
        menu.iniciarMenu(sc);


    }


}

