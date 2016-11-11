package com.schuhr.RCCar;

public class Drive {
	
	public Drive(){
		
	}
	
	public void Process(String message){
		if(!message.startsWith("-")){
			//Adding to the list
			AddControl(message.charAt(0));
		}else{
			//Subtracting from the list
			SubtractControl(message.charAt(1));
		}
	}
	
	public void AddControl(char message){
		switch(message){
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
			
		}
	}
	
	public void SubtractControl(char message){
		
	}
}
