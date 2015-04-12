package cs213.photoAlbum.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

public class GridBagUtility {
    
    private GridBagConstraints lastC = null;
    private GridBagConstraints middleC = null;
    private GridBagConstraints labelC = null;

    public GridBagUtility() {
        
        lastC = new GridBagConstraints();
        lastC.fill = GridBagConstraints.HORIZONTAL;
        lastC.anchor = GridBagConstraints.NORTHWEST;
        lastC.weightx = 1.0;
        lastC.gridwidth = GridBagConstraints.REMAINDER;
        lastC.insets = new Insets(1, 1, 1, 1);
        middleC = (GridBagConstraints) lastC.clone();
        middleC.gridwidth = GridBagConstraints.RELATIVE;

        labelC = (GridBagConstraints) lastC.clone();

        labelC.weightx = 0.0;
        labelC.gridwidth = 1;
    }

   
    public void addFinalFieldInGridRow(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, lastC);
        parent.add(c);
    }
    
   
    public void addLabel(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, labelC);
        parent.add(c);
    }

   
    public JLabel addLabel(String s, Container parent) {
        JLabel c = new JLabel(s);
        addLabel(c, parent);
        return c;
    }

  
    public void appendComponentInGridRow(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, middleC);
        parent.add(c);
    }
    
}
