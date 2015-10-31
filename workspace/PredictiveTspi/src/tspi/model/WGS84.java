package tspi.model;


import rotation.Angle;
import rotation.Principle;
import rotation.Vector3;

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
	protected Principle _latitude;
	
	/**	 * WGS 84 geodetic longitude	 */
	protected Principle _longitude;
	
	/**	 * WGS 84 ellipsoid height	 */
	protected double _height;
	
	
	
	
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
	
	/**	 * @return Angle of the WGS84 North latitude	 */
	public Angle getAngleLatitude() {
		return _latitude.signedAngle();
	}
	/**	 * @return the WGS84 North latitude	 */
	public Principle getLatitude() {
		return _latitude;
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

	
	public WGS84 setXYZ(Vector3 EFG){	

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
        _height = (r - _a*t)*_latitude.cos() + (z - B)*_latitude.sin();

 		return this;		
	}
	
	
	



	/**
	 * @param latitude the WGS84 North latitude to set
	 */
	public void setLatitude(Principle latitude) {
		this._latitude = latitude;
	}
	/**
	 * @param latitude the WGS84 North latitude to set
	 */
	public void setAngleLatitude(Angle latitude) {
		this._latitude = new Principle(latitude);
	}




	/**
	 * @param WGS84 East longitude
	 */
	public void setLongitude(Principle longitude) {
		this._longitude = longitude;
	}

	/**
	 * @param WGS84 East longitude
	 */
	public void setAngleLongitude(Angle longitude) {
		this._longitude = new Principle(longitude);
	}



	/**
	 * @param height the WGS84 ellipsoid height to set
	 */
	public void setHeight(double height) {
		this._height = height;
	}
	
	
	

}
