import java.awt.Color;
import java.awt.Graphics;

public class Semaforo
{
    // Estados del semáforo
    public static final int ROJO = 0;
    public static final int AMARILLO = 1;
    public static final int VERDE = 2;

    // Posición
    private int x;
    private int y;

    // Estado actual
    private int estado;

    /**
     * Constructor
     */
    public Semaforo(int x, int y)
    {
        this.x = x;
        this.y = y;

        // Todos inician en rojo
        estado = ROJO;
    }

    /**
     * Cambia el estado del semáforo.
     */
    public void cambiarEstado(int estado)
    {
        this.estado = estado;
    }

    /**
     * Regresa el estado actual.
     */
    public int getEstado()
    {
        return estado;
    }

    /**
     * Dibuja el semáforo.
     */
    public void dibujar(Graphics g)
    {
        // Poste
        g.setColor(Color.BLACK);
        g.fillRect(x + 12, y + 70, 6, 30);

        // Caja del semáforo
        g.setColor(Color.BLACK);
        g.fillRoundRect(x, y, 30, 70, 10, 10);

        // Luz roja
        if(estado == ROJO)
            g.setColor(Color.RED);
        else
            g.setColor(Color.DARK_GRAY);

        g.fillOval(x + 5, y + 5, 20, 20);

        // Luz amarilla
        if(estado == AMARILLO)
            g.setColor(Color.YELLOW);
        else
            g.setColor(Color.DARK_GRAY);

        g.fillOval(x + 5, y + 25, 20, 20);

        // Luz verde
        if(estado == VERDE)
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.DARK_GRAY);

        g.fillOval(x + 5, y + 45, 20, 20);
    }
}