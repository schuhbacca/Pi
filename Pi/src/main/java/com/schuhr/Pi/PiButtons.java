package com.schuhr.Pi;

import java.io.*;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class PiButtons implements Runnable {

	GpioPinListenerDigital listen = new GpioPinListenerDigital() {
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			// display pin state on console
			light = event.getState();
			System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
			// Python Script
			try {
				ProcessBuilder pb = BuildProcess();
				Process p = pb.start();

				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				int ret = new Integer(in.readLine()).intValue();
				System.out.println("value is : " + ret);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	};
	
	private ProcessBuilder BuildProcess(){
		String prg = "import sys\nprint (int(sys.argv[1])+int(sys.argv[2]))\n";
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter("test1.py"));
			out.write(prg);
			out.close();
			int number1 = 10;
			int number2 = 32;

			return new ProcessBuilder("python", "test1.py", "" + number1, "" + number2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static GpioPinDigitalOutput pin;

	private Thread t;
	private String threadName;
	private PinState light;
	private boolean running = false;

	public PiButtons(String tName) {
		threadName = tName;
	}

	public void run() {

		GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
		button.setShutdownOptions(true);
		button.addListener(listen);

		if (pin == null) {
			pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
		}

		while (running) {
			if (light.isHigh()) {
				System.out.println("Pin is High");
				pin.setState(PinState.HIGH);
			} else {
				System.out.println("Pin is Low");
				pin.setState(PinState.LOW);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public Boolean Start() {
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
			running = true;
			return true;
		}
		return false;
	}

	public void Stop() {
		running = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
