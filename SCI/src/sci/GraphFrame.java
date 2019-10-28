package sci;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GraphFrame extends JFrame {
    private final static int X = 500, Y = 500;
    private final static int PADDING = 40;
    private final static int XAXIS = X - PADDING * 2, YAXIS = Y - PADDING * 2;
    private final static int BAR_PADDING_MIN = 5;
    private final static int SCALE_PADDING_MIN = 2, SCALE_LENGTH = 3;
    
    protected static GraphicsRunnable painter;
            
    protected static void drawBars(Graphics g, ArrayList<String> values) {
        HashMap<String, Integer> unique = new HashMap<>();
        values.forEach(s -> {
            if (unique.containsKey(s)) unique.put(s, unique.get(s) + 1);
            else unique.put(s, 1);
        });
        int maxHeight = 0;
        for (Map.Entry<String, Integer> x : unique.entrySet()) {
            if (x.getValue() > maxHeight) {
                maxHeight = x.getValue();
            }
        }
        int bars = unique.size();
        int bar_width = XAXIS / bars - BAR_PADDING_MIN;
        int scale_padding = YAXIS / maxHeight, i = 0;
        int coeff = 1;
        while (scale_padding < SCALE_PADDING_MIN) {
            coeff ++;
            scale_padding = YAXIS / (maxHeight / coeff);
        }
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        for (int j = 0; j < maxHeight / coeff; j++) {
            g.drawLine(PADDING - SCALE_LENGTH, PADDING + YAXIS - j * scale_padding, PADDING, PADDING + YAXIS - j * scale_padding);
            String num = Integer.toString(j * coeff);
            g.drawString(num, PADDING - SCALE_LENGTH - fm.stringWidth(num), PADDING + YAXIS - j * scale_padding + fm.getAscent() / 2);
        }
        Color[] colors = new Color[] {new Color(188, 83, 77), new Color(81, 126, 194), new Color(155, 187, 88), new Color(179, 119, 63)}; //kys
        
        
        for (Map.Entry<String, Integer> x : unique.entrySet()) {
            int freq = x.getValue();
            String name = x.getKey();
            drawRect(g, new Rectangle(PADDING + BAR_PADDING_MIN + i * (bar_width + BAR_PADDING_MIN), PADDING + YAXIS - freq / coeff * scale_padding, bar_width, freq / coeff * scale_padding), colors[i % 4]);
            g.drawString(name, BAR_PADDING_MIN + PADDING + bar_width / 2 + i * (bar_width + BAR_PADDING_MIN) - fm.stringWidth(name) / 2, PADDING + YAXIS + fm.getHeight());
            i++;
        }
        
        
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
