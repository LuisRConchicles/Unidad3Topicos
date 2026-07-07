import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class PanelCruce extends JPanel
{
    public PanelCruce()
    {
        setBackground(new Color(40,120,40));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);

        g.drawString("Simulador de Crucero",20,20);
    }
}
