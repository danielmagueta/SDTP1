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

/**
 *   Pilot thread.
 *
 *   It simulates the pilot life cycle.
 *   Static solution.
 */

public class Pilot extends Thread{
    
    
     /**
   *  Pilot state.
   */

   private int pilotState;
   
   /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport dAirport;
   
   /**
   *  Reference to the plane.
   */

   private final Plane plane;
   
   /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirport aAirport;

   /**
   *   Instantiation of a pilot thread.
   *
   *     @param name thread name
   *     @param dAirport reference to the departure airport
   *     @param plane reference to the plane
   *     @param aAirport reference to the arrival airport
   */

   public Pilot (String name, DepartureAirport dAirport, Plane plane, ArrivalAirport aAirport)
   {
      super (name);
      pilotState = PilotStates.AT_TRANSFER_GATES;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set pilot state.
     *
     *     @param state new pilot state
     */

    public void setPilotState(int state) {
        this.pilotState = state;
     }

    /**
     *   Get pilot state.
     *
     *     @return passenger state
     */

    public int getPilotState() {
        return pilotState;
     }

    /**
     *   Life cycle of the pilot.
     */
    
    @Override
    public void run ()
    {
       
       while ( !(aAirport.getnPassengerArrived() == 21))                                 // check for end of operations
       { dAirport.informPlaneReadyForBoarding ();               // the pilot informs the plane is ready for boarding
         GenericIO.writelnString ("11");  
         dAirport.waitForAllInBoard ();                         // the pilot awaits for all the passenger to be in board
         GenericIO.writelnString ("12");
         plane.flyToDestinationPoint ();                        // the pilot flies to destination point
         GenericIO.writelnString ("13");
         plane.announceArrival();                            // the pilot awaits for all the passengers to deboard the plane 
         while(plane.getnINF() != aAirport.getnOut());
         plane.setnINF(0);
         aAirport.setnOut(0);
         GenericIO.writelnString ("14");
         plane.flyToDeparturePoint ();                          // the pilot flies to departure point
         GenericIO.writelnString ("15");
         dAirport.parkAtTransferGate();                         //the pilot parks the plane in the departure airport
         GenericIO.writelnString ("16");
       }
    }
    
   
    

  
   
   
}