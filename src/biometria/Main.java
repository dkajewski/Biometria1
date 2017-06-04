package biometria;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main{
	static ImageComponent image = null;
	static BufferedImage img;
	static double zoom = 1;
	
	static int red = 0;
	static int green = 0;
	static int blue = 0;
	
	static int x = 0;
	static int y = 0;
	
	static int[] redValues = new int[256];
	static int[] greenValues = new int[256];
	static int[] blueValues = new int[256];
	static int[] meanValues = new int[256];
	
	static double[][] meanPixels;
	
	static JPanel imagePanel = new JPanel();
	static JSlider slider = new JSlider();
	static JPanel leftPanel = new JPanel();
	static JTextField Rvalue = new JTextField();
	static JTextField Gvalue = new JTextField();
	static JTextField Bvalue = new JTextField();
	static JLabel selectedColor = new JLabel();
	static JButton save = new JButton("Save");
	static JButton histograms = new JButton("Histograms");
	static JScrollPane scroll = new JScrollPane();
	
	static double[][] rPixels;
	static double[][] gPixels;
	static double[][] bPixels;
	//static double[][] rgbPixels;
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Biometria");
		frame.setSize(new Dimension(1000, 600));
		
		JPanel panel = new JPanel();
		
		JPanel rightPanel = new JPanel();
		rightPanel.setSize(new Dimension(400, 600));
		
		histograms.setBounds((rightPanel.getWidth()/2)-70, 191, 150, 40);
		histograms.setEnabled(false);
		
		JButton brighter = new JButton("Brighter");
		brighter.setBounds(50, 235, 150, 40);
		
		JButton darker = new JButton("Darker");
		darker.setBounds(200, 235, 150, 40);
		
		JLabel minLbl = new JLabel("min:");
		minLbl.setBounds((rightPanel.getWidth()/2)-190, 290, 50, 20);
		
		JTextField minVal = new JTextField();
		minVal.setBounds((rightPanel.getWidth()/2)-160, 290, 50, 20);
		
		JLabel maxLbl = new JLabel("max:");
		maxLbl.setBounds((rightPanel.getWidth()/2)-100, 290, 50, 20);
		
		JTextField maxVal = new JTextField();
		maxVal.setBounds((rightPanel.getWidth()/2)-70, 290, 50, 20);
		
		JButton stretch = new JButton("Stretch");
		stretch.setBounds((rightPanel.getWidth()/2), 280, 150, 40);
		
		JTextField binaryThreshold = new JTextField("0");
		binaryThreshold.setBounds(40, 335, 50, 20);
		
		JButton binary = new JButton("Binary");
		binary.setBounds(100, 325, 100, 40);
		
		JButton otsu = new JButton("Otsu");
		otsu.setBounds(220, 325, 100, 40);
		
		JTextField ratio = new JTextField("-0.5");
		ratio.setBounds(40, 390, 30, 20);
		
		JTextField width = new JTextField("3");
		width.setBounds(80, 390, 30, 20);
		
		JButton niblack = new JButton("Niblack");
		niblack.setBounds((rightPanel.getWidth()/2)-50, 380, 100, 40);
		
		save.setBounds((rightPanel.getWidth()/2)-70, 150, 150, 40);
		save.setEnabled(false);
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser jfc = new JFileChooser();
				int retVal = jfc.showSaveDialog(null);
				if(retVal==JFileChooser.APPROVE_OPTION){
				    File f = jfc.getSelectedFile();
				    try {
						ImageIO.write(img,"png", new File(f.getAbsolutePath()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				 }
			}
			
		});
		
		slider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	        	//System.out.println(8. * slider.getValue() / slider.getMaximum());
	        	zoom = 8. * slider.getValue() / slider.getMaximum();
	            image.setZoom(8. * slider.getValue() / slider.getMaximum());
	        }
	     
	    });
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		leftPanel.setPreferredSize(new Dimension(600, 600));
		leftPanel.setMinimumSize(new Dimension(600, 600));
		leftPanel.setMaximumSize(new Dimension(600, 600));
		
		rightPanel.setPreferredSize(new Dimension(400, 600));
		rightPanel.setMinimumSize(new Dimension(400, 600));
		rightPanel.setMaximumSize(new Dimension(400, 600));
		
		
		JButton browse = new JButton("Browse");
		browse.setBounds((rightPanel.getWidth()/2)-70, 10, 150, 40);
		//browse.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel R = new JLabel("R:");
		R.setBounds(90, 120, 20, 20);
		
		Rvalue.setBounds(103, 120, 30, 20);
		
		JLabel G = new JLabel("G:");
		G.setBounds(180, 120, 20, 20);
		
		Gvalue.setBounds(193, 120, 30, 20);
		
		JLabel B = new JLabel("B:");
		B.setBounds(270, 120, 20, 20);
		
		Bvalue.setBounds(283, 120, 30, 20);
		
		Rvalue.setText("0");
		Gvalue.setText("0");
		Bvalue.setText("0");
		
		selectedColor.setBounds(192, 80, 30, 30);
		selectedColor.setBackground(Color.BLACK);
		selectedColor.setOpaque(true);
		selectedColor.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				Color newColor = JColorChooser.showDialog(null, "Choose a color", new Color(red, green, blue));
				if(newColor != null){
					Rvalue.setText(newColor.getRed()+"");
					Gvalue.setText(newColor.getGreen()+"");
					Bvalue.setText(newColor.getBlue()+"");
					
					selectedColor.setBackground(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue()));
					
					if(img!=null){
						img.setRGB(x, y, newColor.getRGB());
						File tempFile = new File("temp.png");
						String path = tempFile.getAbsolutePath();
			            String tempFilePath = "file:///";
			            for(int i=0; i<path.length(); i++){
			            	if(path.charAt(i)=='\\'){
			            		tempFilePath+="/";
			            	}else{
			            		tempFilePath+=path.charAt(i);
			            	}
			            }
						try {
							ImageIO.write(img, "png", tempFile);
							image = new ImageComponent(tempFilePath);
							image.addMouseListener(new MouseListener() {
								
								@Override
								public void mouseReleased(MouseEvent arg0) {
									
								}
								
								@Override
								public void mousePressed(MouseEvent e) {
									
									x = (int)(e.getX()/zoom);
									y = (int)(e.getY()/zoom);
									
									//System.out.println(x+" "+y);
									int clr=  img.getRGB(x, y);
									red   = (clr & 0x00ff0000) >> 16;
			            			green = (clr & 0x0000ff00) >> 8;
			            			blue  =  clr & 0x000000ff;
									Rvalue.setText(red+"");
									Gvalue.setText(green+"");
									Bvalue.setText(blue+"");
									selectedColor.setBackground(new Color(red, green, blue));
								}
								
								@Override
								public void mouseExited(MouseEvent arg0) {
									
								}
								
								@Override
								public void mouseEntered(MouseEvent arg0) {
									
								}
								
								@Override
								public void mouseClicked(MouseEvent arg0) {
									
								}
							});
							
							
							imagePanel.removeAll();
							imagePanel.add(slider);
							JScrollPane scroll = new JScrollPane(image);
							scroll.setPreferredSize(new Dimension(570, 520));
							scroll.setVisible(true);
							imagePanel.add(scroll);
							imagePanel.revalidate();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
					}
				}
			}
			
		});
		
		rightPanel.setLayout(null);
		browse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				browse();	
			}
			
		});
		
		histograms.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Histogram(img);
			}
			
		});
		
		brighter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setBrightness();
				
			}
		
		});
		
		darker.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				darkImage();
				
			}
			
		});
		
		stretch.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stretchImage(Integer.parseInt(minVal.getText()), Integer.parseInt(maxVal.getText()));
				
			}
			
		});
		
		binary.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				binary(Integer.parseInt(binaryThreshold.getText()));
				
			}
			
		});
		
		otsu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				double threshold = threshold();
				binary((int) threshold);
			}
			
		});
		
		niblack.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				niblackBinary(Double.parseDouble(ratio.getText()), Integer.parseInt(width.getText()));
			}
		});
		
		rightPanel.add(browse);
		rightPanel.add(R);
		rightPanel.add(Rvalue);
		rightPanel.add(G);
		rightPanel.add(Gvalue);
		rightPanel.add(B);
		rightPanel.add(Bvalue);
		rightPanel.add(selectedColor);
		rightPanel.add(save);
		rightPanel.add(histograms);
		rightPanel.add(brighter);
		rightPanel.add(darker);
		rightPanel.add(minLbl);
		rightPanel.add(minVal);
		rightPanel.add(maxLbl);
		rightPanel.add(maxVal);
		rightPanel.add(stretch);
		rightPanel.add(binaryThreshold);
		rightPanel.add(binary);
		rightPanel.add(otsu);
		rightPanel.add(ratio);
		rightPanel.add(width);
		rightPanel.add(niblack);
		panel.add(leftPanel);
		panel.add(rightPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void setBrightness(){
		Double r, g, b;
		initRGBarrs();
		for(int x=0; x<img.getWidth(); x++){
			for(int y=0; y<img.getHeight(); y++){
				int clr= img.getRGB(x, y);
				r = ((clr & 0x00ff0000) >> 16)*1.0;
    			g = ((clr & 0x0000ff00) >> 8)*1.0;
    			b =  (clr & 0x000000ff)*1.000;
    			rPixels[x][y] = Math.log(r+1)*255;
    			gPixels[x][y] = Math.log(g+1)*255;
    			bPixels[x][y] = Math.log(b+1)*255;
    			
    			
    			//int rgb = new Color(r2, g2, b2).getRGB();
    			
    			//System.out.println(rgb);
    			
    			//img.setRGB(x, y, rgb);
			}
		}
		normalizeRGB();
		//System.out.println(rgbPixels[46][54]);
		//testowe
		File tempFile = new File("temp.png");
		String path = tempFile.getAbsolutePath();
        String tempFilePath = "file:///";
        System.out.println("pliczek");
        for(int i=0; i<path.length(); i++){
        	if(path.charAt(i)=='\\'){
        		tempFilePath+="/";
        	}else{
        		tempFilePath+=path.charAt(i);
        	}
        }
		try {
			ImageIO.write(img, "png", tempFile);
			image = new ImageComponent(tempFilePath);
			img = image.getImg();
			
			imagePanel.removeAll();
			imagePanel.add(slider);
			JScrollPane scroll = new JScrollPane(image);
			scroll.setPreferredSize(new Dimension(570, 520));
			scroll.setVisible(true);
			imagePanel.add(scroll);
			imagePanel.revalidate();
		} catch(IOException e){
			e.printStackTrace();
		}
		//testowe koniec
	}
	
	public static void initRGBarrs(){
		rPixels = new double[(int) img.getHeight()][(int) img.getWidth()];
		gPixels = new double[(int) img.getHeight()][(int) img.getWidth()];
		bPixels = new double[(int) img.getHeight()][(int) img.getWidth()];
		//rgbPixels = new double[(int) img.getHeight()][(int) img.getWidth()];
	}
	
	public static void normalizeRGB(){
		rPixels = normalizeArr(rPixels);
		gPixels = normalizeArr(gPixels);
		bPixels = normalizeArr(bPixels);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				//rgbPixels[x][y] = (int) rPixels[x][y] << 16 | (int) gPixels[x][y] << 8 | (int) bPixels[x][y];
				img.setRGB(x, y, new Color((int)rPixels[x][y], (int)gPixels[x][y], (int)bPixels[x][y]).getRGB());
			}
		}
	}
	
	public static double[][] normalizeArr(double[][] arr){
		double min = 0, max = 0;
		for (double[] x : arr) {
			for (double y : x) {
				if (min > y)
					min = y;
				if (max < y)
					max = y;
			}
		}
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < arr[j].length; i++) {
				arr[j][i] = ((arr[j][i] - min) / (max - min)) * 255;
			}
		}
		return arr;
	}
	
	public static void darkImage(){
		initRGBarrs();
		double r, g, b;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int clr= img.getRGB(x, y);
				r = ((clr & 0x00ff0000) >> 16)*1.0;
    			g = ((clr & 0x0000ff00) >> 8)*1.0;
    			b =  (clr & 0x000000ff)*1.0;
				rPixels[x][y] = r * r;
				gPixels[x][y] = g * g;
				bPixels[x][y] = b * b;
			}
		}
		normalizeRGB();
		
		File tempFile = new File("temp.png");
		String path = tempFile.getAbsolutePath();
        String tempFilePath = "file:///";
        System.out.println("pliczek");
        for(int i=0; i<path.length(); i++){
        	if(path.charAt(i)=='\\'){
        		tempFilePath+="/";
        	}else{
        		tempFilePath+=path.charAt(i);
        	}
        }
		try {
			ImageIO.write(img, "png", tempFile);
			image = new ImageComponent(tempFilePath);
			img = image.getImg();
			
			imagePanel.removeAll();
			imagePanel.add(slider);
			JScrollPane scroll = new JScrollPane(image);
			scroll.setPreferredSize(new Dimension(570, 520));
			scroll.setVisible(true);
			imagePanel.add(scroll);
			imagePanel.revalidate();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void stretchImage(int min, int max){
		initRGBarrs();
		double r, g, b;
		int aa = img.getRGB(7, 9);
		double a = ((aa & 0x00ff0000) >> 16)*1.0;
		double bb = ((aa & 0x0000ff00) >> 8)*1.0;
		double c =  (aa & 0x000000ff)*1.0;
		System.out.println(a/255 + " " + bb/255 + " " + c/255);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int clr= img.getRGB(x, y);
				r = (((clr & 0x00ff0000) >> 16)*1.0) / 255;
    			g = (((clr & 0x0000ff00) >> 8)*1.0) / 255;
    			b =  ((clr & 0x000000ff)*1.0) / 255;
				rPixels[x][y] = 255 / (max - min) * (r * 255 - min);
				gPixels[x][y] = 255 / (max - min) * (g * 255 - min);
				bPixels[x][y] = 255 / (max - min) * (b * 255 - min);
			}
		}
		
		normalizeRGB();
		
		File tempFile = new File("temp.png");
		String path = tempFile.getAbsolutePath();
        String tempFilePath = "file:///";
        System.out.println("pliczek");
        for(int i=0; i<path.length(); i++){
        	if(path.charAt(i)=='\\'){
        		tempFilePath+="/";
        	}else{
        		tempFilePath+=path.charAt(i);
        	}
        }
		try {
			ImageIO.write(img, "png", tempFile);
			image = new ImageComponent(tempFilePath);
			img = image.getImg();
			
			imagePanel.removeAll();
			imagePanel.add(slider);
			JScrollPane scroll = new JScrollPane(image);
			scroll.setPreferredSize(new Dimension(570, 520));
			scroll.setVisible(true);
			imagePanel.add(scroll);
			imagePanel.revalidate();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void binary(int treshold) {
		initRGBarrs();
		double r, g, b;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int clr = img.getRGB(i, j);
				r = ((((clr & 0x00ff0000) >> 16))*1.0)/2;
    			g = (((clr & 0x0000ff00) >> 8)*1.0)/2;
    			b = ((clr & 0x000000ff)*1.0)/2;
    			//System.out.println(r+" "+g+" "+b);
				if ((r+g+b)/3 > treshold) {
					rPixels[i][j] = 255;
					gPixels[i][j] = 255;
					bPixels[i][j] = 255;
				} else {
					rPixels[i][j] = 0;
					gPixels[i][j] = 0;
					bPixels[i][j] = 0;
				}

			}
		}
		normalizeRGB();
		
		File tempFile = new File("temp.png");
		String path = tempFile.getAbsolutePath();
        String tempFilePath = "file:///";
        System.out.println("pliczek");
        for(int i=0; i<path.length(); i++){
        	if(path.charAt(i)=='\\'){
        		tempFilePath+="/";
        	}else{
        		tempFilePath+=path.charAt(i);
        	}
        }
		try {
			ImageIO.write(img, "png", tempFile);
			image = new ImageComponent(tempFilePath);
			img = image.getImg();
			
			imagePanel.removeAll();
			imagePanel.add(slider);
			JScrollPane scroll = new JScrollPane(image);
			scroll.setPreferredSize(new Dimension(570, 520));
			scroll.setVisible(true);
			imagePanel.add(scroll);
			imagePanel.revalidate();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static double calcThreshold(int[] n_t, int N, int sum) {
		double threshold = 0;
		double variance; // objective function to maximize
		double bestVariance = Double.NEGATIVE_INFINITY;
		double mean_bg = 0;
		double weight_bg = 0;
		double mean_fg = (double) sum / (double) N; // mean of population
		double weight_fg = N; // weight of population
		double diff_means;

		int t = 0;
		while (t < 256) {
			diff_means = mean_fg - mean_bg;
			variance = weight_bg * weight_fg * diff_means * diff_means;

			if (variance > bestVariance) {
				bestVariance = variance;
				threshold = t;
			}

			while (t < 255 && n_t[t] == 0)
				t++;

			mean_bg = (mean_bg * weight_bg + n_t[t] * t) / (weight_bg + n_t[t]);
			mean_fg = (mean_fg * weight_fg - n_t[t] * t) / (weight_fg - n_t[t]);
			weight_bg += n_t[t];
			weight_fg -= n_t[t];
			t++;
		}
		return threshold;
	}
	
	private static int sumIntensities(int[] n_t) {
		int sum = 0;
		for (int i = 0; i < n_t.length; i++)
			sum += i * n_t[i];
		return sum;
	}
	
	protected static double threshold() {
		getPixelsValues();
		int sum = sumIntensities(meanValues);
		return calcThreshold(meanValues, img.getWidth() * img.getHeight(), sum);
	}
	
	private static void getPixelsValues() {
		double mean = 0;
		//pixelsColor = new Color[(int) img.getWidth()][(int) img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				//Color color = img.getRGB(x, y);
				//pixelsColor[x][y] = color;
				int clr = img.getRGB(x, y);
				redValues[(int) (((((clr & 0x00ff0000) >> 16))*1.0)/2)]++;
				blueValues[(int) ((((clr & 0x0000ff00) >> 8)*1.0)/2)]++;
				greenValues[(int) (((clr & 0x000000ff)*1.0)/2)]++;
				mean = (double) ((((((((clr & 0x00ff0000) >> 16))*1.0)/2) + ((((clr & 0x0000ff00) >> 8)*1.0)/2) + (((clr & 0x000000ff)*1.0)/2))) / 3);
				meanValues[(int) mean]++;
			}
		}
	}
	
	private static void niblackBinary(double ratio, int width) {
		getPixelsValues();
		initRGBarrs();
		int x = (int) img.getWidth();
		int y = (int) img.getHeight();
		meanPixels = new double[x][y];
		int r, g, b;
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				int clr = img.getRGB(i, j);
				r = (int) ((((clr & 0x00ff0000) >> 16))*1.0)/2;
    			g = (int)  (((clr & 0x0000ff00) >> 8)*1.0)/2;
    			b = (int)   ((clr & 0x000000ff)*1.0)/2;
				meanPixels[i][j] = (r + g + b) / 3;
			}
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				double mean = getMean(i, j, width, y, x);
				double odchylenie = getOdchylenieStandardowe(i, j, width, mean, y, x);
				double niblackTreshold = mean + ratio * odchylenie;
				if (meanPixels[i][j] > niblackTreshold) {
					rPixels[i][j] = 255;
					gPixels[i][j] = 255;
					bPixels[i][j] = 255;
				} else {
					rPixels[i][j] = 0;
					gPixels[i][j] = 0;
					bPixels[i][j] = 0; 
				}

			}
		}
		normalizeRGB();  
		
		File tempFile = new File("temp.png");
		String path = tempFile.getAbsolutePath();
        String tempFilePath = "file:///";
        System.out.println("pliczek");
        for(int i=0; i<path.length(); i++){
        	if(path.charAt(i)=='\\'){
        		tempFilePath+="/";
        	}else{
        		tempFilePath+=path.charAt(i);
        	}
        }
		try {
			ImageIO.write(img, "png", tempFile);
			image = new ImageComponent(tempFilePath);
			img = image.getImg();
			
			imagePanel.removeAll();
			imagePanel.add(slider);
			JScrollPane scroll = new JScrollPane(image);
			scroll.setPreferredSize(new Dimension(570, 520));
			scroll.setVisible(true);
			imagePanel.add(scroll);
			imagePanel.revalidate();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static double getOdchylenieStandardowe(int x, int y, int width, double mean, int maxH, int maxW) {
		double odchylenie = 0;
		int a = 0;
		for (int i = x - width / 2; i <= x + width / 2; i++) {
			for (int j = y - width / 2; j <= y + width / 2; j++) {

				if (i < 0 || j < 0 | i >= maxW || j >= maxH)
					continue;
				if (i == x && j == y)
					continue;

				odchylenie += ((mean - meanPixels[i][j]) * (mean - meanPixels[i][j]));
				a++;
			}
		}
		return Math.sqrt(odchylenie / a);
	}
	
	private static double getMean(int x, int y, int width, int maxH, int maxW) {
		double mean = 0;
		int a = 0;
		for (int i = x - width / 2; i <= x + width / 2; i++) {
			for (int j = y - width / 2; j <= y + width / 2; j++) {

				if (i < 0 || j < 0 | i >= maxW || j >= maxH)
					continue;
				if (i == x && j == y)
					continue;

				mean += meanPixels[i][j];
				a++;
			}
		}
		return mean / a;
	}
	
	public static void browse(){
		if(imagePanel!=null){
			imagePanel.remove(slider);
			imagePanel.remove(scroll);
		}
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.home")+"\\Desktop"));
		int returnVal = fc.showOpenDialog(fc);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            System.out.println("Otwieranie: " + file.getName() + ".");
            
            String path = file.getAbsolutePath();
            String filePath = "file:///";
            for(int i=0; i<path.length(); i++){
            	if(path.charAt(i)=='\\'){
            		filePath+="/";
            	}else{
            		filePath+=path.charAt(i);
            	}
            }
            
            System.out.println(filePath);
            
            try {
            	imagePanel.setPreferredSize(new Dimension(600, 550));
				image = new ImageComponent(filePath);
				img = image.getImg();
				image.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent arg0) {
						
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						
						x = (int)(e.getX()/zoom);
						y = (int)(e.getY()/zoom);
						
						System.out.println(x+" "+y);
						int clr=  img.getRGB(x, y);
						red   = (clr & 0x00ff0000) >> 16;
            			green = (clr & 0x0000ff00) >> 8;
            			blue  =  clr & 0x000000ff;
						Rvalue.setText(red+"");
						Gvalue.setText(green+"");
						Bvalue.setText(blue+"");
						selectedColor.setBackground(new Color(red, green, blue));
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) {
						
					}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {
						
					}
					
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
					}
				});
				
				//leftPanel.setBackground(Color.CYAN);
				imagePanel.add(slider);
				scroll = new JScrollPane(image);
				scroll.setPreferredSize(new Dimension(570, 520));
				scroll.setVisible(true);
				imagePanel.add(scroll);
				//leftPanel.add(scroll);
				//imagePanel.revalidate();
				leftPanel.add(imagePanel);
				leftPanel.revalidate();
				save.setEnabled(true);
				histograms.setEnabled(true);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        } else {
            System.out.println("Operacja anulowana przez u¿ytkownika");
        }	
	}
}


