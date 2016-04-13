//--------------------------------------
//	Kyle Russell
//	AUT University
//	AI Assignment one
//--------------------------------------

import java.io.Serializable;

public class WekaSet implements Serializable
{
    private GridButton[] data;
    private String nameID;
    private int size;
    
    public WekaSet(String nameID, int size)
    {
        this.nameID =   nameID;
        this.data   =   new GridButton[size * size];
        this.size   =   size;
    }
    
    public GridButton[] getData()
    {
        return data;
    }
    
    public String getNameID()
    {
        return nameID;
    }
    
    public void setData(GridButton[] data)
    {
        this.data   =   data;
    }
    
    public void setSize(int size)
    {
        this.size   =   size;
    }
    
    public int getSize()
    {
        return size;
    }
    
    @Override
    public String toString()
    {
        String output   =   "% Input " + nameID + "\n";
        for(int i = 0; i < data.length; i++)
            output += (data[i].isSet() ? "1" : "0") + ",";
        
        output  +=  nameID;
        return output;
    }
}
