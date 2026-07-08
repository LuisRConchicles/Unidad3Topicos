/**
 * Write a description of class Control here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ControlSemaforo implements Runnable
{
    private PanelCruce panel;

    private final int TIEMPO_VERDE = 5000;
    private final int TIEMPO_AMARILLO = 2000;
    // Colchón con ambos ejes en rojo, para que los carros que ya iban
    // cruzando (o girando) terminen de despejar el cruce antes de que
    // se le dé verde al otro eje.
    private final int TIEMPO_TODO_ROJO = 800;

    public ControlSemaforo(PanelCruce panel)
    {
        this.panel = panel;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                // Norte-Sur en VERDE
                panel.getNorte().cambiarEstado(Semaforo.VERDE);
                panel.getSur().cambiarEstado(Semaforo.VERDE);

                panel.getEste().cambiarEstado(Semaforo.ROJO);
                panel.getOeste().cambiarEstado(Semaforo.ROJO);

                panel.repaint();

                Thread.sleep(TIEMPO_VERDE);

                // Norte-Sur en AMARILLO
                panel.getNorte().cambiarEstado(Semaforo.AMARILLO);
                panel.getSur().cambiarEstado(Semaforo.AMARILLO);

                panel.repaint();

                Thread.sleep(TIEMPO_AMARILLO);

                // Norte-Sur en ROJO (todo el cruce en rojo un momento)
                panel.getNorte().cambiarEstado(Semaforo.ROJO);
                panel.getSur().cambiarEstado(Semaforo.ROJO);

                panel.repaint();

                Thread.sleep(TIEMPO_TODO_ROJO);

                // Este-Oeste en VERDE
                panel.getEste().cambiarEstado(Semaforo.VERDE);
                panel.getOeste().cambiarEstado(Semaforo.VERDE);

                panel.repaint();

                Thread.sleep(TIEMPO_VERDE);

                // Este-Oeste en AMARILLO
                panel.getEste().cambiarEstado(Semaforo.AMARILLO);
                panel.getOeste().cambiarEstado(Semaforo.AMARILLO);

                panel.repaint();

                Thread.sleep(TIEMPO_AMARILLO);

                // Este-Oeste en ROJO (todo el cruce en rojo un momento)
                panel.getEste().cambiarEstado(Semaforo.ROJO);
                panel.getOeste().cambiarEstado(Semaforo.ROJO);

                panel.repaint();

                Thread.sleep(TIEMPO_TODO_ROJO);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}