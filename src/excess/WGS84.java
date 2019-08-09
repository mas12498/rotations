/**
 * 
 */
package excess;

import tspi.model.Ellipsoid;
import tspi.rotation.Angle;
import tspi.rotation.CodedPhase;
import tspi.rotation.Rotator;
import tspi.rotation.RotatorMath;
import tspi.rotation.Vector3;

/**
 * @author mike
 *
 */
public class WGS84 {
	
	protected CodedPhase _lambda;
	protected CodedPhase _mu;
	protected double     _height;
	
	//Definition WGS84 ellipsoid.
	public static final double _a = 6378137.0d;        //WGS84 semi-major ellipsoid radius meters
	public static final double _f = 0.00335281068118d; //WGS84 flattening (unitless)

	//Conversion algorithm constants.
	protected static final double ZERO         = 0d;	
	protected static final double ONE          = 1d;
	protected static final double TWO          = 2d;
	protected static final double THREE        = 3d;
	protected static final double ONE_THIRD    = ONE/THREE;
	protected static final double FOUR_THIRDS  = 4d/THREE;
	
	protected static final double DEFLATE      = ONE -  _f;               // = _b/_a 
    protected static final double DEFLATE_SQ   = (DEFLATE)*(DEFLATE); // = (_b/_a)*(_b/_a)
    protected static final double FLAT_FN      = (TWO - _f)*_f;           // = 1-((1-f)^2) == 1 - DEFLATE_SQ

	public void set(WGS84 p){
		_mu.set(p._mu);
		_lambda.set(p._lambda);
		_height = p._height;
	}

	public WGS84(WGS84 p){
		_mu = new CodedPhase(p._mu);
		_lambda = new CodedPhase(p._lambda);
		_height = p._height;
	}
 
	protected WGS84(Angle latitude, Angle longitude, double height){
		_mu = new Angle(latitude).add(Angle.RIGHT).negate().codedPhase();
		_lambda = longitude.codedPhase();
		_height = height;
	}
    	
//	private WGS84(CodedPhase latitude, CodedPhase longitude, double height){
//		_mu = new CodedPhase(latitude).addRight().negate();
//		_lambda = new CodedPhase(longitude);
//		_height = height;
//	}

	protected WGS84(){
		// latitude, longitude, height
		this(Angle.EMPTY, Angle.EMPTY, Double.NaN);
	}
	
	public void set(Angle latitude, Angle longitude, double height){
		_mu = new Angle(latitude).add(Angle.RIGHT).negate().codedPhase();
		_lambda = longitude.codedPhase();
		_height = height;
	}
           
