package main;

import entities.*;
import sharedRegions.*;
import genclass.GenericIO;
import genclass.FileOp;

/**
 *   Simulation of the Problem of the Sleeping Barbers.
 *   Static solution based on a priori reasoning to terminate the barbers threads.
 */

public class AirLift
{
  /**
   *    Main method.
   *
   *    @param args runtime arguments
   */

   public static void main (String [] args)
   {
      Passenger [] passenger = new Passenger [SimulPar.N];   // array of passenger threads
      Pilot pilot;                                           // pilot thread
      Hostess hostess;                                       // hostess thread
      DepartureAirport dAirport;                             // reference to the departure airport
      ArrivalAirport aAirport;                               // reference to the arrival airport
      Plane plane;                                           // reference to the plane
      GeneralRepos repos;                                    // reference to the general repository
      int passengersLeft;                                    // number of passengers left to take a flight
      String fileName;                                       // logging file name
      char opt;                                              // selected option
      boolean success;                                       // end of operation flag

     /* problem initialization */

      GenericIO.writelnString ("\n" + "      AirLift Problem\n");
      passengersLeft = SimulPar.N;
      
      do
      { GenericIO.writeString ("Logging file name? ");
        fileName = GenericIO.readlnString ();
        if (FileOp.exists (".", fileName))
           { do
             { GenericIO.writeString ("There is already a file with this name. Delete it (y - yes; n - no)? ");
               opt = GenericIO.readlnChar ();
             } while ((opt != 'y') && (opt != 'n'));
             if (opt == 'y')
                success = true;
                else success = false;
           }
           else success = true;
      } while (!success);

      repos = new GeneralRepos (fileName);
      dAirport = new DepartureAirport(repos);
      plane = new Plane(repos);
      aAirport = new ArrivalAirport(repos);
      pilot = new Pilot ("Pilot", dAirport, plane, aAirport);
      hostess = new Hostess ("Hostess", dAirport, aAirport, repos);  
      for (int i = 0; i < SimulPar.N; i++)
        passenger[i] = new Passenger ("Passenger_" + (i+1), i, dAirport, plane, aAirport);
      
  

     /* start of the simulation */

      for (int i = 0; i < SimulPar.N; i++)
        passenger[i].start ();
      pilot.start();
      hostess.start();

     /* waiting for the end of the simulation */

      GenericIO.writelnString ();
      for (int i = 0; i < SimulPar.N; i++)
      { try
        { passenger[i].join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The passenger " + (i+1) + " has terminated.");
      }
      GenericIO.writelnString ();
      try
        { pilot.join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The pilot has terminated.");
      GenericIO.writelnString ();
      try
        { hostess.join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The hostess has terminated.");
      
      GenericIO.writelnString ();
    }
}