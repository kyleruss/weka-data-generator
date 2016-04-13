//--------------------------------------
//	Kyle Russell
//	AUT University
//	AI Assignment one
//--------------------------------------

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Window 
{
    private static Window instance;
    private JFrame frame;
    private MainPanel panel;
    
    private Window()
    {
        initFrame();
    }
    
    private void initFrame()
    {
        frame   =   new JFrame();
        panel   =   MainPanel.getInstance();
        
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    private void initLookAndFeel()
    {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    private void display()
    {
        frame.setVisible(true);
    }
    
    public static Window getInstance()
    {
        if(instance == null) instance = new Window();
        return instance;
    }
    
    public static void main(String[] args)
    {
        Window window   =   getInstance();
        window.display();
    }
}
