package Group_Project;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import javax.swing.*;
import java.util.*;
/**
 * This is the front end GUI of the Secretary program class that initiates the 
 * running of the simulation, display the results, and allows the user to adjust
 * the values that the program uses to generate how often the customers arrive, 
 * how long their question takes to answer, and how long the simulation runs. 
 * @author Kyle Hewitt and Belen Levine 
 */
public class GroupGUI{
    JFrame frame, resultFrame, changeFrame, dFrame;//Various display frames
    JButton runSim, showDetails, changeSimProp, okay;//Function buttons
    JPanel north, south, center, changeNorth,changeSouth;//Panels for the frame contents
    Secretary simulation;//referance to the main class
    JTextArea resultDisplay, descriptionDisplay;//Show text to display 
    JScrollPane scroll, dScroll;//For large text areas that need scroll bars
    JLabel totalCus, phoneCus, doorCus, unhelped;//Labels for the main display
    JTextField qMean, dMean, pMean, simTime;//Field for user entered times
    JLabel qMean2, dMean2, pMean2, simTime2;//labels for user entry field
    int doorCount, phoneCount, customerCount, unhelpedCount;//Counts for final display
    String time, questionT, doorT, phoneT;//Values entered by user for program specs
    JMenuBar menuBar;//For opening program description
    JMenu help;//For opening program description
    JMenuItem programDescription;//For opening program description
    
