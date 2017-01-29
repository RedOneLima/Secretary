package Group_Project;

import java.util.LinkedList;

/**
 * This class is in charge of running the simulation. It takes endTime, meanDoor,
 * meanPhone, and meanQ values from the GUI.
 * @author Belen Levine and Kyle Hewitt
 */
public class Secretary {
    
    double meanDoor=45.0;       //poisson mean for door arrivals 
    double meanPhone=55.0;      //poisson mean for phone call increments
    double meanQ=24.0;      //poisson mean for question time length
    int doorIn, phoneIn, clock, qAnswered, qTime;
    int endTime=3000;       //time (seconds) when the simulation ends
    LinkedList<Customer> theLine= new LinkedList();
    Customer current=null;      // customer currently with the secretary
    int custNum=0;      //tracks which numbered customer walks/calls in
    int doorCount = 0;      //tracks how many walk-in customers there are in total
    int phoneCount = 0;     //tacks how many phone call customers there are in total
    int unhelpedCustomers = 0;      //variable to hold the number of customers left unhelped at the end
    private String results="";      //a string to print results
    int phoneLine=0;        //keeps track of how many phone callers are in line
    int doorLine=0;     //keeps track of how many door customers are in line
    
    
    public static void main(String[]args){
       GroupGUI run = new GroupGUI();
    }
    
    /**
     * The method in charge of determining which event is the next to occur.
     */
    public void runSim(){
        
        doorIn=poisson(meanDoor);
        phoneIn=poisson(meanPhone);
        clock=0;
        qAnswered=Integer.MAX_VALUE;
        
        while(clock<=endTime){
            
            if(doorIn<qAnswered && doorIn<phoneIn){ //the door is the next event
                clock=doorIn;
                if(clock>endTime){
                    break;
                }
                else{
                    doorEvent();
                }
            }
            
            else if(phoneIn<qAnswered && doorIn>=phoneIn){  //phone is next event
                clock=phoneIn;
                if(clock>endTime){
                    break;
                }
                else{
                    phoneEvent();
                }
            }
            
            else{   //question is answered is the next event
                clock=qAnswered;
                if(clock>endTime){
                    break;
                }
                else{
                    answerEvent();
                }
                
            }
            
            if(current!=null ){
                if(current.getPhoneCall()){
                    msg("Phone customer "+current.getCustNum()+" is currently with the secretary.");
                }
                else{
                    msg("Door customer "+current.getCustNum()+" is currently with the secretary.");
                }
                
            }
        }
        
         unhelpedCustomers = doorLine+phoneLine;
        
    }
    
    /**
     * The method that controls what happens when a door event is the next to 
     * occur. The new customer is added to the end of the queue and then, if
     * current is null (no one is currently with the secretary at the front desk),
     * the customer in the front of the line is moved to the front desk.
     */
    public void doorEvent(){
        doorCount++;
        doorLine++;
        custNum++;
        
        //adds the new customer to the end of the line
        theLine.addLast(new Customer(false, clock, custNum, poisson(meanQ)));
        printInReport(false);   
        
        if(current==null){   // if no one is at the front desk.

            //move whoever is in the front of line to front desk.
            current=theLine.removeFirst();
            current.setAtDesk(clock);
            qAnswered=current.getQTimeRem()+clock;

        }
        doorIn+=poisson(meanDoor);
    }
    
    /**
     * The method that controls what happens when a phone event is the next to 
     * occur. If there is currently a customer at the front desk, they are moved
     * to the front of the line and the amount of time that they were at the
     * front desk is recorded. The new phone customer is then put at the front 
     * desk.
     */
    public void phoneEvent(){
        //clock=phoneIn;
        phoneCount++;
        phoneLine++;
        custNum++;

        if(current!=null){  //if someone is at front desk.
            //interrupts current front desk customer

            current.setQTimeRem(clock); //calculates/sets time remaining for question of interrupted
            theLine.addFirst(current);//puts customer currently at desk back in front of line
        }
        current=new Customer(true, clock, custNum, poisson(meanQ));//puts new phone call at the desk
        current.setAtDesk(clock);//sets phone calls desk time
        qAnswered=current.getQTime()+clock;
        
        phoneIn+=poisson(meanPhone);

        printInReport(true);
                
    }
    
    /**
     * The method that manages what occurs when a question getting answered is 
     * the next event. The number of phone and door customers in line is 
     * incremented according to what type of customer just had their question 
     * answered. Then, if there are others still in line, the person at the front
     * of the line is moved to the front desk.
     */
    public void answerEvent(){
        //clock=qAnswered;
                
        if(current.getPhoneCall()){
            phoneLine--;
        }
        else{
            doorLine--;
        }

        printOutReport();

        if(theLine.size()>0){   //if there are others in line
            //moves first person in line up to front desk
            current=theLine.removeFirst();
            current.setAtDesk(clock);
            qAnswered=current.getQTimeRem()+clock;
        }
        else{   //if no one is in line
            qAnswered=Integer.MAX_VALUE;
            current=null;
        }
    }
    
