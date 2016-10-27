package com.schuhr.Pi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import com.schuhr.Pi.Constants;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Camera implements Runnable {

	private String picString = "fswebcam -r 1280x720 --no-banner /home/pi/webcam/now.strftime('%Y_%m_%d_%H%M%S') + \".jpg\"";
	GpioPinDigitalInput button;
	private static GpioPinDigitalOutput pin;
	private Thread t;
	private String threadName;
	private boolean running = false;
	private boolean pictureWasTaken = false;
	private String lastPicture;
	private Browser browse;

	public Camera(String tName) {
		threadName = tName;
		browse = new Browser();
	}

	GpioPinListenerDigital listen = new GpioPinListenerDigital() {
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			if(event.getState() == PinState.LOW){
				System.out.println("Button is Low: Ignored");
				return;
			}
				
			
			try {
				// Get the currrent date and time and save the image with that
				// as the file name
				String prg = "import sys\n" + "import datetime\n" + "now = datetime.datetime.now()\n"
						+ "print(now.strftime('%Y_%m_%d_%H%M%S'))" + picString;
				BufferedWriter out = new BufferedWriter(new FileWriter("test1.py"));
				out.write(prg);
				out.close();

				ProcessBuilder pb = new ProcessBuilder("py", "test1.py");
				pb.start();
				Process p = pb.start();

				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				lastPicture = new String(in.readLine());
				pictureWasTaken = true;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	};

	public void Start() {
		GpioController gpio = GpioFactory.getInstance();
		if (button == null) {
			button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
			button.setShutdownOptions(true);
			button.addListener(listen);
		}

		if (Constants.IS_DEBUG) {
			// Adding this for testing purposes to verify a photo was taken
			if (pin == null) {
				pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
			}
		}

		// If the thread is null then create it
		if (t == null) {
			t = new Thread(this, threadName);
			running = true;
			t.start();
		}

		// Stop the thread if a browser is not supported
		if (browse.IsBrowserSupported()) {
			running = false;
			System.out.println("Error: No browser supported, stopping thread");
			Stop();
			return;
		}
	}

	public void run() {
		while (running) {
			if (pictureWasTaken == true) {
				try {
					Thread.sleep(1000);
					if (pin.getState() == PinState.HIGH) {
						pin.setState(PinState.LOW);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Code for testing purposes
				File f = new File("/home/pi/webcam/" + lastPicture);
				if (Constants.IS_DEBUG && f.exists() && !f.isDirectory()) {
					pin.setState(PinState.HIGH);
				}
				/*
				 * Tess t = new Tess("C:\\"); t.Run();
				 */
				pictureWasTaken = false;
			}
		}
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
