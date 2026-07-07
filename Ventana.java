import javax.swing.JFrame;

public class Ventana extends JFrame
{
    public Ventana()
    {
        setTitle("Simulador de Semáforo");

        setSize(900,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new PanelCruce());

        setVisible(true);
    }
}
