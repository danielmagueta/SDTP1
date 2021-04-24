/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
   *  Pilot identification.
   */
    
    private int pilotId;
    
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
   *     @param hostessId pilot id
   *     @param dAirport reference to the departure airport
   *     @param plane reference to the plane
   *     @param aAirport reference to the arrival airport
   */

   public Pilot (String name, int pilotId, DepartureAirport dAirport, Plane plane, ArrivalAirport aAirport)
   {
      super (name);
      this.pilotId = pilotId;
      pilotState = PilotStates.AT_TRANSFER_GATES;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set pilot id.
     *
     *     @param id pilot id
     */

    public void setPilotId(int id) {
        this.pilotId = id;
    }

    /**
     *   Get pilot id.
     *
     *     @return pilot id
     */

    public int getPilotId() {
        return pilotId;
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
       int passengerID;                                      // customer id
       
       /*
       while (bShop.goOn ())                                // check for end of operations
       { bShop.goToSleep ();                                // the barber sleeps while waiting for a customer to service
         customerId = bShop.callACustomer ();               // the barber has waken up and calls next customer
         cutHair ();                                        // the barber cuts the customer hair
         bShop.receivePayment (customerId);                 // the barber finishes his service and receives payment for it
       }*/
    }
    
   /**
    *  Flying to destination airport.
    *
    *  Internal operation.
    */
   
    private void flyToDestinationPoint ()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
    /**
    *  Flying back to initial airport.
    *
    *  Internal operation.
    */
   
    private void flyToDeparturePoint ()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
  
   
   
}