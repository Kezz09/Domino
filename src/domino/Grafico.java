package domino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Grafico extends JFrame {
    public static final int FICHA_ANCHO = 75;
    public static final int FICHA_ALTO = 150;

    private Juego juego;
    private FondoPanel panelMesa, panelJugador, panelComputadora, panelPozo;
    private JLabel estadoJuego;
    private JButton pasarTurnoBtn;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel inicioPanel;
    private JButton btnJugar;
    private JLayeredPane layeredPane;

    private Map<Ficha, JLabel> fichaLabelJugador = new HashMap<>();
    private Map<Ficha, JLabel> fichaLabelPozo = new HashMap<>();

    public Grafico(Juego juego) {
        // Cursor personalizado
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            URL url = getClass().getClassLoader().getResource("cursor/mi_cursor.png");
            if (url != null) {
                Image image = toolkit.getImage(url);
                Cursor c = toolkit.createCustomCursor(image, new Point(0, 0), "MiCursor");
                setCursor(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Pantalla de inicio
        inicioPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imgUrl = getClass().getClassLoader().getResource("fichas/fondo_inicio.png");
                if (imgUrl != null) {
                    ImageIcon bgIcon = new ImageIcon(imgUrl);
                    Image img = bgIcon.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        inicioPanel.setLayout(new GridBagLayout());

        URL botonUrl = getClass().getClassLoader().getResource("fichas/boton_jugar.png");
        ImageIcon iconoJugar = null;
        if (botonUrl != null) {
            iconoJugar = new ImageIcon(botonUrl);
            Image img = iconoJugar.getImage().getScaledInstance(180, 135, Image.SCALE_SMOOTH);
            iconoJugar = new ImageIcon(img);
        }
        btnJugar = new JButton(iconoJugar);
        btnJugar.setBorderPainted(false);
        btnJugar.setContentAreaFilled(false);
        btnJugar.setFocusPainted(false);
        btnJugar.setOpaque(false);
        btnJugar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnJugar.addActionListener(e -> {
            Sonidos.playSound("sonidos/click.wav");
            this.juego = new Juego();
            mostrarPantallaJuego();
        });
        inicioPanel.add(btnJugar, new GridBagConstraints());

        JPanel juegoPanel = crearPanelJuego();
        mainPanel.add(inicioPanel, "inicio");
        mainPanel.add(juegoPanel, "juego");

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        mainPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        setContentPane(layeredPane);
        setTitle("Dominó - Proyecto Final");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                mainPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                for (Component c : layeredPane.getComponentsInLayer(JLayeredPane.POPUP_LAYER)) {
                    c.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                }
                mainPanel.revalidate();
            }
        });

        mostrarPantallaInicio();
        setVisible(true);
    }

    private JPanel crearPanelJuego() {
        JPanel panel = new JPanel(new BorderLayout());

        estadoJuego = new JLabel("Tu turno", SwingConstants.CENTER);
        estadoJuego.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(estadoJuego, BorderLayout.NORTH);

        // Panel de la mesa con fondo
        panelMesa = new FondoPanel("fondos/mesa_bg.png");
        panelMesa.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelMesa.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Mesa", 0, 0, null, Color.WHITE));
        panel.add(panelMesa, BorderLayout.CENTER);

        // Panel de mi mano con fondo
        panelJugador = new FondoPanel("fondos/mano_jugador_bg.png");
        panelJugador.setLayout(new FlowLayout());
        panelJugador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Tus fichas", 0, 0, null, Color.WHITE));
        panel.add(panelJugador, BorderLayout.SOUTH);

        // Panel de la mano de la computadora con fondo
        panelComputadora = new FondoPanel("fondos/mano_pc_bg.png");
        panelComputadora.setLayout(new FlowLayout());
        panelComputadora.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Fichas de la computadora", 0, 0, null, Color.WHITE));
        panel.add(panelComputadora, BorderLayout.NORTH);

        // Panel del pozo con fondo
        panelPozo = new FondoPanel("fondos/pozo_bg.png");
        panelPozo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelPozo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Pozo (clic para robar)", 0, 0, null, Color.WHITE));
        panelPozo.setPreferredSize(new Dimension(120, 400));
        panel.add(panelPozo, BorderLayout.EAST);

        pasarTurnoBtn = new JButton("Pasar turno");
        pasarTurnoBtn.setBackground(Color.PINK);
        pasarTurnoBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        panel.add(pasarTurnoBtn, BorderLayout.WEST);

        return panel;
    }

    private void mostrarPantallaInicio() {
        Sonidos.playBackground("sonidos/background.wav", true); // Solo aquí
        cardLayout.show(mainPanel, "inicio");
    }

    private void mostrarPantallaJuego() {
        Sonidos.stopBackground(); // Detenemos background al ir al juego
        mainPanel.remove(1);
        JPanel nuevoPanelJuego = crearPanelJuego();
        mainPanel.add(nuevoPanelJuego, "juego");
        cardLayout.show(mainPanel, "juego");
        actualizarVista();
    }

    public void actualizarVista() {
        fichaLabelJugador.clear();
        fichaLabelPozo.clear();
        panelMesa.removeAll();
        panelJugador.removeAll();
        panelComputadora.removeAll();
        panelPozo.removeAll();

        // MESA
        for (Ficha ficha : juego.getMesa()) {
            panelMesa.add(crearLabelFicha(ficha));
        }

        // JUGADOR
        for (Ficha ficha : juego.getFichasJugador()) {
            JLabel fichaLabel = crearLabelFicha(ficha);
            fichaLabelJugador.put(ficha, fichaLabel);
            fichaLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Sonidos.playSound("sonidos/click.wav");
                    if (!juego.esTurnoJugador()) return;
                    Point origen = calcularPosicionEnPantalla(panelJugador, fichaLabel);
                    Point destino = calcularDestinoMesa();
                    animarMovimientoFicha(ficha, origen, destino, () -> {
                        Sonidos.playSound("sonidos/ficha.wav");
                        if (juego.jugarFichaJugador(ficha)) {
                            actualizarVista();
                            if (!juego.esTurnoJugador() && juego.chequearGanador() == null) {
                                SwingUtilities.invokeLater(() -> {
                                    animarTurnoComputadoraConRobo();
                                });
                            }
                        }
                    });
                }
            });
            panelJugador.add(fichaLabel);
        }

        // COMPUTADORA (reverso)
        for (Ficha ficha : juego.getFichasComputadora()) {
            JLabel reverso = crearLabelFichaReverso();
            panelComputadora.add(reverso);
        }

        // POZO
        for (Ficha ficha : juego.getPozo()) {
            JLabel reverso = crearLabelFichaReverso();
            fichaLabelPozo.put(ficha, reverso);
            reverso.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Sonidos.playSound("sonidos/click.wav");
                    if (!juego.esTurnoJugador()) return;
                    Point origen = calcularPosicionEnPantalla(panelPozo, reverso);
                    Point destino = calcularDestinoJugador();
                    animarMovimientoFicha(ficha, origen, destino, () -> {
                        Sonidos.playSound("sonidos/robar.wav");
                        if (juego.robarDelPozo()) {
                            actualizarVista();
                            if (!juego.esTurnoJugador() && juego.chequearGanador() == null) {
                                SwingUtilities.invokeLater(() -> {
                                    animarTurnoComputadoraConRobo();
                                });
                            }
                        }
                    });
                }
            });
            panelPozo.add(reverso);
        }

        // Ganador o bloqueo
        String ganador = juego.chequearGanador();
        if (ganador != null) {
            Sonidos.stopBackground();
            if (ganador.toLowerCase().contains("ganaste") || ganador.toLowerCase().contains("victoria")) {
                Sonidos.playSound("sonidos/victoria.wav");
            } else if (ganador.toLowerCase().contains("perdiste") || ganador.toLowerCase().contains("derrota")) {
                Sonidos.playSound("sonidos/derrota.wav");
            }
            JOptionPane.showMessageDialog(this, ganador, "¡Juego terminado!", JOptionPane.INFORMATION_MESSAGE);
            for (Component c : panelJugador.getComponents()) c.setEnabled(false);
            for (Component c : panelPozo.getComponents()) c.setEnabled(false);
            pasarTurnoBtn.setEnabled(false);
            estadoJuego.setText(ganador);

            Timer timer = new Timer(2000, e -> mostrarPantallaInicio());
            timer.setRepeats(false);
            timer.start();
        } else {
            estadoJuego.setText(juego.esTurnoJugador() ? "Tu turno" : "Turno de la computadora");
            pasarTurnoBtn.setEnabled(juego.esTurnoJugador());
        }

        revalidate();
        repaint();
    }

    //ANIMACIÓN

    public void animarMovimientoFicha(Ficha ficha, Point origen, Point destino, Runnable alTerminar) {
        int a = ficha.getLadoA();
        int b = ficha.getLadoB();
        int menor = Math.min(a, b);
        int mayor = Math.max(a, b);
        String nombre = "fichas/ficha_" + menor + "_" + mayor + ".png";
        URL imgURL = getClass().getClassLoader().getResource(nombre);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(FICHA_ANCHO, FICHA_ALTO, Image.SCALE_SMOOTH);
            final FichaAnimada[] animada = new FichaAnimada[1];
            animada[0] = new FichaAnimada(img, origen.x, origen.y, destino.x, destino.y, () -> {
                layeredPane.remove(animada[0]);
                layeredPane.repaint();
                alTerminar.run();
            });
            animada[0].setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
            layeredPane.add(animada[0], JLayeredPane.POPUP_LAYER);
            layeredPane.repaint();
        } else {
            alTerminar.run();
        }
    }

    private Point calcularPosicionEnPantalla(JPanel panel, Component fichaLabel) {
        Point p = fichaLabel.getLocation();
        SwingUtilities.convertPointToScreen(p, panel);
        Point frame = this.getLocationOnScreen();
        return new Point(p.x - frame.x, p.y - frame.y);
    }

    private Point calcularDestinoMesa() {
        Point mesaPanel = panelMesa.getLocationOnScreen();
        Point frame = this.getLocationOnScreen();
        int offset = panelMesa.getComponentCount() * (FICHA_ANCHO + 10);
        return new Point(mesaPanel.x - frame.x + offset, mesaPanel.y - frame.y + 30);
    }

    private Point calcularDestinoJugador() {
        Point jugadorPanel = panelJugador.getLocationOnScreen();
        Point frame = this.getLocationOnScreen();
        int offset = panelJugador.getComponentCount() * (FICHA_ANCHO + 5);
        return new Point(jugadorPanel.x - frame.x + offset, jugadorPanel.y - frame.y + 15);
    }

    // FIN ANIMACIÓN

    // Animación de turno de la computadora con robo
    private void animarTurnoComputadoraConRobo() {
        Ficha ficha = juego.buscarFichaParaComputadora();
        if (ficha != null) {
            JLabel reverso = panelComputadora.getComponentCount() > 0 ? (JLabel) panelComputadora.getComponent(0) : null;
            Point origen;
            if (reverso != null) {
                origen = calcularPosicionEnPantalla(panelComputadora, reverso);
            } else {
                origen = new Point(50, 50);
            }
            Point destino = calcularDestinoMesa();
            animarMovimientoFicha(ficha, origen, destino, () -> {
                Sonidos.playSound("sonidos/ficha.wav");
                juego.jugarTurnoComputadora();
                actualizarVista();
            });
        } else if (!juego.getPozo().isEmpty()) {
            Ficha fichaRobo = juego.getPozo().get(0);
            JLabel reversoPozo = fichaLabelPozo.get(fichaRobo);
            if (reversoPozo == null && panelPozo.getComponentCount() > 0)
                reversoPozo = (JLabel) panelPozo.getComponent(0);
            Point origen = reversoPozo != null ?
                    calcularPosicionEnPantalla(panelPozo, reversoPozo) : new Point(panelPozo.getX(), panelPozo.getY());
            Point destino = new Point(50, 80); // Mano virtual computadora
            animarMovimientoFicha(fichaRobo, origen, destino, () -> {
                Sonidos.playSound("sonidos/robar.wav");
                juego.robarFicha(juego.getFichasComputadora());
                actualizarVista();
                SwingUtilities.invokeLater(this::animarTurnoComputadoraConRobo);
            });
        } else {
            juego.jugarTurnoComputadora();
            actualizarVista();
        }
    }

    private JLabel crearLabelFicha(Ficha ficha) {
        int a = ficha.getLadoA();
        int b = ficha.getLadoB();
        int menor = Math.min(a, b);
        int mayor = Math.max(a, b);
        String nombre = "fichas/ficha_" + menor + "_" + mayor + ".png";
        URL imgURL = getClass().getClassLoader().getResource(nombre);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(FICHA_ANCHO, FICHA_ALTO, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            return new JLabel(icon);
        } else {
            System.err.println("No se encontró la imagen: " + nombre);
            return new JLabel("[" + ficha.getLadoA() + "|" + ficha.getLadoB() + "]");
        }
    }

    private JLabel crearLabelFichaReverso() {
        URL imgURL = getClass().getClassLoader().getResource("fichas/ficha_reverso.png");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(FICHA_ANCHO, FICHA_ALTO, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            return new JLabel(icon);
        } else {
            return new JLabel("[X|X]");
        }
    }
}

