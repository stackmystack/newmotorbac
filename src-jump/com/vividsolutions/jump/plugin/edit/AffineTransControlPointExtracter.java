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
package com.vividsolutions.jump.plugin.edit;

import java.util.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.geom.*;

/**
 * Uses geometry data in two {@link FeatureCollection}s to
 * extract parameters for an affine transformation.
 * The contents of the layers determines how the
 * parameters are extracted.
 * The layers can contain:
 * <ul>
 * <li>A 2-point vector - parameters will have isometric scaling and no shear
 * <li>A 3-point LineString - a general affine transformation will be computed
 * <li>(FUTURE) 1, 2 or 3 points, each with a tag value - the affine transformation will be
 * computed from the mappings between the points.
 * </ul>
 *
 * @author Martin Davis
 * @version 1.0
 */
public class AffineTransControlPointExtracter
{
  public static final int TYPE_UNKNOWN = 0;
  public static final int TYPE_VECTOR = 1;
  public static final int TYPE_LINE_3 = 2;

  private FeatureCollection fcSrc;
  private FeatureCollection fcDest;

  private int inputType = TYPE_UNKNOWN;
  private String parseErrMsg = "Unrecognized control point geometry";

  private int numGeoms;
  private Geometry[] geomSrc = new Geometry[3];
  private Geometry[] geomDest = new Geometry[3];

  private int numControlPts;
  private Coordinate[] controlPtSrc;
  private Coordinate[] controlPtDest;

  public AffineTransControlPointExtracter(FeatureCollection fcSrc, FeatureCollection fcDest) {
    this.fcSrc = fcSrc;
    this.fcDest = fcDest;
    init();
  }

  public int getInputType() { return inputType; }
  public String getParseErrorMessage() { return parseErrMsg; }

  public Coordinate[] getSrcControlPoints() { return controlPtSrc; }
  public Coordinate[] getDestControlPoints() { return controlPtDest; }

  private void init()
  {
    parseInput();
  }

  private void parseInput()
  {
    inputType = TYPE_UNKNOWN;
    int fcSrcSize = fcSrc.size();
    int fcDestSize = fcDest.size();

    // error - # geoms must match
    if (fcSrcSize != fcDestSize) {
      parseErrMsg = "Control point collections must be same size";
      return;
    }
    // for now only handling pair of geoms to define control points
    if (fcSrcSize != 1) {
      parseErrMsg = "Control points must be a single geometry";
      return;
    }

    geomSrc[0] = ((Feature) fcSrc.iterator().next()).getGeometry();
    geomDest[0] = ((Feature) fcDest.iterator().next()).getGeometry();

    if (geomSrc[0].getClass() != geomDest[0].getClass()) {
      parseErrMsg = "Control points must be LineStrings";
      return;
    }

    // for now only handling LineStrings
    if (! (geomSrc[0] instanceof LineString)) {
      parseErrMsg = "Control points must be LineStrings";
      return;
    }

    parseLines();
    return;
  }

  private void parseLines()
  {
    controlPtSrc = geomSrc[0].getCoordinates();
    controlPtDest = geomDest[0].getCoordinates();

    if (controlPtSrc.length != controlPtDest.length) {
      parseErrMsg = "Control Point LineStrings are different lengths";
      return;
    }
    if (controlPtSrc.length < 2) {
      parseErrMsg = "Single control points are not supported";
    }
    if (controlPtSrc.length > 3) {
      parseErrMsg = "Too many points in LineString";
    }
    inputType = TYPE_VECTOR;
  }


}