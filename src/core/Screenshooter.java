package core;

	import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
	
/**
 * 
 * @author JBT
 *The core class that allows handling images and screen captures.
 */
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
	
	/**
	 * Compares two images, pixel by pixel
	 * @param img1
	 * @param img2
	 * @return <code>false</code> if images sizes are different or if at least 1 pixel does not match
	 * else return <code>true</code>
	 */
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
	
	/**
	 * @param msg
	 * The msg value is compared to the <code>Constants</code> values and accordingly execute the action.
	 * If msg is not in Constants values, nothing is done.
	  **/
	public void handle(int msg) {
		//TODO exception to be thrown if msg is not in Constants
		//TODO refactor the different cases in separated methods
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
						while(isRunning()){
							mainFrame.updateDisplay();
							try {
								//System.out.println(getFps());
								Thread.sleep((long) (1000/getFps()));
							} catch (InterruptedException e) {
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
