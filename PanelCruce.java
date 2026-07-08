import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class PanelCruce extends JPanel
{
    private Semaforo norte;
    private Semaforo sur;
    private Semaforo este;
    private Semaforo oeste;
    
    private ArrayList<Carro> carros;
    
    // Líneas donde deben detenerse los carros
    private final int ALTO_NORTE = 230;
    private final int ALTO_SUR = 440;
    private final int ALTO_ESTE = 550;
    private final int ALTO_OESTE = 330;
    public PanelCruce()
    {
        norte = new Semaforo(435,150);
        sur = new Semaforo(435,400);
        este = new Semaforo(330,260);
        oeste = new Semaforo(540,260);
        
        carros = new ArrayList<Carro>();
        carros.add(new Carro(410,600,Direccion.NORTE));
        carros.add(new Carro(460,650,Direccion.NORTE));
        carros.add(new Carro(420,-40,Direccion.SUR));
        carros.add(new Carro(0,280,Direccion.ESTE));
        carros.add(new Carro(850,380,Direccion.OESTE));
        
        norte.cambiarEstado(Semaforo.VERDE);
        sur.cambiarEstado(Semaforo.VERDE);
        este.cambiarEstado(Semaforo.ROJO);
        oeste.cambiarEstado(Semaforo.ROJO);
        
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
        //NORTE
        g.fillRect(350, 220, 200, 5);
        //SUR
        g.fillRect(350, 475, 200, 5);
        //OESTE
        g.fillRect(320, 250, 5, 200);
        //ESTE
        g.fillRect(575, 250, 5, 200);
        g.drawString("Simulador de Semaforo",20,20);
        
        norte.dibujar(g);
        sur.dibujar(g);
        este.dibujar(g);
        oeste.dibujar(g);
        
        for(Carro carro : carros){
            carro.dibujar(g);
        }
    }
    
    public ArrayList<Carro> getCarros(){
        return carros;
    }
    
    public boolean puedeAvanzar(Carro carro){
        switch(carro.getDireccion()){
          case NORTE:  
              if(carro.getY() <= ALTO_NORTE){
                  return norte.getEstado() != Semaforo.ROJO; 
              }
              return true;
              
               case SUR:
                   if(carro.getY() >= ALTO_SUR){
                       return sur.getEstado() != Semaforo.ROJO;
                   }
                   return true;
                   
                   case ESTE:
                       if(carro.getX() >= ALTO_ESTE){
                          return este.getEstado() != Semaforo.ROJO; 
                       }
                       return true;
                       
                       case OESTE:
                           if(carro.getX() <= ALTO_OESTE){
                               return oeste.getEstado() != Semaforo.ROJO;
                           }
                           return true;
        }
        return true;
    }
    
    
    
    public Semaforo getNorte(){
        return norte;
    }
    public Semaforo getSur(){
        return sur;
    }
    public Semaforo getEste(){
        return este;
    }
    public Semaforo getOeste(){
        return oeste;
    }
    
}
