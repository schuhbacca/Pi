package com.schuhr.RCCar;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class PiComm {

	private GpioPinDigitalOutput leftMotor, rightMotor, leftServo, rightServo;

	public PiComm() {
		GpioController gpio = GpioFactory.getInstance();

		if (leftMotor == null)
			leftMotor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LeftMotor", PinState.LOW);
		if (rightMotor == null)
			rightMotor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "RightMotor", PinState.LOW);
		if (leftServo == null)
			leftServo = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LeftServo", PinState.LOW);
		if(rightServo == null)
			rightServo = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "RightServo", PinState.LOW);
	}
	
	public void MoveForward(){
		leftMotor.setState(PinState.HIGH);
		rightMotor.setState(PinState.HIGH);
	}
	
	public void StopMotors(){
		leftMotor.setState(PinState.LOW);
		rightMotor.setState(PinState.LOW);
	}
	
	public void GoRight(){
		
	}
	
	public void GoLeft(){
		
	}
	
	public void CenterWheels(){
		
	}
}
