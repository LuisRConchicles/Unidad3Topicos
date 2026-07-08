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
    private double x;
    private double y;

    private int ancho;
    private int alto;

    private int velocidad;

    private Direccion direccion;
    private boolean detenido;
    private Color color;

    private static final Color[] PALETA = {
        new Color(196, 58, 58),   // rojo
        new Color(58, 92, 196),   // azul
        new Color(224, 190, 60),  // amarillo
        new Color(225, 225, 225), // blanco/plata
        new Color(55, 55, 60)     // gris oscuro
    };

    // ---- Giro dentro del cruce ----
    private boolean girando;
    private boolean yaDecidioGiro;
    private Direccion direccionDespuesDelGiro;
    private double giroDestinoX;
    private double giroDestinoY;
    private double giroPasoX;
    private double giroPasoY;
    private int giroPasosRestantes;

    // Cuantos "ticks" dura la curva del giro (mas alto = giro mas suave)
    private static final int GIRO_PASOS = 40;

    public Carro(int x, int y, Direccion direccion)
    {
        this.x = x;
        this.y = y;

        this.direccion = direccion;

        velocidad = 2;
        detenido = false;

        ancho = 20;
        alto = 35;

        girando = false;
        yaDecidioGiro = false;

        color = PALETA[(int)(Math.random() * PALETA.length)];
    }

    public void dibujar(Graphics g)
    {
        boolean horizontal = (direccion == Direccion.ESTE || direccion == Direccion.OESTE);

        // Si va en horizontal, el carro se "acuesta": lo largo (alto) queda
        // en el eje X y lo angosto (ancho) en el eje Y, para que se vea
        // acostado en su carril y no como un rectángulo parado fuera de lugar.
        int w = horizontal ? alto : ancho;
        int h = horizontal ? ancho : alto;

        int px = (int)x;
        int py = (int)y;

        // Sombrita para que no se vea tan plano
        g.setColor(new Color(0, 0, 0, 70));
        g.fillRoundRect(px + 2, py + 2, w, h, 10, 10);

        // Carrocería
        g.setColor(color);
        g.fillRoundRect(px, py, w, h, 10, 10);

        // Contorno
        g.setColor(color.darker());
        g.drawRoundRect(px, py, w, h, 10, 10);

        // Parabrisas: marca hacia dónde está el frente del carro
        g.setColor(new Color(190, 220, 250));
        switch(direccion){
            case NORTE:
                g.fillRoundRect(px + 3, py + 4, w - 6, 9, 5, 5);
                break;
            case SUR:
                g.fillRoundRect(px + 3, py + h - 13, w - 6, 9, 5, 5);
                break;
            case ESTE:
                g.fillRoundRect(px + w - 13, py + 3, 9, h - 6, 5, 5);
                break;
            case OESTE:
                g.fillRoundRect(px + 4, py + 3, 9, h - 6, 5, 5);
                break;
        }
    }

    public void avanzar()
    {
        if(girando)
        {
            avanzarGiro();
            return;
        }

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

    /**
     * Avanza un paso de la animacion de giro (linea recta hacia el
     * punto de destino en el nuevo carril). Un carro girando nunca
     * se detiene: ya se comprometio a cruzar.
     */
    private void avanzarGiro()
    {
        if(giroPasosRestantes <= 0)
        {
            // Termina el giro: queda alineado en el carril nuevo
            x = giroDestinoX;
            y = giroDestinoY;
            direccion = direccionDespuesDelGiro;
            girando = false;
            return;
        }

        x += giroPasoX;
        y += giroPasoY;
        giroPasosRestantes--;
    }

    /**
     * Inicia un giro suave hacia nuevaDireccion. destinoX/destinoY es el
     * punto del carril nuevo, ya dentro del cruce, donde el carro queda
     * alineado al terminar de girar.
     */
    public void iniciarGiro(Direccion nuevaDireccion, double destinoX, double destinoY)
    {
        if(girando || yaDecidioGiro){
            return;
        }

        yaDecidioGiro = true;
        girando = true;

        direccionDespuesDelGiro = nuevaDireccion;
        giroDestinoX = destinoX;
        giroDestinoY = destinoY;

        giroPasosRestantes = GIRO_PASOS;
        giroPasoX = (destinoX - x) / GIRO_PASOS;
        giroPasoY = (destinoY - y) / GIRO_PASOS;
    }

    public boolean isGirando(){
        return girando;
    }

    public boolean yaDecidioGiro(){
        return yaDecidioGiro;
    }

    /**
     * Marca que este carro ya paso por el punto de decision y va a
     * seguir derecho (no necesita animacion de giro).
     */
    public void marcarDecisionTomada(){
        yaDecidioGiro = true;
    }

    public void detener(){
        // Un carro que ya esta girando/cruzando no se puede detener a la mitad.
        if(girando){
            return;
        }
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
        return (int)x;
    }

    public int getY()
    {
        return (int)y;
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