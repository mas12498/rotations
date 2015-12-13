package tspi.model;
import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Operator;
import rotation.Principle;
import rotation.QuaternionMath;
import rotation.Vector3;

/**
 * Stores geodetic location in WGS84 ellipsoid coordinates: 
 * <br>-- North latitude: (Angle) 
 * <br>-- East longitude: (Angle)
 * <br>-- height: (double) meters above ellipsoid
 *  
 * <p> Includes methods for generating: 
 * <br>-- (Vector3) Cartesian geocentric coordinates {E,F,G}
 * <br>-- (Operator) axial rotator from local navigation {N,E,D} to
 *               geocentric {E,F,G} Cartesian coordinate frame orientation 
 *                          
 * @author mike
 *
 */
public class GeodeticLocation {	
	private static final double ZERO = 0d;
	private static final double ONE = 1d;
	private static final double TWO = 2d;
	private static final double THREE = 3d;
	private static final double ONE_THIRD = ONE/THREE;
	private static final double FOUR_THIRDS = 4d/THREE;
	
	/**	 * WGS 84 oblate ellipsoid flattening	 */
	public static final double _f = 0.00335281068118d;
	/**	 * WGS 84 semimajor axis.	 */
	public static final double _a = 6378137.0d;

	public static final double _b = _a * (ONE - _f);
	private static final double DIFFERENCE_AXES_RATIOS = (_a/_b) - (_b/_a);
    private static final double FLATFN= (TWO - _f)*_f;
    private static final double FUNSQ= (ONE - _f)*(ONE - _f);
    
	/* Reference ellipsoid determined by _f and _a: Hard coded here as WGS84 values: */   
	protected final Angle _latitude;
	protected final Angle _longitude;
	protected double _height;
	
	/**
	 * Copy constructor.
	 * @param referenceEllipsoid
	 */
	public GeodeticLocation(GeodeticLocation referenceEllipsoid){
		_latitude = new Angle(referenceEllipsoid.getNorthLatitude()); 
		_longitude = new Angle(referenceEllipsoid.getEastLongitude()); 
		_height = new Double(referenceEllipsoid.getEllipsoidHeight());
	}
	
	/**
	 * Empty constructor.
	 * @param missingValue
	 */
	public GeodeticLocation(double missingValue){
		_latitude = Angle.inPiRadians(missingValue);
		_longitude = Angle.inPiRadians(missingValue); 
		_height = missingValue;
	}
	
	/**
	 * @return meters above ellipsoid
	 */
	public double getEllipsoidHeight() {
		return _height;
	}

	/** 
	 * @return new <b>Angle</b> of North latitude	 
	 */
	public Angle getNorthLatitude() {
		return _latitude.signedPrincipleAngle();
	}

	/**
	 * @return new <b>Angle</b> of East longitude
	 */
	public Angle getEastLongitude() {
		return _longitude.unsignedPrincipleAngle();
	}

	/**
	 * @param latitude
	 * @param longitude
	 * @param height (meters above reference ellipsoid).
	 */
	public void set(Angle latitude, Angle longitude, double height){
		_latitude.set(latitude); 
		_longitude.set(longitude); 
		_height = new Double(height);
	}
		
