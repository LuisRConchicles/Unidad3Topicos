import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;

public class PanelCruce extends JPanel
{
    private Semaforo norte;
    private Semaforo sur;
    private Semaforo este;
    private Semaforo oeste;
    
    // CopyOnWriteArrayList: varios hilos (4 de carril + el de pintado)
    // leen/iteran esta lista constantemente, y solo ocasionalmente se
    // agregan o quitan carros. Este tipo de lista es thread-safe para
    // iterar sin bloqueos ni ConcurrentModificationException, y permite
    // remover elementos dentro de un for-each sin problema.
    private CopyOnWriteArrayList<Carro> carros;
    
    // LÃ­neas de ALTO: el borde del cruce donde el carro debe detenerse
    private final int PARADA_NORTE = 480; 
    private final int PARADA_SUR   = 185; 
    private final int PARADA_ESTE  = 285; 
    private final int PARADA_OESTE = 580; 

    // Centro del cruce
    private final int CENTRO_X = 450;
    private final int CENTRO_Y = 350;

    // Carriles
    private final int CARRIL_NORTE = 500; 
    private final int CARRIL_SUR   = 395; 
    private final int CARRIL_ESTE  = 400; 
    private final int CARRIL_OESTE = 295; 

    public PanelCruce()
    {
        Carro.reiniciarContador();

        norte = new Semaforo(435, 150);
        sur   = new Semaforo(435, 400);
        este  = new Semaforo(330, 260);
        oeste = new Semaforo(540, 260);
        
        carros = new CopyOnWriteArrayList<Carro>();

        int espacio = 90;
        for(int i = 0; i < 3; i++){
            carros.add(new Carro(CARRIL_NORTE, 600 + i * espacio, Direccion.NORTE));
            carros.add(new Carro(CARRIL_SUR, -40 - i * espacio, Direccion.SUR));
            carros.add(new Carro(0 - i * espacio, CARRIL_ESTE, Direccion.ESTE));
            carros.add(new Carro(850 + i * espacio, CARRIL_OESTE, Direccion.OESTE));
        }
        
        norte.cambiarEstado(Semaforo.VERDE);
        sur.cambiarEstado(Semaforo.VERDE);
        este.cambiarEstado(Semaforo.ROJO);
        oeste.cambiarEstado(Semaforo.ROJO);
        
        setBackground(new Color(40,120,40));
    }

    // MÃ©todo para inyectar trÃ¡fico de forma infinita (thread-safe gracias a CopyOnWriteArrayList)
    public void intentarSpawnCarro(Direccion dir) {
        int spawnX = 0, spawnY = 0;
        switch(dir) {
            case NORTE: spawnX = CARRIL_NORTE; spawnY = 720; break;
            case SUR:   spawnX = CARRIL_SUR;   spawnY = -70; break;
            case ESTE:  spawnX = -70;          spawnY = CARRIL_ESTE; break;
            case OESTE: spawnX = 920;          spawnY = CARRIL_OESTE; break;
        }

        for(Carro c : carros) {
            if(dir == c.getDireccion()) {
                if(dir == Direccion.NORTE && c.getY() > 630) return;
                if(dir == Direccion.SUR && c.getY() < 20) return;
                if(dir == Direccion.ESTE && c.getX() < 20) return;
                if(dir == Direccion.OESTE && c.getX() > 830) return;
            }
        }
        carros.add(new Carro(spawnX, spawnY, dir));
    }

    /**
     * Quita de la lista los carros que ya salieron del Ã¡rea visible
     * del panel, segÃºn su direcciÃ³n de avance. Evita que la lista
     * de carros crezca indefinidamente si se sigue inyectando trÃ¡fico.
     */
    public void limpiarCarrosFueraDePantalla()
    {
        for(Carro c : carros) {
            switch(c.getDireccion()) {
                case NORTE:
                    if(c.getY() < -60) carros.remove(c);
                    break;
                case SUR:
                    if(c.getY() > getHeight() + 60) carros.remove(c);
                    break;
                case ESTE:
                    if(c.getX() > getWidth() + 60) carros.remove(c);
                    break;
                case OESTE:
                    if(c.getX() < -60) carros.remove(c);
                    break;
            }
        }
    }

    public void reiniciarCarros()
    {
        carros.clear();
        Carro.reiniciarContador();
        
        int espacio = 90;
        for(int i = 0; i < 3; i++){
            carros.add(new Carro(CARRIL_NORTE, 600 + i * espacio, Direccion.NORTE));
            carros.add(new Carro(CARRIL_SUR, -40 - i * espacio, Direccion.SUR));
            carros.add(new Carro(0 - i * espacio, CARRIL_ESTE, Direccion.ESTE));
            carros.add(new Carro(850 + i * espacio, CARRIL_OESTE, Direccion.OESTE));
        }
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
        
        // LÃ­neas centrales verticales
        g.setColor(Color.WHITE);
        for(int y=0; y<getHeight(); y+=40){
            if(y>250 && y<450) continue;
            g.fillRect(447,y,6,20);
        }
        
        // LÃ­neas centrales horizontales
        for(int x=0; x<getWidth(); x+=40){
            if(x>350 && x<550) continue;
            g.fillRect(x,347,20,6);
        }
        
        // Borde lÃ­neas de parada
        g.setColor(Color.WHITE);
        g.fillRect(350, 220, 200, 5);  // NORTE
        g.fillRect(350, 475, 200, 5);  // SUR
        g.fillRect(320, 250, 5, 200);  // OESTE
        g.fillRect(575, 250, 5, 200);  // ESTE
        g.drawString("Simulador de Semaforo",20,20);
        
        norte.dibujar(g);
        sur.dibujar(g);
        este.dibujar(g);
        oeste.dibujar(g);
        
        for(Carro carro : carros){
            carro.dibujar(g);
        }
    }
    
