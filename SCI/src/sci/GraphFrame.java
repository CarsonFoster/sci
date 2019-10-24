package sci;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class GraphFrame extends JFrame {
    private final static int X = 500, Y = 500;
    private final static int PADDING = 40;
    private final static int XAXIS = X - PADDING * 2, YAXIS = Y - PADDING * 2;
    private final static int BAR_PADDING_MIN = 5;
    
    protected static GraphicsRunnable painter;
            
    protected static void drawBars(Graphics g, ArrayList<String> values) {
        HashMap<String, Integer> unique = new HashMap<>();
        values.forEach(s -> {
            if (unique.containsKey(s)) unique.put(s, unique.get(s));
            else unique.put(s, 1);
        });
        int maxHeight = 0;
        int bars = unique.size();
        int bar_width = XAXIS / bars - BAR_PADDING_MIN;
        
    }
    
    protected static void drawRect(Graphics g, Rectangle rect, Color x) {
        g.setColor(x);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    protected static void drawAxes(Graphics g, String x, String y, String title) {
        // padding | graph | padding
        // (padding, padding) -> (padding, y-padding)
        g.drawLine(PADDING, PADDING, PADDING, Y - PADDING);
        g.drawLine(PADDING, Y - PADDING, X - PADDING, Y - PADDING);
        
        Font original = g.getFont();
        // (padding + (x - padding * 2) / 2)
        int xLength = g.getFontMetrics().stringWidth(x), height = g.getFontMetrics().getMaxAscent();
        g.drawString(x, PADDING + XAXIS / 2 - xLength / 2, Y - g.getFontMetrics().getMaxDescent());
        
        Font bold = new Font(null, Font.BOLD, original.getSize() * 2);
        int titleLength = g.getFontMetrics(bold).stringWidth(title);
        g.setFont(bold);
        g.drawString(title, X / 2 - titleLength / 2, g.getFontMetrics(bold).getMaxAscent());
        
        int yLength = g.getFontMetrics(original).stringWidth(y);
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(-90), 0, 0);
        Font rotated = original.deriveFont(at);
        g.setFont(rotated);
        g.drawString(y, height, PADDING + (YAXIS / 2) + (yLength / 2));
        g.setFont(original);
    }
    
    class SciPanel extends JPanel {
        
        public SciPanel() {
            //setBorder(BorderFactory.createLineBorder(Color.black));
        }
        
        public Dimension getPreferredSize() {
            return new Dimension(X, Y);
        } 
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            painter.paint(g);
        }
    }
    
    public GraphFrame() {
        super("SCI Graphing");
        //setPreferredSize(new Dimension(X, Y));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new SciPanel());
        pack();
        setVisible(false);
    }
}

interface GraphicsRunnable {
    public void paint(Graphics g);
}
