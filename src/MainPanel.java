//--------------------------------------
//	Kyle Russell
//	AUT University
//	AI Assignment one
//--------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JPanel
{
    private static MainPanel instance;
    private ControlPanel controlPanel;
    private DrawingPanel drawingPanel;
    private RelationControlPanel relationPanel;
    private WekaRelation currentRelation;
    private String currentSetName;
    
    private MainPanel()
    {
        setPreferredSize(new Dimension(450, 300));
        setLayout(new BorderLayout());
        
        controlPanel    =   new ControlPanel();
        drawingPanel    =   new DrawingPanel(8);
        relationPanel   =   new RelationControlPanel();
        currentRelation =   new WekaRelation();
        
        add(controlPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(relationPanel, BorderLayout.SOUTH);
    }
    
    private void initGrid()
    {
        int size            =   Integer.parseInt(controlPanel.gridSize.getText());
        DrawingPanel panel  =   new DrawingPanel(size);
        changeDrawingPanel(panel);
    }
    
    private void changeDrawingPanel(DrawingPanel panel)
    {
        remove(drawingPanel);
        drawingPanel    =   panel;
        add(drawingPanel, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    
    private void initRelation(WekaRelation relation)
    {
        if(relation == null) return;
         
        currentRelation =   relation;
        relationPanel.relationSetField.removeAllItems();
        relationPanel.relationField.setText(relation.getName());
        
        for(String setName : currentRelation.getSets().keySet())
            relationPanel.relationSetField.addItem(setName);
    }
    
    private void initSet(String name)
    {
        if(currentRelation != null && currentRelation.getSets().containsKey(name))
        {
            WekaSet wekaSet =   currentRelation.getSets().get(name);
            changeDrawingPanel(new DrawingPanel(wekaSet));
            currentSetName  =   name;
        }
    }
    
    private void addSet()
    {
        String name             =   JOptionPane.showInputDialog(null, "Enter the set name");
        if(name != null)
        {
            int size            =   Integer.parseInt(controlPanel.gridSize.getText());
            relationPanel.relationSetField.addItem(name);
            currentSetName      =   name;
            
            WekaSet wekaSet     =   new WekaSet(name, size);
            currentRelation.addSet(name, wekaSet);
            changeDrawingPanel(new DrawingPanel(size));
        }
    }
    
    private void exportRelation()
    {
        JFileChooser fileChooser    =   new JFileChooser();
        int option                  =   fileChooser.showSaveDialog(null);
        currentRelation.setName(relationPanel.relationField.getText());

        if(option == JFileChooser.APPROVE_OPTION)
        {
            File exportFile =   fileChooser.getSelectedFile();
            try(ObjectOutputStream oos  =   new ObjectOutputStream(new FileOutputStream(exportFile)))
            {
                oos.writeObject(currentRelation);
            }

            catch(IOException e)
            {
                JOptionPane.showMessageDialog(null, "Failed to save relation");
            }
        }
        
        int exportWeka  =   JOptionPane.showConfirmDialog(null, "Export Weka output?");
        if(exportWeka == JOptionPane.OK_OPTION)
        {
            fileChooser =   new JFileChooser();
            option      =   fileChooser.showSaveDialog(null);
            if(option == JFileChooser.APPROVE_OPTION)
                currentRelation.exportRelation(fileChooser.getSelectedFile());
        }
    }

    private void importRelation()
    {
        JFileChooser fileChooser    =   new JFileChooser();
        int option                  =   fileChooser.showOpenDialog(null);

        if(option == JFileChooser.APPROVE_OPTION)
        {
            File importFile             =   fileChooser.getSelectedFile();
            try(ObjectInputStream ois   =   new ObjectInputStream(new FileInputStream(importFile)))
            {
                initRelation((WekaRelation) ois.readObject());
            }

            catch(IOException | ClassNotFoundException e)
            {
                JOptionPane.showMessageDialog(null, "Failed to save relation");
            }
        }
    }
    
    private void removeSet()
    {
        String name             =   JOptionPane.showInputDialog(null, "Enter the set name");
        if(name != null)
        {
            currentRelation.removeSet(name);
            relationPanel.relationSetField.removeItem(name);
        }
    }
    
    public static MainPanel getInstance()
    {
        if(instance == null) instance = new MainPanel();
        return instance;
    }
    
    private class DrawingPanel extends JPanel implements ActionListener
    {
        private GridButton[][] btnGrid;
        
        public DrawingPanel(int gridSize)
        {
            btnGrid         =   new GridButton[gridSize][gridSize];
            setLayout(new GridLayout(gridSize, gridSize));
            createGrid();
        }
        
        public DrawingPanel(WekaSet set)
        {
            importGrid(set);
        }
        
        private void importGrid(WekaSet set)
        {
            if(set == null) 
                return;
            
            int size    =   set.getSize();
            btnGrid     =   new GridButton[size][size];
            setLayout(new GridLayout(size, size));
            
            int i = 0;
            for(int row = 0; row < size; row++)
            {
                for(int col = 0; col < size; col++, i++)
                {
                    btnGrid[row][col]   =   set.getData()[i];
                    add(btnGrid[row][col]);
                }
            }
        }
        
        private void createGrid()
        {
            int i = 0;
            for(int row = 0; row < btnGrid.length; row++)
            {
                for(int col = 0; col < btnGrid.length; col++, i++)
                {
                    GridButton gridBtn              =   new GridButton();
                    gridBtn.addActionListener(this);
                    
                    btnGrid[row][col]     =   gridBtn;
                    
                    if(currentSetName != null && currentRelation.getSet(currentSetName) != null)
                        currentRelation.getSet(currentSetName).getData()[i] = gridBtn;
                    
                    add(gridBtn);
                }
            }
            
        }
        
        private GridButton[] flattenGrid()
        {
            GridButton[] flatGrid   =   new GridButton[btnGrid.length * btnGrid[0].length];
            
            int i = 0;
            for(int row = 0; row < btnGrid.length; row++)
                for(int col = 0; col < btnGrid[0].length; col++, i++)
                    flatGrid[i] =   btnGrid[row][col];
            
            return flatGrid;
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            GridButton btn  =   (GridButton) e.getSource();
            btn.toggle();
        }
    }
    
    private class ControlPanel extends JPanel implements ActionListener
    {
        private JTextField gridSize;
        private JButton createGridButton;
        
        private ControlPanel()
        {
            setPreferredSize(new Dimension(0, 35));
            setBackground(Color.LIGHT_GRAY);
            
            JLabel gridLabel    =   new JLabel("Grid size: ");
            gridSize            =   new JTextField();
            createGridButton    =   new JButton("Create grid");
            
            gridSize.setPreferredSize(new Dimension(50, 25));
            gridSize.setText("8");
            
            add(gridLabel);
            add(gridSize);
            add(createGridButton);
            
            createGridButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            if(src == createGridButton)
                initGrid();
        }
    }
    
    private class RelationControlPanel extends JPanel implements ActionListener
    {
        private JTextField relationField;
        private JComboBox relationSetField;
        private JButton addSetButton, removeSetButton, importButton, exportButton;
        
        public RelationControlPanel()
        {
            setLayout(new GridLayout(2, 1));
            
            relationField       =   new JTextField();
            relationSetField    =   new JComboBox();
            addSetButton        =   new JButton("Add set");
            removeSetButton     =   new JButton("Remove set");
            importButton        =   new JButton("Import");
            exportButton        =   new JButton("Export");
            
            relationSetField.setPreferredSize(new Dimension(105, 20));
            relationField.setPreferredSize(new Dimension(105, 20));
            
            JPanel setWrapper       =   new JPanel();
            JPanel relationWrapper  =   new JPanel();
            
            setWrapper.add(new JLabel("Sets: "));
            setWrapper.add(relationSetField);
            setWrapper.add(addSetButton);
            setWrapper.add(removeSetButton);
            
            relationWrapper.add(new JLabel("Relation: "));
            relationWrapper.add(relationField);
            relationWrapper.add(importButton);
            relationWrapper.add(exportButton);
            
            add(setWrapper);
            add(relationWrapper);
            
            addSetButton.addActionListener(this);
            removeSetButton.addActionListener(this);
            importButton.addActionListener(this);
            exportButton.addActionListener(this);
            relationSetField.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            
            if(src == addSetButton)
                addSet();
            
            else if(src == removeSetButton)
                removeSet();
            
            else if(src == importButton)
                importRelation();
            
            else if(src == exportButton)
                exportRelation();
            
            else if(src == relationSetField)
                initSet(relationSetField.getSelectedItem().toString());
        }
    }
}
