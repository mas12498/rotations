package tspi.model;

import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Vector3;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected final Location wgs84; // geodetic position: lat, lon angles, h meters ellipsoid coordinates
	protected final Vector3 efg; // geocentric position vector: EFG
	protected final Aperture aer; //Aperture position: az, el angle r meters 
	//axial operators...
	protected final Operator q_NG; //navigation North East Down {NED} to geocentric {EFG}: N-->G
	protected final Operator q_AG; //aperture range, horz-right, vert-down  {RHV} to geocentric {EFG}: A-->G
	
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds

	public Pedestal( String id, Angle lat, Angle lon, double h) {
		
		this.systemId = id;
		
		//WGS84 ellipsoid coordinates: Latitude, Longitude, height
		this.wgs84 = new Location(lat,lon, h );
		
		//produce WGS84 location in Cartesian coordinates (geocentric): E,F,G [a.k.a. ITRF X,Y,Z]
		this.efg = wgs84.geocentric();
		
		//produce axial rotator from local navigation alignment to geocentric: {NED}-->{XYZ}  
		this.q_NG = wgs84.axialOperator_NG();
				
		/* Pedestal: position aperture class variable PLACE HOLDERS... */

		//Pedestal aperture position in local (AER) coordinates: Azimuth, Elevation, Range
		this.aer = new Aperture();
	
		//Pedestal: position aperture... in EFG orientation
		this.q_AG = new Operator(); 
				
	}
	
	public String getSystemId() { return this.systemId; }
	
	public Aperture getAER() { return this.aer; }
	public Angle getAzimuth() {	return this.aer.getAzimuth(); }
	public Angle getElevation() { return this.aer.getElevation(); }
	public Double getRange() { return this.aer.getRange(); }
	
	public Vector3 getGeocentricCartesianCoordinates() { return this.efg; }	 
	public double getE() { return this.efg.getX(); } 
	public double getF() { return this.efg.getY(); }
	public double getG() { return this.efg.getZ(); }
	
	public Location getGeodeticEllipsoidCoordinates() { return this.wgs84; }
	public Angle getLatitude() { return this.wgs84.getNorthLatitude(); }
	public Angle getLongitude() { return this.wgs84.getEastLongitude(); }
	public double getHeight() { return this.wgs84.getEllipsoidHeight(); }


	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(Angle lat) {
		this.wgs84.setNorthLatitude(lat);
		this.efg.put(this.wgs84.geocentric());
		this.q_NG.set(this.wgs84.axialOperator_NG());
	}
	
	public void setLongitude(Angle lon) {
		this.wgs84.setEastLongitude(lon);
		this.efg.put(this.wgs84.geocentric());
		this.q_NG.set(this.wgs84.axialOperator_NG());
	}
	
	/**
	 * Get local geodetic oriented location (default WGS84._HEIGHT height):
	 */
	protected Location getEuler_NG() { // Operator q_NG84,double height84){
		double dump = q_NG.getEuler_i_kji().tanHalf();
		Principle pLat = q_NG.getEuler_j_kji().addRight().negate();
		Principle pLon = q_NG.getEuler_k_kji();
		Location extracted = new Location(); 
		if (Double.isNaN(dump)) { // Northern hemisphere
			extracted.set(pLat.negate().signedAngle(), pLon.addStraight().negate().unsignedAngle(),
					wgs84._height);
		} else if (dump == 0d) { // Southern hemisphere
			extracted.set(pLat.signedAngle(), pLon.unsignedAngle(), wgs84._height);
		} else {
		System.out.println("ERROR: Not wgs84 geolocation Location! -- NOT VALID q_NG ROTATION OP.");
		}
		return  extracted;
	}
	
	public void setHeight(double meters) {
		this.wgs84.setEllipsoidHeight( meters );//.put(wgs84.getX(), wgs84.getY(), meters);
		this.efg.put(this.wgs84.geocentric());
		this.q_NG.set(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.efg.put(E, efg.getY(), efg.getZ());
		this.wgs84.set(this.efg);
		this.q_NG.set(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.efg.put(efg.getX(), F, efg.getZ());
		this.wgs84.set(this.efg);
		this.q_NG.set(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.efg.put(efg.getX(), efg.getY(), G);
		this.wgs84.set(this.efg);
		this.q_NG.set(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(Angle paz) {
		this.aer.setAzimuth(paz);
		this.q_AG.set(q_NG);
		this.q_AG.rightMultExpK(aer.getAzimuth().getPrinciple()).rightMultExpJ(aer.getElevation().getPrinciple());
	}
	
	public void setElevation(Angle pel) {
		this.aer.setElevation(pel);
		this.q_AG.set(q_NG);
		this.q_AG.rightMultExpK(aer.getAzimuth().getPrinciple()).rightMultExpJ(aer.getElevation().getPrinciple());
	}
	
	public void setAperturePosition(Angle paz, Angle pel) {
		this.aer.setAzimuth(paz);
		this.aer.setElevation( pel);
		this.q_AG.set(q_NG);
	}
	
	public void setRange( double meters ) {
		this.aer.setRange(meters);
	}
	
	/** Updates the pedestal state to point to the given target geocentric coordinates.
	 * Actually, aligns given geocentric to pedestal's aperture coordinate frame as positioned on line-of-sight to target.
	 * Assumes no atmosphere and WGS84 earth model in positioing.
	 * @param targetEFG Position of the taget in geocentric coordinates.  */
	public void point(Vector3 targetEFG) {
		//Define geocentric vector from ped to target -- obtain pedestal.aperture range to target.
		Vector3 r_PT_G = new Vector3(targetEFG).subtract(efg);
		this.aer.setRange(r_PT_G.getAbs());
				
		//Solve pedestal.aperture azimuth & elevation direction (allowing twisted line-of-sight) from within Geocentric frame.
		this.q_AG.set(this.q_NG);
		this.q_AG.leftMultiplyTiltI(r_PT_G).conjugate();
		Principle pAzimuth = this.q_AG.getEuler_k_kji();
		Principle pElevation = this.q_AG.getEuler_j_kji();
		
		//Re-Solve axial Op from defined 'untwisted' pedestal aperture frame to geocentric frame orientation:
		this.q_AG.set(this.q_NG);
		this.q_AG.rightMultExpK(pAzimuth).rightMultExpJ(pElevation);
		
		//Set pedestal Euler positioning definition: 
		this.aer.setAzimuth(pAzimuth.unsignedAngle());		
		this.aer.setElevation( pElevation.signedAngle());
	}

	public String toString() { 
		return this.systemId 
				+ "("+ this.wgs84.getNorthLatitude().toDegrees(8) +", "+ this.wgs84.getEastLongitude().toDegrees(8)+", "+this.getHeight()+")"
				+"("+aer.getAzimuth().toDegrees(4)+", "+aer.getElevation().toDegrees(4)+")";
	}

}
