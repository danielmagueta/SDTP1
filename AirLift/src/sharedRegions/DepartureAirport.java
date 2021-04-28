package sharedRegions;

import main.*;
import entities.*;
import commInfra.*;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 *    Departure Airport.
 *
 */

public class DepartureAirport {

  /**
   *  Reference to passenger threads.
   */

   private final Passenger [] passenger;
   
  /**
   *   Passenger waiting in queue to be chacked.
   */

   private MemFIFO<Integer> passengerFIFO;
   
   
  /**
   *   Reference to the general repository.
   */

   private final GeneralRepos repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;

  /**
   *  Blocking semaphore for the pilot thread who is waiting for the boarding to be concluded.
   */

   private final Semaphore waiting_boarding;

  /**
   *  Blocking semaphore for the hostess thread while she waits for the next flight.
   */

   private final Semaphore waiting_next_flight;
  
   /**
   *  Blocking semaphore for the hostess thread while she waits for the next passenger.
   */
   
   
   private final Semaphore waiting_passenger;
  
   /**
   *  Blocking semaphore for the hostess thread while she checks the passenger.
   */
   
   private final Semaphore checking_passenger;
  
   /**
   *  Array of blocking semaphores for the passenger threads while they wait to be checked and while they wait for a new passenger to arrive.
   */
   
     private final Semaphore [] inQueue;
   
   /**
   *  Bollean for the termination of hostess thread.
   */
   
   private boolean endHostess;
   
   /**
   *  Number of passengers in queue.
   */
   
   private int nINQ;
   
   /**
   *  Number of passengers in this flight.
   */
   
   private int nINF;
   
   /**
   *  Total number of passengers that have departed.
   */
   
   private int ntotalINF;
   
    /**
   *  Passengers that haven't still arrived at airport and boarded.
   */
   
   private int passengersLEFT;
   
      /**
   *  Bollean for if the hostess can (true) or not (false) take off
   */
   
   private boolean HostessTakeOff;
 
   

   /**
   *  Departure Airport instantiation.
   *
   *    @param repos reference to the general repository
   *    @param aAirport reference to the arrival airport
   */

