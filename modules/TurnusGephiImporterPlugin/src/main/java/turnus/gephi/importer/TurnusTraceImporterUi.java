/* 
 * TURNUS - www.turnus.co
 * 
 * Copyright (C) 2010-2016 EPFL SCI STI MM
 *
 * This file is part of TURNUS.
 *
 * TURNUS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TURNUS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TURNUS.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining it
 * with Eclipse (or a modified version of Eclipse or an Eclipse plugin or 
 * an Eclipse library), containing parts covered by the terms of the 
 * Eclipse Public License (EPL), the licensors of this Program grant you 
 * additional permission to convey the resulting work.  Corresponding Source 
 * for a non-source form of such a combination shall include the source code 
 * for the parts of Eclipse libraries used as well as that of the  covered work.
 * 
 */
package turnus.gephi.importer;

import static turnus.gephi.importer.TurnusTraceOptions.*;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gephi.io.importer.spi.Importer;
import org.gephi.io.importer.spi.ImporterUI;
import org.openide.util.lookup.ServiceProvider;

/**
 * 
 * @author Simone Casale Brunet
 *
 */
@ServiceProvider(service = ImporterUI.class)
public class TurnusTraceImporterUi implements ImporterUI {
   
    private TurnusTraceImporter importer;
    private TurnusTraceOptions options;
    private JPanel panel;
    
    
    private JPanel createAdvancedPanel() {
        JPanel advancedPanel = new JPanel();
        advancedPanel.setLayout(new GridLayout(3, 1));
        
        final JPanel limitPanel = new JPanel();
        limitPanel.setLayout(new GridLayout(1, 1));
        Integer value = DEFAULT_MAX_FIRINGS;
        Integer min = 0;
        Integer max = Integer.MAX_VALUE;
        Integer step = 10;
        SpinnerNumberModel limitModel = new SpinnerNumberModel(value, min, max, step);
        final JSpinner limit = new JSpinner(limitModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(limit, "#");
        limit.setEditor(editor);
        limit.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent ce) {
                int value = (Integer) limit.getValue();
                options.setMaxFirings(value);
                System.out.println("MAX Firings: " + value);
            }
        });
        limitPanel.add(limit);
        limitPanel.setVisible(DEFAULT_FIRINGS_LIMIT);
        
        final JCheckBox limitLoad = new JCheckBox("Limit the number of loaded firings");
        limitLoad.setSelected(DEFAULT_FIRINGS_LIMIT);
        limitLoad.setRolloverEnabled(false);
        limitLoad.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("Limit: " + selected);
                options.setFiringsLimit(selected);
                limitPanel.setVisible(selected);
                
                if (selected) {
                    int value = (Integer) limit.getValue();
                    options.setMaxFirings(value);
                    System.out.println("update max firings: " + value);
                } else {
                    options.setMaxFirings(DEFAULT_MAX_FIRINGS);
                    System.out.println("update max firings with default value");
                }
                
            }
        }
        );
        
        advancedPanel.add(limitLoad);
        advancedPanel.add(limitPanel);
        
        return advancedPanel;
    }
    
    private JPanel createDependsPanel() {
        JPanel depsPanel = new JPanel();
        depsPanel.setLayout(new GridLayout(6, 1));
        
        final JCheckBox fsm = new JCheckBox("FSM");
        fsm.setSelected(DEFAULT_FSM);
        fsm.setRolloverEnabled(false);
        fsm.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("FSM: " + selected);
                options.setLoadFsm(selected);
            }
        }
        );
        
        final JCheckBox guard = new JCheckBox("Guard");
        guard.setSelected(DEFAULT_GUARD);
        guard.setRolloverEnabled(false);
        guard.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("GUARD: " + selected);
                options.setLoadGuard(selected);
            }
        }
        );
        
        final JCheckBox port = new JCheckBox("Port");
        port.setSelected(DEFAULT_PORT);
        port.setRolloverEnabled(false);
        port.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("PORT: " + selected);
                options.setLoadPort(selected);
            }
        }
        );
        
        final JCheckBox var = new JCheckBox("Internal variable");
        var.setSelected(DEFAULT_INTERNAL_VARS);
        var.setRolloverEnabled(false);
        var.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("VAR: " + selected);
                options.setLoadInternalVar(selected);
            }
        }
        );
        
        final JCheckBox tokens = new JCheckBox("Tokens");
        tokens.setSelected(DEFAULT_TOKENS);
        tokens.setRolloverEnabled(false);
        tokens.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("TOKENS: " + selected);
                options.setLoadTokens(selected);
            }
        }
        );
        
        final JCheckBox unknown = new JCheckBox("Unknown");
        unknown.setSelected(DEFAULT_UNKNOWN);
        unknown.setRolloverEnabled(false);
        unknown.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel button = abstractButton.getModel();
                boolean selected = button.isSelected();
                System.out.println("UNKNOWN: " + selected);
                options.setLoadUnknown(selected);
            }
        }
        );
        
        depsPanel.add(fsm);
        depsPanel.add(guard);
        depsPanel.add(port);
        depsPanel.add(var);
        depsPanel.add(tokens);
        depsPanel.add(unknown);
        
        return depsPanel;
    }
    
    @Override
    public String getDisplayName() {
        return "TURNUS Execution Trace Graph Importer";
    }
    
    @Override
    public JPanel getPanel() {
        
        options = new TurnusTraceOptions();
        
        panel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel dependsPanel = createDependsPanel();
        tabbedPane.addTab("Dependencies", null, dependsPanel,
                "Define the set of dependencies to be imported");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JPanel advancedPanel = createAdvancedPanel();
        tabbedPane.addTab("Loading options", null, advancedPanel,
                "Define the set of dependencies to be imported");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);
        
        panel.add(tabbedPane);
        
        return panel;
    }
    
   
    
    @Override
    public boolean isUIForImporter(Importer importer) {
        return importer instanceof TurnusTraceImporter;
    }
    
    @Override
    public void unsetup(boolean update) {
        if (update) {
            importer.setOptions(options);
        }
        panel = null;
        importer = null;
        options = null;
    }
    
    /*
     @Override
    public void setup(Importer importer) {
        this.importer = (TurnusTraceImporter) importer;
    }*/

    public void setup(Importer[] imprtrs) {
        for(Importer imp : imprtrs){
            if(imp instanceof TurnusTraceImporter){
                 this.importer = (TurnusTraceImporter) imp;
                 break;
            }
        }
    }
}
