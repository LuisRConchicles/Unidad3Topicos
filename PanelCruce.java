import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class PanelCruce extends JPanel
{
    private Semaforo norte;
    private Semaforo sur;
    private Semaforo este;
    private Semaforo oeste;
    
    public PanelCruce()
    {
        norte = new Semaforo(470,180);
        sur = new Semaforo(400,470);
        este = new Semaforo(610,330);
        oeste = new Semaforo(260,260);
        
        setBackground(new Color(40,120,40));
    }

    protected void paintComponent(Graphics g){
        
        super.paintComponent(g);
        
        // Fondo (Pasto)
        g.setColor(new Color(40,120,40));
        g.fillRect(0,0,getWidth(),getHeight());
        
        // Calle Vertical
        g.setColor(Color.DARK_GRAY);
        g.fillRect(350,0,200,getHeight());
        
        // Calle Horizontal
        g.fillRect(0,250,getWidth(),200);
        
        // Líneas centrales verticales
        g.setColor(Color.WHITE);
        
        for(int y=0; y<getHeight(); y+=40){
            if(y>250 && y<450)
            continue;
            
            g.fillRect(447,y,6,20);
        }
        
        // Líneas centrales horizontales
        for(int x=0; x<getWidth(); x+=40){
            if(x>350 && x<550)
            continue;
            
            g.fillRect(x,347,20,6);
        }
        
        //Nombre
        g.setColor(Color.WHITE);
        g.drawString("Simulador de Semaforo",20,20);
        
        norte.dibujar(g);
        sur.dibujar(g);
        este.dibujar(g);
        oeste.dibujar(g);
    }
}