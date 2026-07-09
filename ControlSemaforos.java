/**
 * Monitor compartido entre los 4 hilos de semáforo (uno por dirección).
 * Decide de quién es el turno: del par Norte-Sur o del par Este-Oeste.
 * Un hilo que quiera ponerse en verde debe esperar aquí a que sea el
 * turno de su par.
 */
public class ControlSemaforos
{
    // true = le toca al par Norte-Sur, false = le toca al par Este-Oeste
    private boolean turnoNorteSur = true;

    /**
     * Bloquea al hilo hasta que sea el turno de su par.
     * @param esParNorteSur true si quien pregunta es Norte o Sur.
     */
    public synchronized void esperarTurno(boolean esParNorteSur) throws InterruptedException
    {
        while(turnoNorteSur != esParNorteSur){
            wait();
        }
    }

    /**
     * Le pasa el turno al otro par y despierta a los hilos que estén
     * esperando.
     */
    public synchronized void cederTurno()
    {
        turnoNorteSur = !turnoNorteSur;
        notifyAll();
    }
}