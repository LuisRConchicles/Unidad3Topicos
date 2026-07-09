import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

/**
 * Panel lateral que muestra, en una tabla, a qué hilo (dirección)
 * pertenece cada carro, su estado actual y su dirección de avance. 
 * Se refresca sola cada 200 ms mientras la simulación corre.
 */
public class PanelTablaCarros extends JPanel
{
    private PanelCruce panelCruce;
    private DefaultTableModel modelo;

    public PanelTablaCarros(PanelCruce panelCruce)
    {
        this.panelCruce = panelCruce;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(320, 0)); // Ampliado ligeramente para acomodar la nueva columna
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Monitoreo de Hilos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(50, 50, 50));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Añadida la columna "Dirección" al arreglo de encabezados
        String[] columnas = {"#", "Hilo", "Dirección", "Estado"};
        modelo = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int fila, int columna){
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setShowVerticalLines(false); // Estilo plano sin rejillas verticales

        // Diseño del encabezado de la tabla
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(45, 45, 48));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Renderizadores para centrar el texto de los datos
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Ajustar anchos relativos de las 4 columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(95);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Refresca la tabla sola, sin bloquear los hilos de la simulación
        Timer temporizador = new Timer(200, e -> actualizar());
        temporizador.start();
    }

    private void actualizar()
    {
        modelo.setRowCount(0);

        for(Carro carro : panelCruce.getCarros()){
            modelo.addRow(new Object[]{
                carro.getId(),
                "Hilo " + carro.getDireccion(),
                carro.getDireccion(), // Nueva celda que extrae el ENUM de dirección directamente
                carro.getEstadoTexto()
            });
        }
    }
}