	/** 
	 * @param geocentricEFG earth-centered, earth-fixed Cartesian position
	 */
	public void setGeocentric(Vector3 geocentricEFG){	
		
	    /* 1.0 scale coordinates */
		double x = geocentricEFG.getX() / _a; // E/a
		double y = geocentricEFG.getY() / _a; // F/a
		double z = geocentricEFG.getZ() / _a; // G/a
	
	    /* 2.0 compute intermediate values for latitude */
		double r = StrictMath.hypot(x, y);		
		if(r==0) { //Pole singularity of Vector3 subspace mapping: cannot determine _pLambda
			_mu.set((z < 0) ?  CodedPhase.ZERO  :  CodedPhase.STRAIGHT  );
			_lambda.set(CodedPhase.EMPTY ); //Mark that longitude (_pLambda) is undetermined.
			double h = _a * (z - StrictMath.copySign(DEFLATE,z ));
			_height = StrictMath.signum(z) * h;
//		    System.out.println("\nraw mu: "+_mu.tanHalf());			    	    		
			return;
		}
		double e = (StrictMath.abs(z) * DEFLATE - FLAT_FN) / r;
		double f = (StrictMath.abs(z) * DEFLATE + FLAT_FN) / r;
		
	    /* 3.0 Find solution to: t^4 + 2*E*t^3 + 2*F*t - 1 = 0  */
		double p = FOUR_THIRDS * (e * f + ONE);
		double q = StrictMath.scalb((e * e - f * f),1);
		double d = p * p * p + q * q;
		double v;
		if (d >= ZERO) {
			v = StrictMath.pow((StrictMath.sqrt(d) - q), ONE_THIRD)
					- StrictMath.pow((StrictMath.sqrt(d) + q), ONE_THIRD);
		} else {
			v = StrictMath.scalb(StrictMath.sqrt(-p),1) * StrictMath.cos(StrictMath.acos(q / (p * StrictMath.sqrt(-p))) / THREE);
		}
	    
	    /* 4.0 Improve v. NOTE: not really necessary unless point is near pole */
	    double vv = v*v;
	    if( vv < StrictMath.abs(p) ){
	            v= -(vv*v +  StrictMath.scalb(q,1)) / ( THREE*p);
	    }
	    double g = StrictMath.scalb((StrictMath.sqrt( e*e + v ) + e),-1);
	    double t = StrictMath.sqrt( g*g  + (f - v*g)/( StrictMath.scalb(g,1) - e) ) - g;
	    
	    /* 5.0 Set sign to get latitude and height correct */
	    boolean isSouth =(z< ZERO);
	    double deflation = isSouth?-DEFLATE:DEFLATE;	
	    
	    /*-- Lambda [Longitude] */
	    double cLon = ( x/r );
	    _lambda = ( CodedPhase.encodes(  StrictMath.sqrt((ONE - cLon)/(ONE + cLon))  ) ) ;	 
	    if(y<ZERO) _lambda.negate();  
	    
	    /*-- Mu */
	    double y1 =  ONE - t*t;
	    double x1 = ( StrictMath.scalb(deflation,1) * t);
	    _mu.set( CodedPhase.encodes(  (y1 + StrictMath.hypot(x1, y1))/x1  ).negate() );
	    if(isSouth) _mu.addStraight();	   
	    
	    /*-- Latitude and Height as f(_mu)*/
	    double tanSq = _mu.tanHalf() * _mu.tanHalf();
	    double z_deflation = z - deflation;
	    _height = (z<0) //(StrictMath.abs(_mu.tanHalf())<1)
			?( _a / (ONE + tanSq) )     * ( StrictMath.scalb((t - r), 1) * _mu.tanHalf() + z_deflation * (tanSq - ONE) )
			:( _a / (ONE + ONE/tanSq) ) * ( StrictMath.scalb((t - r), 1) / _mu.tanHalf() + (z_deflation - z_deflation/tanSq) )	
		;		

//	    double secSq = ONE + tanSq;
//	    double	sinLat = -StrictMath.scalb(_mu.tanHalf(), 1) / secSq;
//	    double	cosLat = (tanSq - ONE) / secSq;
//	    _height = _a * ((r - t) * sinLat + (z - deflation) * cosLat);

////
//		double tanMu = (z<0)?_mu.tanHalf() :ONE / _mu.tanHalf() ;
//		double tanMuSq = tanMu*tanMu;
//		_height = (_a / (ONE + tanMuSq)) * (StrictMath.scalb((t - r), 1) * tanMu + (z - deflation) * (ONE - tanMuSq)*StrictMath.signum(z));		
////
			
//		//assume |_mu| < 1: South
//		double _height1=    (_a / (ONE + tanSq))     * (StrictMath.scalb( (t - r), 1)* _mu.tanHalf()  + z_deflation * (tanSq - ONE));
//		//assume |_mu| > 1: North
//		double _height2=    (_a / (ONE + ONE/tanSq)) * (StrictMath.scalb((t - r), 1)/ _mu.tanHalf()  + (z_deflation - z_deflation/tanSq));
//		
//		System.out.println( "\n\nCompare: "+_height+" south "+_height1 +" north "+_height2+" G "+z);
	    
		return;	
	
	}

	/**
	 * Factory: Cartesian position coordinates of this GeodeticLocation, earth-centered and earth-fixed. 
	 * @return Vector3 geocentric {E,F,G}
	 */
	public Vector3 getGeocentric() {
		Vector3 up = this.getGeodetic().getImage_k().unit().negate();
		double radiusInflate = Ellipsoid._a / StrictMath.sqrt(ONE - FLAT_FN * up.getZ() * up.getZ());
		double re = radiusInflate + this.getEllipsoidHeight();
		return new Vector3(re * up.getX(), re * up.getY(),
				(radiusInflate * DEFLATE_SQ + this.getEllipsoidHeight()) * up.getZ());
	}

	/** 
	 * Clears this Geodetic location -- re-initializes as empty.
	 */
	public void clear(){			
		_height = Double.NaN;
		_lambda = CodedPhase.EMPTY;
		_mu = CodedPhase.EMPTY;
	}
	    
	/**
	 * @return the North latitude
	 */
	public Angle getNorthLatitude() {
		return _mu.angle().add(Angle.RIGHT).negate().signedPrinciple(); 
		//return _mu.angle().negate().subtract(Angle.RIGHT).signedPrinciple(); //equivalent...
		//return new CodedPhase(_mu).addRight().negate().angle().signedPrinciple(); //equivalent...
		//return new CodedPhase(_mu).negate().subtractRight().angle().signedPrinciple(); //equivalent...
	}
	
	/**
	 * @param set North latitude.
	 */
	public void setNorthLatitude(Angle latitude) {
		_mu.set(new Angle(latitude).add(Angle.RIGHT).negate().codedPhase());
	}
	
