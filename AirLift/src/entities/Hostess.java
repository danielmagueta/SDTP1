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
 *   Hostess thread.
 *
 *   It simulates the hostess life cycle.
 *   Static solution.
 */

public class Hostess extends Thread{
    
    /**
   *  Hostess identification.
   */
    
    private int hostessId;
    
     /**
   *  Hostess state.
   */

   private int hostessState;
   
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
   *   Instantiation of a hostess thread.
   *
   *     @param name thread name
   *     @param hostessId hostess id
   *     @param dAirport reference to the departure airport
   */

   public Hostess (String name, int hostessId, DepartureAirport dAirport, Plane plane, ArrivalAirport aAirport)
   {
      super (name);
      this.hostessId = hostessId;
      hostessState = HostessStates.WAIT_FOR_NEXT_FLIGHT;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set hostess id.
     *
     *     @param id hostess id
     */

    public void setHostessId(int id) {
        this.hostessId = id;
    }

    /**
     *   Get hostess id.
     *
     *     @return hostess id
     */

    public int getHostessId() {
        return hostessId;
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
       int passengerID;                                      // customer id
       
       /*
       while (bShop.goOn ())                                // check for end of operations
       { bShop.goToSleep ();                                // the barber sleeps while waiting for a customer to service
         customerId = bShop.callACustomer ();               // the barber has waken up and calls next customer
         cutHair ();                                        // the barber cuts the customer hair
         bShop.receivePayment (customerId);                 // the barber finishes his service and receives payment for it
       }*/
    }
    
   

}