	/** 
	 * @param geocentricEFG earth-centered, earth-fixed Cartesian position
	 */
	public void set(Vector3 geocentricEFG){	
	
	    double x= geocentricEFG.getX(); //E
	    double y= geocentricEFG.getY(); //F
	    double z= geocentricEFG.getZ(); //G   
	
	    /* 2.0 compute intermediate values for latitude */
	    double r= StrictMath.sqrt( x*x + y*y );
	    double e = (StrictMath.abs(z) / _a - DIFFERENCE_AXES_RATIOS) / (r / _b);
		double f = (StrictMath.abs(z) / _a + DIFFERENCE_AXES_RATIOS) / (r / _b);
	    
	    /* 3.0 Find solution to: t^4 + 2*E*t^3 + 2*F*t - 1 = 0  */
	    double p= FOUR_THIRDS * (e*f + ONE);
	    double q= TWO * (e*e - f*f);
	    
	    double d = p*p*p + q*q;
	    double v;
	    if( d >= ZERO ) {
	            v= StrictMath.pow( (StrictMath.sqrt( d ) - q), ONE_THIRD )
	             - StrictMath.pow( (StrictMath.sqrt( d ) + q), ONE_THIRD );
	    } else {
	            v= TWO * StrictMath.sqrt( -p )
	             * StrictMath.cos( StrictMath.acos( q/(p * StrictMath.sqrt( -p )) ) / THREE );
	    }
	    
	    /* 4.0 Improve v. NOTE: not really necessary unless point is near pole */
	    if( v*v < StrictMath.abs(p) ) {
	            v= -(v*v*v + TWO*q) / (THREE*p);
	    }
	    double g = (StrictMath.sqrt( e*e + v ) + e) / TWO;
	    double t = StrictMath.sqrt( g*g  + (f - v*g)/(TWO*g - e) ) - g;
	
	    /* 5.0 Set B sign to get sign of latitude and height correct */
	    double B = (z<ZERO)?-_b:_b;
	            
	    _longitude.set(Angle.inRadians(StrictMath.atan2( y, x )));
	    
	    _latitude.set(Angle.inRadians(StrictMath.atan( (_a*(ONE - t*t)) / (TWO*B*t) )));
	    
	    _height = (r - _a*t)*StrictMath.cos(_latitude.getRadians()) + (z - B)*StrictMath.sin(_latitude.getRadians());
	
	}

	/**
	 * Sets geodetic north latitude and geodetic east longitude as extracted from q_NG.
	 * @param q_NG rotator defining local geodetic frame alignment
	 */
	public void setEllipsoidHorizontal(Operator q_NG) { 
		double dump = q_NG.getEuler_i_kji().tanHalf();
		Principle pLat = q_NG.getEuler_j_kji().addRight().negate();
		Principle pLon = q_NG.getEuler_k_kji();
		if (Double.isNaN(dump)) { // Northern hemisphere
			_latitude.set(pLat.negate().signedAngle());
			_longitude.set(pLon.addStraight().negate().unsignedAngle());
		} else if (dump == 0d) { // Southern hemisphere
			_latitude.set(pLat.signedAngle());
			_longitude.set(pLon.unsignedAngle());
		} else {
		System.out.println("Null pointer exception ERROR: Not wgs84 geo-location! -- NOT VALID q_NG Operator.");
		_latitude.set(null);
		_longitude.set(null);
		}
	}
		
	/**
	 * @param meters above WGS reference ellipsoid.
	 */
	public void setEllipsoidHeight(double meters) {
		this._height = meters;
	}

	/** 
	 * @param latitude defined positive in northern hemisphere
	 */
	public void setNorthLatitude(Angle latitude) {
		_latitude.set(latitude);
		
	}

		
		/** Setter:
		 * @param GeodeticLocation East longitude
		 */
		public void setEastLongitude(Angle eastLongitude) {
			_longitude.set(eastLongitude);
		}

			
		/** Setter: 
		 * Clears this GeodeticLocation -- re-initializes.
		 * @param missingValue sentinel
		 */
		public void clear(double missingValue){
			_latitude.set(Angle.inPiRadians(missingValue));
			_longitude.set(Angle.inPiRadians(missingValue)); 
			_height = missingValue;
		}

			/**
	 * Factory: Rotator from local navigation coordinate frame {N,E,D}
	 * to geocentric frame {E,F,G}. 
	 * 
	 * @return Operator quaternion {w,xI,yJ,zK}
	 */
	public Operator axialOperator_NG(){
		Angle theta = new Angle(_latitude).add(Angle.QUARTER_REVOLUTION).negate();
		return QuaternionMath.eulerRotate_kj(_longitude.getPrinciple(),theta.getPrinciple());
	}

	/**
	 * Factory: Cartesian position coordinates of this GeodeticLocation, earth-centered and earth-fixed. 
	 * @return Vector3 geocentric {E,F,G}
	 */
	public Vector3 geocentric(){
		double sin_lat = StrictMath.sin(_latitude.getRadians());
		double rTmp = _a / StrictMath.sqrt(ONE - FLATFN * sin_lat * sin_lat);
		double x = (rTmp + _height) * StrictMath.cos(_latitude.getRadians());
		return new Vector3(
				x * StrictMath.cos(_longitude.getRadians()),
				x * StrictMath.sin(_longitude.getRadians()),
				(rTmp * FUNSQ + _height) * sin_lat);
	}	
	

}