package domino;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FondoPanel extends JPanel {
    private Image fondo;

    public FondoPanel(String ruta) {
        URL url = getClass().getClassLoader().getResource(ruta);
        if (url != null) {
            fondo = new ImageIcon(url).getImage();
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
