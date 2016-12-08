package core;

	import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
	
public class Screenshooter{

	private Robot robot;
	private Rectangle screenZone;
	private BufferedImage currentImg;
	private ScreenZone sZ;
	private boolean running;
	private MainFrame mainFrame;
	private float fps;

	public Screenshooter(MainFrame mF)  {
		try {
			setFps(24);
			robot = new Robot();
			screenZone = new Rectangle(100,100,500,500);
			sZ = new ScreenZone(screenZone,this);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		setRunning(false);
		mainFrame = mF;
	}
	
	private void takeScreen(){
		if(screenZone != null)
			currentImg = robot.createScreenCapture(screenZone);
	}
	
	private void updateScreen(){
		if(screenZone != null){
			if(currentImg==null){
				currentImg = robot.createScreenCapture(screenZone);
			}
			BufferedImage img = robot.createScreenCapture(screenZone);
			if(! bufferedImagesEqual(img,currentImg))
				currentImg=img;
		}			
	}
	
	public boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
	    if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
	        for (int x = 0; x < img1.getWidth(); x++) {
	            for (int y = 0; y < img1.getHeight(); y++) {
	                if (img1.getRGB(x, y) != img2.getRGB(x, y))
	                    return false;
	            }
	        }
	    } else {
	        return false;
	    }
	    return true;
	}
	
	public BufferedImage getLastImg(){
		return currentImg;
	}
	
	public Rectangle getScreenZone() {
		return sZ.getScreenZone();
	}
	
	public void setScreenZone(Rectangle screenZone) {
	}
	
	/*return false if operation is not done */
	public boolean handle(int msg) {
		// TODO Auto-generated method stub
		switch(msg){
			case Constants.SCREENZONE_SHOW:{

				System.out.println("sZ is visible");
				sZ.setVisible(true);
				break;
			}
			case Constants.SCREENZONE_HIDE:{
				screenZone = (Rectangle) sZ.getScreenZone();

				System.out.println("sZ is not visible");
				sZ.setVisible(false);
				break;
			}
			case Constants.SCREENZONE_CLOSEDBYUSER:{
				mainFrame.actionPerformed(new ActionEvent(this, Constants.SCREENZONE_CLOSEDBYUSER, null));
				break;
			}
			case Constants.IMAGE_UPDATE:{
				updateScreen();
				break;
			}case Constants.CAPTION_START:{
				setRunning(true);
				Thread t = new Thread(){
					public void run() {
						// TODO Auto-generated method stub
						while(isRunning()){
							mainFrame.updateDisplay();
							try {
								//System.out.println(getFps());
								Thread.sleep((long) (1000/getFps()));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
				};
				t.start();
				break;
			}case Constants.CAPTION_STOP:{
				setRunning(false);
				break;
			}
		}
		return true;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public float getFps() {
		return fps;
	}

	public void setFps(float fps) {
		this.fps = fps;
	}
	
}
