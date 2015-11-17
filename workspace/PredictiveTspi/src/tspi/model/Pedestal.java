package tspi.model;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.QuaternionMath;
import rotation.Vector3;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected final Location wgs84; // lat, lon angles, h meters ellipsoid coordinates
	protected final Vector3 geo; // EFG: G location geocentric vector
	protected final Operator q_NG; //North East Down NED: N-->G
	protected final Position aperture; // az, el angle r meters 
	protected final Vector3 direction; // NED navigation vector
	protected final Operator q_AN; //range forward, horz right, vert down  RHV: A-->N
	protected final Operator q_AG;
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds
	

	public Pedestal( String id, double lat, double lon, double h) {
		this.systemId = id;
		
		/* Pedestal: fixed location... in EFG orientation */
		
		//Pedestal location (WGS84) in ellipsoid coordinates: Latitude, Longitude, height
		this.wgs84 = new Location(Angle.inDegrees(lat),Angle.inDegrees(lon), h );
		
		//Pedestal location (WGS84) in geocentric Cartesian frame coordinates (WGS84): E,F,G [a.k.a. ECEF X,Y,Z]
		this.geo = wgs84.getGeocentric();
		
		//axial operator transforms from local navigation alignment to Geocentric: {NED}-->{XYZ}  
		this.q_NG = wgs84.getOpNavToGeo();
		
		/* Pedestal: position aperture class variable PLACE HOLDERS... */
		
		//Pedestal aperture position in local (AER) coordinates: Azimuth, Elevation, Range
		this.aperture = new Position(Angle.inDegrees(Double.NaN),Angle.inDegrees(Double.NaN),Double.NaN);
	
		//axial operator from local pedestal aperture: {RHV} --> {NED:
		this.q_AN = new Operator(Quaternion.NAN); //aperture.op_AN();
		this.direction = new Vector3(Vector3.NAN);
		//Pedestal: position aperture... in EFG orientation
		this.q_AG = new Operator(Quaternion.NAN); //aperture.op_AN();
		// q_AG = q_NG.euler_kj(paz,el);
		
	}
	
	public Location getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }
	public String getSystemId() { return this.systemId; }
	public double getLatitude() { return this.wgs84.getNorthLatitude().getDegrees(); }
	public double getLongitude() { return this.wgs84.getEastLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getEllipsoidHeight(); }
	public double getE() { return this.geo.getX(); }
	public double getF() { return this.geo.getY(); }
	public double getG() { return this.geo.getZ(); }
	
	// the following getters may return null to denote there is no current need for a heading
	
	public Double getAzimuth() {	
		return this.aperture.getAzimuth().getDegrees();	
	}
	public Double getElevation() {
		return this.aperture.getElevation().getDegrees();
	}
	public Double getRange() { 
		return this.aperture.getRange(); 
	}
	
	public void clearOrientaion() {
		this.aperture.setAzimuth(Angle.inDegrees(Double.NaN));
		this.aperture.setElevation(Angle.inDegrees(Double.NaN));
		this.aperture.setRange(Double.NaN);
	}
	
	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(double lat) {
		this.wgs84.setNorthLatitude(Angle.inDegrees(lat) );//put(lat, wgs84.getY(), wgs84.getZ());
		this.geo.put(this.wgs84.getGeocentric());
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setLongitude(double lon) {
		this.wgs84.setEastLongitude( Angle.inDegrees(lon) );//.put(wgs84.getX(), lon, wgs84.getZ());
		this.geo.put(this.wgs84.getGeocentric());
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setHeight(double meters) {
		this.wgs84.setEllipsoidHeight( meters );//.put(wgs84.getX(), wgs84.getY(), meters);
		this.geo.put(this.wgs84.getGeocentric());
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		this.wgs84.set(this.geo);
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		this.wgs84.set(this.geo);
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		this.wgs84.set(this.geo);
		this.q_NG.put(this.wgs84.getOpNavToGeo());//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(double degrees) {
		this.aperture.setAzimuth(Angle.inDegrees(degrees));
		this.q_AN.put(this.aperture.op_AN());
		direction.put(q_AN.getImage_i()).unit();		
	}
	
	public void setElevation(double degrees) {
		this.aperture.setElevation( Angle.inDegrees(degrees));
		this.q_AN.put(this.aperture.op_AN());
		direction.put(q_AN.getImage_i()).unit();		
	}
	
	public void setOrientation(double az, double el) {
		this.aperture.setAzimuth(Angle.inDegrees(az));
		this.aperture.setElevation( Angle.inDegrees(el));
		this.q_AN.put(this.aperture.op_AN());
		direction.put(q_AN.getImage_i()).unit();		
	}
	
	public void setRange( double meters ) {
		this.aperture.setRange(meters);
	}
	
	// pedestal.point = f(direct) 
	/** Updates the pedestal state to point to the given target geocentric coordinates.
	 * Actually, aligns given geocentric to pedestal's aperture coordinate frame as positioned on line-of-sight to target.
	 * Assumes no atmosphere and WGS84 earth model in positioing.
	 * @param targetEFG Position of the taget in geocentric coordinates.  */
	public void point(Vector3 targetEFG) {
		//Define geocentric vector from ped to target -- obtain pedestal.aperture range to target.
		Vector3 r_PT_G = new Vector3(targetEFG).subtract(geo);
		aperture.setRange(r_PT_G.getAbs());
		
		//Make initial (rank-2) 'put' of q_AN -- returns pedestal.aperture azimuth & elevation direction (line-of-sight) to target.
		this.q_AN.put(this.q_NG).leftMultiplyTiltI(r_PT_G).conjugate();
		aperture.setAzimuth(this.q_AN.getEuler_k_kji().unsignedAngle());		
		aperture.setElevation( this.q_AN.getEuler_j_kji().signedAngle());

		//Refine 'put': Repair third rank of q_AN* (Annihilate Euler i-twist in field-of-view to match pedestal.aperture definition.)
		//-- makes q_AN rank-3 axial Operator between pedestal.aperture and pedestal.location coordinate frame definitions.
		Principle gTwist = q_AN.getEuler_i_kji();
		this.q_AN.rightMultExpI(gTwist.negate()); 
		
		//Assign local aperture directions in local navigation frame coordinates -- from Operator q_AN:
		direction.put(this.q_AN.getImage_i()); //out pedestal.aperture (unit i) 
//		Vector3 vertical.put(this.q_AN.getImage_j()); //right (across) pedestal.aperture (unit j)
//		Vector3 horizontal.put(this.q_AN.getImage_k()); //downward (across) pedestal.aperture (unit k)
		
		
		// //OP aligns geocentric to pedestal.aperture axial coordinates if perfectly located, aligned pedestal and no atmosphere:
		//System.out.println("post-twist annihilation:"+ this.q_AN.getImage_i().toString(5));		
		//System.out.println("q_AN twisted aftr repair:"+ q_AN.getEuler_i_kji().signedAngle().getDegrees());
		//System.out.println("Geocentric direction:"+ r_PT_G.divide(this.aperture.getRange()).toString(7));
		//System.out.println("Op Geocentric direction match:"+ new Operator(new Quaternion(q_NG).rightMultiply(q_AN)).getImage_i().unit().toString(7));				
	}

	// pedestal.point = f(orient) 	
	public void point(Operator orientation) {
		aperture.getPrincipleAzimuth().put( orientation.getEuler_k_kji());
		aperture.getPrincipleElevation().put( orientation.getEuler_j_kji());		
	}
	
	/** Combine two pedestals' location and orientation to find an estimate of he location of a single target. (midpoint of shortest line between the two boresight lines? Not sure how you define this...)
	 * @returns synthesized target in geocentric coordinates */
	public Vector3 fusePair(Pedestal pedestal) {
		return null; //TODO
		//TODO update range?
	}
	//TODO or this way?
//	public static What fusePair(Pedestal p1, Pedestal p2) {}
	
	public String toString() { 
		return this.systemId 
				+ "("+this.getLatitude()+", "+this.getLongitude()+", "+this.getHeight()+")"
				+"("+aperture.getAzimuth().getDegrees()+", "+aperture.getElevation().getDegrees()+")";
	}



//	// TODO
//	//	pedestal.direction =f(az,el); //NED
//	protected Vector3 direction(Principle az, Principle el){
//		double elCos = el.cos();
//		return direction.put(elCos * az.cos(), elCos * az.sin(),
//				-el.sin());
//	}

	




}
