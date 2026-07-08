/**
 * Write a description of class Control here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ControlCarril implements Runnable
{
    private PanelCruce panel;
    private Direccion direccion;

    public ControlCarril(PanelCruce panel, Direccion direccion)
    {
        this.panel = panel;
        this.direccion = direccion;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                // Recorre todos los carros
                for(Carro carro : panel.getCarros())
                {
                    // Solo mueve los carros de este carril
                    if(carro.getDireccion() == direccion)
                    {
                        // ¿Ya le toca decidir si sigue derecho o gira?
                        panel.actualizarGiro(carro);

                        // ¿Puede avanzar (semáforo en verde) y NO hay carro enfrente?
                        if(panel.puedeAvanzar(carro) && !panel.vaAChocar(carro))
                        {
                            carro.continuar();
                        }
                        else
                        {
                            carro.detener();
                        }

                        // Intenta avanzar
                        carro.avanzar();
                    }
                }

                // Actualiza la pantalla
                panel.repaint();

                // Espera 30 ms
                Thread.sleep(30);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}