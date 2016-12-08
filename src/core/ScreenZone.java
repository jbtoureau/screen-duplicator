package core;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class ScreenZone extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8027935798586038816L;
	Rectangle screenZone;
	final Screenshooter sS;
	
	public ScreenZone(Rectangle screenZone,final Screenshooter sS){
		this.setLocation(screenZone.x, screenZone.y);
		this.setSize(screenZone.width, screenZone.height);
		this.sS = sS;
		//this.setUndecorated(true);
		//this.setOpacity((float) 0.9);
		this.setName("ScreenZone");
		this.setTitle("Zone de capture - positionner sur la zone à dupliquer");
		//this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                  //e.getWindow().setVisible(false);
                  sS.handle(Constants.SCREENZONE_CLOSEDBYUSER);
            }
   });
	}
	
		
	public Rectangle getScreenZone() {
		return new Rectangle(this.getBounds());
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this){
		}
	}
}
