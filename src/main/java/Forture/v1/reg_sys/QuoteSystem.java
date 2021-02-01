package Forture.v1.reg_sys;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;

import Forture.v1.GradientPanel;
import Forture.v1.analytics.StockTool; 
import Forture.v1.predict_sys.PredictionSystem;

/**
 * The current stock data for the company's stock and graph
 *
 * @author Ananth Pilaka, Chinmay Gowdru, Snehith Nayak
 * @version May 27, 2019
 * @author Period: 1
 * @author Assignment: The Amazing Stock Steroid
 *
 * @author Sources: Our Team
 */
public class QuoteSystem extends JFrame implements ActionListener
{
    private static JFrame thisWindow;

    public GradientPanel panel;

    private StockGraph graph;

    public StockTool stockTool;

    public JTextField textField;

    public JTextArea textArea;

    private JMenuItem openItem, exitItem;

    public JButton refreshButton, data;

    private JDialog dialog;

    private boolean shift = false;

    public int height, width;

    public JLabel label;

    public JProgressBar progress;


    /**
     * Constructor for the QuoteSystem class and sets the bounds for the graph
     * 
     * @param purpose
     *            String purpose parameter
     */
    public QuoteSystem( String purpose )
    {
        super( purpose );

        width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        thisWindow = this;
        setBounds( 0, 0, width, height );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        panel = new GradientPanel(getWidth(), getHeight());
        thisWindow.getContentPane().add( panel );
        panel.setLayout( null );

        progress = new JProgressBar();
        progress.setBounds( 50, 350, 200, 20 );
        progress.setIndeterminate( true );
        panel.add( progress );
        progress.setVisible( false );
        progress.setStringPainted( true );
        progress.setString( "Searching for Stock..." );
        

        addComponents();
        setVisible( true );
        stockTool = new StockTool();
    }


    // @Override
    // public void paint(Graphics g) {
    // Graphics2D g2d = (Graphics2D) g.create();
    // Color color1 = new Color(255, 0, 0);
    // Color color2 = new Color(0, 0, 255);
    // GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(),
    // color2);
    // g2d.setPaint(gp);
    // g2d.fillRect(0, 0, getWidth(), getHeight());
    // }

