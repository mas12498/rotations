package tspi.model;


import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.QuaternionMath;
import rotation.Vector3;



/**
 * Stores geodetic location in WGS84 ellipsoid coordinates.
 *  
 * <p> Includes methods for generating:
 *  
 *  	<p>-- Vector3 of location for geocentric coordinates {X,Y,Z} (-- Sometimes documented as {E,F,G}).
 *  
 *  	<p>-- Operator of axial rotation for transformation from local orientation {N,E,D} to
 *               geocentric axis orientation {X,Y,Z}.
 *               
 *               
 * @author mike
 *
 */
public class WGS84 {
	
	
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
	
	/**	 * WGS 84 local latitude rotation to vertical	 */
	protected final Principle _theta;
	
	public WGS84(Principle latitude84, Principle longitude84, double ellipsoidHeight84){
		_latitude = new Principle(latitude84); //copy constructor
		_longitude = new Principle(longitude84); //copy constructor
		_height = ellipsoidHeight84;
		// coded principle theta is function of latitude only: reciprocal!
		_theta = Principle.arcTanHalfAngle(_latitude.cotHalf()); //factory
	}
	
	public WGS84(Angle latitude84, Angle longitude84, double ellipsoidHeight84){
		_latitude = latitude84.getPrinciple();	//factory
		_longitude = longitude84.getPrinciple(); //factory
		_height = ellipsoidHeight84;
		// coded principle theta is function of latitude only: reciprocal!
		_theta = Principle.arcTanHalfAngle(_latitude.cotHalf()); //factory
	}
	
	public WGS84(Vector3 locationXYZ){
		_latitude = new Principle(Principle.ZERO);	//factory dummy load
		_longitude = new Principle(Principle.ZERO); //factory dummy load
		_theta = new Principle(Principle.ZERO); //factory dummy load
		_height = 0; // factory dummy load
		putXYZ(locationXYZ); //adapt load
	}
	

	public WGS84(Operator q_NG, double ellipsoidHeight){
		_longitude = q_NG.getEuler_k_kji();
		_theta = q_NG.getEuler_j_kji();
		_latitude = Principle.arcTanHalfAngle(_theta.cotHalf());
		_height = ellipsoidHeight; // factory dummy load
	}
	

	
	
	
	/**
	 * Generate geocentric Cartesian coordinate: earth-centered earth-fixed 
	 * @param _latitude
	 * @param _longitude
	 * @param _height
	 * @return Vector3 {X,Y,Z}
	 */
	public Vector3 getXYZ(){
		double sin_lat = _latitude.sin();
		double rTmp = _a / StrictMath.sqrt(ONE - FLATFN * sin_lat * sin_lat);
		double x = (rTmp + _height) * _latitude.cos();
		return new Vector3(x * _longitude.cos(), x * _longitude.sin(), (rTmp * FUNSQ + _height) * sin_lat);
	}
	
	/**
	 * Generate quaternion Operator that rotates 
	 * local coordinate frame {N,E,D} to 
	 * geocentric oriented frame {X,Y,Z}. 
	 * 
	 * @return Operator {W,X,Y,Z}
	 */
	public Operator getFromNEDtoEFG(){
		return (Operator) QuaternionMath.exp_k(_longitude).exp_j(_theta);
		//return QuaternionMath.eulerRotate_kj(_longitude,_theta);
	}
	
	/**	 * @return Angle of the WGS84 North latitude	 */
	public Angle getAngleLatitude() {
		return _latitude.signedAngle();
	}
	/**	 * @return the WGS84 North latitude	 */
	public Principle getLatitude() {
		return _latitude;
	}
	
	public Principle getTheta(){
		return _theta; 
	}
	
	/**	 * @return Angle of the WGS84 East longitude	 */
	public Angle getAngleLongitude() {
		return _longitude.unsignedAngle();
	}
	/**	 * @return the WGS84 East longitude	 */
	public Principle getLongitude() {
		return _longitude;
	}
	
	/**
	 * @return the WGS84 ellipsoid height
	 */
	public double getHeight() {
		return _height;
	}

	/**
	 * Mutator.
	 * @param EFG
	 * @return
	 */
	public WGS84 putXYZ(Vector3 EFG){	

        double x= EFG.getX();
        double y= EFG.getY();
        double z= EFG.getZ();       

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
        _theta.put(Principle.arcTanHalfAngle(_latitude.cotHalf()));
        _height = (r - _a*t)*_latitude.cos() + (z - B)*_latitude.sin();

 		return this;		
	}

	/** Mutator
	 * @param latitude the WGS84 North latitude to setmeasure
	 */
	public WGS84 putLatitude(Principle latitude) {
		this._latitude.put(latitude);
		this._theta.put( Principle.arcTanHalfAngle(_latitude.cotHalf()));
		return this;
	}
	/** Mutator
	 * @param latitude the WGS84 North latitude to set
	 */
	public WGS84 putLatitude(Angle latitude) {
		this._latitude.put(latitude);
		this._theta.put( Principle.arcTanHalfAngle(_latitude.cotHalf()));
		return this;
	}

	/** Mutator
	 * @param WGS84 East longitude
	 */
	public WGS84 putLongitude(Principle longitude) {
		this._longitude.put(longitude);
		return this;
	}

	/** Mutator
	 * @param WGS84 East longitude
	 */
	public WGS84 putLongitude(Angle longitude) {
		this._longitude.put(longitude);
		return this;
	}



	/**Mutator.
	 * @param height the WGS84 ellipsoid height to set
	 */
	public WGS84 putHeight(double height) {
		this._height = height;
		return this;
	}
	
	
	

}
