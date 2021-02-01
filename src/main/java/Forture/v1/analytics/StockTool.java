package Forture.v1.analytics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Finds the stock for a company by using the StockPairs.txt file because it has
 * the company names and stock symbols in it. Adds the symbols and names in the
 * hashmaps. Translates the symbols to company names and vice versa.
 *
 * @author Ananth Pilaka, Chinmay Gowdru, Snehith Nayak
 * @version May 27, 2019
 * @author Period: 1
 * @author Assignment: The Amazing Stock Steroid
 *
 * @author Sources: Our Team
 */
public class StockTool
{
    public final static String API_KEY = "50M3AP1K3Y";

    private static HashMap<String, String> getSymbols;

    private static HashMap<String, String> getCompanies;

    public static File pairSource;

    private Scanner scanner;


    /**
     * Constructor for the StockTool class and uses the companies and symbols
     * from stockpairs.txt to retrieve the stocks
     */
    public StockTool()
    {
        getSymbols = new HashMap<String, String>();
        getCompanies = new HashMap<String, String>();
        pairSource = new File( "src/main/resources/StockPairs.txt" );
        try
        {
            scanner = new Scanner( pairSource );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        read();
    }


    /**
     * Checks if the Hashmap, getSymbols is empty
     * 
     * @param name
     *            String name parameter
     * @return if the name in , Hashmap, getSymbols is empty returns false, else
     *         true
     */
    public boolean hasCompany( String name )
    {
        return ( getSymbols.get( name ) != null );
    }


    /**
     * Checks if the Hashmap, getCompanies is empty
     * 
     * @param symbol
     *            String symbol parameter
     * @return if the name in , Hashmap, getCompanies is empty returns false,
     *         else true
     */
    public boolean hasSymbol( String symbol )
    {
        return ( getCompanies.get( symbol ) != null );
    }


    /**
     * Writes/adds a pair of the name and the symbol
     * 
     * @param name
     *            String name parameter
     * @param symbol
     *            String symbol parameter
     */
    public void addPair( String name, String symbol )
    {
        BufferedWriter writer;
        try
        {
            writer = new BufferedWriter( new FileWriter( pairSource, true ) );
            writer.append( '\n' + name + ":" + symbol );
            writer.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }


    /**
     * Translates the symbol to a company name
     * 
     * @param symbol
     *            String symbol parameter
     * @return symbol
     */
    public String translateToName( String symbol )
    {
        return getCompanies.get( symbol.toLowerCase() );
    }


    /**
     * Translates the company name to a symbol
     * 
     * @param name
     *            String name parameter
     * @return name of the stock
     */
    public String translateToSymbol( String name )
    {
        return getSymbols.get( name.toLowerCase() );
    }


    /**
     * Puts name and symbol in getSymbols and puts symbol and name in
     * getCompanies
     * 
     * @param name
     *            String name parameter
     * @param symbol
     *            String symbol parameter
     */
    public void putStockPair( String name, String symbol )
    {
        getSymbols.put( name.toLowerCase(), symbol.toLowerCase() );
        getCompanies.put( symbol.toLowerCase(), name.toLowerCase() );
    }


    /**
     * Scans each line and splits each line by looking for ":" since the company
     * name is on the left and the stock symbol is on the right
     */
    public void read()
    {
        String[] pair = new String[2];
        while ( scanner.hasNextLine() )
        {
            pair = scanner.nextLine().split( ":" );
            putStockPair( pair[0].toLowerCase(), pair[1].toLowerCase() );
        }
    }


    // testing getter methods
    /**
     * gets the pairSource
     * @return pairsource
     */
    public File getFile()
    {
        return pairSource;
    }


    /**
     * gets the stock symbols
     * @return getSymbols
     */
    public HashMap<String, String> getSymbolsMap()
    {
        return getSymbols;
    }


    /**
     * gets the company names
     * @return getCompanies
     */
    public HashMap<String, String> getCompaniesMap()
    {
        return getCompanies;
    }

    // /**
    // * The main method for the StockTool class and reads the tool
    // * @param args String args parameter
    // */
    // public static void main ( String[] args )
    // {
    // StockTool tool = new StockTool();
    // //tool.addPair( "HP", "HPQ");
    // tool.read();
    // }

}
