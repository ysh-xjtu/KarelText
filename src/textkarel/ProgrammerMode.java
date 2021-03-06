/*
 * Program Mode class.
 * This is started when the user hits "p" for Program mode.
 * This executes the program that is written in the text file
 * detailed by the user.
 */


package textkarel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Gonzo
 */
public class ProgrammerMode {
    private final String TURNLEFT = "LEFT";
    private final String TURNRIGHT = "RIGHT";
    private final String FORWARD = "GO";
    private final String PICKUP = "GET";
    private final String PLACE = "PUT";
    private final String IF = "IF";
    private final String REPEAT = "REPEAT";
    private final String WHILE = "WHILE";
    private final String ELSE = "ELSE";
    private final String NOT = "NOT";
    private final String HOME = "HOME";
    private final String NORTH = "NORTH";
    private final String GEM = "GEM";
    private final String WALL = "WALL";
    private final String EMPTY = "EMPTY";
    private final String [] labels= new String [20];
    private final String[] vars=new String [30];
    private final String[] reserved= new String [30];
    private final String[] conditions= new String [15];
    private final String[] special= new String [5];
    private final String[] sensors = new String[5];
    private ArrayList <String> instruction = new ArrayList();
    private String FILENAME;
    private Board board;
    private Player karel;
   
    
    
    public ProgrammerMode(String file, Board tempboard){
    
        FILENAME=file;
        board=tempboard;
        //karel=player;
        SetReservedWord();
        setSensors();
        
        
    }
    
    /**
     * Set reserved word in a string array.
     */
    public void SetReservedWord() {
        
        reserved[0]="LEFT";
        reserved[1]="RIGHT";
        reserved[2]="GO";
        reserved[3]="GET";
        reserved[4]="PUT";
        reserved[5]="REPEAT";
        reserved[6]="IF";
        reserved[7]="NOT";
        reserved[8]="WHILE";
        reserved[9]="ELSE";
        for(int i=10;i<20;i++)
        {
            reserved[i]=" ";
        }
    }
    
    public void setSensors(){
        sensors[0]="WALL";
        sensors[1]="GEM";
        sensors[2]="EMPTY";
        sensors[3]="HOME";
        sensors[4]="NORTH";
    }
    
    /**
     * Set conditions in a string array.
     */
    public void SetConditions(){
        
        conditions[0]="==";
        conditions[1]="!=";
        conditions[2]="<";
        conditions[3]=">";
        conditions[4]="<=";
        conditions[5]=">=";
        for(int i=6;i<15;i++)
        {
            conditions[i]=" ";
        }
    }
    
    /**
     * Set the special conditions in a string array.
     */ 
    public void SetSpecial(){
        
        special[0]="++";
        special[1]="--";
        for(int i=2;i<5;i++){
            
            special[i]=" ";
        }
      }
    
    /**
     * Reads in the file that has the instructions.
     */
    public void ReadInFile(){
        String tempString;
         try{
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            while ((tempString = br.readLine()) != null) {
                tempString = tempString.toUpperCase();   //converts to all uppercase. user program no longer case sensitive 
                instruction.add(tempString);
            }
         }
         catch(IOException e){
             System.out.println("File name invalid. Enter new file name.");
            Scanner in = new Scanner(System.in);
            FILENAME = in.nextLine();
            ReadInFile();
        }
    }
    
    /**
     * Checks for errors in the instruction of the file.
     * @return True if an error is found
     */
    public boolean ErrorChecking(){
        
        int line = 1;
        String temp;
        String[] tempStrArray;
        
        boolean Error = true;
        
        for(int i=0;i<instruction.size();i++){
            Error=true;
            temp=instruction.get(i);
            tempStrArray = temp.split(" ");
            
            
           // System.out.println(tempStrArray[0]);
            
            //loop through reserved words to find a match
            for(int j=0;j<reserved.length;j++){
               
                if(tempStrArray[0].endsWith(reserved[j]))
                {
                    
                    if(tempStrArray[0].endsWith(IF)) {  
                        Error = errorCheckIfStatement(tempStrArray, line);
                    }
                    
                    else if(tempStrArray[0].endsWith(REPEAT)){   
                        Error = errorCheckRepeatStatement(tempStrArray, line);
                    }
                    
                    else if(tempStrArray[0].endsWith(WHILE)){
                        Error = errorCheckWhileStatement(tempStrArray, line);
                    }
                    
                    else if(tempStrArray[0].endsWith(ELSE)){
                        Error = errorCheckElseStatement(tempStrArray, line);
                    }
                    
                    else{
                        Error = false;
                    }
                    
                    break;     
                }
                
            }//end for
            
            if(Error==false)
            {
                line++;
                continue;
            }
            
            else{
                break;
            }
         }
        
       if(Error==true)
       {
        return true;
       }
       else{
           return false;
       }
    }
    
    
    
