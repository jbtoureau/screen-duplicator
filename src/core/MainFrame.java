package core;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author JBT
 *
 */
public class MainFrame extends JFrame implements ActionListener{

/**
	 * 
	 */
	private static final long serialVersionUID = -8385537002160864651L;
	//Menu
	JMenuBar menuBar;
	JMenu menu1;
	JMenuItem item11;
	JCheckBox boxScreenZone;
	JMenuItem item13;
	JCheckBox boxStart;
	
	JMenu menuFPS;
	JMenuItem fps05;
	JMenuItem fps01;
	JMenuItem fps1;
	JMenuItem fps6;
	JMenuItem fps12;
	JMenuItem fps24;
	JMenuItem fps30;
	JMenuItem fps60;
	JMenuItem[] list;
	
	JMenu menuApropos;
	JMenuItem info ;
//Screenshoter
	Screenshooter sS;
	float[] fps = {0.1f,0.5f,1f,6f,12f,24f,30f,60f};
	
//Display
	ImageIcon display;
	JLabel displayLabel;
	
	public MainFrame() {
		//this.setAlwaysOnTop(true);
		this.setName("Screnshooter");
		this.setTitle("Screen Mirroring");
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeMenu();
		sS = new Screenshooter(this);
		sS.setFps(24);
		display = new ImageIcon();
		displayLabel = new JLabel(display);
		displayLabel.setVisible(true);
		this.add(displayLabel);
	}
	
	
	private void initializeMenu(){
		
		menuBar = new JMenuBar();
		
		//menu1
		menu1 = new JMenu("Menu");
		item11 = new JMenuItem("Show ScreenZone");
		boxScreenZone = new JCheckBox("Zone de capture");
		boxScreenZone.setSize(1000, 500);
		item11.add(boxScreenZone);
		menu1.add(item11);
		boxScreenZone.addActionListener(this);
			
		item13 = new JMenuItem("Capture");
		boxStart = new JCheckBox("Capture");
		boxStart.setEnabled(false);
		item13.add(boxStart);
		menu1.add(item13);
		boxStart.addActionListener(this);
			
		menuBar.add(menu1);
		
		//menu FPS
		menuFPS = new JMenu("FPS");
		list = new JMenuItem[8];
		fps01 = new JMenuItem("0.1 FPS"); list [0] = fps01;
		fps05 = new JMenuItem("0.5 FPS"); list [1] = fps05;
		fps1 = new JMenuItem("1 FPS"); list [2] = fps1;
		fps6 = new JMenuItem("6 FPS"); list [3] = fps6;
		fps12 = new JMenuItem("12 FPS"); list [4] = fps12;
		fps24 = new JMenuItem("24 FPS"); list [5] = fps24;
		fps30 = new JMenuItem("30 FPS"); list [6] = fps30;
		fps60 = new JMenuItem("60 FPS"); list [7] = fps60;
		for(int i =0; i<list.length; i++){
			menuFPS.add(list[i]);
			list[i].addActionListener(this);
		}
		fps24.setEnabled(false);
		menuBar.add(menuFPS);
		
		menuApropos = new JMenu("?");
		info = new JMenuItem("A propos");
		info.addActionListener(this);
		menuApropos.add(info);
		menuBar.add(menuApropos);
		
		this.setJMenuBar(menuBar);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == boxScreenZone){
			
			//si on coche,
			if(boxScreenZone.isSelected()){
				sS.handle(Constants.SCREENZONE_SHOW);
				boxStart.setEnabled(false);
				
			}else{

				boxStart.setEnabled(true);
				sS.handle(Constants.SCREENZONE_HIDE);
			}
		}else if(e.getID()==Constants.SCREENZONE_CLOSEDBYUSER){
			boxStart.setEnabled(true);
			boxScreenZone.setSelected(false);
			sS.handle(Constants.SCREENZONE_HIDE);
			
		}else if(e.getSource() == boxStart){
		
			
			if(boxStart.isSelected()){
				sS.handle(Constants.CAPTION_START);
				boxScreenZone.setEnabled(false);
				
			}else{
				sS.handle(Constants.CAPTION_STOP);

				boxScreenZone.setEnabled(true);
			}
		}else if(e.getSource() == info){

			System.out.println("test");
			JOptionPane.showMessageDialog(this,"Screen Mirroring v1 - J.B. TOUREAU - 2015\n Droits réservés","A propos",JOptionPane.INFORMATION_MESSAGE) ;
		}
		else{ 
			for(int i = 0; i<list.length;i++){
				if(e.getSource() == list[i]){
					for(int j =0; j<list.length; j++){
						System.out.println("test"+j);
						if(j!=i)
							
							list[j].setEnabled(true);
						else{
							list[j].setEnabled(false);
							sS.setFps(fps[j]);
						}
					}
				}
			}
		}
		
	}

	public void updateDisplay() {
		
		sS.handle(Constants.IMAGE_UPDATE);
		BufferedImage srcImg = sS.getLastImg();
		if (srcImg != null){
			int srcWidth = srcImg.getWidth();
			int srcHeight = srcImg.getHeight();
			if(srcWidth > srcHeight){
				display.setImage(scaleImage(srcImg,this.getWidth(), (int) (this.getWidth()*srcHeight/srcWidth)));
			}else{
				display.setImage(scaleImage(srcImg,(int) (this.getHeight()*srcWidth/srcHeight), this.getHeight()));
			}
			
			displayLabel.repaint();	
		}
	}
	private static BufferedImage scaleImage(Image source, int width, int height) {
	    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = (Graphics2D) img.getGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(source, 0, 0, width, height, null);
	    g.dispose();
	    return img;
	}
}