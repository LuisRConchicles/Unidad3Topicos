import javax.swing.JFrame;

public class Ventana extends JFrame
{
    private PanelCruce panel;
    public Ventana()
    {
        setTitle("Simulador de Semáforo");

        setSize(900,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new PanelCruce();
        add(panel);
        
        setVisible(true);
        
        Thread hiloSemaforo = new Thread(new ControlSemaforo(panel));
        hiloSemaforo.start();
        
        Thread hiloNorte = new Thread(new ControlCarril(panel, Direccion.NORTE));
        Thread hiloSur = new Thread(new ControlCarril(panel, Direccion.SUR));
        Thread hiloEste = new Thread(new ControlCarril(panel, Direccion.ESTE));
        Thread hiloOeste = new Thread(new ControlCarril(panel, Direccion.OESTE));

        hiloNorte.start();
        hiloSur.start();
        hiloEste.start();
        hiloOeste.start();
    }
}
