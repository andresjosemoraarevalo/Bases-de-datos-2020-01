/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorios;
import dto.DTOLinea;
import entidades.Libro;
import entidades.Linea;
import entidades.Moneda;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import entidades.Prestamo;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import jdbc_conexion.Constantes;
/**
 *
 * @author Juan Pablo Amorocho
 */
public class RepositorioPrestamo implements Interfaz.IGestionPrestamo{
    
    public void agregarPrestamo(Prestamo aAgregar)
    {
        String SQL_INSERT = "INSERT INTO Prestamo (LocalDateTime, Numero, CantiQuinientos, CantiMil, MontoTotal) VALUES (?,?, ?, ?, ?)";
        int rowAff;
        if(aAgregar != null)
        {
        try(
          Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
          PreparedStatement ps = conex.prepareStatement(SQL_INSERT);    
          )
          {
          ps.setDate(1,aAgregar.getFecha());
          ps.setInt(2,aAgregar.getNumero());
          ps.setInt(3, 0);
          ps.setInt(4, 0);
          ps.setBigDecimal(5, BigDecimal.ZERO);
          rowAff = ps.executeUpdate();
              
          }
        catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }   
       }
    }
    

    public ArrayList<Prestamo> getPrestamo()
    {
        ArrayList<Prestamo> Resultado = new ArrayList<>();
        String GetInfoPrestamo = "select * from Prestamo";
        Prestamo aAgregar;
        Date LocalDateTime;
        int Numero, CantiQ, CantiM;
        BigDecimal Total;
        
        try(Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
    PreparedStatement ps = conex.prepareStatement(GetInfoPrestamo);
    ResultSet rs = ps.executeQuery();)
        {
          while (rs.next())
        {
            LocalDateTime = rs.getDate("LocalDateTime");
            Numero = rs.getInt("Numero");
            CantiQ = rs.getInt("CantiQuinientos");
            CantiM = rs.getInt("CantiMil");
            Total = rs.getBigDecimal("MontoTotal");
            aAgregar = new Prestamo(LocalDateTime, Numero, CantiQ, CantiM, Total);
            Resultado.add(aAgregar);
        }  
        }
        catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
        return Resultado;
    }
    
    public ArrayList<DTOLinea> getLineaDTO (int NumeroDelPrestamo)
    {
        ArrayList<DTOLinea> Resultado = new ArrayList<>();
        String GetInfoLinea = "select * from Lineas";
        DTOLinea aAgregar;
        RepositorioLibro rt = new RepositorioLibro();
        ArrayList<Libro> Library = rt.CargarLibro();
        Libro elAgregadoLibro = new Libro();
        String ISBN;
        int Numero, Cantidad;
        
        try(Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
    PreparedStatement ps = conex.prepareStatement(GetInfoLinea);
    ResultSet rs = ps.executeQuery();)
        {
          while (rs.next())
        {
            if(rs.getInt("Numero_Prestamo")==NumeroDelPrestamo)
            {
            ISBN = rs.getString("ISBN_Libro");
            for (Libro libroCurr : Library)
            {
                if(ISBN.equals(libroCurr.getIsbn()))
                {
                    elAgregadoLibro = libroCurr;
                    break;
                }
            }
            Cantidad = rs.getInt("Cantidad");
            aAgregar = new DTOLinea(elAgregadoLibro, Cantidad, 0, 0);
            Resultado.add(aAgregar);
            }
        }  
        }
        catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
        return Resultado;
    }
    
    public ArrayList<Linea> getLineas(int Numero_Prestamo)
    {
        ArrayList<Linea> Resultado = new ArrayList<>();
        String GetInfoLinea = "select * from Lineas";
        Linea aAgregar;
        //LIBRO...
        RepositorioLibro rt = new RepositorioLibro();
        ArrayList<Libro> Library = rt.CargarLibro();
        Libro elAgregadoLibro = new Libro();
        //PRESTAMO...
        ArrayList<Prestamo> PresLibrary = this.getPrestamo();
        Prestamo elAgregadoPrestamo = new Prestamo();
        //DATOS...
        String ISBN;
        int Numero, Cantidad;
        
        try(Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
    PreparedStatement ps = conex.prepareStatement(GetInfoLinea);
    ResultSet rs = ps.executeQuery();)
        {
          while (rs.next())
        {
            Numero = rs.getInt("Numero_Prestamo");
            if(Numero == Numero_Prestamo)
            {
  
            ISBN = rs.getString("ISBN_Libro");
            for (Libro libroCurr : Library)
            {
                if(ISBN.equals(libroCurr.getIsbn()))
                {
                    elAgregadoLibro = libroCurr;
                    break;
                }
            }
            
            for(Prestamo prestamoCurr : PresLibrary)
            {
                if(prestamoCurr.getNumero()==Numero)
                {
                    elAgregadoPrestamo = prestamoCurr;
                    break;
                }
            }
            Cantidad = rs.getInt("Cantidad");
            aAgregar = new Linea(elAgregadoLibro, elAgregadoPrestamo, Cantidad);
            Resultado.add(aAgregar);
            }
            
        }  
        }
        catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
        return Resultado;
    }
    
        public int numeroPrestamoMayor()
    {
        int resultado = -1;
        ArrayList<Prestamo> Buscar = getPrestamo();
        for (Prestamo prestamoCurr : Buscar)
        {
            if(prestamoCurr.getNumero()>resultado)
            {
                resultado = prestamoCurr.getNumero();
            }
        }
        return resultado;
        
    }
    

    
    public void agregarLinea(Linea aAgregar)
    {
         String SQL_INSERT = "INSERT INTO Lineas (ISBN_libro, Numero_Prestamo, Cantidad) VALUES (?,?,?)";
         String ISBN_libro;
         int Numero_Prestamo, rowAff, Cantidad;
        ISBN_libro = aAgregar.getLibroEnPrestamo().getIsbn();
        Numero_Prestamo = aAgregar.getPrestamoPadre().getNumero();
        Cantidad = aAgregar.getCantidadLibros();
            try(
          Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
          PreparedStatement ps = conex.prepareStatement(SQL_INSERT);)
                {
                    ps.setString(1, ISBN_libro);
                    ps.setInt(2, Numero_Prestamo);
                    ps.setInt(3, Cantidad);
                    rowAff = ps.executeUpdate();
                }
            catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
    }
    
    public void borrarLinea(Linea aBorrar)
    {
        String SQL_DELETE = "DELETE FROM Lineas WHERE ISBN_libro = ? AND Numero_Prestamo = ?";
        String ISBN_Libro = aBorrar.getLibroEnPrestamo().getIsbn();
        int Numero_Prestamo = aBorrar.getPrestamoPadre().getNumero(), rowAff;
        try(
          Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
          PreparedStatement ps = conex.prepareStatement(SQL_DELETE);)
                {
                    ps.setString(1, ISBN_Libro);
                    ps.setInt(2, Numero_Prestamo);
                    rowAff = ps.executeUpdate();
                }
            catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
    }
    


    
    public void actualizarMonedaEnPrestamo(Prestamo aActualizar)
    {
        String elString = "UPDATE Prestamo SET CantiQuinientos = ?, CantiMil = ?, MontoTotal = ? WHERE Numero = ?";
        int Numero = aActualizar.getNumero(),CantiQ = aActualizar.getCantiMonedas500();
        int CantiM = aActualizar.getCantiMonedas1000(), rowAff;
        BigDecimal Monto = aActualizar.getMontoTotal();

        try(
          Connection conex = DriverManager.getConnection(Constantes.THINCONN, Constantes.USERNAME, Constantes.PASSWORD);
          PreparedStatement ps = conex.prepareStatement(elString);)
        {
            ps.setInt(1,CantiQ);
            ps.setInt(2, CantiM);
            ps.setBigDecimal(3, Monto);
            ps.setInt(4, Numero);
            rowAff = ps.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println("Error de conexion:" + ex.toString());
            ex.printStackTrace();
        }
    }
    //PARA PROBAR...
    public void ImprimirDatos()
    {
        ArrayList<Prestamo> catalogo = this.getPrestamo();
        for (Prestamo libroCurr : catalogo) {
            System.out.println("Fecha: " + libroCurr.getFecha());
            System.out.println("Numero: " + libroCurr.getNumero());
            System.out.println("Monto: " + libroCurr.getMontoTotal());
            System.out.println("500: " + libroCurr.getCantiMonedas500());
        }
        
    }
}