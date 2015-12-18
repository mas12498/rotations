package tspi.model;

import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.Vector3;
import sun.security.action.PutAllAction;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected final Location wgs84; // geodetic position: lat, lon angles, h meters ellipsoid coordinates
	protected final Vector3 efg; // geocentric position vector: EFG
	protected final Aperture aer; //Aperture position: az, el angle r meters 
	//axial operators...
	protected final Operator q_NG; //navigation North East Down {NED} to geocentric {EFG}: N-->G
//	protected final Operator q_AN; //aperture range, horz-right, vert-down  {RHV} to navigation {NED}: A-->N
	protected final Operator q_AG; //aperture range, horz-right, vert-down  {RHV} to geocentric {EFG}: A-->G
	
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds

	public Pedestal( String id, double lat, double lon, double h) {
		
		this.systemId = id;
		
		//WGS84 ellipsoid coordinates: Latitude, Longitude, height
		this.wgs84 = new Location();
		this.wgs84.set(Angle.inDegrees(lat),Angle.inDegrees(lon), h );
		
		//WGS84 location in Cartesian coordinates (geocentric): E,F,G [a.k.a. ITRF X,Y,Z]
		this.efg = wgs84.geocentric();
		
		//axial operator transforms from local navigation alignment to geocentric: {NED}-->{XYZ}  
		this.q_NG = wgs84.axialOperator_NG();
				
		/* Pedestal: position aperture class variable PLACE HOLDERS... */

		//Pedestal aperture position in local (AER) coordinates: Azimuth, Elevation, Range
		this.aer = new Aperture();
	
		//Pedestal: position aperture... in EFG orientation
		this.q_AG = new Operator(); 
				
	}
	
	public String getSystemId() { return this.systemId; }
	public Location getGeodeticEllipsoidCoordinates() { return this.wgs84; }
	public double getLatitude() { return this.wgs84.getNorthLatitude().getDegrees(); }
	public double getLongitude() { return this.wgs84.getEastLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getEllipsoidHeight(); }
	public Vector3 getGeocentricCartesianCoordinates() { return this.efg; }	//this.survey.getGeocentricCoordinates(); //slower 
	public double getE() { return this.efg.getX(); } //this.survey.geocentric().getX(); //slower
	public double getF() { return this.efg.getY(); }
	public double getG() { return this.efg.getZ(); }
		
	// the following getters may return null to denote there is no current need for a heading
	
	public Double getAzimuth() {	
		return this.aer.getAzimuth().getDegrees();	
	}
	public Double getElevation() {
		return this.aer.getElevation().getDegrees();
	}
	public Double getRange() { 
		return this.aer.getRange(); 
	}
	
	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(double lat) {
		this.wgs84.setNorthLatitude(Angle.inDegrees(lat) );
		this.efg.put(this.wgs84.geocentric());
		this.q_NG.put(this.wgs84.axialOperator_NG());
	}
	
	public void setLongitude(double lon) {
		this.wgs84.setEastLongitude( Angle.inDegrees(lon) );
		this.efg.put(this.wgs84.geocentric());
		this.q_NG.put(this.wgs84.axialOperator_NG());
	}
	
	/**
	 * Get Location by Op q_NG (default WGS84._HEIGHT height):
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
		this.q_NG.put(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.efg.put(E, efg.getY(), efg.getZ());
		this.wgs84.set(this.efg);
		this.q_NG.put(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.efg.put(efg.getX(), F, efg.getZ());
		this.wgs84.set(this.efg);
		this.q_NG.put(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.efg.put(efg.getX(), efg.getY(), G);
		this.wgs84.set(this.efg);
		this.q_NG.put(this.wgs84.axialOperator_NG());//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(double degrees) {
		this.aer.setAzimuth(Angle.inDegrees(degrees));
		this.q_AG.put(q_NG).rightMultExpK(aer.getAzimuth().getPrinciple()).rightMultExpJ(aer.getElevation().getPrinciple());
	}
	
	public void setElevation(double degrees) {
		this.aer.setElevation( Angle.inDegrees(degrees));
		this.q_AG.put(q_NG).rightMultExpK(aer.getAzimuth().getPrinciple()).rightMultExpJ(aer.getElevation().getPrinciple());
	}
	
	public void setAperturePosition(double az, double el) {
		this.aer.setAzimuth(Angle.inDegrees(az));
		this.aer.setElevation( Angle.inDegrees(el));
		this.q_AG.put(q_NG).rightMultExpK(aer.getAzimuth().getPrinciple()).rightMultExpJ(aer.getElevation().getPrinciple());
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
		this.q_AG.put(this.q_NG).leftMultiplyTiltI(r_PT_G).conjugate();
		
		//Re-Solve axial Op from defined 'untwisted' pedestal aperture frame to geocentric frame orientation:
		Principle pAzimuth = this.q_AG.getEuler_k_kji();
		Principle pElevation = this.q_AG.getEuler_j_kji();
		this.q_AG.put(this.q_NG).rightMultExpK(pAzimuth).rightMultExpJ(pElevation);
		
		//Set pedestal Euler positioning definition: 
		this.aer.setAzimuth(pAzimuth.unsignedAngle());		
		this.aer.setElevation( pElevation.signedAngle());
	}

	public String toString() { 
		return this.systemId 
				+ "("+ this.wgs84.getNorthLatitude().toDegrees(7) +", "+ this.wgs84.getEastLongitude().toDegrees(7)+", "+this.getHeight()+")"
				+"("+aer.getAzimuth().toDegrees(4)+", "+aer.getElevation().toDegrees(4)+")";
	}

}
