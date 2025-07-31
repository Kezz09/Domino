package domino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FichaAnimada extends JComponent {
    private Image fichaImg;
    private int startX, startY, endX, endY;
    private double progress = 0.0;
    private Timer timer;
    private Runnable onFinish;

    public FichaAnimada(Image fichaImg, int startX, int startY, int endX, int endY, Runnable onFinish) {
        this.fichaImg = fichaImg;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.onFinish = onFinish;
        setOpaque(false);
        setBounds(0, 0, 2000, 2000); // Ajusta según tamaño ventana

        timer = new Timer(15, (ActionEvent e) -> animateStep());
        timer.start();
    }

    private void animateStep() {
        progress += 0.02; // 0.02*50 = 1 segundo aprox
        if (progress >= 1.0) {
            progress = 1.0;
            timer.stop();
            if (onFinish != null) SwingUtilities.invokeLater(onFinish);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (int)(startX + (endX - startX)*progress);
        int y = (int)(startY + (endY - startY)*progress);
        g.drawImage(fichaImg, x, y, Grafico.FICHA_ANCHO, Grafico.FICHA_ALTO, this);
    }
}
