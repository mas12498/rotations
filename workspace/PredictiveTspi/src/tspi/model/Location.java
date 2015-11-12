package tspi.model;


import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Operator;
import rotation.Principle;
import rotation.QuaternionMath;
import rotation.Vector3;



/**
 * Stores geodetic location in WGS84 ellipsoid coordinates: geodetic latitude, geodetic longitude geodetic height.
 *  
 * <p> Includes methods for generating: 
 * 
 *  
 *  	<p>-- Vector3 of location for geocentric coordinates G:{X,Y,Z} (-- Sometimes documented as {E,F,G}).
 *  
 *  	<p>-- Axial operator for coordinate transformation from local frame orientation N:{N,E,D} to
 *               geocentric frame orientation G:{X,Y,Z}.
 *                          
 * @author mike
 *
 */
public class Location {
	
	
	static final double ZERO = 0d;
	static final double ONE = 1d;
	static final double TWO = 2d;
	static final double THREE = 3d;
	static final double FOUR = 4d;
	static final double FIVE = 5d;	
	/**	 * WGS 84 oblate ellipsoid flattening	 */
	public static final double _f = 0.00335281068118d;
	/**	 * WGS 84 semimajor axis.	 */
	public static final double _a = 6378137.0d;
	/**	 * WGS 84 semiminor axis.	 */
	public static final double _b = _a * (ONE - _f);
	private static final double DIFFERENCE_AXES_RATIOS = (_a/_b) - (_b/_a);
    private static final double FLATFN= (TWO - _f)*_f;
    private static final double FUNSQ= (ONE - _f)*(ONE - _f);
	
	/**	 * WGS 84 geodetic latitude	 */
	protected final Principle _latitude;
	
	/**	 * WGS 84 geodetic longitude	 */
	protected final Principle _longitude;
	
	/**	 * WGS 84 ellipsoid height	 */
	protected double _height;
	
//	/**	 * WGS 84 local latitude rotation to vertical	 */
//	protected final Principle _theta;
	
	
	//Constructors...
	
	/**
	 * Copy constructor.
	 * @param ellipsoid84
	 */
	public Location(Location ellipsoid84){
		_latitude = ellipsoid84.getNorthLatitude().getPrinciple(); //copy constructor
		_longitude = ellipsoid84.getEastLongitude().getPrinciple(); //copy constructor
		_height = ellipsoid84.getEllipsoidHeight();
//		_theta = ellipsoid84.getTheta(); //factory
	}
	
	/**
	 * Constructor by component WGS84 ellipsoid coordinates:
	 * @param latitude84 
	 * @param longitude84
	 * @param height84 (meters above WGS84 ellipsoid).
	 */
	public Location(Angle latitude84, Angle longitude84, double height84){
		//Construct geodetic ellipsoid coordinates by component angles and meters height
		_latitude = latitude84.getPrinciple();	//factory
		_longitude = longitude84.getPrinciple(); //factory
		_height = height84;
//		_theta = latitude84.getPrincipleSumRight().negate(); //factory
		//_theta = new Principle(_latitude).addRight().negate();

	}
	
//	/**
//	 * Constructor by Cartesian geocentric positon-vector: G:{XYZ}
//	 * @param locationXYZ
//	 */
//	public WGS84(Vector3 locationXYZ){
//		//Construct by Cartesian Geocentric equivalence
//		//Construct component Principle objects with dummy placeholders
//		_latitude = new Principle(Principle.ZERO);	//dummy init object
//		_longitude = new Principle(Principle.ZERO); //dummy init object
//		_theta = new Principle(Principle.ZERO); 	//dummy init object
//		//put to Principle dummy objects and primitive _height
//		putXYZ(locationXYZ); 
//	}
	
//    // By Quaternion axial operator definition and normal height above ellipsoid
//	public WGS84(Operator q_NG, double ellipsoidHeight){
//		Operator tmp = new Operator(q_NG).turn(BasisUnit.J);
//		_longitude = tmp.getEuler_k_kji();
//		_latitude = tmp.getEuler_j_kji(); 
//		_theta = new Principle(_latitude).addRight().negate();
//		
//		//Principle.arcTanHalfAngle(_theta.cotHalf());
//		_height = ellipsoidHeight; // factory dummy load		
////		_longitude = q_NG.getEuler_k_kji();
////		_latitude = q_NG.getEuler_j_kji().negate();
////		_theta = new Principle(_latitude).addRight().negate(); //Principle.arcTanHalfAngle(q_NG.getEuler_j_kji().cotHalf());						
////		if(_latitude.isStraight()){ //.signedAngle().getRadians()>=0){
////			_longitude.put(Principle.arcTanHalfAngle(_longitude.cotHalf()).negate());
////			_latitude.negate();
////		}
//		
//		
//	}	
	
	
//    // By Quaternion axial operator definition and geocentric position-vector
//	protected WGS84(Operator q_NG, Vector3 locationXYZ){
//		_longitude = q_NG.getEuler_k_kji();
//		_theta = q_NG.getEuler_j_kji();
//		_latitude = Principle.arcTanHalfAngle(_theta.cotHalf());
//		
//		
//		
//		double sin_lat = _latitude.sin();
//		double rTmp = _a / StrictMath.sqrt(ONE - FLATFN * sin_lat * sin_lat);
//		double x = rTmp * _latitude.cos();
//		Vector3 pXYZ = new Vector3(x * _longitude.cos(), x * _longitude.sin(), rTmp * FUNSQ * sin_lat);
//		_height = pXYZ.subtract(locationXYZ).getDot(q_NG.getImage_k());
//	}	
	
