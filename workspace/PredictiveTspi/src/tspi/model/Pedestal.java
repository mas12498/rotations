package tspi.model;

import java.util.Iterator;
import java.util.List;

import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.Vector3;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected final Location wgs84_LLh; // lat, lon angles, h meters ellipsoid coordinates
	protected final Vector3 geo_EFG; // EFG: G location geocentric vector
	protected final Operator q_NG; //North East Down NED: N-->G
	protected final Position ped_AER; // az, el angle r meters 
	protected final Vector3 dir_NED; // NED navigation vector
	protected final Operator q_AN; //range forward, horz right, vert down  RHV: A-->N
	protected final Operator q_AG;
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds

	public Pedestal( String id, double lat, double lon, double h) {
		this.systemId = id;
		
		/* Pedestal: fixed location... in EFG orientation */
		
		//Pedestal location (WGS84) in ellipsoid coordinates: Latitude, Longitude, height
		this.wgs84_LLh = new Location(Angle.inDegrees(lat),Angle.inDegrees(lon), h );
		
		//Pedestal location (WGS84) in geocentric Cartesian frame coordinates (WGS84): E,F,G [a.k.a. ECEF X,Y,Z]
		this.geo_EFG = wgs84_LLh.getGeocentric();
		
		//axial operator transforms from local navigation alignment to Geocentric: {NED}-->{XYZ}  
		this.q_NG = wgs84_LLh.getOpNavToGeo();
		
		
		/* Pedestal: position aperture class variable PLACE HOLDERS... */
		
		//Pedestal aperture position in local (AER) coordinates: Azimuth, Elevation, Range
		this.ped_AER = new Position(Angle.inDegrees(Double.NaN),Angle.inDegrees(Double.NaN),Double.NaN);
	
		//axial operator from local pedestal aperture to local navigation: {RHV} --> {NED:
		this.q_AN = new Operator(Quaternion.NAN); //aperture.op_AN();
		this.dir_NED = new Vector3(Vector3.NAN);
		//Pedestal: position aperture... in EFG orientation
		this.q_AG = new Operator(Quaternion.NAN); //aperture.op_AN();
		// q_AG = q_NG.euler_kj(paz,el);
		
	}
	
	public Location getEllipsoidalCoordinates() { return this.wgs84_LLh; }
	public Vector3 getGeocentricCoordinates() { return this.geo_EFG; }
	public String getSystemId() { return this.systemId; }
	public double getLatitude() { return this.wgs84_LLh.getNorthLatitude().getDegrees(); }
	public double getLongitude() { return this.wgs84_LLh.getEastLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84_LLh.getEllipsoidHeight(); }
	public double getE() { return this.geo_EFG.getX(); }
	public double getF() { return this.geo_EFG.getY(); }
	public double getG() { return this.geo_EFG.getZ(); }
		
	// the following getters may return null to denote there is no current need for a heading
	
	public Double getAzimuth() {	
		return this.ped_AER.getAzimuth().getDegrees();	
	}
	public Double getElevation() {
		return this.ped_AER.getElevation().getDegrees();
	}
	public Double getRange() { 
		return this.ped_AER.getRange(); 
	}
	
	public void clearAperturePosition() {
		this.ped_AER.setAzimuth(Angle.inDegrees(Double.NaN));
		this.ped_AER.setElevation(Angle.inDegrees(Double.NaN));
		this.ped_AER.setRange(Double.NaN);
	}
	
	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(double lat) {
		this.wgs84_LLh.setNorthLatitude(Angle.inDegrees(lat) );//put(lat, wgs84.getY(), wgs84.getZ());
		this.geo_EFG.put(this.wgs84_LLh.getGeocentric());
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setLongitude(double lon) {
		this.wgs84_LLh.setEastLongitude( Angle.inDegrees(lon) );//.put(wgs84.getX(), lon, wgs84.getZ());
		this.geo_EFG.put(this.wgs84_LLh.getGeocentric());
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	/**
	 * Get Location by Op q_NG (default WGS84._HEIGHT height):
	 */
	protected Location getLocationFromOp() { // Operator q_NG84,double height84){
		double dump = q_NG.getEuler_i_kji().tanHalf();
		Principle pLat = q_NG.getEuler_j_kji().addRight().negate();
		Principle pLon = q_NG.getEuler_k_kji();
		if (Double.isNaN(dump)) { // Northern hemisphere
			return new Location(pLat.negate().signedAngle(), pLon.addStraight().negate().unsignedAngle(),
					wgs84_LLh._height);
		} else if (dump == 0d) { // Southern hemisphere
			return new Location(pLat.signedAngle(), pLon.unsignedAngle(), wgs84_LLh._height);
		}
		System.out.println("ERROR: Not wgs84 geolocation Location! -- NOT VALID q_NG ROTATION OP.");
		return this.wgs84_LLh;
	}
	
	public void setHeight(double meters) {
		this.wgs84_LLh.setEllipsoidHeight( meters );//.put(wgs84.getX(), wgs84.getY(), meters);
		this.geo_EFG.put(this.wgs84_LLh.getGeocentric());
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.geo_EFG.put(E, geo_EFG.getY(), geo_EFG.getZ());
		this.wgs84_LLh.set(this.geo_EFG);
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.geo_EFG.put(geo_EFG.getX(), F, geo_EFG.getZ());
		this.wgs84_LLh.set(this.geo_EFG);
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.geo_EFG.put(geo_EFG.getX(), geo_EFG.getY(), G);
		this.wgs84_LLh.set(this.geo_EFG);
		this.q_NG.put(this.wgs84_LLh.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(double degrees) {
		this.ped_AER.setAzimuth(Angle.inDegrees(degrees));
		this.q_AN.put(this.ped_AER.op_AN());
		dir_NED.put(q_AN.getImage_i()).unit();		
	}
	
	public void setElevation(double degrees) {
		this.ped_AER.setElevation( Angle.inDegrees(degrees));
		this.q_AN.put(this.ped_AER.op_AN());
		dir_NED.put(q_AN.getImage_i()).unit();		
	}
	
	public void setAperturePosition(double az, double el) {
		this.ped_AER.setAzimuth(Angle.inDegrees(az));
		this.ped_AER.setElevation( Angle.inDegrees(el));
		this.q_AN.put(this.ped_AER.op_AN());
		dir_NED.put(q_AN.getImage_i()).unit();		
	}
	
	public void setRange( double meters ) {
		this.ped_AER.setRange(meters);
	}
	
	// pedestal.point = f(direct) 
	/** Updates the pedestal state to point to the given target geocentric coordinates.
	 * Actually, aligns given geocentric to pedestal's aperture coordinate frame as positioned on line-of-sight to target.
	 * Assumes no atmosphere and WGS84 earth model in positioing.
	 * @param targetEFG Position of the taget in geocentric coordinates.  */
	public void point(Vector3 targetEFG) {
		//Define geocentric vector from ped to target -- obtain pedestal.aperture range to target.
		Vector3 r_PT_G = new Vector3(targetEFG).subtract(geo_EFG);
		ped_AER.setRange(r_PT_G.getAbs());
		
		//Make initial (rank-2) 'put' of q_AN -- returns pedestal.aperture azimuth & elevation direction (line-of-sight) to target.
		this.q_AN.put(this.q_NG).leftMultiplyTiltI(r_PT_G).conjugate();
		ped_AER.setAzimuth(this.q_AN.getEuler_k_kji().unsignedAngle());		
		ped_AER.setElevation( this.q_AN.getEuler_j_kji().signedAngle());

		//Refine 'put': Repair third rank of q_AN* (Annihilate Euler i-twist in field-of-view to match pedestal.aperture definition.)
		//-- makes q_AN rank-3 axial Operator between pedestal.aperture and pedestal.location coordinate frame definitions.
		Principle gTwist = q_AN.getEuler_i_kji();
		this.q_AN.rightMultExpI(gTwist.negate()); 
		
		//Assign local aperture directions in local navigation frame coordinates -- from Operator q_AN:
		dir_NED.put(this.q_AN.getImage_i()); //out pedestal.aperture (unit i) 
//		Vector3 vertical.put(this.q_AN.getImage_j()); //right (across) pedestal.aperture (unit j)
//		Vector3 horizontal.put(this.q_AN.getImage_k()); //downward (across) pedestal.aperture (unit k)
		
		
		// //OP aligns geocentric to pedestal.aperture axial coordinates if perfectly located, aligned pedestal and no atmosphere:
		//System.out.println("post-twist annihilation:"+ this.q_AN.getImage_i().toString(5));		
		//System.out.println("q_AN twisted aftr repair:"+ q_AN.getEuler_i_kji().signedAngle().getDegrees());
		//System.out.println("Geocentric direction:"+ r_PT_G.divide(this.aperture.getRange()).toString(7));
		//System.out.println("Op Geocentric direction match:"+ new Operator(new Quaternion(q_NG).rightMultiply(q_AN)).getImage_i().unit().toString(7));				
	}

	// pedestal.point = f(orient) 	
	public void pointNED(Operator ped_q_AN) {
		//assume non dumping pedestal
		ped_AER.getPrincipleAzimuth().put( ped_q_AN.getEuler_k_kji());
		ped_AER.getPrincipleElevation().put( ped_q_AN.getEuler_j_kji());		
	}
	
	/** Combine two pedestals' location and orientation to find an estimate of he location of a single target. 
	 * a. Simplest solution of two pedestals: 3 sensors of best geometry
	 * c. Use all sensors available
	 * @returns synthesized target in geocentric coordinates */
	public Vector3 fusePair(Pedestal pedestal) {
		return null; //TODO
		//TODO update range?
	}
	//TODO or this way?
//	public static What fusePair(Pedestal p1, Pedestal p2) {}
	
	public Vector3 computeTarget(List<Pedestal> pedestals) {
		//TODO
		for(Pedestal pedestal : pedestals)
			;
		return null;
	}
	
	public String toString() { 
		return this.systemId 
				+ "("+this.getLatitude()+", "+this.getLongitude()+", "+this.getHeight()+")"
				+"("+ped_AER.getAzimuth().getDegrees()+", "+ped_AER.getElevation().getDegrees()+")";
	}



//	// TODO
//	//	pedestal.direction =f(az,el); //NED
//	protected Vector3 direction(Principle az, Principle el){
//		double elCos = el.cos();
//		return direction.put(elCos * az.cos(), elCos * az.sin(),
//				-el.sin());
//	}

	




}
