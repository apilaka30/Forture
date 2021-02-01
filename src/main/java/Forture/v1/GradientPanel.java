package Forture.v1;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 *  Gradient Panel which makes the background a gradient color
 *
 *  @author  Ananth Pilaka, Chinmay Gowdru, Snehith Nayak
 *  @version May 28, 2019
 *  @author  Period: 1
 *  @author  Assignment: The Amazing Stock Steroid
 *
 *  @author  Sources: Our Team
 */
public class GradientPanel extends JPanel
{

    private int w, h;
    
    /**
     * constructor for gradient panel
     * @param w int w parameter
     * @param h int h parameter
     */
    public GradientPanel(int w, int h)
    {
        this.w = w;
        this.h = h;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = new Color(230, 50, 120);
        Color color2 = new Color(120, 50, 230);
        GradientPaint gp = new GradientPaint(0, 0, color2, 0, h, color1);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
    
}
