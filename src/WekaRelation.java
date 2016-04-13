//--------------------------------------
//	Kyle Russell
//	AUT University
//	AI Assignment one
//--------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class WekaRelation implements Serializable
{
    private String name;
    private Map<String, WekaSet> sets;
    
    public WekaRelation()
    {
        this("");
    }
    
    public WekaRelation(String name)
    {
        sets        =   new HashMap<>();
        this.name   =   name;
    }
    
    public Map<String, WekaSet> getSets()
    {
        return sets;
    }
    
    public void addSet(String setName, WekaSet set)
    {
        sets.put(setName, set);
    }
    
    public WekaSet getSet(String name)
    {
        return sets.get(name);
    }
    
    public void removeSet(String name)
    {
        sets.remove(name);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
    }
    
    public int getSize()
    {
        return sets.size();
    }
    
    public String getSetsOutput()
    {
        String output   =   "@DATA\n";
        for(WekaSet set : sets.values())
            output  +=  set + "\n";
        
        return output;
    }
    
    public String getAttributes()
    {
        String attrs    =   "";
        int size        =   sets.values().iterator().next().getSize();
        
        for(int row = 0; row < size; row++)
            for(int col = 0; col < size; col++)
                attrs +=    "@ATTRIBUTE pixel" + row + col + " {0,1}\n";
        
        String classAttr                    =   "@ATTRIBUTE class {";
        List<WekaSet> setCollection         =   new ArrayList<>(sets.values());
        int setSize                         =   setCollection.size();
        for(int  i = 0; i < setSize; i++)
        {
            WekaSet wekaSet =   setCollection.get(i);
            classAttr      +=   wekaSet.getNameID() + (i < (setSize - 1)? "," : "}");
        }
        
        return attrs + classAttr + "\n";
    }
    
    @Override
    public String toString()
    {
        String output   =   "@RELATION " + name + "\n\n";
        output          +=  getAttributes() + "\n";
        output          +=  getSetsOutput();
        
        return output;
    }
    
    public void exportRelation(File file)
    {
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            String relationOutput   =   toString();
            writer.write(relationOutput);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to export relation");
        }
    }
}