   public DepartureAirport (GeneralRepos repos)
   {
      endHostess = false;
      passenger = new Passenger [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        passenger[i] = null;
      try
      { passengerFIFO = new MemFIFO<> (new Integer [SimulPar.N]);
      }
      catch (MemException e)
      { GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
        passengerFIFO = null;
        System.exit (1);
      }
      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      waiting_boarding = new Semaphore ();
      waiting_next_flight = new Semaphore ();
      waiting_passenger = new Semaphore ();
      checking_passenger = new Semaphore ();
      inQueue = new Semaphore [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        inQueue[i] = new Semaphore ();
     nINQ = 0;
     nINF = 0;
     ntotalINF = 0;
     passengersLEFT = SimulPar.N;
     HostessTakeOff = false;
   }
   

    
        public int getnINQ() {
        return nINQ;
    }


    
    

//pilot life cicle
   
  /**
   *  Operation for the pilot to unblock the waiting hostess.
   *
   *  It is called by a pilot to unlock the hostess thread, so ic can terminate.
   *
   */
    
    public void endHostess ()
    {
        waiting_next_flight.up();
    }
    
  /**
   *  Operation inform that the plane is ready for boarding.
   *
   *  It is called by a pilot when the plane is ready for the next boarding.
   *
   */
    
    public void informPlaneReadyForBoarding ()
    {
        access.down();
        repos.reportBoarding();
        ((Pilot) Thread.currentThread()).setPilotState(1);
        repos.setPilotState ((Pilot) Thread.currentThread(),1);
        access.up();
        waiting_next_flight.up();


    }
    
  /**
   *  Operation for the pilot to wait for conclusion of boarding.
   *
   *  It is called by a pilot when the hostess informs the plane is ready to take off.
   *
   */
    
    public void waitForAllInBoard ()
    {
        access.down();
        ((Pilot) Thread.currentThread()).setPilotState(2);
        repos.setPilotState ((Pilot) Thread.currentThread(),2);
        access.up();
        waiting_boarding.down();
    }
    

  /**
   *  Operation for the pilot to park at the transfer gate.
   *
   *  It is called by a pilot when he parks the plane at the transfer gate, .
   *
   */
    public void parkAtTransferGate (){
        access.down();
        ((Pilot) Thread.currentThread()).setPilotState(0);
        repos.setPilotState ((Pilot) Thread.currentThread(),0);
        access.up(); 
    }
    
    //hostess life cicle
    
   /**
   *  Operation for the hostess to prepare for pass boarding.
   *
   *  It is called by a hostess when she prepares to wait for the passengers.
   *
   */
    public void prepareForPassBoarding ()
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(1);
        repos.setHostessState ((Hostess) Thread.currentThread(),1);
        access.up();
        waiting_passenger.down();
    }
    
    
    /**
   *  Operation for the hostess to check documents.
   *
   *  It is called by a hostess when she is checking the passenger documents.
   * @return id of the passenger
   *
   */
    public int checkDocuments ()
    {
        int passengerID = -1;
        access.down();
        try
        { passengerID = passengerFIFO.read ();                            // the hostess calls the passenger to check documents
            if ((passengerID < 0) || (passengerID >= SimulPar.N))
             throw new MemException ("illegal passenger id!");
        }
        catch (MemException e)
        { GenericIO.writelnString ("CheckDocuments failed: " + e.getMessage ());
          access.up ();                                                // exit critical region
          System.exit (1);
        }
        ((Hostess) Thread.currentThread()).setHostessState(2);
        nINQ --;
        repos.subtractInQ();
        repos.reportCheck (passengerID);
        repos.setHostessState ((Hostess) Thread.currentThread(),2);
        access.up();
        inQueue[passengerID].up();
        checking_passenger.down();
        return passengerID;
    }


    

    
    
    /**
   *  Operation for the hostess to be locked in the first iteration.
   *
   *  It is called by a hostess in the first iteration so shw waits for the plane to be ready for boarding.
   * 

   */
    public void waitPilot ()
    {
        waiting_next_flight.down();

    }
    
    /**
   *  Operation for the hostess to wait for a new passenger.
   *
   *  It is called by a hostess when she waits for a new passenger.
   * 
   * @param passengerID id of passenger
   *
   */
    public boolean waitForNextPassenger (int passengerID)
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(1);
        repos.setHostessState ((Hostess) Thread.currentThread(),1);
        inQueue[passengerID].up();
        nINF++;
        ntotalINF++;
        passengersLEFT--;
        access.up();
        access.up();
        try
        { sleep ((long) (2));       //ssmall break so the passenger has time to print his final state in the repository, purely aesthetic
        }
        catch (InterruptedException e) {}
        if( ((nINF>=SimulPar.MIN) && (nINQ == 0))  || (nINF == SimulPar.MAX) ||  (passengersLEFT == 0))
        {
            nINF = 0;
            return true; 
        }
        else
        {   waiting_passenger.down();
            return false;
        }
        
        
        
    }
    
    /**
   *  Operation for the hostess to inform the palne is ready to flight.
   *
   *  It is called by a hostess after the plane gets ready to leave.
   *
   */
    public void informPlaneReadyToTakeOff ()
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(3);
        repos.reportDeparted();
        repos.setHostessState ((Hostess) Thread.currentThread(),3);
        access.up();
        waiting_boarding.up();
    }
    
    /**
   *  Operation for the hostess to wait for the next flight.
   *
   *  It is called by ahostess while she is waiting for the next flight.
   *
   */
    public void waitForNextFlight ()
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(0);
        repos.setHostessState ((Hostess) Thread.currentThread(),0);
        access.up();
        waiting_next_flight.down();
    }
    
     //passenger life cicle
    
    
   /**
   *  Operation for the passenger to go to the waiting queue.
   *
   *  It is called by a passenger after going to the airport.
   *
   */
    public void waitInQueue ()
    {
        nINQ ++;
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        try
        { passengerFIFO.write (passengerID); 
        
        }
        catch (MemException e)
        { GenericIO.writelnString ("Insertion of passenger id in queue for plane failed: " + e.getMessage ());
          access.up ();                
          System.exit (1);
        }
        repos.addInQ();
        ((Passenger) Thread.currentThread()).setPassengerState(1);
        repos.setPassengerState(passengerID,1);
        access.up();
        waiting_passenger.up();
        inQueue[passengerID].down();
    }
    
    /**
   *  Operation for the passenger to show his/her documents to the hostess.
   *
   *  It is called by a passenger showing his/her documents to teh hostess.
   *
   */
    public void showDocuments ()
    {
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        access.up();
        checking_passenger.up();
        inQueue[passengerID].down();
        
    }

    
}
