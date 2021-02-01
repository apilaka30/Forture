package Forture.v1;

import javax.swing.SwingUtilities;

import Forture.v1.reg_sys.QuoteSystem;


/**
 *  The main class that runs the whole program and includes the main method
 *  
 *  @author  Ananth Pilaka, Chinmay Gowdru, Snehith Nayak
 *  @version May 26, 2019
 *  @author  Period: 1
 *  @author  Assignment: The Amazing Stock Steroid 
 *
 *  @author  Sources: Our Team
 */
public class Coordinator
{
    private static QuoteSystem qs;


    /**
     * Sets qs to Quote Search
     */
    public void start()
    {
        qs = new QuoteSystem( "Quote Search:" );
    }


    /**
     * Returns the quoteSystem
     * @return qs
     */
    public QuoteSystem getSystem()
    {
        return qs;
    }


    // UNCOMMENT TO RUN PROGRAM
    /**
     * The main method for the program
     * @param args String parameter
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                new Coordinator().start();
            }
        } );
    }
}
