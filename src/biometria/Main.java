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
	
	static JPanel imagePanel = new JPanel();
	static JSlider slider = new JSlider();
	
	static double[][] rPixels;
	static double[][] gPixels;
	static double[][] bPixels;
	//static double[][] rgbPixels;
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Biometria");
		frame.setSize(new Dimension(1000, 600));
		
		JPanel panel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		rightPanel.setSize(new Dimension(400, 600));
		
		JButton save = new JButton("Save");
		JButton histograms = new JButton("Histograms");
		histograms.setBounds((rightPanel.getWidth()/2)-70, 250, 150, 40);
		histograms.setEnabled(false);
		
		JButton brighter = new JButton("Brighter");
		brighter.setBounds((rightPanel.getWidth()/2)-70, 350, 150, 40);
		
		
		save.setBounds((rightPanel.getWidth()/2)-70, 200, 150, 40);
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
		
		JTextField Rvalue = new JTextField();
		Rvalue.setBounds(103, 120, 30, 20);
		
		JLabel G = new JLabel("G:");
		G.setBounds(180, 120, 20, 20);
		
		JTextField Gvalue = new JTextField();
		Gvalue.setBounds(193, 120, 30, 20);
		
		JLabel B = new JLabel("B:");
		B.setBounds(270, 120, 20, 20);
		
		JTextField Bvalue = new JTextField();
		Bvalue.setBounds(283, 120, 30, 20);
		
		Rvalue.setText("0");
		Gvalue.setText("0");
		Bvalue.setText("0");
		
		JLabel selectedColor = new JLabel();
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
						JScrollPane scroll = new JScrollPane(image);
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
				r   = ((clr & 0x00ff0000) >> 16)*1.0;
    			g = ((clr & 0x0000ff00) >> 8)*1.0;
    			b  =  (clr & 0x000000ff)*1.000;
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
				img.setRGB(x, y, new Color((int)rPixels[x][y], (int)gPixels[x][y], (int)bPixels[x][x]).getRGB());
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
}


