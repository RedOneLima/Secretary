package Group_Project;

import java.lang.Integer;
/**
 *
 * @author Belen Levine and Kyle Hewitt 
 */
public class Customer {
    
    /**
     *
     * @param args
     */
    public static void main(String[]args){
        
    }
    //phoneCall
    final boolean phoneCall;    //true if customer is phone, false if walk in
    private final int timeIn;   //the time (seconds) the customer came in
    private final int qTime;    //how long it takes to answer their question
    private int qTimeRem;   //how much time remains for their question to be answered
    private int atDesk; //the time (seconds) that the customer gets to the front desk
    private final int custNum;  //the customer number
    
    /**
     * The constructor that saves the type of customer, what time they entered 
     * the office, the customer number, and how long it will take to answer
     * the customer's question.
     * @param phoneCall     true if phone caller, false if walk-in
     * @param timeIn        time(secs) that the customer called/walked in
     * @param custNum       the customer number
     * @param qTime     how long it will take to answer the customer's question
     */
    public Customer(boolean phoneCall, int timeIn, int custNum, int qTime){
        
        this.phoneCall=phoneCall;
        this.timeIn=timeIn;
        this.custNum=custNum;
        this.qTime=qTime;
        qTimeRem=qTime;
        
    }
    
    /**
     * Sets the time (seconds) that the customer is at the front desk.
     * @param c     the time(seconds) when the customer is at front desk
     */
    public void setAtDesk(int c){
        atDesk=c;
    }
    
    /**
     * Sets the time remaining (seconds) for the customer's question to be 
     * answered, if they were interrupted.
     * @param c     the time the customer was interrupted
     */
    public void setQTimeRem(int c){
        qTimeRem=qTime-(c-atDesk);
    }
    
    /**
     * Gets the time (seconds) the customer is at the front desk.
     * @return      time (seconds) the customer got to the front desk
     */
    public int getAtDesk(){
        return atDesk;
    }
    
    /**
     * Gets whether or not the customer is a phone caller or walk-in.
     * @return      true if phone caller, false if walk-in
     */
        public boolean getPhoneCall(){
        return phoneCall;
    }
    
    /**
     * Gets the time (seconds) that the customer walked/called in.
     * @return  time(seconds)
     */
    public int getTimeIn(){
        return timeIn;
    }
    
    /**
     * Gets the time it will take to answer the customer's question.
     * @return      time (seconds)
     */
    public int getQTime(){
        return qTime;
    }
    
    /**
     * Gets the time remaining for the question to be answered if interrupted.
     * @return      time (seconds)
     */
    public int getQTimeRem(){
        return qTimeRem;
    }
    
    /**
     * Gets the customer number.
     * @return       customer number
     */
    public int getCustNum(){
        return custNum;
    }
    
   
}
