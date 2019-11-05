package sci;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;

public class GraphFrame extends JFrame {
    private final static int X = 500, Y = 500;
    private final static int PADDING = 40, PIE_PADDING = 30;
    private final static int XAXIS = X - PADDING * 2, YAXIS = Y - PADDING * 2;
    private final static int BAR_PADDING_MIN = 5;
    private final static int SCALE_LENGTH = 3;
    private final static int LINE_WIDTH = 1; // i think?
    
    protected static GraphicsRunnable painter;
            
    protected static void drawHistogramAxes(Graphics g, ArrayList<BigDecimal> values, BigDecimal xmin, boolean xmin_auto, BigDecimal xstep, boolean xstep_auto) {
        BigDecimal max = values.stream().max(Comparator.naturalOrder()).get();
        // assuming auto is false, do later
        for (BigDecimal i = xmin; i.compareTo(max) < 0; i = i.add(xstep)) {
            
        }
    }
    
    protected static void drawPie(Graphics g, ArrayList<String> values, String title) {
        drawTitle(g, title, g.getFont());
        HashMap<String, Integer> unique = new HashMap<>();
        values.forEach(s -> {
           if (unique.containsKey(s)) unique.put(s, unique.get(s) + 1);
           else unique.put(s, 1);
        });
        ArrayList<Map.Entry<String, Integer>> arr = new ArrayList<>();
        for (Map.Entry<String, Integer> x : unique.entrySet()){
            unique.put(x.getKey(), (int)((double)x.getValue() / values.size() * 360));
            arr.add(x);
        }
        int angle = 0;
        Color[] colors = new Color[] {new Color(188, 83, 77), new Color(81, 126, 194), new Color(155, 187, 88), new Color(179, 119, 63)}; //kys
        for (int i = 0; i < arr.size(); i++) {
            Map.Entry<String, Integer> x = arr.get(i);
            int theta = x.getValue();
            int width = X - PIE_PADDING * 2, height = Y - PIE_PADDING * 2;
            int x1 = (int)Math.round(Math.cos(Math.toRadians(theta / 2 + angle)) * width / 4) + (width / 2 + PIE_PADDING);
            int y1 = -(int)Math.round(Math.sin(Math.toRadians(theta / 2 + angle)) * width / 4) + (height / 2 + PIE_PADDING);
            String text = new BigDecimal(theta).divide(new BigDecimal(3.6), new MathContext(4)) + "% = " + x.getKey();
            if (arr.size() % 4 == 1 && i == arr.size() - 1)
                g.setColor(colors[1 + i % 2]);
            else
                g.setColor(colors[i % 4]);
            g.fillArc(PIE_PADDING, PIE_PADDING, width, height, angle, theta);
            g.setColor(Color.BLACK);
            g.drawString(text, x1, y1);
            
            angle += theta;
        }
    }
    
    protected static void drawBars(Graphics g, ArrayList<String> values) {
        // get unique values and count
        HashMap<String, Integer> unique = new HashMap<>();
        values.forEach(s -> {
            if (unique.containsKey(s)) unique.put(s, unique.get(s) + 1);
            else unique.put(s, 1);
        });
        // get the biggest height
        int maxHeight = 0;
        for (Map.Entry<String, Integer> x : unique.entrySet()) {
            if (x.getValue() > maxHeight) {
                maxHeight = x.getValue();
            }
        }
        //if (SCI.DEBUG) System.out.println(maxHeight);
        int bars = unique.size(); // number of bars
        int bar_width = XAXIS / bars - BAR_PADDING_MIN; // width of each bar
        FontMetrics fm = g.getFontMetrics();
        int min_scale_padding = (int)(fm.getMaxAscent());// * 0.7);
        
        // determine the space between each scale and the actual number each scale mark increases by (coefficient)
        int scale_padding = YAXIS / maxHeight - LINE_WIDTH;
        int coeff = 1;
        while (scale_padding < min_scale_padding) {
            coeff ++;
            scale_padding = YAXIS / (maxHeight / coeff) - LINE_WIDTH;
        }
        g.setColor(Color.BLACK);
        //if (SCI.DEBUG) System.out.println(scale_padding + " " + coeff + " " + fm.getMaxAscent()); //15 5 13
        
        // drawing the scale lines and numbers
        for (int j = 0; j <= Math.ceil(maxHeight / coeff) + 1; j++) {
            g.drawLine(PADDING - SCALE_LENGTH, PADDING + YAXIS - j * scale_padding, PADDING, PADDING + YAXIS - j * scale_padding);
            String num = Integer.toString(j * coeff);
            g.drawString(num, PADDING - SCALE_LENGTH - fm.stringWidth(num), PADDING + YAXIS - j * scale_padding + fm.getAscent() / 2);
        }
        
        Color[] colors = new Color[] {new Color(188, 83, 77), new Color(81, 126, 194), new Color(155, 187, 88), new Color(179, 119, 63)}; //kys
        int i = 0;
        for (Map.Entry<String, Integer> x : unique.entrySet()) {
            int freq = x.getValue();
            int x1 = PADDING + BAR_PADDING_MIN + i * (bar_width + BAR_PADDING_MIN), y1 = PADDING + YAXIS - (int)((double)freq / coeff * scale_padding);
            int w = bar_width, h = (int)((double)freq / coeff * scale_padding);
            drawRect(g, new Rectangle(x1, y1, w, h), colors[i % 4]);
            // draw the label and frequency of the bar
            String name = x.getKey();
            String num = Integer.toString(freq);
            int half_width = BAR_PADDING_MIN + PADDING + bar_width / 2 + i * (bar_width + BAR_PADDING_MIN);
            g.drawString(name, half_width - fm.stringWidth(name) / 2, PADDING + YAXIS + fm.getHeight());
            g.drawString(num, half_width - fm.stringWidth(num) / 2, y1 - fm.getMaxDescent());
            i++; // number of bars - 1
        }
        
        
    }
    
    protected static void drawRect(Graphics g, Rectangle rect, Color x) {
        g.setColor(x);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    protected static void drawTitle(Graphics g, String title, Font original) {
        Font bold = new Font(null, Font.BOLD, original.getSize() * 2);
        int titleLength = g.getFontMetrics(bold).stringWidth(title);
        g.setFont(bold);
        g.drawString(title, X / 2 - titleLength / 2, g.getFontMetrics(bold).getMaxAscent());
        g.setFont(original);
    }
    
    protected static void drawAxes(Graphics g, String x, String y, String title) {
        // padding | graph | padding
        // (padding, padding) -> (padding, y-padding)
        g.drawLine(PADDING, PADDING, PADDING, Y - PADDING);
        g.drawLine(PADDING, Y - PADDING, X - PADDING, Y - PADDING);
        
        Font original = g.getFont();
        // (padding + (x - padding * 2) / 2)
        int xLength = g.getFontMetrics().stringWidth(x), height = g.getFontMetrics().getMaxAscent();
        g.drawString(x, PADDING + XAXIS / 2 - xLength / 2, Y - g.getFontMetrics().getMaxDescent() - 5);
        
        drawTitle(g, title, original);
        
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
