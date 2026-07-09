import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Un hilo por cada dirección (NORTE, SUR, ESTE, OESTE) que controla
 * únicamente el semáforo de esa dirección. Los dos hilos de un mismo
 * par de calle (Norte-Sur o Este-Oeste) se sincronizan entre ellos con
 * un CyclicBarrier, para que cambien de color exactamente juntos.
 * El turno entre pares se coordina con el monitor ControlSemaforos.
 */
public class HiloSemaforo implements Runnable
{
    private Direccion direccion;
    private PanelCruce panel;
    private ControlSemaforos control;
    private CyclicBarrier barreraPareja;
    private boolean esLider; // el que le pasa el turno al otro par cuando termina el ciclo
    private boolean esParNorteSur;

    private final int TIEMPO_VERDE = 5000;
    private final int TIEMPO_AMARILLO = 2000;
    // Colchón con ambos ejes en rojo, para que los carros que ya iban
    // cruzando (o girando) terminen de despejar el cruce.
    private final int TIEMPO_TODO_ROJO = 800;

    public HiloSemaforo(Direccion direccion, PanelCruce panel, ControlSemaforos control, CyclicBarrier barreraPareja, boolean esLider)
    {
        this.direccion = direccion;
        this.panel = panel;
        this.control = control;
        this.barreraPareja = barreraPareja;
        this.esLider = esLider;
        this.esParNorteSur = (direccion == Direccion.NORTE || direccion == Direccion.SUR);
    }

    private Semaforo miSemaforo()
    {
        switch(direccion){
            case NORTE: return panel.getNorte();
            case SUR:   return panel.getSur();
            case ESTE:  return panel.getEste();
            default:    return panel.getOeste();
        }
    }

    @Override
    public void run()
    {
        while(true)
         {
            try
            {
                // Asegura la sincronización al inicio de cada ciclo
                barreraPareja.await();

                // Espera a que sea el turno de su par de calle
                control.esperarTurno(esParNorteSur);

                miSemaforo().cambiarEstado(Semaforo.VERDE);
                panel.repaint();
                barreraPareja.await();
                Thread.sleep(TIEMPO_VERDE);

                miSemaforo().cambiarEstado(Semaforo.AMARILLO);
                panel.repaint();
                barreraPareja.await();
                Thread.sleep(TIEMPO_AMARILLO);

                miSemaforo().cambiarEstado(Semaforo.ROJO);
                panel.repaint();
                barreraPareja.await();
                Thread.sleep(TIEMPO_TODO_ROJO);

                // Solo uno de los dos hilos del par le pasa el turno al otro
                if(esLider){
                    control.cederTurno();
                }
            }
            catch(InterruptedException | BrokenBarrierException e)
            {
                e.printStackTrace();
            }
        }
    }
}