    /**
     * Creates pop-ups for the user to use and click on such as Stock not found
     */
    private void addComponents()
    {
        
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu( "View" );
        fileMenu.setMnemonic( 'v' );

        openItem = new JMenuItem( "Open..." );
        openItem.setMnemonic( 'o' );
        openItem.addActionListener( new ActionListener()
        {
            String pathName = System.getProperty( "c.dir" ) + "/";


            @Override
            public void actionPerformed( ActionEvent e )
            {
                JFileChooser fileChooser = new JFileChooser( pathName );
                fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                int result = fileChooser.showOpenDialog( thisWindow );
                if ( result == JFileChooser.CANCEL_OPTION )
                    return;

                File file = fileChooser.getSelectedFile();
                if ( file != null )
                    pathName = file.getAbsolutePath();

                BufferedReader inputFile;
                try
                {
                    inputFile = new BufferedReader( new FileReader( pathName ), 1024 );
                }
                catch ( FileNotFoundException ex )
                {
                    JOptionPane.showMessageDialog( thisWindow,
                        "Invalid File Name",
                        "Cannot open " + pathName,
                        JOptionPane.ERROR_MESSAGE );
                    return;
                }

                try
                {
                    inputFile.close();
                }
                catch ( IOException ex )
                {
                    System.err.println( "Error closing " + pathName + "\n" );
                    return;
                }

                textArea.setText( "Indexed " + pathName );

            }

        } );
        exitItem = new JMenuItem( "Exit" );
        exitItem.setMnemonic( 'x' );
        exitItem.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                System.exit( 0 );
            }

        } );

        fileMenu.add( makeChangedItem() );
        fileMenu.addSeparator();
        fileMenu.add( openItem );
        fileMenu.addSeparator();
        fileMenu.add( exitItem );

        menuBar.add( fileMenu );
        setJMenuBar( menuBar );

        ImageIcon refresh = new ImageIcon( "src/main/resources/refresh_icon.png" );
        refreshButton = new JButton( "Refresh Graph  ", refresh );
        refreshButton.setBounds( 140, 310, 94 + 69, refresh.getIconHeight() );
        refreshButton.setHorizontalTextPosition( JButton.RIGHT );
        refreshButton.setVerticalTextPosition( JButton.CENTER );
        panel.add( refreshButton );
        refreshButton.setActionCommand( "makegraph" );
        refreshButton.addActionListener( this );

        data = new JButton( "Get Data" );
        data.setBounds( 20, 310, 100, refresh.getIconHeight() );
        panel.add( data );
        data.setActionCommand( "getquote" );
        data.addActionListener( this );

        textField = new JTextField();
        textField.setBounds( 20, 40, 300, 20 );
        textField.addKeyListener( new KeyListener()
        {

            @Override
            public void keyPressed( KeyEvent e )
            {

                if ( e.getKeyCode() == KeyEvent.VK_SHIFT )
                {
                    shift = true;
                }
                else if ( !shift && e.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    if ( !textField.getText().toString().trim().equals( "" ) )
                    {
                        processQuote();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( dialog,
                            "Please enter a stock name or symbol" );
                    }
                }
                else if ( shift && e.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    processGraph();
                }

            }


            @Override
            public void keyReleased( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_SHIFT )
                {
                    shift = false;
                }
            }


            @Override
            public void keyTyped( KeyEvent e )
            {

            }

        } );
        panel.add( textField );

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds( 20, 80, 300, 200 );
        panel.add( scrollPane );

        textArea = new JTextArea();
        scrollPane.setViewportView( textArea );

        graph = new StockGraph();
        label = new JLabel( graph.title.toUpperCase() + " - 4 Days" );
        label.setBounds( 330 + ( width - 330 ) / 2, 20 + ( height - 200 ), 200, 20 );
        label.setBackground( Color.WHITE );
        panel.add( label );
        panel.add( graph );

        JLabel label = new JLabel( "Type in the Company Name or Symbol:" );
        label.setBounds( 20, 20, 250, 20 );
        panel.add( label );

        JLabel label2 = new JLabel( "Data:" );
        label2.setBounds( 20, 60, 200, 20 );
        panel.add( label2 );
    }


    /**
     * The user can choose Open predictive system or open quote system, uses
     * Prediction system
     * @return changedItem
     */
    public JMenuItem makeChangedItem()
    {
        JMenuItem changedItem = new JMenuItem( "Open Predictive System" );
        changedItem.setMnemonic( 'p' );
        changedItem.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                thisWindow = new PredictionSystem();
            }

        } );
        return changedItem;
    }


    /**
     *  If the command is makegraph, then get the graph, else if the command is
     *  getquote, then get the data for the stock
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {

        if ( e.getActionCommand().equals( "makegraph" ) )
        {
            processGraph();
        }
        else if ( e.getActionCommand().equals( "getquote" ) )
        {
            processQuote();
        }

    }


    /**
     * Returns the data for the stock bu using the Stock Api
     * @param symbol String symbol parameter
     * @return stock data
     */
    public String getQuote( String symbol )
    {
        int iLoveYou = 3000;
        AlphaVantageConnector apiConnection = new AlphaVantageConnector( StockTool.API_KEY,
            iLoveYou );
        TimeSeries stockTimeSeries = new TimeSeries( apiConnection );

        StockData quote = null;
        try
        {
            IntraDay response = stockTimeSeries
                .intraDay( symbol, Interval.ONE_MIN, OutputSize.COMPACT );

            List<StockData> stockData = response.getStockData();
            quote = response.getStockData().get( 0 );

        }
        catch ( AlphaVantageException e )
        {

        }
        String name = stockTool.translateToName( symbol ).toUpperCase();
        return name + ": (" + symbol.toUpperCase() + ")" + '\n' + "  Price: " + quote.getClose()
            + '\n' + "  High: " + quote.getHigh() + '\n' + "  Low: " + quote.getLow() + '\n'
            + "  Volume: " + quote.getVolume() + '\n' + "  Date: "
            + quote.getDateTime().toString().substring( 0, 10 ) + '\n' + "  Time: "
            + quote.getDateTime().toString().substring( 11 );

    }


    /**
     * Processes the graph for the quote system/current data. if the stock is
     * not found, stock is not available will be occur
     */
    public void processGraph()
    {
        if ( stockTool.translateToName( textField.getText().toString().toLowerCase() ) != null )// means
                                                                                                // textField
                                                                                                // contains
                                                                                                // symbol
        {
            progress.setVisible( true );
            panel.remove( graph );
            if ( label != null )
                panel.remove( label );
            panel.repaint();
            // loading symbol
            graph = new StockGraph( textField.getText().toString().toLowerCase(), false );
            graph.setBounds( 330, 20, width - 365, height - 200 );
            panel.add( graph );
            label = new JLabel(
                stockTool.translateToName( graph.title ).toUpperCase() + " - 4 Days" );
            label.setBounds( 330 + ( width - 365 ) / 2, 40 + ( height - 200 ), 200, 20 );
            panel.add( label );
            progress.setVisible( false );
            panel.repaint();
        }
        else if ( stockTool
            .translateToSymbol( textField.getText().toString().toLowerCase() ) != null )
        {
            progress.setVisible( true );
            panel.remove( graph );
            if ( label != null )
                panel.remove( label );
            panel.repaint();
            graph = new StockGraph(
                stockTool.translateToSymbol( textField.getText().toString().toLowerCase() ),
                false );
            graph.setBounds( 330, 20, width - 365, height - 200 );
            panel.add( graph );
            label = new JLabel(
                stockTool.translateToName( graph.title ).toUpperCase() + " - 4 Days" );
            label.setBounds( 330 + ( width - 365 ) / 2, 40 + ( height - 200 ), 200, 20 );
            panel.add( label );
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    progress.setVisible( false );
                }
            } ).start();
            panel.repaint();
        }
        else
        {
            JOptionPane.showMessageDialog( dialog, "Stock not available" );
        }

    }


    /**
     * Converts the stock to all lowercase and look through the stockPairs.txt 
     * file for the stock
     */
    public void processQuote()
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                progress.setVisible( true );
            }
        } ).start();
        if ( stockTool.translateToName( textField.getText().toString().toLowerCase() ) != null ) //means textField contains symbol
        {
            progress.setVisible( true );
            textArea.setText( getQuote( textField.getText().toString().toLowerCase() ) );
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    progress.setVisible( false );
                }
            } ).start();
        }
        else if ( stockTool
            .translateToSymbol( textField.getText().toString().toLowerCase() ) != null )
        {
            textArea.setText( getQuote(
                stockTool.translateToSymbol( textField.getText().toString().toLowerCase() ) ) );
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    progress.setVisible( false );
                }
            } ).start();
        }
        else
        {
            JOptionPane.showMessageDialog( dialog, "Stock not available" );
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    progress.setVisible( false );
                }
            } ).start();
        }
    }
    
    /**
     * Returns 0.0 as the forecast
     * @return 0.0
     */
    public double getForecast()
    {
        return 0.0;
    }


    /**
     * Returns the instance this
     * @return this
     */
    public JFrame getInstance()
    {
        return thisWindow;
    }
}
