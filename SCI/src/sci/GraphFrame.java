package sci;

import java.awt.*;
import javax.swing.*;

public class GraphFrame extends JFrame {
    private final int X = 500, Y = 500;
    private final int PADDING = 40;
            
    private void drawRect(Graphics g, Rectangle rect, Color x) {
        g.setColor(x);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    private void drawAxes(Graphics g, String x, String y) {
        // padding | graph | padding
        // (padding, padding) -> (padding, y-padding)
        g.drawLine(PADDING, PADDING, PADDING, Y - PADDING);
        g.drawLine(PADDING, Y - PADDING, X - PADDING, Y - PADDING);
        
        // (padding + (x - padding * 2) / 2)
        g.drawString(x, PADDING + (X - PADDING * 2) / 2, Y - PADDING / 2);
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
            
            drawAxes(g, "Test", "");
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
