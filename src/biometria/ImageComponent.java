package biometria;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

@SuppressWarnings("serial")
class ImageComponent extends JComponent {

    final BufferedImage img;

    public ImageComponent(String url) throws IOException {
    	URL u = new URL(url);
        img = ImageIO.read(u);
        setZoom(1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getPreferredSize();
        g.drawImage(img, 0, 0, dim.width, dim.height, this);
    }

    void setZoom(double zoom) {
        int w = (int) (zoom * img.getWidth());
        int h = (int) (zoom * img.getHeight());
        //System.out.println(w+ " "+h);
        setPreferredSize(new Dimension(w, h));
        revalidate();
        repaint();
    }
    
    public BufferedImage getImg(){
    	return img;
    }
}