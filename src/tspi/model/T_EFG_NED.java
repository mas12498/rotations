package tspi.model;
import tspi.rotation.Angle;
import tspi.rotation.CodedPhase;
import tspi.rotation.Rotator;
import tspi.rotation.RotatorMath;
import tspi.rotation.Vector3;

/**
 * Stores local geodetic [WGS84] definition:<p>
 * <p> Topocentric coordinate frame (local geodetic)
 * <br>-- (Operator) Geocentric rotator to local orientation of geodetic navigation {N,E,D}
 * <br>-- (double) Helmert height above ellipsoid from which to calculate gravity potentials and elevations 
 *
 * <p> Class includes methods for generating: 
 * <br>-- (Vector3) Cartesian geocentric coordinates {E,F,G}
 * <br>-- (Ellipsoid) geodetic coordinates {latitude,longitude,height}
 *                          
 * @author mike
 *
 */
public class T_EFG_NED {	
	
	//Definition WGS84 ellipsoid.
//	public static final double _a = Ellipsoid._a; //WGS84 semi-major axis radius
//	public static final double _f = Ellipsoid._f; //WGS84 flattening

	//Conversion algorithm constants.
	protected static final double ZERO         = 0d;
	private static final double NEGATIVE_ZERO  = -ZERO;
	
	protected static final double ONE          = 1d;
	private static final double NEGATIVE_ONE   = -ONE;
	
	protected static final double TWO          = 2d;
	protected static final double THREE        = 3d;
	protected static final double ONE_THIRD    = ONE/THREE;
	protected static final double FOUR_THIRDS  = 4d/THREE;
	
	private static final double MIN_RATIO      = ONE - Ellipsoid._f; // = _b/_a 
    protected static final double MIN_RATIO_SQ = (MIN_RATIO)*(MIN_RATIO); // = (_b/_a)*(_b/_a)
    protected static final double DIFF_RATIOS  = (ONE - MIN_RATIO_SQ)/MIN_RATIO; //= (_a/_b) - (_b/_a);	

 //   protected static final double _b = Ellipsoid._b; //WGS84 semi-minor axis radius
    	
	//minimalist geodetic OP holding:
	protected final Rotator _local; //rotater
	protected double _height;  //translater		

	/**
	 * Geodetic local copy-constructor:
	 */
	public T_EFG_NED(Rotator local, double height){
		_height = height;
		_local = local;
	}

	
	/**
	 * Constructor: initialize 'Empty'.
	 */
	public T_EFG_NED(){
		_height = Double.NaN;
		_local = new Rotator(Rotator.EMPTY);
	}	
	
	/**
	 * Geodetic local copy-constructor:
	 */
	public T_EFG_NED(T_EFG_NED wgs84){
		_height = wgs84._height;
		_local = new Rotator(wgs84._local);
	}


	/**
	 * Factory: Rotator defines local Cartesian frame orientation from horizontal geodetic coordinates of local origin. 
	 * @return Rotator local
	 */
	
	public static Rotator local(Angle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		Angle theta = new Angle(northGeodeticLatitude).add(Angle.RIGHT).negate();
		return RotatorMath.eulerRotate_kj(eastGeodeticLongitude.codedPhase(),theta.codedPhase());		
	}


	public static Rotator local(CodedPhase northGeodeticLatitude, CodedPhase eastGeodeticLongitude){
		CodedPhase theta = new CodedPhase(northGeodeticLatitude).addRight().negate();
		return RotatorMath.eulerRotate_kj(eastGeodeticLongitude,theta);		
	}


	/**
		 * Factory: Rotator from local navigation coordinate frame {N,E,D}
		 * to geocentric frame {E,F,G}. 
		 * 
		 * @return Operator quaternion {w,xI,yJ,zK}
		 */
		public Rotator getLocal(){
			return new Rotator(_local);			
		}


	/**
	 * @return meters vertical (Helmert height) above WGS 84 ellipsoid
	 */
	public double getLocalHeight() {
		return _height;
	}


	/**
	 * [Recovery...]
	 * Sets geodetic north latitude and geodetic east longitude as extracted from q_NG.
	 * @param q_NG rotator defining local geodetic frame alignment
	 */
	public Ellipsoid getEllipsoid(){ //Operator q_NG) { 
		Ellipsoid wgs84 = new Ellipsoid();
		wgs84.setGeodtic(_local);
		wgs84.setEllipsoidHeight(_height);
		return wgs84;
	
	}


	public void set(T_EFG_NED frame){
		_local.set(frame.getLocal());
		_height = frame._height;
	}
	
	public void setLocal(Rotator geocentric_to_topocentric){
		_local.set(geocentric_to_topocentric);
	}

	public void setLocalHeight(double heightGeodetic){
		_height = heightGeodetic;
	}

	/**
	 * Setter: by Ellipsoid coordinates.
	 * @param coordinates
	 */
	public void set(Ellipsoid coordinates){
		_height = coordinates._height; //pass thru...
		_local.set(local(coordinates.getNorthLatitude(),coordinates.getEastLongitude()));
	}

	/** 
	 * @param geocentricEFG earth-centered, earth-fixed Cartesian position
	 */
	public void set(Vector3 geocentricEFG){	
		Ellipsoid wgs84 = new Ellipsoid();
		wgs84.setGeocentric(geocentricEFG);
        this.setLocal(wgs84.getGeodetic());
		this.setLocalHeight(wgs84.getEllipsoidHeight());
		return;
	}

	

//	/**
//	 * @param meters above WGS reference ellipsoid.
//	 */
//	public void setEllipsoidHeight(double meters) {
//		this._height = meters;
//	}
//
//	/** 
//	 * @param latitude defined positive in northern hemisphere
//	 */
//	public void setNorthLatitude(Angle latitude) {
//		_latitude.set(latitude);
//		
//	}

		
//		/** Setter:
//		 * @param Location East longitude
//		 */
//		public void setEastLongitude(Angle eastLongitude) {
//			_longitude.set(eastLongitude);
//		}

			
	/**
	 * Factory: Cartesian position coordinates of this GeodeticLocation, earth-centered and earth-fixed. 
	 * @return Vector3 geocentric {E,F,G}
	 */
	public Vector3 getGeocentric(){
		Ellipsoid wgs84 = new Ellipsoid();		
		wgs84.setGeodtic(_local);
		wgs84.setEllipsoidHeight(_height);
		return wgs84.getGeocentric();		
	}

		/** 
		 * Clears this Geodetic location -- re-initializes as empty.
		 */
		public void clear(){			
			_height = Double.NaN;
			_local.set(Rotator.EMPTY);
		}
	
}
