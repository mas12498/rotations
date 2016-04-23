package tspi.model;

import rotation.Angle;
import rotation.Vector3;
/**
 * Transfer Object for passing ellipsoid coordinates.
 * 
 * @author mike
 *
 */
public class Ellipsoid {
	/* Reference ellipsoid coordinates: Defined per WGS84 */   
	protected final Angle _latitude;
	protected final Angle _longitude;
	protected double _height;
	
	protected Ellipsoid(Angle latitude, Angle longitude, double height){
		_latitude = new Angle(latitude);
		_longitude = new Angle (longitude);
		_height = height;
	}

	public Ellipsoid(){
		this(Angle.EMPTY, Angle.EMPTY, Double.NaN);
	}

	public Ellipsoid(Ellipsoid coordinates) {
		this(coordinates._latitude, coordinates._longitude, coordinates._height);
	}

	public void set(Angle latitude, Angle longitude, double height){
		_latitude.set(latitude);
		_longitude.set(longitude);
		_height = height;
	}
	
	public void set(Ellipsoid local){
		_latitude.set(local._latitude);
		_longitude.set(local._longitude);
		_height = local._height;
	}
	

	/**
	 * @return the North latitude
	 */
	public Angle getNorthLatitude() {
		return _latitude;
	}

	/**
	 * @param set North latitude.
	 */
	public void setNorthLatitude(Angle latitude) {
		_latitude.set(latitude);
	}

	/**
	 * @return the East longitude
	 */
	public Angle getEastLongitude() {
		return _longitude;
	}
	
	/**
	 * @param set East longitude
	 */
	public void setEastLongitude(Angle longitude) {
		this._longitude.set(longitude);
	}

	/**
	 * @return the _height
	 */
	public double getEllipsoidHeight() {
		return _height;
	}

	/**
	 * @param _height the _height to set
	 */
	public void setEllipsoidHeight(double height) {
		this._height = height;
	}
	
//	public void set(Angle latitude,Angle longitude, double metersHeight){
//		_latitude.set(latitude);
//		_longitude.set(longitude);
//		_height = metersHeight;
//	}
	
	public Vector3 getGeocentric(){
		double latitudeRadians = this.getNorthLatitude().getRadians();
		double sin_lat = StrictMath.sin(latitudeRadians);
		double rPE = EFG_NED._a / StrictMath.sqrt(EFG_NED.ONE - EFG_NED.FLATFN * sin_lat * sin_lat);
		double x = (rPE + this.getEllipsoidHeight()) * StrictMath.cos(latitudeRadians);
		double longitudeRadians = this.getEastLongitude().getRadians();
		return new Vector3(
				x * StrictMath.cos(longitudeRadians), 
				x * StrictMath.sin(longitudeRadians),
				(rPE * EFG_NED.FUNSQ + this.getEllipsoidHeight()) * sin_lat
		);
	}	
	

	public void setGeocentric(Vector3 geocentricEFG){	
	
	    double x= geocentricEFG.getX(); //E
	    double y= geocentricEFG.getY(); //F
	    double z= geocentricEFG.getZ(); //G   
	
	    /* 2.0 compute intermediate values for latitude */
	    double r= StrictMath.sqrt( x*x + y*y );
	    double e = (StrictMath.abs(z) / EFG_NED._a - EFG_NED.DIFFERENCE_AXES_RATIOS) / (r / EFG_NED._b);
		double f = (StrictMath.abs(z) / EFG_NED._a + EFG_NED.DIFFERENCE_AXES_RATIOS) / (r / EFG_NED._b);
	    
	    /* 3.0 Find solution to: t^4 + 2*E*t^3 + 2*F*t - 1 = 0  */
	    double p= EFG_NED.FOUR_THIRDS * (e*f + EFG_NED.ONE);
	    double q= EFG_NED.TWO * (e*e - f*f);
	    
	    double d = p*p*p + q*q;
	    double v;
	    if( d >= EFG_NED.ZERO ) {
	            v= StrictMath.pow( (StrictMath.sqrt( d ) - q), EFG_NED.ONE_THIRD )
	             - StrictMath.pow( (StrictMath.sqrt( d ) + q), EFG_NED.ONE_THIRD );
	    } else {
	            v= EFG_NED.TWO * StrictMath.sqrt( -p )
	             * StrictMath.cos( StrictMath.acos( q/(p * StrictMath.sqrt( -p )) ) / EFG_NED.THREE );
	    }
	    
	    /* 4.0 Improve v. NOTE: not really necessary unless point is near pole */
	    if( v*v < StrictMath.abs(p) ) {
	            v= -(v*v*v + EFG_NED.TWO*q) / (EFG_NED.THREE*p);
	    }
	    double g = (StrictMath.sqrt( e*e + v ) + e) / EFG_NED.TWO;
	    double t = StrictMath.sqrt( g*g  + (f - v*g)/(EFG_NED.TWO*g - e) ) - g;
	
	    /* 5.0 Set B sign to get sign of latitude and height correct */
	    double B = (z<EFG_NED.ZERO)?-EFG_NED._b:EFG_NED._b;
		    
	    _longitude.setRadians(StrictMath.atan2( y, x ));
	    
	    _latitude.setRadians(StrictMath.atan( (EFG_NED._a*(EFG_NED.ONE - t*t)) / (EFG_NED.TWO*B*t) ));
	    
	    _height = (r - EFG_NED._a*t)*StrictMath.cos(_latitude.getRadians()) + (z - B)*StrictMath.sin(_latitude.getRadians());
	
	}





}
