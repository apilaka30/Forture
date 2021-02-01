package Forture.v1;

import static org.junit.Assert.*;

import org.junit.Test;

import Forture.v1.reg_sys.QuoteSystem;

public class QuoteSystemTest
{

    QuoteSystem sys = new QuoteSystem("Quote Search:");
    
    @Test
    public void testConstructor()
    {
        assertNotNull(sys.panel);
    }
    
    @Test
    public void testProcessGraph()
    {
        sys.textField.setText( "MICROSOFT" );
        sys.processGraph();
        assertNotNull(sys.getForecast());
    }
    
    
    @Test
    public void testProcessQuote()
    {
        sys.textField.setText( "MSFT" );
        sys.processQuote();
        assertTrue(sys.textArea.getText().contains( "MICROSOFT" ));
    }

    
}
