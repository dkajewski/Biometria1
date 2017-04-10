package biometria;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Histogram{
	BufferedImage img = null;
	public Histogram(BufferedImage img){
		this.img=img;		
		setUpJFrame();
	}
	
	public void setUpJFrame(){
		JFrame hist = new JFrame();
		hist.setSize(1200, 400);
		hist.setVisible(true);
		int width = img.getWidth();
		int height = img.getHeight();
		
		int redarr[] = new int[256];
		int greenarr[] = new int[256];
		int bluearr[] = new int[256];
		for(int i=0; i<redarr.length; i++){
			redarr[i]=0;
			greenarr[i]=0;
			bluearr[i]=0;
		}
		int r, g, b;
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				int clr = img.getRGB(i, j);
				r = (clr & 0x00ff0000) >> 16;
				g = (clr & 0x0000ff00) >> 8;
				b =  clr & 0x000000ff;
				redarr[r]++;
				greenarr[g]++;
				bluearr[b]++;
				//System.out.println(r+" "+g+" "+b);
			}
		}
		
		
		/*int iloœæ = 0;
		for(int i=0; i<256; i++){
			System.out.println(i+": "+redarr[i]+" "+greenarr[i]+" "+bluearr[i]);
			iloœæ+=redarr[i];
		}
		System.out.println(iloœæ);*/
		
		hist.add(new HistogramPanel(redarr, greenarr, bluearr));
	}
}
