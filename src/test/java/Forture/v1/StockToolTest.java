package Forture.v1;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Forture.v1.analytics.StockTool;

public class StockToolTest
{

    File file = new File("src/main/resources/StockPairs.txt");
    StockTool t = new StockTool();
    String name = "microsoft";
    String symbol = "msft";
    
    @Test
    public void testConstructor()
    {
        assertEquals(file, t.getFile());
    }
    
    @Test
    public void testHasCompany()
    {
        assertTrue(t.hasCompany( name ));
        assertFalse(t.hasCompany( "abcdefg" ));
        assertFalse(t.hasCompany( symbol ));
        assertFalse(t.hasCompany( "abc" ));
    }
    
    @Test
    public void testHasSymbol()
    {
        assertTrue(t.hasSymbol( symbol ));
        assertFalse(t.hasSymbol( "abc" ));
        assertFalse(t.hasSymbol( name ));
        assertFalse(t.hasSymbol( "abcdefg" ));
    }
    
    @Test
    public void testAddPair()
    {
        t.addPair( name, symbol );
        assertTrue(t.hasCompany( name ));
        assertFalse(t.hasCompany( "abcdefg" ));
        assertFalse(t.hasCompany( symbol ));
        assertFalse(t.hasCompany( "abc" ));
        assertTrue(t.hasSymbol( symbol ));
        assertFalse(t.hasSymbol( "abc" ));
        assertFalse(t.hasSymbol( name ));
        assertFalse(t.hasSymbol( "abcdefg" ));
        
    }
    
    @Test
    public void testTranslateToName()
    {
        assertEquals(name, t.translateToName( symbol ));
        assertNotEquals(symbol, t.translateToName( name ));
    }

    @Test
    public void testTranslateToSymbol()
    {
        assertEquals(symbol, t.translateToSymbol( name ));
        assertNotEquals(name, t.translateToSymbol( symbol ));
    }
    
    @Test
    public void testPutPair()
    {
        t.putStockPair( "deca", "dca" );
        assertTrue(t.getSymbolsMap().containsKey( "deca" ));
        assertTrue(t.getSymbolsMap().containsValue( "dca" ));
        assertTrue(t.getCompaniesMap().containsKey( "dca" ));
        assertTrue(t.getCompaniesMap().containsValue( "deca" ));
    }
    
    @Test
    public void testRead()
    {
        t.read();
        assertTrue(t.hasCompany( name ));
        assertFalse(t.hasCompany( "abcdefg" ));
        assertFalse(t.hasCompany( symbol ));
        assertFalse(t.hasCompany( "abc" ));
        assertTrue(t.hasSymbol( symbol ));
        assertFalse(t.hasSymbol( "abc" ));
        assertFalse(t.hasSymbol( name ));
        assertFalse(t.hasSymbol( "abcdefg" ));
    }
}
