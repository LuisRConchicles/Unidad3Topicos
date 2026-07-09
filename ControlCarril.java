/**
 * Write a description of class Control here.
 * * @author (your name) 
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
                // (carros es un CopyOnWriteArrayList, asÃ­ que este for-each
                // es seguro incluso si otro hilo agrega o quita elementos
                // al mismo tiempo, sin necesidad de synchronized)
                for(Carro carro : panel.getCarros())
                {
                    // Solo mueve los carros de este carril
                    if(carro.getDireccion() == direccion)
                    {
                        // Â¿Ya le toca decidir si sigue derecho o gira?
                        panel.actualizarGiro(carro);

                        // Â¿Puede avanzar (semÃ¡foro en verde) y NO hay carro enfrente?
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

                // Limpia los carros de este carril que ya salieron del mapa
                // (evita que la lista crezca indefinidamente si se sigue
                // inyectando trÃ¡fico con intentarSpawnCarro)
                panel.limpiarCarrosFueraDePantalla();

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