


import java.io.IOException;

import javax.imageio.IIOException;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.ServoPositionChangeEvent;
import com.phidgets.event.ServoPositionChangeListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;


public class RFIDMotor implements TagLossListener, TagGainListener{
// Demonstrates moving motor according to rfid tag gained
// links a servo motor and an rfid reader
// Moves motor 4 times every time a tag is gained (does not check tag value)

/**
* @param args
*/
	// servo board and port number of motor
	static AdvancedServoPhidget servo;
	static AdvancedServoPhidget servo1;
    int servoNumber = 0; 
    int servoNumber1 = 1;
	
    public RFIDMotor() throws PhidgetException {

    		// Create rfid reader object and attach loss/gain listeners
    		RFIDPhidget phid = new RFIDPhidget();
        phid.addTagLossListener(this);
        phid.addTagGainListener(this);
        phid.openAny();
        phid.waitForAttachment();

        // wait for motor attachments on servo board
        servo.openAny();
		servo.waitForAttachment();

		
		// display info on rfid reader to show connected
		System.out.println(phid.getDeviceType());
        System.out.println("RFID Reader Serial Number " + phid.getSerialNumber());
        System.out.println("Device Version " + phid.getDeviceVersion());
        System.out.println("Attached " + phid.isAttached());
        
        // Display info on servo motors and set min/max values of turn
		System.out.println("Servo Motor Serial: " + servo.getSerialNumber());
		System.out.println("Servo motor count: " + servo.getMotorCount());
		servo.setPositionMin(servoNumber,0);
		servo.setPositionMax(servoNumber,220);
		System.out.println("Servo Min Position: " + servo.getPositionMin(servoNumber));
		System.out.println("Servo Max Position: " + servo.getPositionMax(servoNumber));
		
		servo.setPositionMin(servoNumber1,0);
		servo.setPositionMax(servoNumber1,220);
		System.out.println("Servo Min Position: " + servo.getPositionMin(servoNumber1));
		System.out.println("Servo Max Position: " + servo.getPositionMax(servoNumber1));

        
        phid.setLEDOn(true);
        phid.setAntennaOn(true);

        // sleeping around, just to avoid the program finishing
        while (true)
        	// repeat forever detecting rfid tag values
            try {
                Thread.sleep(1000);
            } catch (Throwable t) {
                t.printStackTrace();
            }	
	
}

public static void setServoListeners(){
	servo.addAttachListener(new AttachListener() {
	public void attached(AttachEvent ae) {
		System.out.println("attachment of " + ae);
		}
	});

	servo.addDetachListener(new DetachListener() {
	public void detached(DetachEvent ae) {
		System.out.println("detachment of " + ae);
		}
	});

	servo.addErrorListener(new ErrorListener() {
	public void error(ErrorEvent ee) {
		System.out.println("error event for " + ee);
		}
	});

	servo.addServoPositionChangeListener(new ServoPositionChangeListener()
	{

		public void servoPositionChanged(ServoPositionChangeEvent oe)
	{
	// 
	// System.out.println(oe);
	}
	});

}



public static void main(String[] args) throws PhidgetException {
// TODO Auto-generated method stub

	
	System.out.println(Phidget.getLibraryVersion());	
	servo = new AdvancedServoPhidget();

// main code starts here
	new RFIDMotor();

}

public void moveServoTo(int position){
    // utility method to move motor to indicated position
    try {
	    servo.setEngaged(servoNumber, false);
	    servo.setSpeedRampingOn(servoNumber, false);
	    
	    servo.setPosition(servoNumber, position);
	    servo.setEngaged(servoNumber, true);
	    Thread.sleep(500);

		System.out.println("Servio motor      "+servo.getPosition(servoNumber));
		System.out.println("Moved to position "+position);


	    
	}
    catch (PhidgetException pe) {System.out.println("Motor error");}
    catch (InterruptedException ie) {System.out.println("Sleep error");}

}

public void moveServoTo1(int position){
    // utility method to move motor to indicated position
    try {
	    servo.setEngaged(servoNumber1, false);
	    servo.setSpeedRampingOn(servoNumber1, false);
	    
	    servo.setPosition(servoNumber1, position);
	    servo.setEngaged(servoNumber1, true);
	    Thread.sleep(500);

		System.out.println("Servio motor      "+servo.getPosition(servoNumber1));
		System.out.println("Moved to position "+position);

	    
	}
    catch (PhidgetException pe) {System.out.println("Motor error");}
    catch (InterruptedException ie) {System.out.println("Sleep error");}

}


@Override
public void tagGained(TagGainEvent arg0)  {
	// Move the motor 4 times
    System.out.println(arg0);
    String tagnumber = arg0.getValue();        
    if (tagnumber.equals("card1")) {  // insert your known tag id here
    	System.out.println("card one or master key used" ); 
    	moveServoTo(120);
    }
    else if (tagnumber.equals("card2")) {  // insert your known tag id here
    	System.out.println("card2 or master key used" ); 
    	moveServoTo1(180);
    }
    else if (tagnumber.equals("keyfob1")) {  // insert your known tag id here
    	System.out.println("card one or master key used" ); 
    	moveServoTo1(180);
    	moveServoTo(120);
    	
    }
    else { 
    	System.out.println("UNKNOWN TAG. PLEASE GO AWAY."); 
    }  

}

@Override
public void tagLost(TagLossEvent arg0) {
    System.out.println(arg0);
    System.out.println("Bye bye");
    moveServoTo(180);
    moveServoTo1(120);
}

}


