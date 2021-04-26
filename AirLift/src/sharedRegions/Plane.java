package sharedRegions;

import main.*;
import entities.*;
import commInfra.*;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 *    Plane.
 *
 *    It is responsible to keep a continuously updated account of the customers inside the barber shop
 *    and is implemented using semaphores for synchronization.
 *    All public methods are executed in mutual exclusion.
 *    There are two internal synchronization points: a single blocking point for the barbers, where they wait for a customer;
 *    and an array of blocking points, one per each customer, where he both waits his turn to cut the hair and sits on the
 *    cutting chair while having his hair cut.
 */

public class Plane {
 
  /**
   *  Reference to passenger threads.
   */

   private final Passenger [] passenger;
   
    /**
   *  Reference to number of passengers in flight.
   */

   private int nINF;
   
  /**
   *   Reference to the general repository.
   */

   private final GeneralRepos repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;



    /**
   *   Passenger waiting in flight waiting to leave.
   */

   private MemFIFO<Integer> passengerINF;
   
        /**
   *  Blocking semaphore for the passengers while they are in flight.
   */

   private final Semaphore [] in_flight;
   
   /**
   *  Plane instantiation.
   *
   *    @param repos reference to the general repository
   */

   public Plane (GeneralRepos repos)
   {
      nINF = 0;
       passenger = new Passenger [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        passenger[i] = null;
      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      in_flight = new Semaphore [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        in_flight[i] = new Semaphore ();
      try
      { passengerINF = new MemFIFO<> (new Integer [SimulPar.N]);
      }
      catch (MemException e)
      { GenericIO.writelnString ("Instantiation of in flight FIFO failed: " + e.getMessage ());
        passengerINF = null;
        System.exit (1);
      }
   }

    public int getnINF() {
        return nINF;
    }

    public void setnINF(int nINF) {
        this.nINF = nINF;
    }
    

   
     /**
   *    Passengers in flight
   * 
   *       @return FIFo of ids of passengers in the flight
   */
    public MemFIFO<Integer> getPassengerINF() {
        return passengerINF;
    }
    
   /**
    *  Flying to destination airport.
    *
    *  It is called by a pilot when he is flying to the destination airport.
    */
   
    public void flyToDestinationPoint ()
   {
      access.down();
      ((Pilot) Thread.currentThread()).setPilotState(3);
      repos.setPilotState ((Pilot) Thread.currentThread(),3);
      access.up();
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
   /**
    *  Flying back to initial airport.
    *
    *  It is called by a pilot when he is flying to the departure airport.
    */
   
    public void flyToDeparturePoint ()
   {
      
      access.down();
      ((Pilot) Thread.currentThread()).setPilotState(5);
      repos.setPilotState ((Pilot) Thread.currentThread(),5);
      access.up();
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
   
     //passenger life cicle
   
   /**
   *  Operation for the passenger board the plane.
   *
   *  It is called by a passenger boarding the plane.
   *
   */
    public void boardThePlane() 
    {
        access.down();
        nINF ++;
        repos.addInF();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();     
        try
        { passengerINF.write (passengerID);                    
        }
        catch (MemException e)
        { GenericIO.writelnString ("Insertion of passenger id in waiting FIFO failed: " + e.getMessage ());
          access.up ();                
          System.exit (1);
        }
        ((Passenger) Thread.currentThread()).setPassengerState(2);
        repos.setPassengerState(passengerID,2);
        access.up();
    }
    
    
    /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * 
    */ 
    public void announceArrival ()
    {
        access.down();
        int passengerID = -1;
        int inf = repos.getInF();
        for(int i = 0; i<nINF; i++)
        {
            try
            { passengerID = passengerINF.read ();                            
              if ((passengerID < 0) || (passengerID >= SimulPar.N))
                 throw new MemException ("illegal passenger id!");
            }
            catch (MemException e)
            { GenericIO.writelnString ("Retrieval of passenger id from waiting FIFO failed: " + e.getMessage ());
              access.up ();                                                // exit critical region
              System.exit (1);
            }

            ((Pilot) Thread.currentThread()).setPilotState(4);
            repos.setPilotState((Pilot) Thread.currentThread(),4);
            access.up();
            in_flight[passengerID].up();
        }
 
    }
    
    
        /**
   *  Operation for the passenger waiting for the end of the flight.
   *
   *  It is called by a passenger awating for the arrival of the plane at the arrival airport.
   *
   */
    public void waitForEndOfFligh ()
    {
    
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        access.up();
        in_flight[passengerID].down();
    }

    
    


}