    /**
     * Takes in a clock time and converts that time to HOUR:MINUTE:SECOND AM/PM 
     * format.
     * @param c     the time to be converted
     * @return      a string representation of the time converted to HOUR:MINUTE:SECOND AM/PM
     */
    public String time(int c){
        String aNoon="AM";
        int hours=(c/3600)+8;
        if(hours>11){
            aNoon="PM";
            if(hours>12){
                hours=hours%12;
            }
        }
        
        int minutes=(c%3600)/60;
        String minuteS=Integer.toString(minutes);
        if(minuteS.length()!=2){
            minuteS="0"+minuteS;
        }
        
        int seconds=(c%3600)%60;
        String secondS=Integer.toString(seconds);
        if(secondS.length()!=2){
            secondS="0"+secondS;
        }
        
        return hours+":"+minuteS+":"+secondS+" "+aNoon;
    }
    
    /**
     *  Prints a string to the console and GUI.
     * @param s     the information to be printed to console and GUI
     */
    public void msg(String s){
        System.out.println(s);
        results += s +"\n\n";
    }
    
    /**
     *  Prints a report for a question answered event.
     */
    public void printOutReport(){
        msg("Customer "+current.getCustNum()+" got their question answered at "
                            +time(qAnswered)+". "+"\n"+"The line is "+theLine.size()
                            +" person/people long."+"\n"+"There are "+doorLine
                            +" walk-in clients in line and "+phoneLine
                            +" phone call clients in line.");
        
    }
    
    /**
     * Prints a report for a customer in event, depending on if they are a phone
     * or door customer type.
     * @param call      true if Phone caller, false if Door walk-in
     */
    public void printInReport(boolean call){
        if(call){
            msg("Phone customer "+custNum+" called in at "+time(clock)+". " 
                            +"\n"+"The line is "+(theLine.size()+1)+" person/people long."+"\n"
                            +"There are "+doorLine+" walk-in clients in line and "
                            +phoneLine+" phone call clients in line."+"\n"
                            +"Customer "+custNum+" will have their question answered at "
                            +time(current.getQTime()+clock)+".");
        }
        else{
            msg("Door customer "+custNum+" walked in at "+time(clock)+". " 
                            +"\n"+"The line is "+(theLine.size()+1)+" person/people long."+"\n"
                            +"There are "+doorLine+" walk-in clients in line and "
                            +phoneLine+" phone call clients in line."+"\n"
                            +"Customer "+custNum+" will have their question answered at "
                            +time(theLine.peekLast().getQTime()+clock)+".");
        }
        
    }
    
    /**
     * Creates a random poisson number.
     * @param m     The mean to be used
     * @return      An int random poisson number with m mean
     */
    public int poisson(double m){
        double p,U;
        U=Math.random();
        p=m* -Math.log(U);
        return (int)Math.round(p);
    }
    
    /**
     * Gets the results that have been accumulated.
     * @return      A string compilation of the report results
     */
    public String getResults(){
        return results;
    }
    
    /**
     * Gets the number of customers.
     * @return      The total number of customers
     */
    public int getCustomerCount(){
        return custNum;
    }
    
    /**
     * Gets the number of phone customers.
     * @return      The total number of phone customers
     */
    public int getPhoneCount(){
        return phoneCount;
    }
    
    /**
     * Gets the number of phone customers.
     * @return      The total number of phone customers
     */
    public int getDoorCount(){
        return doorCount;
    }
    
    /**
     * Gets the number of customers who did not have their questions answered.
     * @return      The total number of customers in line after the sim is finished
     */
    public int getUnhelped(){
        return unhelpedCustomers;
    }
    
    /**
     * Converts the time in minutes to seconds and from string to int.
     * @param newTime       end time of the simulation in minutes as string
     */
    public void setTime(String newTime){
        this.endTime=60*(Integer.parseInt(newTime));
    }
    
    /**
     * Sets the question mean time.
     * @param newQ      the new question mean time as a string
     */
    public void setQ(String newQ){
        this.meanQ=Double.parseDouble(newQ);
    }
    
    /**
     * Sets the door interval mean time.
     * @param newD      the new door interval mean time as a string
     */
    public void setD(String newD){
        this.meanDoor=Double.parseDouble(newD);
    }
    
    /**
     * Sets the phone call interval mean time.
     * @param newP      the new phone call interval mean time as a string
     */
    public void setP(String newP){
        this.meanPhone=Double.parseDouble(newP);
    }
    
    
}
    
    
    
    
  
   