	//Getters
	
	/**
	 * Create geocentric Cartesian coordinates: earth-centered earth-fixed 
	 * @param _latitude
	 * @param _longitude
	 * @param _height
	 * @return Vector3 {E,F,G}
	 */
	public Vector3 getGeocentric(){
		double sin_lat = _latitude.sin();
		double rTmp = _a / StrictMath.sqrt(ONE - FLATFN * sin_lat * sin_lat);
		double x = (rTmp + _height) * _latitude.cos();
		return new Vector3(x * _longitude.cos(), x * _longitude.sin(), (rTmp * FUNSQ + _height) * sin_lat);
	}
	
	
	
	/**
	 * Create quaternion Operator that rotates 
	 * from local navigation coordinate frame {N,E,D}
	 * to geocentric frame {X,Y,Z}. 
	 * 
	 * @return Operator {W,X,Y,Z}
	 */
	public Operator getOpNavToGeo(){
		Principle theta = _latitude.addRight().negate();
		return QuaternionMath.eulerRotate_kj(_longitude,theta);
	}
	
	/**	 * @return Angle of the WGS84 North latitude	 */
	public Angle getNorthLatitude() {
		return _latitude.signedAngle();
	}
	
//	/**	 * @return Angle of the WGS84 North latitude	 */
//	public Angle getSouthLatitude() {
//		return new Principle(_latitude).negate().signedAngle();
//	}
	
	/**	 * @return the WGS84 Principle North latitude	 */
	protected Principle getLatitude() {
		return _latitude;
	}
	
//	protected Principle getTheta(){
//		//_theta.put(_latitude).addRight().negate();
//		return _theta; 
//	}
	
	/**	 * @return Angle of the WGS84 East longitude	 */
	public Angle getEastLongitude() {
		return _longitude.unsignedAngle();
	}

//	/**	 * @return Angle of the WGS84 East longitude	 */
//	public Angle getWestLongitude() {
//		return new Principle(_longitude).negate().unsignedAngle();
//	}
	
	
	/**	 * @return the WGS84 Principle East longitude	 */
	protected Principle getLongitude() {	
		return _longitude;
	}
	
	/**
	 * @return the WGS84 ellipsoid height
	 */
	public double getEllipsoidHeight() {
		return _height;
	}

