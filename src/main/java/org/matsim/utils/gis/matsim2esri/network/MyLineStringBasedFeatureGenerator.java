package org.matsim.utils.gis.matsim2esri.network;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.io.FFFOsmNetworkReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MyLineStringBasedFeatureGenerator implements FeatureGenerator{

	private SimpleFeatureBuilder builder;
	private final CoordinateReferenceSystem crs;
	private final GeometryFactory geofac;


	public MyLineStringBasedFeatureGenerator(final WidthCalculator widthCalculator, final CoordinateReferenceSystem crs) {
		this.crs = crs;
		this.geofac = new GeometryFactory();
		initFeatureType();
	}


	private void initFeatureType() {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("link");
		typeBuilder.setCRS(this.crs);
		typeBuilder.add("the_geom", LineString.class);
		typeBuilder.add("ID", String.class);
		typeBuilder.add("FromID", String.class);
		typeBuilder.add("ToID", String.class);
		typeBuilder.add("OSM_Id", Long.class);
		typeBuilder.add("Type", String.class);
		typeBuilder.add("Length", Double.class);
		typeBuilder.add("OpenFor", Integer.class);
		typeBuilder.add("OpenBack", Integer.class);
		typeBuilder.add("LanesFor", Integer.class);
		typeBuilder.add("LanesBack", Integer.class);
		typeBuilder.add("MaxSpeed", Double.class);
		typeBuilder.add("AdvisedSpd", Double.class);
		typeBuilder.add("CycTypFor", String.class);
		typeBuilder.add("CycTypBack", String.class);
		typeBuilder.add("CycWidFor", Double.class);
		typeBuilder.add("CycWidBack", Double.class);
		typeBuilder.add("Surface", String.class);
		typeBuilder.add("Lit", String.class);
		typeBuilder.add("Service", String.class);
		typeBuilder.add("Name", String.class);
		this.builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
	}


	@Override
	public SimpleFeature getFeature(final Link link) {
		
		Coordinate[] internalNodes = FFFOsmNetworkReader.nodesMap.get(link.getId());
		LineString ls = this.geofac.createLineString(internalNodes);

		Object [] attribs = new Object[21];
		attribs[0] = ls;
		attribs[1] = link.getId().toString();
		attribs[2] = link.getFromNode().getId().toString();
		attribs[3] = link.getToNode().getId().toString();
		attribs[4] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_OSM_ID);
		attribs[5] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_HIGHWAY);
		attribs[6] = link.getLength();
		attribs[7] = link.getAttributes().getAttribute("OpenFor");
		attribs[8] = link.getAttributes().getAttribute("OpenBack");
		attribs[9] = link.getAttributes().getAttribute("LanesFor");
		attribs[10] = link.getAttributes().getAttribute("LanesBack");
		attribs[11] = link.getAttributes().getAttribute("MaxSpeed");
		attribs[12] = link.getAttributes().getAttribute("AdvisedSpeed");
		attribs[13] = link.getAttributes().getAttribute("BicycleTypeFor");
		attribs[14] = link.getAttributes().getAttribute("BicycleTypeBack");
		attribs[15] = link.getAttributes().getAttribute("BicycleWidthFor");
		attribs[16] = link.getAttributes().getAttribute("BicycleWidthBack");
		attribs[17] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_SURFACE);
		attribs[18] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_LIT);
		attribs[19] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_SERVICE);
		attribs[20] = link.getAttributes().getAttribute(FFFOsmNetworkReader.TAG_NAME);
		
		try {
			return this.builder.buildFeature(null, attribs);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

	}

}
