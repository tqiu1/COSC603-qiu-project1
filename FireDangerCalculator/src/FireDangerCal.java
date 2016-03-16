import java.lang.Math;  
import java.util.Scanner;
/**
*ROUTINE FOR COMPUTING NATIONAL FIRE DANGER RATINGS AND FIRE LOAD INDEX DATA NEEDED FOR 
*THE CALCULATIONS ARE: 
*DRY, DRY BULB TEMPERATURE. 
*WET, WET BULB TEMPERATURE. 
*ISNOW, SOME POSITIVE NON ZERO NUMBER IF THERE IS SNOW ON THE GROUND. 
*WIND, THE CURRENT WIND SPEED IN MILES PER HOUR. 
*BUO, THE LAST VALUE OF THE BUILD UP INDEX. 
*IHERB, THE CURRENT HERB STATE OF THE DISTRICT (1•CURED,2•TRANSITION, 3•GREEN DATA RETURNED ARE
*DF, DRYING FACTOR 
*FFM, FINE FUEL MOISTURE, 
*ADFM ADJUSTED (10 DAY LAG FUEL MOISTURE, 
*GRASS SPREAD INDEX, 
*TIMBER SPREAD INDEX. 
*FLOAD, FIRE LOAD RATING (MAN,HOUR BASE),
*BUO BUILD UP INDEX
*/

public class FireDangerCal {

	double ADFM;  //ADJUSTED (10 DAY LAG) FUEL MOISTURE 
	double BUO;  //THE LAST VALUE OF THE BUILD UP INDEX
	int DF;  //DRYING FACTOR
	double DRY;  //DRY BULB TEMPERATURE
	double FFM; //FINE fUEL MOISTURE
	double FLOAD;  //FIRE LOAD RATING (MAN,HOUR BASE)
	double GRASS;  //GRASS SPREAD INDEX
	int IHERB ; //THE CURRENT .HERB STATE OF THE DISTRICT
	double ISNOW;  //SOME POSITIVE NON ZERO NUMBER IF THERE IS SNOW ON THE GROUND
	int PRECIP;  
	double TIMBER;  //TIMBER SPREAD INDEX
	double WET;  //WET BULB TEMPERATURE
	double WIND;  //THE CURRENT WIND SPEED IN MILES PER HOUR
	
	double[] A=new double[5];
	double[] B=new double[5];	
	double[] C=new double[5];
	double[] D=new double[7];
	
	static double dry;
	static double wet;
	static double isnow;
	static int precip;
	static double wind;
	static double buo;
	static int iherb;
	static int df;
	static double ffm;
	static double adfm;
	static double grass;
	static double timber;
	static double fload;
    
