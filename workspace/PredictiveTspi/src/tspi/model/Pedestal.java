package tspi.model;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Vector3;

/**  */
public class Pedestal {
	
	protected String systemId; // system identifier
	protected Location wgs84; // lat, lon angles, h meters ellipsoid coordinates
	protected Vector3 geo; // E,F,G location geocentric vector
	protected Operator q_NG; //Op: N-->G
	protected Position aperture; // az, el angle r meters 
	protected Vector3 direction; // n, e, d vector
	protected Operator q_AN; //forward, right,down: A-->N
	//TODO Matrix error;// error model 
	//TODO SolidAngle mask;//angularBounds
	
	public Pedestal( String id, double lat, double lon, double h) {//, double az, double el ) {
		this.systemId = id;
		
		//Pedestal location ellipsoid coordinates:
		this.wgs84 = new Location(Angle.inDegrees(lat),Angle.inDegrees(lon), h );
		
		//Geocentric location f(WGS84)
		this.geo = wgs84.getGeocentric();
		
		//axial operator from local Navigation to Geocentric framework: {NED}-->{XYZ}  
		this.q_NG = wgs84.getOpNavToGeo();//local Nav to Geo axial rotation.
		
		//Pedestal aperture line-of-sight + fov [axial] coordinate framework: {LHV} 
		//{Line-of-sight [Twist], Horizontal-right [Elevation trunnion], Vertical-down [Azimuth bearing]}
		this.aperture = new Position(null,null,Double.NaN);
//		this.range = null;
//		this.az = null;//new Principle( Angle.inDegrees(az) );
//		this.el = null;//new Principle( Angle.inDegrees(el) );
//		this.aer = null; //new AER(az,el,rg);
//		this.q_PN = null; //aer.getFromRHVtoNED;

//		this.direction = new Vector3(0.0,0.0,0.0);
//		this.direction(this.az, this.el);
		this.direction = null;
//		this.Hnormal=null; //delta elevation
//		this.Vnormal=null; //delta azimuthal
		
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
		if(aperture.getAzimuth()==null)
			return null;
		return this.aperture.getAzimuth().getDegrees();	
	}
	public Double getElevation() {
		if(aperture.getElevation()==null)
			return null;
		return this.aperture.getElevation().getDegrees();
	}
	public Double getRange() { return this.aperture.getRange(); }
	
	public void clearOrientaion() {
		this.aperture.setAzimuth(null);
		this.aperture.setElevation(null);
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
		direction.put(aperture.getDirection());
	}
	
	public void setElevation(double degrees) {
		this.aperture.setElevation( Angle.inDegrees(degrees));
		direction.put(aperture.getDirection());
	}
	
	public void setOrientation(double az, double el) {
		this.aperture.setAzimuth(Angle.inDegrees(az));
		this.aperture.setElevation( Angle.inDegrees(el));
		direction.put(aperture.getDirection());
	//	q_AN.put(this.aperture.op_A_N());
	}
	
	public void setRange( double meters ) {
		this.aperture.setRange(meters);
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
		aperture.getPrincipleAzimuth().put( Angle.inRadians(
				StrictMath.atan2(sa, ca)) );
		aperture.getPrincipleElevation().put( Angle.inRadians(
				StrictMath.atan2(-direction.getY(), StrictMath.hypot(ca, sa))) );
		//TODO update range?
	}

	// pedestal.point = f(orient) 	
	public void point(Operator orientation) {
		aperture.getPrincipleAzimuth().put( orientation.getEuler_k_kji());
		aperture.getPrincipleElevation().put( orientation.getEuler_j_kji());		
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
				+"("+aperture.getAzimuth().getDegrees()+", "+aperture.getElevation().getDegrees()+")";
	}
}