    /**
     * Processes the correct instructions read in.
     */ 
    public void Execution(ArrayList<String> instr, int tCount){
        
      int i = 0;
      int tabCount = tCount;
      boolean usedIF = false;
      boolean encounteredIF = false;
      
       
      while(i <instr.size() && !board.ReturnCrashState()){
        String instruct=instr.get(i);
        String [] tempStrArray = instruct.split(" ");
        //System.out.println(instruct);
        
        
        if(tempStrArray[0].endsWith(TURNLEFT)){
            board.keyPressed('a');
            i++;
        }
        
        else if(tempStrArray[0].endsWith(TURNRIGHT)){
            
             board.keyPressed('d');
             i++;
        }
        
        else if(tempStrArray[0].endsWith(FORWARD)){
            
             board.keyPressed('w');
             i++;
        }
        
        else if(tempStrArray[0].endsWith(PICKUP)){
            
             board.keyPressed('e');
             i++;
        }
        
        else if(tempStrArray[0].endsWith(PLACE)){
            
             board.keyPressed('s');
             i++;
        }
        
        else if(tempStrArray[0].endsWith(IF)){
            tabCount++;
            encounteredIF = true;
            ArrayList<String> ifInstructions = new ArrayList();
            int x = i+1;
            String temp = instr.get(x);
            String tab = new String();
            
            for(int r=0; r<tabCount; r++){
                 tab = tab.concat("\t");
                
            }
            
            

            while( temp.startsWith(tab) ){
                ifInstructions.add(temp);
                x++;
                if (x >= instr.size()){
                    break;
                }
                temp = instr.get(x); 
                    
            }
            
            if(tempStrArray[1].equals(NOT)){
                
                if(tempStrArray[2].equals(WALL)){
                    if(!board.wallSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;

                }

                else if(tempStrArray[2].equals(GEM)){
                    if(!board.gemSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }

                else if(tempStrArray[2].equals(EMPTY)){
                    if(!board.emptySensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }
                
                else if(tempStrArray[2].equals(HOME)){
                    if(!board.homeSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }
                
                else if(tempStrArray[2].equals(NORTH)){
                    if(!board.northSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }
            }
            else{
                if(tempStrArray[1].equals(WALL)){
                
                    if(board.wallSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;

                }

                else if(tempStrArray[1].equals(GEM)){
                    if(board.gemSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }

                else if(tempStrArray[1].equals(EMPTY)){
                    if(board.emptySensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    
                    tabCount--;
                }
                
                else if(tempStrArray[1].equals(HOME)){
                    if(board.homeSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }
                
                else if(tempStrArray[1].equals(NORTH)){
                    if(board.northSensor()){
                        Execution(ifInstructions, tabCount);
                        usedIF = true;
                    }
                    tabCount--;
                }
            }
            
            
            
            
            i=x;
            
        }
        
        
        else if( (tempStrArray[0].endsWith(ELSE)) ){
            tabCount++;
            ArrayList<String> elseInstructions = new ArrayList();
            int x = i+1;
            String temp = instr.get(x);
            String tab = new String();
            //System.out.println("temp=" +temp + "  tabCount =" + tabCount);
            for(int r=0; r<tabCount; r++){
                tab = tab.concat("\t");
                
            }
            
            

            while( temp.startsWith(tab) ){
                elseInstructions.add(temp);
                x++;
                if (x >= instr.size()){
                    break;
                }
                temp = instr.get(x);       
            }
            
            //only run else if "if" was not run
            if((usedIF == false) && (encounteredIF == true)){
                Execution(elseInstructions, tabCount);
                usedIF = false;
                encounteredIF = false;
            }
            
            
            
            i = x;
            tabCount--;
            
        }
        
        
        
        
        
        else if(tempStrArray[0].endsWith(REPEAT)){
            
            tabCount++;
            ArrayList<String> rInstructions = new ArrayList();
            int x = i+1;
            String temp = instr.get(x);
            String tab = new String();
            
            for(int r=0; r<tabCount; r++){
                tab = tab.concat("\t"); 
            }
            
            
            while( (temp.startsWith(tab)) ){
                
                rInstructions.add(temp);
                x++;
                if (x >= instr.size()){
                    break;
                }
                
                temp = instr.get(x);
            }
            
            
            
            int counter = Integer.parseInt(tempStrArray[1]);
            for( int h=0; h<counter ; h++){
                Execution(rInstructions, tabCount);
            }
            i = x;
            tabCount--; 
             
        }
        
        else if(tempStrArray[0].endsWith(WHILE)){ //not complete. only runs one time
            tabCount++;
            ArrayList<String> ifInstructions = new ArrayList();
            int x = i+1;
            String temp = instr.get(x);
            String tab = new String();
            //System.out.println("temp=" +temp + "  tabCount =" + tabCount);
            for(int r=0; r<tabCount; r++){
                tab = tab.concat("\t");
                
            }
            
            

            while( temp.startsWith(tab) ){
                ifInstructions.add(temp);
                x++;
                if (x >= instr.size()){
                    break;
                }
                temp = instr.get(x); 
                
                
            }
            
            if(tempStrArray[1].equals(NOT)){
                
                if(tempStrArray[2].equals(WALL)){
                    while(!board.wallSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;

                }

                else if(tempStrArray[2].equals(GEM)){
                    while(!board.gemSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }

                else if(tempStrArray[2].equals(EMPTY)){
                    while(!board.emptySensor()){
                        Execution(ifInstructions, tabCount);  
                    }
                    
                    tabCount--;
                }
                
                else if(tempStrArray[2].equals(HOME)){
                    while(!board.homeSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }
                
                else if(tempStrArray[2].equals(NORTH)){
                    while(!board.northSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }
            }
            else{
                if(tempStrArray[1].equals(WALL)){
                
                    while(board.wallSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;

                }

                else if(tempStrArray[1].equals(GEM)){
                    
                    while(board.gemSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }

                else if(tempStrArray[1].equals(EMPTY)){
                    while(board.emptySensor()){
                        Execution(ifInstructions, tabCount);
                    } 
                    tabCount--;
                }
                
                else if(tempStrArray[1].equals(HOME)){
                    
                    while(board.homeSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }
                
                else if(tempStrArray[1].equals(NORTH)){
                    while(board.northSensor()){
                        Execution(ifInstructions, tabCount);
                    }
                    tabCount--;
                }
            }
            
            
            
            
            i=x;
            
        }
        
        
        
      }

    }
    
    /**
     * Start Programmer mode.
     */
    public void Start(){
        ReadInFile();
        if(!ErrorChecking())
        {
            Execution(instruction, 0);
            if(!board.checkCompleted() && !board.ReturnCrashState()){
                System.out.println("Failed to complete all task. Restarting in manual mode.");
                
            }
            board.keyPressed('m');
        }
        
        
     }
    
    private boolean errorCheckIfStatement(String[] Statement, int line){
        boolean Error = true;
        
        //if line has three statements the middle statement should be a not statement
        if (Statement.length == 3 ){
            if(Statement[1].equals(NOT)){
                for(int z=0; z<sensors.length; z++){
                    if(Statement[2].equals(sensors[z])){
                        Error=false;
                        break; 
                    }
                 }//end for
             }
        }

        else if(Statement.length == 2){
            for(int z=0; z<sensors.length; z++){
                if(Statement[1].equals(sensors[z])){
                    Error=false;
                    break; 
                }
             }
        }
        
        else if(Statement.length < 2){
            System.out.println("There was a error on line: " + line);
            System.out.println("Too few argument");
        }
        
        else if(Statement.length > 3){
            System.out.println("There was a error on line: " + line);
            System.out.println("Too many argument");
        }
       
        return Error;                   
    }
    
    private boolean errorCheckRepeatStatement(String[] Statement, int line){
        boolean Error = true;
        if (Statement.length == 2){
            try{
                int count = Integer.parseInt(Statement[1]);
                Error=false;

            }
            catch(NumberFormatException e){
                System.out.println("There was a error on line: " + line);
                System.out.println("Expected a numeric value after repeat");
                return true;
            }
        }
        
        else if(Statement.length <2){
            System.out.println("There was a error on line: " + line);
            System.out.println("Expected a numeric value after repeat");
        } 
        
        else if(Statement.length >2){
            System.out.println("There was a error on line: " + line);
            System.out.println("Too many arguments for repeat");
        } 
        return Error;
    }
    
    
    private boolean errorCheckWhileStatement(String[] Statement, int line){
        boolean Error = true;
        
        if (Statement.length == 3 ){
            if(Statement[1].equals(NOT)){
                for(int z=0; z<sensors.length; z++){
                    if(Statement[2].equals(sensors[z])){
                        Error=false;
                        break; 
                    }
                 }
             }
        }
         
        else if(Statement.length == 2){
            for(int z=0; z<sensors.length; z++){
                if(Statement[1].equals(sensors[z])){
                    Error=false;
                    break; 
                }
             }
          }
        
        else if(Statement.length < 2){
            System.out.println("There was a error on line: " + line);
            System.out.println("Too few argument");
        }
        
        else if(Statement.length > 3){
            System.out.println("There was a error on line: " + line);
            System.out.println("Too many argument");
        }
        
         return Error;
    }
    
    private boolean errorCheckElseStatement(String[] Statement, int line){
        boolean Error = true;
        if (Statement.length == 1 ){
            Error = false;
        }
        return Error;
    }
    
    
    
    
    
    
}
