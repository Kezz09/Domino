package domino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaPresentacion extends JFrame {

    public PantallaPresentacion() {
        setTitle("Pantalla de Presentación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centra la ventana

        // Panel con fondo
        JPanel panelFondo = new JPanel() {
            Image imagen = new ImageIcon(getClass().getResource("/fondos/fondo_presentacion.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(new BorderLayout());

        // Panel principal de contenido transparente
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel horizontal para logos y título
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        // Cargar y escalar logos
        ImageIcon originalLogoUni = new ImageIcon(getClass().getResource("/logos/logo_universidad.png"));
        Image imagenLogoUni = originalLogoUni.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon logoUni = new ImageIcon(imagenLogoUni);

        ImageIcon originalLogoFac = new ImageIcon(getClass().getResource("/logos/logo_facultad.png"));
        Image imagenLogoFac = originalLogoFac.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon logoFac = new ImageIcon(imagenLogoFac);

        JLabel labelLogoUni = new JLabel(logoUni);
        JLabel labelLogoFac = new JLabel(logoFac);

        // Título central
        JLabel titulo = new JLabel("Universidad Tecnológica de Panamá", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        // Añadir logos y título al panelHeader
        panelHeader.add(labelLogoUni, BorderLayout.WEST);
        panelHeader.add(titulo, BorderLayout.CENTER);
        panelHeader.add(labelLogoFac, BorderLayout.EAST);

        // Resto de la información (debajo del título y logos)
        JLabel facultad = new JLabel("Facultad de Sistemas");
        JLabel carrera = new JLabel("Carrera: Ingeniería de Software");
        JLabel grupo = new JLabel("Grupo: 1SF122");
        JLabel integrantes = new JLabel("<html>Integrantes:<br>- Kevin Marciaga - 8-1008-259<br>- Aldahir Sánchez - 8-997-2242<br>- Isaac Salinas - 20-14-8000<br>- Isaac Salinas - 20-14-8000</html>");
        JLabel profesor = new JLabel("Profesor: Rodrigo Yángüez");
        JLabel fecha = new JLabel("Fecha de entrega: 01/08/2025");

        JLabel[] textos = {facultad, carrera, grupo, integrantes, profesor, fecha};
        for (JLabel label : textos) {
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Añadir todo al panelContenido
        panelContenido.add(panelHeader);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        for (JLabel label : textos) {
            panelContenido.add(label);
            panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Panel para botón
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);

        JButton btnIniciar = new JButton("Iniciar");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIniciar.setPreferredSize(new Dimension(120, 40));

        btnIniciar.addActionListener(e -> {
            dispose(); // cerrar pantalla de presentación

            // Crear y mostrar el juego manualmente
            Juego juego = new Juego();
            Grafico gui = new Grafico(juego);
            gui.setVisible(true);

        });

        panelBoton.add(btnIniciar);

        // Agregar contenido y botón a panelFondo
        panelFondo.add(panelContenido, BorderLayout.CENTER);
        panelFondo.add(panelBoton, BorderLayout.SOUTH);

        add(panelFondo);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PantallaPresentacion();
    }
}