    FireDangerCal(){
    	
    }
	
/**	
 *Analyze the Danger Rate
 */
public void getDanger(){
	
	if(isnow>0){   //is it snow?
		// There is snow, we set all spread indexes to 0
		set_AllSpreadIndexToZero();
		
		adjust_BUI();
	}else {
			//There is no snow on the ground we will compute the spread indexes and fire load

			//Calculate Fine fuel moisture
			cal_FineFuelMoisture();
			
			//calculate the drying factor for the day
			cal_DryFactor();
			
			// adjust fine fuel moist		
			adjust_FineFuelMoisture();
		}
	
	if (precip>0.1){  // is it rain?
		//There is rain (precipitation >0.1 inch), we must reduce the build up index (BUO) by 
		//an amount equal to rain fall
		adjust_BUI();
	}else{
		//After correct for rain, if any, we are ready to add today's dring factor to obtain the 
		//current build up index
		increase_BUIBYDryingFactor();
		
		//we will adjust the grass index for heavy fuel lags. The result will be the timber spread index
		//The adjusted fuel moisture, ADFM, Adjusted for heavy fuels, will now be computed.
		cal_AdjustedFuelMoist();
		
		if (ADFM>33){
			set_AllSpreadIndexToOne();

			}else{
				//whether wind>14? and calculate grass and timber spread index
				cal_GrassAndTimber();
				//Both BUI and Timber spread index are not 0?
				if (!((TIMBER==0)&(BUO==0))){
					cal_FireLoadRating();
				}
			}
		}
}
	


/**	
*Ask user to input Values for parameters
*/  
public void get_InputValues(){
	A[1]=-0.185900;
	A[2]=-0.85900;
	A[3]=-0.059660;
	A[4]=-0.077373;
	B[1]=30.0;
	B[2]=19.2;
	B[3]=13.8;
	B[4]=22.5;
	C[1]=4.5;
	C[2]=12.5;
	C[3]=27.5;
	D[1]=16.0;
	D[2]=10.0;
	D[3]=7.0;
	D[4]=5.0;
	D[5]=4.0;
	D[6]=3.0;
	
}

/**	
*Set Initial Table Values
*/  
public void get_TableValues(){
	FFM=99;
	ADFM=99;
	DF=0;
	FLOAD=0;
	
}


/**	
*get user inputs for some parameters
*/  
public void get_UserInput(){
	  Scanner s = new Scanner(System.in);
	  // get User inputs 
	  System.out.println("Please enter a number for Dry (double):");
	  DRY = s.nextDouble();
	  System.out.println("Please enter a number for Wet (double):");
	  WET = s.nextDouble(); 
	  System.out.println("Please enter a number for ISNOW (double):");
	  ISNOW = s.nextDouble();
	  System.out.println("Please enter a number for Wind(double):");
	  WIND = s.nextDouble(); 
	  System.out.println("Please enter a number for BUO (double):");
	  BUO = s.nextDouble();
	  System.out.println("Please enter a number for Iherb (int):");
	  IHERB = s.nextInt(); 
	  System.out.println("****Data for parameters are collected****.");
	  System.out.println("Data for parameters are collected.");
	  dry=DRY;
	  wet=WET;
}
/**	
*Output the finally updated parameters
*/  
public void output_UpdatedData(){

	  System.out.println("\nParameters are updated as following...");
	  
	  System.out.println("Dry (double): "+DRY);
	  
	  System.out.println("Wet (double): "+WET);
	  System.out.println("ISNOW (double): "+ISNOW);
	  System.out.println("Wind(double): "+WIND);
	  System.out.println("BUO (double): "+BUO);
	  System.out.println("Iherb (int): "+IHERB);
}

/**	
*Set All Spread Index To Zero
*/  
public void set_AllSpreadIndexToZero(){
	GRASS=0;   
	TIMBER=0;
}


/**	
*Calculate BUO
*/  
public void cal_BUO(){
	
}



/**	
*Calculate Fine Fuel Moisture
*/  
public void cal_FineFuelMoisture(){
	double DIF=dry-wet;
	for (int i=1;i<=3; i++){
	if ((DIF-C[i])<=0){  
		
		FFM=B[i]*Math.exp(A[i]*DIF);
	}
	}
}


/**	
*Calculate Dry Factor
*/  
public void cal_DryFactor(){
	for (int j=1;j<=6; j++){
		if ((FFM-D[j])>0){
			DF=j-1;
		};
	};
}



/**	
*Adjust Fine Fuel Moisture
*/  
public void adjust_FineFuelMoisture(){
	// If FFM is one or less we set it to one		
	if ((FFM-1)<=0){
		FFM=1;
	}else{
		//add 5 percent FFM for each Herb stage greater than one
		FFM=FFM+(iherb-1)*5;
	}
}


/**	
*Adjuest BUI
*/  
public void adjust_BUI(){
	BUO=-50*Math.log(1-(1-Math.exp(-BUO/50))*Math.exp(-1.175*(precip-0.1)));
		if (BUO<0){
			BUO=0;
		}
}


/**	
*Increase BUI By Drying Factor
*/  
public void increase_BUIBYDryingFactor(){
	BUO=BUO+DF;
}

/**	
*Calculate Adjusted Fire Moisture
*/  
public void cal_AdjustedFuelMoist(){
	ADFM=0.9*FFM+0.5+9.5*Math.exp(-BUO/50);
}

/**	
*Set All Spread Index To One
*/  
public void set_AllSpreadIndexToOne(){
	//test to see if the fuel moistures are greater than 33 percent.
	//if they are, set their index value to 1
	if ((FFM>33)){  // fine fuel greater than 33?
		GRASS=0;   
		TIMBER=0;
	}else{
		TIMBER=1;
	}
}


/**	
*Calculate Grass Index
*/  
public void cal_GrassAndTimber(){
	if (wind>=14){
		GRASS=0.00918*(wind+14)*Math.pow((33-FFM),1.65)-3;
	}else{
		TIMBER=GRASS=0.01312*(wind+6)*Math.pow((33-ADFM),1.65)-3;
		GRASS=0.01312*(wind+6)*Math.pow((33-FFM),1.65)-3;
		if (TIMBER<=1){TIMBER=1;}
		if (GRASS<1){GRASS=1;}

	}

}


/**	
*Calculate Fire and Load Rating
*/  
public void cal_FireLoadRating(){
	FLOAD=1.75*Math.log10(TIMBER)+0.32*Math.log10(BUO)-1.640;
	//ensure that FLOAD is greater than 0, otherwise set it to 0;
	if (FLOAD<0){FLOAD=0;
		}else{FLOAD=Math.pow(10, FLOAD);
	}
}


/**	
*Main function that call test class
*/  
public static void main(java.lang.String[] args){
	FireDangerCal test=new FireDangerCal();
	test.get_InputValues();
	test.get_TableValues();
	test.get_UserInput();
	test.getDanger();
	test.output_UpdatedData();
	
}


	
	
}
