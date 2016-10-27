package com.schuhr.Pi;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		if(!Constants.IS_DEBUG){
			PiButtons pb = new PiButtons("thread1");
			pb.Start();

			do{
				//Don't do anything just wait for kill command
			}while(!System.console().readLine().equals("kill"));
			pb.Stop();
		}
		Browser browse = new Browser();
		if(browse.IsBrowserSupported()){
			browse.SearchWebOnWordWithRecipe("test");
		}
	}
	
}
