package com.vividsolutions.jump.workbench.ui.style;

import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.*;

import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
/**
 * Copies the styles for a layer to a paste buffer
 * @author Martin Davis
 * @version 1.0
 */

public class CopyStylesPlugIn extends AbstractPlugIn {

  static Collection stylesBuffer = null;

  public CopyStylesPlugIn() {
  }

  public String getName() {
    return "Copy Styles";
  }

  public static MultiEnableCheck createEnableCheck(
      final WorkbenchContext workbenchContext) {
    EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
    return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
        .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
  }

  public boolean execute(PlugInContext context) throws Exception {
    final Layer layer = context.getSelectedLayer(0);
    stylesBuffer = layer.cloneStyles();
    return true;
  }
}