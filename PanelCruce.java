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
    
    // Líneas de ALTO: el borde del cruce donde el carro debe detenerse
    // ANTES de entrar. Una vez que el carro las cruza, ya no se le
    // vuelve a checar el semáforo: no se puede quedar a mitad del cruce.
    private final int PARADA_NORTE = 480; // borde sur de la calle horizontal
    private final int PARADA_SUR   = 185; // borde norte de la calle horizontal
    private final int PARADA_ESTE  = 285; // borde oeste de la calle vertical
    private final int PARADA_OESTE = 580; // borde este de la calle vertical

    // Centro del cruce, usado para decidir cuándo un carro "elige" girar
    private final int CENTRO_X = 450;
    private final int CENTRO_Y = 350;

    // Carril (centro de línea) de cada dirección, según las flechas moradas:
    // calle vertical -> carril izquierdo baja (SUR), carril derecho sube (NORTE)
    // calle horizontal -> carril de arriba va a la izquierda (OESTE),
    //                      carril de abajo va a la derecha (ESTE)
    private final int CARRIL_NORTE = 500; // carril derecho de la calle vertical
    private final int CARRIL_SUR   = 395; // carril izquierdo de la calle vertical
    private final int CARRIL_ESTE  = 400; // carril de abajo de la calle horizontal
    private final int CARRIL_OESTE = 295; // carril de arriba de la calle horizontal

    public PanelCruce()
    {
        norte = new Semaforo(435,150);
        sur = new Semaforo(435,400);
        este = new Semaforo(330,260);
        oeste = new Semaforo(540,260);
        
        carros = new ArrayList<Carro>();

        // 3 carros por carril, separados para que no se encimen
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
    
    /**
     * Decide si un carro puede avanzar. El carro maneja libre, sin
     * importar el semáforo, hasta que llega justo a la línea de alto
     * (donde iría el cruce peatonal). Ahí sí se checa el semáforo: si
     * está en rojo se detiene EXACTAMENTE en la línea (no desde que
     * arranca). En cuanto logra cruzar la línea (o ya está girando)
     * nunca se le vuelve a checar, para que no se quede a la mitad.
     */
    /**
     * Decide si un carro puede avanzar. Ahora, si el carro está en la zona de parada
     * DETRÁS de la línea blanca, SOLO podrá avanzar si el semáforo está estrictamente
     * en VERDE. Si está en AMARILLO o ROJO, decidirá detenerse.
     * Una vez cruzada la línea, ignora el semáforo para no quedarse a la mitad.
     */
    public boolean puedeAvanzar(Carro carro){

        if(carro.isGirando()){
            return true;
        }

        // Qué tan cerca de la línea tiene que estar para que importe el semáforo
        final int ZONA_PARADA = 8;

        switch(carro.getDireccion()){
          case NORTE:
              if(carro.getY() >= PARADA_NORTE && carro.getY() <= PARADA_NORTE + ZONA_PARADA){
                  return norte.getEstado() == Semaforo.VERDE;
              }
              return true;

          case SUR:
              if(carro.getY() <= PARADA_SUR && carro.getY() >= PARADA_SUR - ZONA_PARADA){
                  return sur.getEstado() == Semaforo.VERDE;
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

    /**
     * Revisa si un carro ya llegó al punto donde debe decidir si sigue
     * derecho o da vuelta a la derecha (no se permiten vueltas a la
     * izquierda), y si es así, inicia el giro. Si el carro ya decidió
     * (o ya está girando), no hace nada.
     */
    public void actualizarGiro(Carro carro)
    {
        if(carro.yaDecidioGiro() || carro.isGirando()){
            return;
        }

        boolean enPuntoDeDecision = false;
        Direccion nuevaDireccion = carro.getDireccion();

        // Ahora deciden girar justo cuando su frente toca el carril hacia donde van
        switch(carro.getDireccion()){
            case NORTE:
                if (carro.getY() <= CARRIL_ESTE) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.ESTE;
                }
                break;
            case SUR:
                if (carro.getY() >= CARRIL_OESTE) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.OESTE;
                }
                break;
            case ESTE:
                if (carro.getX() >= CARRIL_SUR) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.SUR;
                }
                break;
            case OESTE:
                if (carro.getX() <= CARRIL_NORTE) { 
                    enPuntoDeDecision = true;
                    nuevaDireccion = Direccion.NORTE;
                }
                break;
        }

        if(!enPuntoDeDecision){
            return;
        }

        // 60% sigue derecho, 40% da vuelta
        if(Math.random() < 0.6) {
            carro.marcarDecisionTomada();
            return;
        }

        int[] destino = puntoDeCarril(nuevaDireccion);
        carro.iniciarGiro(nuevaDireccion, destino[0], destino[1]);
    }

    // 60% sigue derecho, 40% da vuelta a la derecha (no se permite la izquierda)
    private Direccion elegirGiro(Direccion actual)
    {
        double r = Math.random();

        if(r < 0.6){
            return actual;
        }
        return derecha(actual);
    }

    private Direccion derecha(Direccion actual)
    {
        switch(actual){
            case NORTE: return Direccion.ESTE;
            case SUR:   return Direccion.OESTE;
            case ESTE:  return Direccion.SUR;
            case OESTE: return Direccion.NORTE;
        }
        return actual;
    }

    // Punto de entrada al carril nuevo (ya dentro del cruce) para cada dirección
    private int[] puntoDeCarril(Direccion direccion)
    {
        switch(direccion){
            // Les damos un empujoncito (+20 o -20) para que acaben de salir del centro al girar
            case NORTE: return new int[]{CARRIL_NORTE, PARADA_SUR - 20};
            case SUR:   return new int[]{CARRIL_SUR, PARADA_NORTE + 20};
            case ESTE:  return new int[]{PARADA_OESTE + 20, CARRIL_ESTE};
            case OESTE: return new int[]{PARADA_ESTE - 20, CARRIL_OESTE};
        }
        return new int[]{CENTRO_X, CENTRO_Y};
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

    public boolean vaAChocar(Carro actual) {
        int espacioSeguro = 45; // Distancia mínima en píxeles para frenar

        for(Carro otro : carros) {
            if(otro == actual) continue;

            // Revisamos choques por alcance (mismo carril y dirección)
            if(actual.getDireccion() == otro.getDireccion() && !actual.isGirando() && !otro.isGirando()) {
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
    
}