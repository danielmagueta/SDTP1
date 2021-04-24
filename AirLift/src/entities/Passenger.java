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
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 *   Static solution.
 */

public class Passenger extends Thread{
    
    /**
   *  Passenger identification.
   */
    
    private int passengerId;
    
     /**
   *  Passenger state.
   */

   private int passengerState;
   
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
   *   Instantiation of a passenger thread.
   *
   *     @param name thread name
   *     @param hostessId passenger id
   *     @param dAirport reference to the departure airport
   *     @param plane reference to the plane
   *     @param aAirport reference to the arrival airport
   */

   public Passenger (String name, int passengerId, DepartureAirport dAirport, Plane plane, ArrivalAirport aAirport)
   {
      super (name);
      this.passengerId = passengerId;
      passengerState = PassengerStates.GOING_TO_AIRPORT;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set passenger id.
     *
     *     @param id passenger id
     */

    public void setPassengerId(int id) {
        this.passengerId = id;
    }

    /**
     *   Get passenger id.
     *
     *     @return passenger id
     */

    public int getPassengerId() {
        return passengerId;
     }

    /**
     *   Set passenger state.
     *
     *     @param state new passenger state
     */

    public void setPassengerState(int state) {
        this.passengerState = state;
     }

    /**
     *   Get passenger state.
     *
     *     @return passenger state
     */

    public int getPassengerState() {
        return passengerState;
     }

    /**
     *   Life cycle of the passenger.
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
    *  Passenger going to the departure airport.
    *
    *  Internal operation.
    */

    private void travelToAirport ()
    {
        try
        { sleep ((long) (1 + 200 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }



  
   
   
   

}