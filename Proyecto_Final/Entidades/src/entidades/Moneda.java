/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author andre
 */
public class Moneda {
    private Denominacion denominacion;

    public Moneda() {
    }

    public Moneda(Denominacion denominacion) {
        this.denominacion = denominacion;
    }

    public Denominacion getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(Denominacion denominacion) {
        this.denominacion = denominacion;
    }
    
    
}
