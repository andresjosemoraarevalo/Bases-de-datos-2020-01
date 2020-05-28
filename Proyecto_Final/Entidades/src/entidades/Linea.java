/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Jose Mora, Oscar Andres Pacheco y Juan Pablo Amorocho
 */
public class Linea {
    private Libro libroEnPrestamo;
    private Prestamo padre;
    
    public Linea() {
    }

    public Linea(Libro libroEnPrestamo, Prestamo padre) {
        this.libroEnPrestamo = libroEnPrestamo;
        this.padre = padre;
    }

    public Libro getLibroEnPrestamo() {
        return libroEnPrestamo;
    }

    public void setLibroEnPrestamo(Libro libroEnPrestamo) {
        this.libroEnPrestamo = libroEnPrestamo;
    }
       
            
}
