
import com.mysql.cj.jdbc.MysqlDataSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class Enterprise_3 extends JFrame // this entire class is a frame
{
   // query
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;
   
   JScrollPane list_p;
   JTable resultTable;
   
   //connection
   private Connection connection;
   private Statement statement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   
   // keep track of database connection status
   private boolean connectedToDatabase = false;
   
   MysqlDataSource dataSource = new MysqlDataSource();
   
   public Enterprise_3() throws SQLException, ClassNotFoundException // constructor
   {  
        
      
      super( "SQL Client" );
      
      
       
      // create ResultSetTableModel and display database table
     
         
//          ################################
//              connect to database
//          ##################################
//          
    try{ 
             
         tableModel = new ResultSetTableModel();
         
         
         //###############################################
         // GUI 
         //###############################################
         
         
//          ############
//           GUI   top
//          ############
//       
         /////////////////////// SQL BOX
         
         queryArea = new JTextArea( 3, 100);
         queryArea = new JTextArea( DEFAULT_QUERY, 3, 100 );
         queryArea.setWrapStyleWord( true );
         queryArea.setLineWrap( true );
         
         JScrollPane scrollPane = new JScrollPane( queryArea,
         ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        
         
         
        ////////////////////////////LEFT TOP Boxes       
         // combo box
        String[] driver_options = {"com.mysql.cj.jdbc.Driver", 
            "oracle.jdbc.driver.OracleDriver",
            "com.ibm.db2.jdbc.netDB2Driver",
            "com.jdbc.odbc.jdbcOdbcDriver"
        };
        String[] URL_options = {"jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC"
                , "jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC"
                , "jdbc:mysql://localhost:3306/test?useTimezone=true&serverTimezone=UTC",
        };    
                         
        JComboBox driver_list = new JComboBox(driver_options);
        JComboBox URL_list = new JComboBox(URL_options); 
        URL_list.setPrototypeDisplayValue("ddddddddddddddd");
        driver_list.setPrototypeDisplayValue("ddddddddddddddd");
        JLabel drive_label = new JLabel("JDBC Driver:      ");
        JLabel URL_label = new JLabel("Database URL:  ");
        JLabel user_label= new JLabel("username:   ");
        JTextField user_text = new JTextField(30);
        JLabel password_label= new JLabel("password:   ");
        JPasswordField password_text = new JPasswordField(50);
        JLabel intro= new JLabel("Enter database Information");
        
        // organize gui top        
        Box box_top_left = Box.createVerticalBox();
        
        
        //driver box
        Box box_drive = Box.createHorizontalBox();
        box_drive.add(drive_label);
        box_drive.add(driver_list);
        
        // URl box
        Box box_URL = Box.createHorizontalBox();
        box_URL.add(URL_label);
        box_URL.add(URL_list);
                
        //User box
        Box box_user = Box.createHorizontalBox();
        box_user.add(user_label);
        box_user.add(user_text);
        
        //password box
        Box box_pass = Box.createHorizontalBox();
        box_pass.add(password_label);
        box_pass.add(password_text);

        // add everything
        box_top_left.add(intro);
        box_top_left.add(Box.createRigidArea(new Dimension(10, 0)));
        box_top_left.add(box_drive);
        box_top_left.add(box_URL);
        box_top_left.add(box_user);
        box_top_left.add(box_pass);
        ////////////////////////////////////////////////////////////////////////
        
        /////////////////////////////box Top right
        JLabel intro1= new JLabel("Enter an SQL Command");
        
        Box box_top_right = Box.createVerticalBox();
        box_top_right.add(intro1);
        box_top_right.add(Box.createRigidArea(new Dimension(5, 0)));
        box_top_right.add(scrollPane);
        
        
        ///box top (right and left)       
        Box box_top = Box.createHorizontalBox();               
        box_top.add(Box.createRigidArea(new Dimension(10, 0)));
        box_top.add( box_top_left ); 
        box_top.add(Box.createRigidArea(new Dimension(16, 0)));
        box_top.add( box_top_right );
        //////////////////////////////////////////////////////
         
        
        
        
//          ###############
//           GUI   middle
//          ###############
        
         
         // button 1  not really button
         JTextField data_pane = new JTextField();        
         data_pane.setBackground(Color.BLACK);
         data_pane.setForeground(Color.GREEN);
        

         //button  forget       
         JButton submitButton = new JButton( "Submit Query" );
         submitButton.setBackground(Color.BLUE);
         submitButton.setForeground(Color.YELLOW);
         submitButton.setBorderPainted(false);
         submitButton.setOpaque(true);
 
        // button 2
         JButton connect_to_data = new JButton( "connect to database:" );
         connect_to_data.setBackground(Color.BLACK);
         connect_to_data.setForeground(Color.RED);
         
         //button 3
         JButton execute_SQL = new JButton( "Execute SQL Command:" );
         execute_SQL.setBackground(Color.GREEN);
         
         //button 4
         JButton clear_D = new JButton( "Clear SQL Command:" );
         clear_D.setBackground(Color.WHITE);
         
          
    
        // Organize middle buttons  
         Box box_middle= Box.createHorizontalBox();
         box_middle.add(Box.createRigidArea(new Dimension(10, 0)));
         box_middle.add( data_pane );
         box_middle.add(Box.createRigidArea(new Dimension(6,0)));
         box_middle.add(connect_to_data);
         box_middle.add(Box.createRigidArea(new Dimension(6, 0)));
         box_middle.add(execute_SQL);
         box_middle.add(Box.createRigidArea(new Dimension(6, 0)));
         box_middle.add(clear_D);
         
                  
                
//          ###############
//           GUI   bottom
//          ###############


////////////////////////////////////////////////////////////////////////////////////////// IMPORTANT
        resultTable = new JTable();
        //resultTable = new JTable(tableModel);
        resultTable.setGridColor(Color.BLACK);
         
        list_p=new JScrollPane( resultTable );         
        list_p.setPreferredSize(new Dimension(220, 280));
       
        
        
        
       
          //button bottom
         JButton clear_win = new JButton( "Clear Result window:" );
         clear_win.setBackground(Color.YELLOW);
         connect_to_data.setForeground(Color.cyan);
         
         /// organize elements in the bottom
         Box box_bottom = Box.createVerticalBox();
         box_bottom.add(list_p);
         box_bottom.add(clear_win);




///////////////////////////////////////////////???????????????????????????????///////////// IMPORTANT


         
         
         //////////////////////
         // GUI final touches 
         /////////////////////
         Box box = Box.createVerticalBox();         
         box.add(Box.createRigidArea(new Dimension(0,10)));
         box.add(box_top);
         box.add(Box.createRigidArea(new Dimension(0,10)));
         box.add(box_middle);
         box.add(Box.createRigidArea(new Dimension(0,10)));
         box.add(box_bottom);
         //add(box);
         
    
         list_p.setVisible(true);
         
      
         
         add( box_top, BorderLayout.NORTH);
         add(box_middle,BorderLayout.CENTER);
         add(box_bottom,BorderLayout.SOUTH);
         
         

         //######################
         //#  Action listeners  #
         //######################
         
         
         
         ///////// connect to database button
          connect_to_data.addActionListener(
         
                 new ActionListener()
                 {
                     public void actionPerformed( ActionEvent event )
                     {
                           
                       
                             String username = user_text.getText();
                             String password = password_text.getText();
                             String driver =  driver_list.getSelectedItem().toString();
                             String URL =  URL_list.getSelectedItem().toString();
                             
                         try {
                             
                             connect(URL,username,password);
                             data_pane.setText("Connected to: "+URL);     
                             
                             connectedToDatabase=true;
                             tableModel.setStatement(statement,DEFAULT_QUERY);
                             resultTable.setModel(tableModel);
                             tableModel.fireTableStructureChanged();
                                                          
                             
                         } catch (SQLException ex) {
                              JOptionPane.showMessageDialog( null, ex.getMessage(), 
                                "Database error", JOptionPane.ERROR_MESSAGE );
               
                            // ensure database connection is closed
                            tableModel.disconnectFromDatabase();
                            
                            System.exit( 1 );   // terminate application
                             
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         } catch (ClassNotFoundException ex) {
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         }

                     }
                
                 }
         );
          
          // execute SQL
         execute_SQL.addActionListener(
         
                 new ActionListener()
                 {
                     public void actionPerformed( ActionEvent event )
                     {
                         try {
                             System.out.println("foo");
                             
                            String q=queryArea.getText();
                            statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
                            tableModel.setStatement(statement,q);
                            
                            
                            list_p.setVisible(true);
                            
                         }  catch (IllegalStateException ex) {
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         } catch (SQLException ex) {
                             JOptionPane.showMessageDialog( null, 
                             
                            JOptionPane.ERROR_MESSAGE );
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                
                 }
         ); 
         
        //////////////clear SQL 
        clear_D.addActionListener(
         
                 new ActionListener()
                 {
                     public void actionPerformed( ActionEvent event )
                     {
                         try {
                             queryArea.setText("");
                             
                         }  catch (IllegalStateException ex) {
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                
                 }
         );

        // clear window
           clear_win.addActionListener(
         
                 new ActionListener()
                 {
                     public void actionPerformed( ActionEvent event )
                     {
                         try {
                             System.out.println("foo");
                             
                             list_p.setVisible(false);
                             
                             repaint();
                             
                         }  catch (IllegalStateException ex) {
                             Logger.getLogger(Enterprise_3.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                
                 }
         ); 
         // create event listener for submitButton
         
        

         setSize( 700, 500 ); // set window size
         setVisible( true ); // display window  
         }// end try
    catch ( ClassNotFoundException classNotFound ) 
      {
         JOptionPane.showMessageDialog( null, 
            "MySQL driver not found", "Driver not found",
            JOptionPane.ERROR_MESSAGE );
         
         System.exit( 1 ); // terminate application
      } // end catch
      catch ( SQLException sqlException ) 
      {
         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
            "Database error", JOptionPane.ERROR_MESSAGE );
               
         // ensure database connection is closed
         tableModel.disconnectFromDatabase();
         
         System.exit( 1 );   // terminate application
      } // end catch
         
         
         
         
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor
   
   //helper Functions
   public void connect (String a,String b,String c)
        throws SQLException, ClassNotFoundException
   {         
	   
       //read properties file
	   try {
               //this is just to get the parent directory
               String URL=a;
               String username=b;
               String password=c;
                
	    	
	    	dataSource.setURL(URL);
	    	dataSource.setUser(username);
	    	dataSource.setPassword(password); 		    
            // connect to database bikes and query database
  	        // establish connection to database
   	         connection = dataSource.getConnection();
                
                
	
            // create Statement to query database
            statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

            // update database connection status
            connectedToDatabase = true;

            // set query and execute it
            
		    //set update and execute it
		    //setUpdate (query);
	  } //end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      } // end catch
  
   } 
    
   // execute application
   public static void main( String args[] ) throws SQLException, ClassNotFoundException 
   {
       new Enterprise_3();       
   } // end main
} // end class DisplayQueryResults
    
 
