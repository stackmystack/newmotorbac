
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
import com.vividsolutions.jts.algorithm.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.simplify.*;
import com.vividsolutions.jts.operation.linemerge.*;
import com.vividsolutions.jts.operation.polygonize.*;

/**
 * A function object for {@link Geometry} functions (which return a Geometry).
 * Provides metadata about the function.
 *
 * @author Martin Davis
 * @version 1.0
 */
public abstract class GeometryFunction
{
  static final String METHOD_INTERSECTION = "Intersection";
  static final String METHOD_UNION = "Union";
  static final String METHOD_DIFFERENCE_AB = "Difference (Source-Mask)";
  static final String METHOD_DIFFERENCE_BA = "Difference (Mask-Source)";
  static final String METHOD_SYMDIFF = "Symmetric Difference";
  static final String METHOD_CENTROID_A = "Centroid";
  static final String METHOD_BUFFER = "Buffer";

  private static GeometryFunction[] method = {
    new IntersectionFunction(),
    new UnionFunction(),
    new DifferenceABFunction(),
    new DifferenceBAFunction(),
    new SymDifferenceFunction(),
    new CentroidFunction(),
    new InteriorPointFunction(),
    new BufferFunction(),
    new SimplifyFunction(),
    new SimplifyTopologyFunction(),
    new ConvexHullFunction(),
    new BoundaryFunction(),
    new EnvelopeFunction(),
    new LineMergeFunction(),
    new LineSequenceFunction(),
    new PolygonizeFunction()
  };

  public static List getNames()
  {
    List methodNames = new ArrayList();
    for (int i = 0; i < method.length; i++) {
      methodNames.add(method[i].name);
    }
    return methodNames;
  }

  public static List getNames(Collection functions)
  {
    List names = new ArrayList();
    for (Iterator i = functions.iterator(); i.hasNext(); ) {
    	GeometryFunction func = (GeometryFunction) i.next();
    	names.add(func.name);
    }
    return names;
  }

  public static GeometryFunction getFunction(String name)
  {
    for (int i = 0; i < method.length; i++) {
      if (method[i].name.equals(name))
        return method[i];
    }
    return null;
  }

  public static GeometryFunction getFunction(Collection functions, String name)
  {
	  for (Iterator i = functions.iterator(); i.hasNext(); ) {
		  GeometryFunction func = (GeometryFunction) i.next();
		  if (func.name.equals(name))
			  return func;
	  }
	  return null;
  }

  public static GeometryFunction[] getFunctions()
  {
	  return method;
  }

  private String name;
  private int nArguments;
  private int nParams;
  private boolean isAggregate = false;   // not yet used
  private String description;

  public String getName() { return name; }
  public int getGeometryArgumentCount() { return nArguments; }
  public int getParameterCount() { return nParams; }

  public GeometryFunction(String name, int nArgs, int nParams)
  {
    this(name, nArgs, nParams, name + " function");
  }

  public GeometryFunction(String name, int nArgs, int nParams,
                          String description)
  {
    this.name = name;
    this.nArguments = nArgs;
    this.nParams = nParams;
    this.description = description;
  }

  public String getDescription() { return description; }

  /**
   * Exectute the function on the geometry(s) in the geom array.
   * The function can expect that the correct number of geometry arguments
   * is present in the array.
   * Integer parameters must be passed as doubles.
   * If no result can be computed for some reason, null should be returned
   * to indicate this to the caller.
   * Exceptions may be thrown and must be handled by the caller.
   *
   * @param geom the geometry arguments
   * @param param any non-geometric arguments.
   * @return the geometry result, or null if no result could be computed.
   */
  public abstract Geometry execute(Geometry[] geom, double[] param);

  public String toString() { return name; }

  //====================================================

  private static class IntersectionFunction extends GeometryFunction {
    public IntersectionFunction() {
      super(METHOD_INTERSECTION, 2, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].intersection(geom[1]);
    }
  }

  private static class UnionFunction extends GeometryFunction {
    public UnionFunction() {
      super(METHOD_UNION, 2, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].union(geom[1]);
    }
  }
  private static class DifferenceABFunction extends GeometryFunction {
    public DifferenceABFunction() {
      super(METHOD_DIFFERENCE_AB, 2, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].difference(geom[1]);
    }
  }
  private static class DifferenceBAFunction extends GeometryFunction {
    public DifferenceBAFunction() {
      super(METHOD_DIFFERENCE_BA, 2, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[1].difference(geom[0]);
    }
  }
  private static class SymDifferenceFunction extends GeometryFunction {
    public SymDifferenceFunction() {
      super(METHOD_SYMDIFF, 2, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].symDifference(geom[1]);
    }
  }
  private static class CentroidFunction extends GeometryFunction {
    public CentroidFunction() {
      super(METHOD_CENTROID_A, 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].getCentroid();
    }
  }
  private static class InteriorPointFunction extends GeometryFunction {
    public InteriorPointFunction() {
      super("Interior Point", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].getInteriorPoint();
    }
  }
  private static class BufferFunction extends GeometryFunction {
    public BufferFunction() {
      super(METHOD_BUFFER, 1, 1);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].buffer(param[0]);
    }
  }
  private static class SimplifyFunction extends GeometryFunction {
    public SimplifyFunction() {
      super("Simplify (D-P)", 1, 1,
    		  "Simplifies a geometry using the Douglas-Peucker algorithm");
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return DouglasPeuckerSimplifier.simplify(geom[0], param[0]);
    }
  }
  private static class SimplifyTopologyFunction extends GeometryFunction {
    public SimplifyTopologyFunction() {
      super("Simplify (preserve topology)", 1, 1);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return TopologyPreservingSimplifier.simplify(geom[0], param[0]);
    }
  }
  private static class ConvexHullFunction extends GeometryFunction {
    public ConvexHullFunction() {
      super("Convex Hull", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      ConvexHull hull = new ConvexHull(geom[0]);
      return hull.getConvexHull();
    }
  }
  private static class BoundaryFunction extends GeometryFunction {
    public BoundaryFunction() {
      super("Boundary", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].getBoundary();
    }
  }
  private static class EnvelopeFunction extends GeometryFunction {
    public EnvelopeFunction() {
      super("Envelope", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      return geom[0].getEnvelope();
    }
  }
  private static class LineMergeFunction extends GeometryFunction {
    public LineMergeFunction() {
      super("Line Merge", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      LineMerger merger = new LineMerger();
      merger.add(geom[0]);
      Collection lines = merger.getMergedLineStrings();
      return geom[0].getFactory().buildGeometry(lines);
    }
  }
  private static class LineSequenceFunction extends GeometryFunction {
    public LineSequenceFunction() {
      super("Line Sequence", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      LineSequencer sequencer = new LineSequencer();
      sequencer.add(geom[0]);
      return sequencer.getSequencedLineStrings();
    }
  }
  private static class PolygonizeFunction extends GeometryFunction {
    public PolygonizeFunction() {
      super("Polygonize", 1, 0);
    }

    public Geometry execute(Geometry[] geom, double[] param)
    {
      Polygonizer polygonizer = new Polygonizer();
      polygonizer.add(geom[0]);
      Collection polyColl = polygonizer.getPolygons();
      Geometry[] polys = GeometryFactory.toGeometryArray(polyColl);
      return geom[0].getFactory().createGeometryCollection(polys);
    }
  }
}
