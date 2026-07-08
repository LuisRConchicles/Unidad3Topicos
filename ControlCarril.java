
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
                        // ¿Puede avanzar?
                        if(panel.puedeAvanzar(carro))
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
