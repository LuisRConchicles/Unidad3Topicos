import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.concurrent.CyclicBarrier;

public class Ventana extends JFrame
{
    private PanelCruce panel;

    public Ventana()
    {
        setTitle("Simulador de Semáforo");

        setSize(1180,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        panel = new PanelCruce();
        add(panel, BorderLayout.CENTER);

        // Tabla lateral: qué carro pertenece a qué hilo
        PanelTablaCarros panelTabla = new PanelTablaCarros(panel);
        add(panelTabla, BorderLayout.EAST);

        // Botón para reiniciar los carros
        JButton botonReiniciar = new JButton("Reiniciar carros");
        botonReiniciar.addActionListener(e -> {
            panel.reiniciarCarros();
            panel.repaint();
        });

        JPanel panelControles = new JPanel();
        panelControles.add(botonReiniciar);
        add(panelControles, BorderLayout.SOUTH);

        setVisible(true);

        // --- 4 hilos de semáforo (uno por dirección), sincronizados en
        // parejas (Norte-Sur y Este-Oeste) con un CyclicBarrier cada una,
        // y coordinados entre pares con el monitor ControlSemaforos ---
        ControlSemaforos controlSemaforos = new ControlSemaforos();
        
        // El tamaño de la barrera se fija en 2 para sincronizar limpiamente la pareja de hilos
        CyclicBarrier barreraNorteSur = new CyclicBarrier(2);
        CyclicBarrier barreraEsteOeste = new CyclicBarrier(2);

        Thread hiloNorte = new Thread(new HiloSemaforo(Direccion.NORTE, panel, controlSemaforos, barreraNorteSur, true));
        Thread hiloSur   = new Thread(new HiloSemaforo(Direccion.SUR,   panel, controlSemaforos, barreraNorteSur, false));
        Thread hiloEste  = new Thread(new HiloSemaforo(Direccion.ESTE,  panel, controlSemaforos, barreraEsteOeste, true));
        Thread hiloOeste = new Thread(new HiloSemaforo(Direccion.OESTE, panel, controlSemaforos, barreraEsteOeste, false));

        hiloNorte.start();
        hiloSur.start();
        hiloEste.start();
        hiloOeste.start();

        // --- 4 hilos de movimiento (uno por carril/dirección) ---
        Thread hiloCarrilNorte = new Thread(new ControlCarril(panel, Direccion.NORTE));
        Thread hiloCarrilSur   = new Thread(new ControlCarril(panel, Direccion.SUR));
        Thread hiloCarrilEste  = new Thread(new ControlCarril(panel, Direccion.ESTE));
        Thread hiloCarrilOeste = new Thread(new ControlCarril(panel, Direccion.OESTE));

        hiloCarrilNorte.start();
        hiloCarrilSur.start();
        hiloCarrilEste.start();
        hiloCarrilOeste.start();
    }
}