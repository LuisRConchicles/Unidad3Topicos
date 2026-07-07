
import java.awt.Color;
import java.awt.Graphics;

public class Semaforo
{
    // Posición del semáforo
    private int x;
    private int y;

    // Color actual
    private Color color;

    public Semaforo(int x, int y)
    {
        this.x = x;
        this.y = y;

        // Todos empiezan en rojo
        color = Color.RED;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public void dibujar(Graphics g)
    {
        // Poste
        g.setColor(Color.BLACK);
        g.fillRect(x + 10, y + 50, 10, 40);

        // Caja
        g.setColor(Color.BLACK);
        g.fillRoundRect(x, y, 30, 60, 10, 10);

        // Luz
        g.setColor(color);
        g.fillOval(x + 5, y + 5, 20, 20);
    }
}