    //Inititalization for all the GUI components
    public GroupGUI(){
        frame = new JFrame("Secretary");
        resultFrame = new JFrame("Results");
        changeFrame = new JFrame("Change Simulation Properties");
        dFrame = new JFrame("Program Description");
        runSim = new JButton("Run Simulation");
        showDetails = new JButton("See Details");
        changeSimProp = new JButton("Change Simulation Properties");
        okay = new JButton("Okay");
        north = new JPanel();
        south = new JPanel();
        center = new JPanel();
        changeNorth = new JPanel();
        changeSouth = new JPanel();
        resultDisplay = new JTextArea();
        descriptionDisplay = new JTextArea();
        scroll = new JScrollPane(resultDisplay, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dScroll = new JScrollPane(descriptionDisplay, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        totalCus = new JLabel();
        phoneCus = new JLabel();
        doorCus = new JLabel();
        unhelped = new JLabel();
        qMean = new JTextField(5);
        dMean = new JTextField(5);
        pMean = new JTextField(5);
        simTime = new JTextField(5);
        qMean2 = new JLabel("Question time Mean(Seconds): ");
        dMean2 = new JLabel("Door walk-in time Mean(Seconds): ");
        pMean2 = new JLabel("Phone call-in time Mean (Seconds): ");
        simTime2 = new JLabel("Total Simulation time (Minutes): ");
        menuBar = new JMenuBar();
        help = new JMenu("How to use this program");
        programDescription = new JMenuItem("Program Description");
        GUISetup();
    }//End Constructor
    
    //Sets inital values and connects the pieces
    public void GUISetup(){
        simTime.setText("50");
        qMean.setText("24");
        dMean.setText("45");
        pMean.setText("55");
        changeNorth.add(simTime2);
        changeNorth.add(simTime);
        changeNorth.add(pMean2);
        changeNorth.add(pMean);
        changeNorth.add(dMean2);
        changeNorth.add(dMean);
        changeNorth.add(qMean2);
        changeNorth.add(qMean);
        changeSouth.add(okay);
        
        resultDisplay.setEditable(false);
        descriptionDisplay.setEditable(false);
        
        changeFrame.add(changeNorth, BorderLayout.NORTH);
        changeFrame.add(changeSouth, BorderLayout.SOUTH);
        
        center.add(totalCus, BorderLayout.CENTER);
        center.add(doorCus, BorderLayout.CENTER);
        center.add(phoneCus, BorderLayout.CENTER);
        center.add(unhelped, BorderLayout.CENTER);

        help.add(programDescription);
        menuBar.add(help);
        north.add(menuBar);
        
        frame.setBounds(650, 400, 500, 200);
        changeFrame.setBounds(500, 400, 1100, 145);
        resultFrame.setBounds(500, 200, 600, 500);
        dFrame.setBounds(500, 200, 600, 500);
        frame.add(north, BorderLayout.NORTH);
        frame.add(south, BorderLayout.SOUTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        resultFrame.add(scroll);
        dFrame.add(dScroll);
        showDetails.setEnabled(false);
        
        south.add(runSim);
        south.add(showDetails);
        south.add(changeSimProp);
        
        time = simTime.getText();
        questionT = qMean.getText();
        phoneT = pMean.getText();
        doorT = dMean.getText();
        frame.setVisible(true);
        buttonFunctions();
    }//End GUISetup
    
    
    //Sets what happens when you select a clickable component.
    public void buttonFunctions(){
        runSim.addActionListener((ActionEvent q) -> {
                simulation = new Secretary();
                setNewValues();
                simulation.runSim();
                showDetails.setEnabled(true);
                showResults();         
        });
        showDetails.addActionListener((ActionEvent e) -> {
            resultDisplay.setText(simulation.getResults());
            resultFrame.setVisible(true);  
        });
        changeSimProp.addActionListener((ActionEvent ch) ->{
            changeFrame.setVisible(true);
        });
        okay.addActionListener((ActionEvent ok) ->{ 
            changeFrame.setVisible(false);
        });
        programDescription.addActionListener((ActionEvent d)->{
            descriptionDisplay.setText(programD());
            dFrame.setVisible(true);
        });
    }//End buttonFuntions
    
    //Brings in the results from the Secretary class and sets their contents to 
    // the components that display results in the GUI
    public void showResults(){
        customerCount = simulation.getCustomerCount();
        phoneCount = simulation.getPhoneCount();
        doorCount = simulation.getDoorCount();
        unhelpedCount = simulation.getUnhelped();
        totalCus.setText("Total number of Customers: "+customerCount+"\t\t");
        doorCus.setText("Number of Door Customers: "+doorCount+"\t\t");
        phoneCus.setText("Number of Phone Customers: "+phoneCount+"\t\t");
        unhelped.setText("Number of Customers left unhelped at the end: "+unhelpedCount);
    }//showResults
    
    //Takes in the user selected values and passes them to the Secretary class
    // for mean times 
    public void setNewValues(){
            questionT = qMean.getText();
            phoneT = pMean.getText();
            doorT = dMean.getText();
            time = simTime.getText();
            simulation.setTime(time);
            simulation.setD(doorT);
            simulation.setP(phoneT);
            simulation.setQ(questionT);
    }//End setNewValues
    
    //contains the program description
    public String programD(){
        String programD = "This program will simulate the day in the life of a "
            + "very busy secretary. \n\n He starts his day off at 8:00:00 am, "
            + "and answers questions from both walk-in (door) customers,\n and "
            + "call-in(phone) customers.\n Since the secretary does not know"
            + "who the phone customers are, he assumes that they could be\n someone"
            + " very important and he will stop helping the current customer "
            + "to answer the new phone call\nand then continue his answer with the"
            + "next most current customer (only having to answer the remaining \n"
            + "part of the question). \n\n Although the arraival of both the "
            + "phone and the door customers are random, their arrival can be \n"
            + "averaged to say that each customer arrives at mean time p(phone) and"
            + "d(door), with each customer \ntaking a mean average time of q to "
            + "have their questions answered.\n\n What this program does:\n\n"
            + "This program simulates a day in the life of the secretary. Each"
            + "run through the simulation will show \nthe results of 50 minutes"
            + "with a p time of 55 seconds, d time of 45 seconds and q time of"
            + " 44 \nseconds unless changed by the user. \n The program will then "
            + "display the results of how many total customers had their questions"
            + "\nanswered, how many of those were phone, how many door, and how many"
            + " customers were \nin line unhelped at the end of the simulation time.";
        return programD;
    }//End programDetails
   
}//End GroupGUI class