	/**
	 * Mutator.
	 * @param geocentric
	 * @return WGS84 ellipsoid coordinates
	 */
	public void set(Vector3 geocentric){	

        double x= geocentric.getX(); //E
        double y= geocentric.getY(); //F
        double z= geocentric.getZ(); //G   

/*
 *   2.0 compute intermediate values for latitude
 */
        double r= StrictMath.sqrt( x*x + y*y );
        double e = (StrictMath.abs(z) / _a - DIFFERENCE_AXES_RATIOS) / (r / _b);
		double f = (StrictMath.abs(z) / _a + DIFFERENCE_AXES_RATIOS) / (r / _b);
        
/*
 *   3.0 Find solution to:
 *       t^4 + 2*E*t^3 + 2*F*t - 1 = 0
 */
        double p= (FOUR / THREE) * (e*f + ONE);
        double q= TWO * (e*e - f*f);
        
        double d = p*p*p + q*q;
        double v;
        if( d >= ZERO ) {
                v= StrictMath.pow( (StrictMath.sqrt( d ) - q), (ONE / THREE) )
                 - StrictMath.pow( (StrictMath.sqrt( d ) + q), (ONE / THREE) );
        } else {
                v= TWO * StrictMath.sqrt( -p )
                 * StrictMath.cos( StrictMath.acos( q/(p * StrictMath.sqrt( -p )) ) / THREE );
        }
        
/*
 *   4.0 Improve v
 *       NOTE: not really necessary unless point is near pole
 */
        if( v*v < StrictMath.abs(p) ) {
                v= -(v*v*v + TWO*q) / (THREE*p);
        }
        double g = (StrictMath.sqrt( e*e + v ) + e) / TWO;
        double t = StrictMath.sqrt( g*g  + (f - v*g)/(TWO*g - e) ) - g;

/*
 *   5.0 Set B sign to get sign of latitude and height correct
 */
        double B = (z<0)?-_b:_b;
                
        _longitude.put(Angle.inRadians(StrictMath.atan2( y, x )));
        
        _latitude.put(Angle.inRadians(StrictMath.atan( (_a*(ONE - t*t)) / (TWO*B*t) )));
        
//		_theta.put(_latitude).addRight().negate();
        
        _height = (r - _a*t)*_latitude.cos() + (z - B)*_latitude.sin();

	}

//	/** Mutator
//	 * @param Operator and height put to set measure
//	 */
//	public WGS84 put(Operator q_NG, double ellipsoidHeight) {
//		_longitude.put(q_NG.getEuler_k_kji());
//		_theta.put(q_NG.getEuler_j_kji());
//		_latitude.put(Principle.arcTanHalfAngle(_theta.cotHalf()));
//		_height = ellipsoidHeight; // factory dummy load
//		return this;
//	}	
	
//	/** Mutator
//	 * @param Operator and geocentric position put to set measure
//	 */
//	protected WGS84 put(Operator q_NG, Vector3 locationXYZ) {
//	    // By Quaternion axial operator definition and geocentric position-vector
//		_longitude.put(q_NG.getEuler_k_kji());
//		_theta.put( q_NG.getEuler_j_kji());
//		_latitude.put( Principle.arcTanHalfAngle(_theta.cotHalf()));
//		double sin_lat = _latitude.sin();
//		double rTmp = _a / StrictMath.sqrt(ONE - FLATFN * sin_lat * sin_lat);
//		double x = rTmp * _latitude.cos();
//		Vector3 pXYZ = new Vector3(x * _longitude.cos(), x * _longitude.sin(), rTmp * FUNSQ * sin_lat);
//		_height = pXYZ.subtract(locationXYZ).getDot(q_NG.getImage_k());
//		return this;
//	}	
	
	
//	/** Mutator
//	 * @param latitude the WGS84 North latitude to set measure
//	 */
//	protected WGS84 putLatitude(Principle latitude) {
//		this._latitude.put(latitude);
//		
////		this._theta.put( Principle.arcTanHalfAngle(_latitude.negate().cotHalf()));
////		_theta.put(_latitude).addRight().negate();
//		
//		return this;
//	}
	/** Mutator
	 * @param northLatitude
	 */
	public void setNorthLatitude(Angle northLatitude) {
		_latitude.put(northLatitude);
		
	}

	/** Mutator
	 * @param Location East longitude
	 */
	public void setEastLongitude(Angle eastLongitude) {
		_longitude.put(eastLongitude);
	}



	/**Mutator.
	 * @param height the WGS84 ellipsoid height to set
	 */
	public void setEllipsoidHeight(double height) {
		this._height = height;
	}
	
	
	

}
