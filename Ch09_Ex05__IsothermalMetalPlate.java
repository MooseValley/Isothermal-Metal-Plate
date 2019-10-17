/*
   Author: Mike O'Malley
   Description: IsothermalMetalPlate
   My solution for: Example 9.2 - Temperature Distribution
   p297-304.

   Structured Fortran 77 for Engineers and Scientists,
   D. M. Etter.
   Chapter 9,
   9.5 APPLICATION - Temperature Distribution in Metal Plate, and,
   Example 9.2 - Temperature Distribution
   p297-304.

In this application we consider the temperature distribution in a thin metal
plate as it reaches a point of thermal equilibrium.  The plate is constructed so
that each edge is "isothermal", or maintained at a constant temperature.  The
temperature of an interior point on the plate is a function of the temperature
of the surrounding material.  If we consider the plate to be similar to a grid,
then a two-dimensional array could be used to store the temperatures of the
corressponding points on the plate.

The isothermal temperature at the top, bottom, left, and right [edges of the
metal plate] would be given  The interior points are initially set to some
arbitrary temperature, usually zero.  The new temperature of each interior point
is calculated as the average of its four surrounding [adjacent] points, as follows:

    T1
 T4 T0 T2     T0 = (T1 + T2 + T3 + T4) / 4
    T3

After computing the new temperatures for each interior point, the difference
between the old temperatures and the new temperatures is computed.  If the
magnitude of a temperature change is greater than some specified tolerance
value, the plate is not yet in thermal equilibrium, and the entire process
is repeated.

Since we will use only one array for the temperatures, as we change one
temperature, this new value will affect the change in adjacent temperatures.
The final results will also be slightly different depending on whether the
changes are made across the rows or down the columns.

Use a tolerance value of 0.2.



Mike's Notes:

* A nice little problem.  I LOVED solving problems like this when I was at
school and uni, and ~35+ years later, I still LOVE solving these problems.

* I don't think it is possible for a metal plate (or a plate made with any
other material) to have the suggested properties.  i,e, keeping the edges
at a constant temperature - does not change - must be impossible unless
some special heating / cooling machine is maintaining the edge temperatures.

* My solution is a million times better than the text book's solution.
e.g. I can change the initial temperatures, array sizes, etc by just
changing the constants at the top of my program.

*/

/*
Sample Output:

Initial Temperatures:
100.0   100.0   100.0   100.0
100.0     0.0     0.0   200.0
100.0     0.0     0.0   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #1:
100.0   100.0   100.0   100.0
100.0    50.0    87.5   200.0
100.0    87.5   143.8   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #2:
100.0   100.0   100.0   100.0
100.0    93.8   134.4   200.0
100.0   134.4   167.2   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #3:
100.0   100.0   100.0   100.0
100.0   117.2   146.1   200.0
100.0   146.1   173.0   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #4:
100.0   100.0   100.0   100.0
100.0   123.0   149.0   200.0
100.0   149.0   174.5   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #5:
100.0   100.0   100.0   100.0
100.0   124.5   149.8   200.0
100.0   149.8   174.9   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #6:
100.0   100.0   100.0   100.0
100.0   124.9   149.9   200.0
100.0   149.9   175.0   200.0
200.0   200.0   200.0   200.0

Temperature Iteration #7:
100.0   100.0   100.0   100.0
100.0   125.0   150.0   200.0
100.0   150.0   175.0   200.0
200.0   200.0   200.0   200.0

Equilibrium reached.

*/


public class Ch09_Ex05__IsothermalMetalPlate
{
   // Constants:
   private final static int    ROWS               = 4;
   private final static int    COLS               = 4;
   private final static double TOP_ROW_TEMP       = 100.0;
   private final static double BOTTOM_ROW_TEMP    = 200.0;
   private final static double LEFT_COL_TEMP      = 100.0;
   private final static double RIGHT_COL_TEMP     = 200.0;
   private final static double INNER_START_TEMP   =   0.0;
   private final static double EQUILIB_TOLERANCE  =   0.2;


   private static double tempteratures [][] = new double [ROWS][COLS];
   private static int    iterationCount;

   public static void initialiseTemperatures ()
   {
      iterationCount = 0;

      // Initialise array.
      for (int r = 0; r < ROWS; r++)
      {
         for (int c = 0; c < COLS; c++)
         {
            tempteratures [r][c] = INNER_START_TEMP;
         }
      }

      for (int c = 0; c < COLS; c++)
      {
         tempteratures [0][c]        = TOP_ROW_TEMP;    // Top row temperatures.
         tempteratures [ROWS - 1][c] = BOTTOM_ROW_TEMP; // Bottom row temperatures.
      }

      for (int r = 1; r < ROWS - 1; r++)  // Skip top and bottom rows
      {
         tempteratures [r][0]        = LEFT_COL_TEMP;   // Left column temperatures.
         tempteratures [r][COLS - 1] = RIGHT_COL_TEMP;  // Right column temperatures.
      }
   }


   public static void displayTemperatures ()
   {
      if (iterationCount == 0)
         System.out.println ("\nInitial Temperatures:");
      else
         System.out.println ("\nTemperature Iteration #" + iterationCount + ":");

      // Initialise array.
      for (int r = 0; r < ROWS; r++)
      {
         for (int c = 0; c < COLS; c++)
         {
            System.out.print (String.format ("%,5.1f", tempteratures [r][c])     + "   ");
         }
         System.out.println ();
      }

      iterationCount++;
   }

   public static boolean calculateNewTemperatures ()
   {
      double newTemp;
      boolean equilibriumFound = true;

      // Initialise array.
      for (int r = 1; r < ROWS - 1; r++)  // Skip top and bottom rows
      {
         for (int c = 1; c < COLS - 1; c++) // Skip left and right columns
         {
            newTemp = (tempteratures [r-1][c] + tempteratures [r+1][c] +
                       tempteratures [r][c-1] + tempteratures [r][c+1]) / 4.0;

            if (Math.abs (newTemp - tempteratures [r][c]) > EQUILIB_TOLERANCE)
               equilibriumFound = false;

            tempteratures [r][c] = newTemp;
         }
      }

      return equilibriumFound;
   }

   public static void main (String[] args)
   {
      boolean equilibriumFound = false;

      initialiseTemperatures ();
      displayTemperatures ();

      while (equilibriumFound == false)
      {
         equilibriumFound = calculateNewTemperatures ();
         displayTemperatures ();
      }
      System.out.println ("\nEquilibrium reached.");

   }
} // public class Ch09_Ex05__IsothermalMetalPlate