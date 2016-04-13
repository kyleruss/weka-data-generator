//--------------------------------------
//	Kyle Russell
//	AUT University
//	AI Assignment one
//--------------------------------------

import java.awt.Color;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JButton;


public class GridButton extends JButton implements Serializable
{
    private boolean isSet;
    
    public GridButton()
    {
        isSet   =   false;
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        setBackground(Color.WHITE);
    }
    
    public boolean isSet()
    {
        return isSet;
    }
    
    public void set(boolean set)
    {
        isSet   =   set;
    }
    
    public void toggle()
    {
        isSet   =   !isSet;
        setBackground(isSet? Color.BLACK : Color.WHITE);
    }
}
