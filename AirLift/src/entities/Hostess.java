/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import genclass.GenericIO;
import sharedRegions.DepartureAirport;
import sharedRegions.Plane;
import sharedRegions.ArrivalAirport;
import sharedRegions.GeneralRepos;
import main.SimulPar;

/**
 *   Hostess thread.
 *
 *   It simulates the hostess life cycle.
 *   Static solution.
 */

public class Hostess extends Thread{

    
     /**
   *  Hostess state.
   */

   private int hostessState;
   
  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport dAirport;
  

   
     /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirport aAirport;
  
  /**
   *  Reference to the repository.
   */

   private final GeneralRepos repos;
   

   /**
   *   Instantiation of a hostess thread.
   *
   *     @param name thread name
   *     @param hostessId hostess id
   *     @param dAirport reference to the departure airport
   */

   public Hostess (String name, DepartureAirport dAirport, ArrivalAirport aAirport, GeneralRepos repos)
   {
      super (name);
      hostessState = HostessStates.WAIT_FOR_NEXT_FLIGHT;
      this.dAirport = dAirport;
      this.aAirport = aAirport;
      this.repos = repos;
   }
   

    /**
     *   Set hostess state.
     *
     *     @param state new hostess state
     */

    public void setHostessState(int state) {
        this.hostessState = state;
     }

    /**
     *   Get hostess state.
     *
     *     @return hostess state
     */

    public int getHostessState() {
        return hostessState;
     }

    /**
     *   Life cycle of the hostess.
     */
    
    @Override
    public void run ()
    {
       while (!(aAirport.getnPassengerArrived() == 21))                                 // check for end of operations
       { 
           GenericIO.writelnString ("20");
           dAirport.prepareForPassBoarding();
        GenericIO.writelnString ("21");// the hostess awaits for the pilot to indicate that the plane is ready for boarding
         int passengerID;
         while( !((repos.getInF()>=SimulPar.MIN) && (repos.getInQ() == 0))  || !(repos.getInF() == SimulPar.MAX) ||  !(repos.getInF() + repos.getPTAL() == SimulPar.MAX)){
            passengerID = dAirport.checkDocuments();  
            GenericIO.writelnString ("22");// the hostess checks the passenger documents
            dAirport.waitForNextPassenger(passengerID);                   // the hostess awaits for the next passanger
            GenericIO.writelnString ("23");
         }
         dAirport.informPlaneReadyToTakeOff();                         // the hostess informs the pilot that the plane is ready to take off
         GenericIO.writelnString ("24");
         dAirport.waitForNextFlight();                                 // the hostess awaits for the next flight
         GenericIO.writelnString ("25");
       }
    }
    
   

}