    public CopyOnWriteArrayList<Carro> getCarros(){
        return carros;
    }
    
    public boolean puedeAvanzar(Carro carro){
        if(carro.isGirando()){
            return true;
        }

        final int ZONA_PARADA = 12;

        switch(carro.getDireccion()){
          case NORTE:
              if(carro.getY() >= PARADA_NORTE && carro.getY() <= PARADA_NORTE + ZONA_PARADA){
                  return sur.getEstado() == Semaforo.VERDE;
              }
              return true;

          case SUR:
              if(carro.getY() <= PARADA_SUR && carro.getY() >= PARADA_SUR - ZONA_PARADA){
                  return norte.getEstado() == Semaforo.VERDE;
              }
              return true;

          case ESTE:
              if(carro.getX() <= PARADA_ESTE && carro.getX() >= PARADA_ESTE - ZONA_PARADA){
                  return este.getEstado() == Semaforo.VERDE;
              }
              return true;

          case OESTE:
              if(carro.getX() >= PARADA_OESTE && carro.getX() <= PARADA_OESTE + ZONA_PARADA){
                  return oeste.getEstado() == Semaforo.VERDE;
              }
              return true;
        }
        return true;
    }

    public void actualizarGiro(Carro carro)
    {
        if(carro.yaDecidioGiro() || carro.isGirando()){
            return;
        }

        boolean enPuntoDeDecision = false;
        Direccion nuevaDireccion = carro.getDireccion();

        // Determina el punto exacto de decisiÃ³n Ãºnicamente para el giro a la DERECHA
        switch(carro.getDireccion()){
            case NORTE:
                if (carro.getY() <= CARRIL_ESTE && carro.getY() >= CARRIL_ESTE - 3) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.ESTE;
                }
                break;
            case SUR:
                if (carro.getY() >= CARRIL_OESTE && carro.getY() <= CARRIL_OESTE + 3) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.OESTE;
                }
                break;
            case ESTE:
                if (carro.getX() >= CARRIL_SUR && carro.getX() <= CARRIL_SUR + 3) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.SUR;
                }
                break;
            case OESTE:
                if (carro.getX() <= CARRIL_NORTE && carro.getX() >= CARRIL_NORTE - 3) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.NORTE;
                }
                break;
        }

        if(!enPuntoDeDecision){
            return;
        }

        // 65% sigue derecho de largo, 35% toma la curva a la derecha
        if(Math.random() < 0.65) {
            carro.marcarDecisionTomada();
            return;
        }

        int[] destino = puntoDeCarril(nuevaDireccion);
        carro.iniciarGiro(nuevaDireccion, destino[0], destino[1]); 
    }

    private int[] puntoDeCarril(Direccion direccion)
    {
        switch(direccion){
            case NORTE: return new int[]{CARRIL_NORTE, PARADA_SUR - 40};
            case SUR:   return new int[]{CARRIL_SUR, PARADA_NORTE + 40};
            case ESTE:  return new int[]{PARADA_OESTE + 40, CARRIL_ESTE};
            case OESTE: return new int[]{PARADA_ESTE - 40, CARRIL_OESTE};
        }
        return new int[]{CENTRO_X, CENTRO_Y};
    }

    public boolean vaAChocar(Carro actual) {
        int espacioSeguro = 45; 

        for(Carro otro : carros) {
            if(otro == actual) continue;

            // CORRECCIÃ“N: Si el de enfrente gira, no lo atravieses mÃ¡gicamente, guarda tu distancia.
            if(actual.getDireccion() == otro.getDireccion() && !actual.isGirando()) {
                switch(actual.getDireccion()) {
                    case NORTE:
                        if(actual.getY() > otro.getY() && actual.getY() - otro.getY() < espacioSeguro && Math.abs(actual.getX() - otro.getX()) < 20) return true;
                        break;
                    case SUR:
                        if(actual.getY() < otro.getY() && otro.getY() - actual.getY() < espacioSeguro && Math.abs(actual.getX() - otro.getX()) < 20) return true;
                        break;
                    case ESTE:
                        if(actual.getX() < otro.getX() && otro.getX() - actual.getX() < espacioSeguro && Math.abs(actual.getY() - otro.getY()) < 20) return true;
                        break;
                    case OESTE:
                        if(actual.getX() > otro.getX() && actual.getX() - otro.getX() < espacioSeguro && Math.abs(actual.getY() - otro.getY()) < 20) return true;
                        break;
                }
            }
        }
        return false;
    }

    public Semaforo getNorte(){ return norte; }
    public Semaforo getSur(){ return sur; }
    public Semaforo getEste(){ return este; }
    public Semaforo getOeste(){ return oeste; }
}