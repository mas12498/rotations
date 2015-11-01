package tspi.model;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Vector3;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected WGS84 wgs84; // lat, lon angles, h meters
	protected Vector3 geo; // E,F,G vector
	protected Operator q_NG;
	protected Principle az, el;
	protected Vector3 direction; // n, e, d vector
	protected Double range; // meters?
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds
	
	public Pedestal( String id, double lat, double lon, double h) {//, double az, double el ) {
		this.systemId = id;
		this.wgs84 = new WGS84(Angle.inDegrees(lat),Angle.inDegrees(lon), h );
//		this.wgs84.setLatitude( new Principle( Angle.inDegrees(lat) ) );
//		this.wgs84.setLongitude( new Principle( Angle.inDegrees(lon) ) );
//		this.wgs84.setHeight( h );
		this.geo = wgs84.getXYZ();//Geocentric location.
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
		
		this.az = null;//new Principle( Angle.inDegrees(az) );
		this.el = null;//new Principle( Angle.inDegrees(el) );
//		this.direction = new Vector3(0.0,0.0,0.0);
//		this.direction(this.az, this.el);
		this.direction = null;
		this.range = null;
	}
	
	public WGS84 getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }
	
	public String getSystemId() { return this.systemId; }
	public double getLatitude() { return wgs84.getAngleLatitude().getDegrees(); }
	public double getLongitude() { return wgs84.getAngleLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getHeight(); }
	public double getE() { return this.geo.getX(); }
	public double getF() { return this.geo.getY(); }
	public double getG() { return this.geo.getZ(); }
	
	// the following getters may return null to denote there is no current need for a heading
	
	public Double getAzimuth() {
		if(az==null)
			return null;
		return this.az.signedAngle().getDegrees();
	}
	public Double getElevation() {
		if(el==null)
			return null;
		return this.el.signedAngle().getDegrees();
	}
	public Double getRange() { return this.range; }
	
	public void clearOrientaion() {
		this.az = null;
		this.el = null;
		this.range = null;
	}
	
	public void setSystemId(String id) { this.systemId = id; }
	
	public void setLatitude(double lat) {
		this.wgs84.putLatitude( Angle.inDegrees(lat) );//put(lat, wgs84.getY(), wgs84.getZ());
		this.geo = wgs84.getXYZ();
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setLongitude(double lon) {
		this.wgs84.putLongitude( Angle.inDegrees(lon) );//.put(wgs84.getX(), lon, wgs84.getZ());
		this.geo = wgs84.getXYZ();
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setHeight(double meters) {
		this.wgs84.putHeight( meters );//.put(wgs84.getX(), wgs84.getY(), meters);
		this.geo = wgs84.getXYZ();
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		this.wgs84.putXYZ(this.geo);
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		this.wgs84.putXYZ(this.geo);
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		this.wgs84.putXYZ(this.geo);
		this.q_NG = wgs84.getFromNEDtoEFG();//local Nav to Geo axial rotation.
	}
	
	public void setAzimuth(double degrees) {
		this.az = Angle.inDegrees(degrees).getPrinciple();
		this.direction = direction(this.az, this.el); // update direction
	}
	
	public void setElevation(double degrees) {
		this.el = Angle.inDegrees(degrees).getPrinciple();
		this.direction = direction(this.az, this.el); // update direction
	}
	
	public void setOrientation(double az, double el) {
		this.az = Angle.inDegrees(az).getPrinciple();
		this.el = Angle.inDegrees(el).getPrinciple();
		this.direction = direction(this.az, this.el); // update direction
	}
	
	public void setRange( double meters ) {
		this.range = meters;
	}

	// TODO
	//	pedestal.direction =f(az,el); //NED
	protected Vector3 direction(Principle az, Principle el){
		double elCos = el.cos();
		return direction.put(elCos * az.cos(), elCos * az.sin(),
				-el.sin());
	}

//	//	pedestal.orientation =f(az,el); //NED
//	Operator direction(Principle az, Principle el){
//		double elCos = el.cos();
//		return direction.put(elCos * az.cos(), elCos * az.sin(),
//				-el.sin());
//	}
	
	
	// pedestal.point = f(direct) 
	public void point(Vector3 direction) {
		double ca = direction.getX();
		double sa = direction.getY();
		az = new Principle( Angle.inRadians(
				StrictMath.atan2(sa, ca)) );
		el = new Principle( Angle.inRadians(
				StrictMath.atan2(-direction.getY(), StrictMath.hypot(ca, sa))) );
		//TODO update range?
	}

	// pedestal.point = f(orient) 	
	public void point(Operator orientation) {
		az = orientation.getEuler_k_kji();
		el = orientation.getEuler_j_kji();		
	}
	
	/** @returns synthesized target in geocentric coordinates */
	public Vector3 fusePair(Pedestal pedestal) {
		return null; //TODO
		//TODO update range?
	}
	//TODO or this way?
//	public static What fusePair(Pedestal p1, Pedestal p2) {}
	
	public String toString() { 
		return this.systemId 
				+ "("+this.getLatitude()+", "+this.getLongitude()+", "+this.getHeight()+")"
				+"("+az.signedAngle().getDegrees()+", "+el.signedAngle().getDegrees()+")";
	}
}
