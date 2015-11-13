package tspi.model;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.Vector3;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected Location wgs84; // lat, lon angles, h meters ellipsoid coordinates
	protected Vector3 geo; // EFG: G location geocentric vector
	protected Operator q_NG; //North East Down NED: N-->G
	protected Position aperture; // az, el angle r meters 
	protected Vector3 direction; // NED navigation vector
	protected Operator q_AN; //range forward, horz right, vert down  RHV: A-->N
	protected Operator q_AG;
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
		this.aperture = new Position(Angle.inDegrees(0.0),Angle.inDegrees(0.0),0.0);
	
		//axial operator from local pedestal aperture: {RHV} --> {NED:
		this.q_AN = new Operator(Quaternion.NAN); //aperture.op_AN();
		this.direction = null;
		//Pedestal: position aperture... in EFG orientation
		this.q_AG = new Operator(Quaternion.NAN); //aperture.op_AN();
		// q_AG = q_NG.euler_kj(paz,el);
		
	}
	
	public Location getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }
	public String getSystemId() { return this.systemId; }
	public double getLatitude() { return wgs84.getNorthLatitude().getDegrees(); }
	public double getLongitude() { return wgs84.getEastLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getEllipsoidHeight(); }
	public double getE() { return this.geo.getX(); }
	public double getF() { return this.geo.getY(); }
	public double getG() { return this.geo.getZ(); }
	
	// the following getters may return null to denote there is no current need for a heading
	
	public Double getAzimuth() {	
		if(aperture == null) return null;
		if(aperture.getAzimuth()==null)
			return null;
		return this.aperture.getAzimuth().getDegrees();	
	}
	public Double getElevation() {
		if (aperture == null) return null;
		if(aperture.getElevation()==null)
			return null;
		return this.aperture.getElevation().getDegrees();
	}
	public Double getRange() { return this.aperture.getRange(); }
	
	public void clearOrientaion() {
		this.aperture.setAzimuth(Angle.inDegrees(Double.NaN));
		this.aperture.setElevation(Angle.inDegrees(Double.NaN));
		this.aperture.setRange(Double.NaN);
	}
	
	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(double lat) {
		this.wgs84.setNorthLatitude(Angle.inDegrees(lat) );//put(lat, wgs84.getY(), wgs84.getZ());
		this.geo = wgs84.getGeocentric();
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setLongitude(double lon) {
		this.wgs84.setEastLongitude( Angle.inDegrees(lon) );//.put(wgs84.getX(), lon, wgs84.getZ());
		this.geo = wgs84.getGeocentric();
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setHeight(double meters) {
		this.wgs84.setEllipsoidHeight( meters );//.put(wgs84.getX(), wgs84.getY(), meters);
		this.geo = wgs84.getGeocentric();
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		this.wgs84.set(this.geo);
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		this.wgs84.set(this.geo);
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		this.wgs84.set(this.geo);
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(double degrees) {
		this.aperture.setAzimuth(Angle.inDegrees(degrees));
		this.q_AN = aperture.op_AN();
		direction.put(q_AN.getImage_i()).unit();		
		//direction.put(aperture.getDirection());
	}
	
	public void setElevation(double degrees) {
		this.aperture.setElevation( Angle.inDegrees(degrees));
		this.q_AN = aperture.op_AN();
		direction.put(q_AN.getImage_i()).unit();		
//		direction.put(aperture.getDirection());
	}
	
	public void setOrientation(double az, double el) {
		this.aperture.setAzimuth(Angle.inDegrees(az));
		this.aperture.setElevation( Angle.inDegrees(el));
		this.q_AN = aperture.op_AN();
		direction.put(q_AN.getImage_i()).unit();		
//		direction.put(aperture.getDirection());
	}
	
	public void setRange( double meters ) {
		this.aperture.setRange(meters);
	}

//	// TODO
//	//	pedestal.direction =f(az,el); //NED
//	protected Vector3 direction(Principle az, Principle el){
//		double elCos = el.cos();
//		return direction.put(elCos * az.cos(), elCos * az.sin(),
//				-el.sin());
//	}

	
	
	// pedestal.point = f(direct) 
	/** Updates the pedestal sate to point to the given target coordinates.
	 * @param targetEFG Position of the taget in geocentric coordinates.  */
	public void point(Vector3 targetEFG) {
		double ca = targetEFG.getX();
		double sa = targetEFG.getY();
		aperture.getPrincipleAzimuth().put( Angle.inRadians(
				StrictMath.atan2(sa, ca)) );
		aperture.getPrincipleElevation().put( Angle.inRadians(
				StrictMath.atan2(-targetEFG.getY(), StrictMath.hypot(ca, sa))) );
		//TODO update range?
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
}
