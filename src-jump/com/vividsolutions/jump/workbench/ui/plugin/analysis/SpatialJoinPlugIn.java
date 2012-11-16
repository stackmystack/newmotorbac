
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui.plugin.analysis;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;

import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.task.*;
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.plugin.*;
import com.vividsolutions.jump.workbench.plugin.util.*;
import com.vividsolutions.jump.workbench.ui.*;

/**
* Queries a layer by a spatial predicate.
*/
public class SpatialJoinPlugIn
    extends AbstractPlugIn
    implements ThreadedPlugIn
{
	  private Layer srcLayerA;
	  private Layer srcLayerB;
	  private JTextField paramField;
  private Collection functionNames;
  private MultiInputDialog dialog;
  private String funcNameToRun;
  private GeometryPredicate functionToRun = null;
  private boolean exceptionThrown = false;

  private double[] params = new double[2];

  public SpatialJoinPlugIn() {
    functionNames = GeometryPredicate.getNames();
  }

  private String categoryName = StandardCategoryNames.RESULT;

  public void setCategoryName(String value) {
    categoryName = value;
  }

  public boolean execute(PlugInContext context) throws Exception {
    dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
    setDialogValues(dialog, context);
    GUIUtil.centreOnWindow(dialog);
    dialog.setVisible(true);
    if (! dialog.wasOKPressed()) { return false; }
    getDialogValues(dialog);
    return true;
  }

  public void run(TaskMonitor monitor, PlugInContext context)
      throws Exception {
    monitor.allowCancellationRequests();

    // input-proofing
    if (functionToRun == null) return;
    if (srcLayerA == null) return;
    if (srcLayerB == null) return;

    monitor.report("Executing join " + functionToRun.getName() + "...");

    FeatureCollection srcAFC = srcLayerA.getFeatureCollectionWrapper();
    FeatureCollection srcBFC = srcLayerB.getFeatureCollectionWrapper();

    SpatialJoinExecuter executer = new SpatialJoinExecuter(srcAFC, srcBFC);
    FeatureCollection resultFC = executer.getResultFC();
    executer.execute(monitor, functionToRun, params, resultFC);

    if (monitor.isCancelRequested()) return;

      String outputLayerName = "Join-" + funcNameToRun;
      context.getLayerManager().addCategory(categoryName);
      context.addLayer(categoryName, outputLayerName, resultFC);
    
    if (exceptionThrown) {
      context.getWorkbenchFrame().warnUser("Errors found while executing query");
    }
  }

	private final static String LAYER_A = "Layer A";
	private final static String LAYER_B = "Layer B";
  private final static String PREDICATE = "Relation";
  private final static String PARAM = "Parameter";

  private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
    //dialog.setSideBarImage(new ImageIcon(getClass().getResource("DiffSegments.png")));
    dialog.setSideBarDescription(
        "Joins two layers on a given spatial relationship"
        + " (i.e. SELECT A.*, B.* FROM A JOIN B ON A.Geometry <Relation> B.Geometry)");

    //Set initial layer values to the first and second layers in the layer list.
    //In #initialize we've already checked that the number of layers >= 1. [Jon Aquino]
    Layer initLayer1 = (srcLayerA == null)? context.getCandidateLayer(0) : srcLayerA;
    Layer initLayer2 = (srcLayerB == null)? context.getCandidateLayer(1) : srcLayerB;

    dialog.addLayerComboBox(LAYER_A, initLayer1, context.getLayerManager());
    
    JComboBox functionComboBox = dialog.addComboBox(PREDICATE, funcNameToRun, functionNames, null);
    functionComboBox.addItemListener(new MethodItemListener());
    paramField = dialog.addDoubleField(PARAM, params[0], 10);
    
    dialog.addLayerComboBox(LAYER_B, initLayer2, context.getLayerManager());

    updateUIForFunction(funcNameToRun);
  }

  private void getDialogValues(MultiInputDialog dialog) {
	    srcLayerA = dialog.getLayer(LAYER_A);
    srcLayerB = dialog.getLayer(LAYER_B);
    funcNameToRun = dialog.getText(PREDICATE);
    functionToRun = GeometryPredicate.getPredicate(funcNameToRun);
    params[0] = dialog.getDouble(PARAM);
  }

  private void updateUIForFunction(String funcName) {
    boolean paramUsed = false;
    GeometryPredicate func = GeometryPredicate.getPredicate(funcName);
    if (func != null) {
      paramUsed = func.getParameterCount() > 0;
    }
    paramField.setEnabled(paramUsed);
    // this has the effect of making the background gray (disabled)
    paramField.setOpaque(paramUsed);
  }

  private class MethodItemListener
      implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      updateUIForFunction((String) e.getItem());
    }
  }

}