	/**
	 * @return the East longitude
	 */
	public Angle getEastLongitude() {
		return _lambda.angle().unsignedPrinciple();
	}
	
	/**
	 * @param set East longitude
	 */
	public void setEastLongitude(Angle longitude) {
		this._lambda.set(longitude.codedPhase());
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
	    
	/**
	 * @param get Ellipsoid
	 */
	public Ellipsoid getEllipsoid() {
		Ellipsoid e = new Ellipsoid();
		e.setMu(this.getMu());
		e.setLambda(this.getLambda());
		e.setHeight(this.getEllipsoidHeight());
		return e;
	}
	/**
	 * @param set this to Ellipsoid coordinates
	 */
	public void setEllipsoid(Ellipsoid e) {
		this.setMu(e.getMu());
		this.setLambda(e.getLambda());
		this.setEllipsoidHeight(e.getHeight());
	}


	
	
	/**
	 * @return the coded geodetic Mu rotation
	 */
	public CodedPhase getMu() {
		return _mu;
	}

	/**
	 * @param set lambda about K axis
	 */
	public void setMu(CodedPhase mu) {
		this._mu.set(mu);
	}

	/**
	 * @return the coded geodetic Lambda rotation
	 */
	public CodedPhase getLambda() 
	{
		return _lambda;
	}
	
	/**
	 * @param set lambda about K axis
	 */
	public void setLambda(CodedPhase lambda) 
	{
		this._lambda.set(lambda);
	}

	/**
	 * @return the coded geodetic latitude rotation
	 */
	public CodedPhase getTheta() 
	{
		return new CodedPhase(_mu).negate().subtractRight();
	}
	
	public Rotator getGeodetic() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return RotatorMath.eulerRotate_kj(this.getLambda(),this.getMu());		
	}

	public void setGeodtic(Rotator q) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		setLambda(q.getEuler_k_kj());		
		setMu(q.getEuler_j_kj());
	}
		
//	public void setT_EFG_NED(Rotator q, double h) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		setGeodtic(q);
//		setEllipsoidHeight(h);
//	}
//	
//	public T_EFG_NED getT_EFG_NED() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		return new T_EFG_NED(this.getGeodetic(),this.getEllipsoidHeight());
//	}
	
        
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

//    Angle latitude = Angle.inRadians(StrictMath.atan( (_a*( ONE - t*t)) / ( TWO*B*t) ));
//    Angle longitude = Angle.inRadians(StrictMath.atan2( y, x ));
//    _height = (r - _a*t)*StrictMath.cos(latitude.getRadians()) + (z - B)*StrictMath.sin(latitude.getRadians());
////    _pLambda.set(longitude.codedPhase());    
////    _pMu.set(new Angle(latitude).add(Angle.RIGHT).negate().codedPhase());

//	//_localHorizontal 	
//	if (plon.isAcute()) {
//		if (ptheta.isAcute()) { //acute lon, acute theta
//			if (ptheta.isZero()) {
//				_local.set(ONE, ZERO, ZERO, plon.tanHalf());
//				//_localHorizontal.unit();
//				return;
//			}
//			_local.set(ONE, -ptheta.tanHalf() * plon.tanHalf(), ptheta.tanHalf(), plon.tanHalf());
//			//_localHorizontal.unit();
//			return;
//		} //acute lon, obtuse theta
//		if (ptheta.isStraight()) {
//			_local.set(0, -plon.tanHalf(), ONE, ZERO);
//			//_localHorizontal.unit();
//			return;
//		}
//		_local.set(ONE / ptheta.tanHalf(), -plon.tanHalf(), ONE, plon.tanHalf() / ptheta.tanHalf());
//		//_localHorizontal.unit();
//		return;
//	} //obtuse lon, acute theta
//	if (ptheta.isAcute()) {
//		if (ptheta.isZero()) {
//			_local.set(ONE / plon.tanHalf(), ZERO, ZERO, ONE);
//			//_localHorizontal.unit();
//			return;
//		}
//		_local.set(ONE / plon.tanHalf(), -ptheta.tanHalf(), ptheta.tanHalf() / plon.tanHalf(), ONE);
//		//_localHorizontal.unit();
//		return;
//	} //obtuse lon, obtuse theta
//	if (ptheta.isStraight()) {
//		_local.set(ZERO, NEGATIVE_ONE, ONE / plon.tanHalf(), ZERO);
//		//_localHorizontal.unit();
//		return;
//	}
//	double cotHalfTheta = ONE / ptheta.tanHalf();
//	_local.set(cotHalfTheta / plon.tanHalf(), NEGATIVE_ONE, ONE / plon.tanHalf(), cotHalfTheta);
//	//_localHorizontal.unit();

}
