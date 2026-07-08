
/**
 * Write a description of class Carro here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.Color;
import java.awt.Graphics;

public class Carro
{
    private int x;
    private int y;

    private int ancho;
    private int alto;

    private int velocidad;

    private Direccion direccion;
    private boolean detenido;

    public Carro(int x, int y, Direccion direccion)
    {
        this.x = x;
        this.y = y;

        this.direccion = direccion;

        velocidad = 2;
        detenido = false;

        ancho = 20;
        alto = 35;
    }

    public void dibujar(Graphics g)
    {
        g.setColor(Color.BLUE);

        g.fillRoundRect(x, y, ancho, alto, 8, 8);
    }

    public void avanzar()
    {
        if(detenido){
            return;
        }
        switch(direccion)
        {
            case NORTE:
                y -= velocidad;
                break;

            case SUR:
                y += velocidad;
                break;

            case ESTE:
                x += velocidad;
                break;

            case OESTE:
                x -= velocidad;
                break;
        }
    }
    
    public void detener(){
        detenido = true;
    }
    
    public void continuar(){
        detenido = false;
    }
    
    public boolean estaDetenido(){
        return detenido;
    }

    //====================
    // GETTERS
    //====================

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Direccion getDireccion()
    {
        return direccion;
    }

    //====================
    // SETTERS
    //====================

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setVelocidad(int velocidad)
    {
        this.velocidad = velocidad;
    }
}
