import java.lang.Math;  
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
	
	double dry;
    double wet;
    double isnow;
    int precip;
    double wind;
    double buo;
    int iherb;
    int df;
    double ffm;
    double adfm;
    double grass;
    double timber;
    double fload;
	
/**	
 *Analyze the Danger Rate
 */
public void getDanger(double dry,
                      double wet,
                      double isnow,
                      int precip,
                      double wind,
                      double buo,
                      int iherb,
                      int df,
                      double ffm,
                      double adfm,
                      double grass,
                      double timber,
                      double fload){
	
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
			cal_AdjustedFuelMoist();
		}
	
	if (precip>0.1){  // is it rain?
		//There is rain (precipitation >0.1 inch), we must reduce the build up index (BUO) by 
		//an amount equal to rain fall
		BUO=-50*Math.log(1-(1-Math.exp(-BUO/50))*Math.exp(-1.175*(precip-0.1)));
		
	}
	
}



/**	
*Calculate Spread Index and Fire Load
*/  
public void cal_SpreadIndexAndFireLoad(){
	
}



/**	
*Calculate Grass and Timber Indexes
*/  
public void cal_GrassAndTimberInexes(){

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
*Output the updated parameters
*/  
public void output_UpdatedData(){
	
}



/**	
*Set All Spread Index To Zero
*/  
public void set_AllSpreadIndexToZero(){
	GRASS=0;   
	TIMBER=0;
}



/**	
*Set All Spread Index To One
*/  
public void set_AllSpreadIndexToOne(){

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
	
}



/**	
*Adjuest BUI
*/  
public void adjust_BUI(){
	if (precip>0){  // is it rain?
		//Adjust the BUO
		BUO =50*(Math.log(1-(1-Math.exp(-buo/50))*Math.exp(-1.175*(precip-0.1))));
	}
}



/**	
*Increase BUI By Drying Factor
*/  
public void increase_BUIBYDryingFactor(){
	
}



/**	
*Calculate Adjusted Fire Moisture
*/  
public void cal_AdjustedFuelMoist(){
	// If FFM is one or less we set it to one		
	if ((FFM-1)<=0){
		FFM=1;
	}else{
		//add 5 percent FFM for each Herb stage greater than one
		FFM=FFM+(iherb-1)*5;
	}
}



/**	
*Calculate Grass Index
*/  
public void cal_Grass(){
	
}



/**	
*Calculate Fire and Load Rating
*/  
public void cal_FireLoadRating(){
	
}



/**	
*Main function that call test class
*/  
public static void main(java.lang.String[] args){
	
}



	
	
	
}
