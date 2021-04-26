package sharedRegions;

import main.*;
import entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *    Arrival Airport.
 *
 *    It is responsible to keep a continuously updated account of the passengers in the queue for boarding.
 *    and is implemented using semaphores for synchronization.
 *    All public methods are executed in mutual exclusion.
 *    There are two internal synchronization points: a single blocking point for the barbers, where they wait for a customer;
 *    and an array of blocking points, one per each customer, where he both waits his turn to cut the hair and sits on the
 *    cutting chair while having his hair cut.
 */

public class ArrivalAirport {
    
      
  /**
   *  Reference to passenger threads.
   */

   private final Passenger [] passenger;
   
  /**
   *   Reference to the general repository.
   */

   private final GeneralRepos repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;
   
  /**
   *  Number of passengers that have arrived and deboarded at destination.
   */

   private int nPassengerArrived;
   
   /**
   *  Number of passengers that have leaved the plane.
   */
   private int nOut;

   
   /**
   *  Departure Airport instantiation.
   *
   *    @param repos reference to the general repository
   */

   public ArrivalAirport (GeneralRepos repos)
   {
      passenger = new Passenger [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        passenger[i] = null;
      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      nPassengerArrived = 0;
      nOut = 0;
      
   }

    public int getnOut() {
        return nOut;
    }

    public void setnOut(int nOut) {
        this.nOut = nOut;
    }
   
   
   
   /**
   *    Get number of passengers that have arrived and deboarded already.
   * 
   *       @return number of passengers that have arrived and deboarded alerady
   */
    public int getnPassengerArrived() {
        return nPassengerArrived;
    }
   
  
        /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * 
    */
    public void leaveThePlane ()
    {
        access.down();
        nOut ++;
        repos.subtractInF();
        repos.addPTAL();
        int inf = repos.getInF();
        ((Passenger) Thread.currentThread()).setPassengerState(3);
        repos.setPassengerState(((Passenger) Thread.currentThread()).getPassengerId(),3);
        nPassengerArrived ++;
        access.up();
        //if(inf == 0) deboarding.up();
        GenericIO.writelnString ("inf: " + String.valueOf(inf));

    }

}
