package tspi.model;

import tspi.rotation.Angle;
import tspi.rotation.CodedPhase;
import tspi.rotation.Rotator;
import tspi.rotation.RotatorMath;
import tspi.rotation.Vector3;

/**
 * Transfer Object for ellipsoid coordinates.
 * 
 * @author mike
 *
 */
public class Ellipsoid {
	
	protected double     _height;
	protected CodedPhase _lambda;
	protected CodedPhase _mu;
	//Definition WGS84 ellipsoid.
	public static final double _a = 6378137.0d;        //WGS84 semi-major ellipsoid radius meters
	public static final double _f = 0.00335281068118d; //WGS84 flattening (unitless)

	//Conversion algorithm constants.
	private static final double ZERO         = 0d;	
	private static final double ONE          = 1d;
	private static final double TWO          = 2d;
	private static final double THREE        = 3d;
	private static final double ONE_THIRD    = ONE/THREE;
	private static final double FOUR_THIRDS  = 4d/THREE;
	
	protected static final double DEFLATE      = ONE -  _f;           // = _b/_a 
    protected static final double DEFLATE_SQ   = (DEFLATE)*(DEFLATE); // = (_b/_a)*(_b/_a)
    protected static final double FLAT_FN      = (TWO - _f)*_f;       // = 1-((1-f)^2)     == 1 - DEFLATE_SQ

	//	/**
	//	 * @param location geodetic ellipsoid
	//	 * @return Vector3 geocentric XYZ Cartesian coordinates
	//	 */
	//	public static Vector3 geocentric(Ellipsoid location)
	//	{
	//		return location.getGeocentric();		
	//	}
	
	
		public static Vector3 getGeocentric(Vector3 upDirCos,double ellipsoidHeight) {
			final double ONE = 1;
			double radiusInflate = Ellipsoid._a / StrictMath.sqrt(ONE - Ellipsoid.FLAT_FN * upDirCos.getZ() * upDirCos.getZ());
			double re = radiusInflate + ellipsoidHeight;
			return new Vector3(re * upDirCos.getX(), re * upDirCos.getY(),
					(radiusInflate * Ellipsoid.DEFLATE_SQ + ellipsoidHeight) * upDirCos.getZ());
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
	 * Constructor Ellipsoid empty location
	 */
	public Ellipsoid(){
		// Parm: latitude, longitude, height
		this(Angle.EMPTY, Angle.EMPTY, Double.NaN);
	}

	/**
	 * Constructor Ellipsoid
	 * @param location Ellipsoid
	 */
	public Ellipsoid(Ellipsoid location){
		_mu = new CodedPhase(location._mu);
		_lambda = new CodedPhase(location._lambda);
		_height = location._height;
	}
 
	/**
	 * Constructor Ellipsoid
	 * @param mu CodedPhase
	 * @param lambda CodedPhase
	 * @param ellipsoidHeight meters
	 */
	protected Ellipsoid(CodedPhase lambda, CodedPhase mu, double ellipsoidHeight){
		_lambda = lambda;
		_mu = mu;
		_height = ellipsoidHeight;
	}
	
	/**
	 * Constructor Ellipsoid
	 * @param northGeodeticLatitude Angle
	 * @param eastGeodeticLongitude Angle
	 * @param ellipsoidHeight meters
	 */
	public Ellipsoid(Angle northGeodeticLatitude, Angle eastGeodeticLongitude, double ellipsoidHeight){
		_mu = new Angle(northGeodeticLatitude).add(Angle.RIGHT).negate().codedPhase();
		_lambda = eastGeodeticLongitude.codedPhase();
		_height = ellipsoidHeight;
	}
	
	protected Ellipsoid(Vector3 geocentricEFG) {
//		public void setGeocentric(Vector3 geocentricEFG) {
			//working variable assignment for computing _lambda, _mu, _height...
			double x = geocentricEFG.getX();
			double y = geocentricEFG.getY();
			double z = geocentricEFG.getZ();
			double r = StrictMath.hypot(x, y);
			
			if (r == 0) { // Pole singularity: cannot determine _pLambda from vector
				_mu =  z < ZERO ? CodedPhase.ZERO : CodedPhase.STRAIGHT;
				_lambda = CodedPhase.EMPTY; // Mark that longitude (_pLambda) is undetermined.
				_height = StrictMath.signum(z) * (z - _a * StrictMath.copySign(DEFLATE, z));
				return; //return for pole latitudes
			}

			/* 1.0 Lambda [coded geodetic rotation derived from Longitude] */
			double cLon = (x / r);
			_lambda = (CodedPhase.encodes(StrictMath.copySign(StrictMath.sqrt((ONE - cLon) / (ONE + cLon)),y)));

			/* 2.0 intermediate values for normalized latitude rotations */
			z /= _a;
			r /= _a;
			double e = (StrictMath.abs(z) * DEFLATE - FLAT_FN) / r;
			double f = (StrictMath.abs(z) * DEFLATE + FLAT_FN) / r;

			/* 3.0 Find solution to: t^4 + 2*E*t^3 + 2*F*t - 1 = 0 */
			double p = FOUR_THIRDS * (e * f + ONE);
			double q = StrictMath.scalb((e * e - f * f), 1);
			double d = p * p * p + q * q;
			double v = (d >= ZERO)
					? StrictMath.pow((StrictMath.sqrt(d) - q), ONE_THIRD)
							- StrictMath.pow((StrictMath.sqrt(d) + q), ONE_THIRD)
					: StrictMath.scalb(StrictMath.sqrt(-p), 1)
							* StrictMath.cos(StrictMath.acos(q / (p * StrictMath.sqrt(-p))) / THREE);
			/* Improve v. NOTE: Not too necessary unless point is near pole */
			double vv = v * v; 
			if (vv < StrictMath.abs(p)) {
				v = -(vv * v + StrictMath.scalb(q, 1)) / (THREE * p);
			}
			
			/* 4.0 Mu [derives latitude rotations and height translation] */
			double g = StrictMath.scalb((StrictMath.sqrt(e * e + v) + e), -1);
			double t = StrictMath.sqrt(g * g + (f - v * g) / (StrictMath.scalb(g, 1) - e)) - g;
			double w = t * StrictMath.scalb(DEFLATE, 1);
			double u = ONE - t * t;
			double m = (u + StrictMath.hypot(w, u)) / w;
			double n = StrictMath.scalb(t - r, 1);
			if (z < ZERO) { 
				_mu = CodedPhase.encodes(m).addStraight();
				double t2 = _mu.tanHalf() * _mu.tanHalf();
				_height = _a * ( (n * _mu.tanHalf() + (z + DEFLATE) * (t2 - ONE)) / (ONE + t2) );
				return; //returns for South latitudes
			}		
			_mu = CodedPhase.encodes(-m); 
			double t2 = ONE / ( _mu.tanHalf() * _mu.tanHalf() );
			_height = _a * ( (n / _mu.tanHalf() + (z - DEFLATE) * (ONE - t2)) / (ONE + t2) );
			return; //returns for North latitudes
//		}		
	}
	
	
    	
//	/**
//	 * @param northGeodeticLatitude
//	 * @param eastGeodeticLongitude
//	 * @return Rotator local geodetic coordinate frame from geocentric frame
//	 */
//	public static Rotator geodetic(Angle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		//		return RotatorMath.eulerRotate_kj(eastGeodeticLongitude.codedPhase(),northGeodeticLatitude.codedPhase().addRight().negate());		
//		Angle mu = new Angle(northGeodeticLatitude).add(Angle.RIGHT).negate();		
//		return RotatorMath.eulerRotate_kj(eastGeodeticLongitude.codedPhase(),mu.codedPhase());		
//	}

//	/**
//	 * @param location in Ellipsoid Coordinates
//	 * @return Rotator to local geodetic coordinate frame from geocentric frame
//	 */
//	public static Rotator geodetic(Ellipsoid location)
//	{
//		return location.getGeodetic();		
//	}

//	/**
//	 * @param location geodetic ellipsoid
//	 * @return Vector3 geocentric XYZ Cartesian coordinates
//	 */
//	public static Vector3 geocentric(Ellipsoid location)
//	{
//		return location.getGeocentric();		
//	}


//	/**
//	 * @param geocentric XYZ Cartesian coordinates
//	 * @return Ellipsoid geodetic location
//	 */
//	public static Ellipsoid ellipsoid(Vector3 geocentric)
//	{
////		Ellipsoid e = new Ellipsoid();
////		e.setGeocentric(geocentric);
////		return e;
//		return new Ellipsoid(geocentric);
//	}
	

//	/**
//	 * Copy Constructor this Ellipsoid.
//	 */
//	public Ellipsoid copy() {
//		return new Ellipsoid(this); //.getNorthLatitude(),this.getEastLongitude(),this.getEllipsoidHeight());
//	}
	
	/**
	 * @param set this to Ellipsoid coordinates
	 */
	public void set(Ellipsoid p){
		_mu.set(p._mu);
		_lambda.set(p._lambda);
		_height = p._height;
	}

	public void set(Angle latitude, Angle longitude, double height){
		_mu = new Angle(latitude).add(Angle.RIGHT).negate().codedPhase();
		_lambda = longitude.codedPhase();
		_height = height;
	}

	/**
	 * @param _height the _height to set
	 */
	public void setHeight(double height) {
		this._height = height;
	}

	/**
	 * @param set East longitude
	 */
	public void setEastLongitude(Angle longitude) {
		this._lambda.set(longitude.codedPhase());
	}

	/**
	 * @param set North latitude.
	 */
	public void setNorthLatitude(Angle latitude) {
		_mu.set(new Angle(latitude).add(Angle.RIGHT).negate().codedPhase());
	}

	/** 
	 * @param geocentricEFG earth-centered, earth-fixed Cartesian position
	 */
	public void setGeocentric(Vector3 geocentricEFG) {
		set(new Ellipsoid(geocentricEFG));
	}

	public void setGeodticRotator(Rotator q) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		setLambda(q.getEuler_k_kj());		
		setMu(q.getEuler_j_kj());
	}

	/**
	 * @param set lambda about K axis
	 */
	protected void setMu(CodedPhase mu) {
		this._mu.set(mu);
	}

	/**
	 * @return the coded geodetic Mu: ((-theta-90) degrees rotation
	 */
	protected CodedPhase getMu() {
		return _mu;
	}

	/**
	 * @param set lambda about K axis
	 */
	protected void setLambda(CodedPhase lambda) 
	{
		this._lambda.set(lambda);
	}

	/**
	 * @return the coded geodetic Lambda rotation
	 */
	protected CodedPhase getLambda() 
	{
		return _lambda;
	}

	/**
	 * @param set North latitude.
	 */
	protected void setTheta(CodedPhase theta) {
		_mu = new CodedPhase(theta).addRight().negate();
	}

	/**
	 * @return the coded geodetic latitude rotation
	 */
	protected CodedPhase getTheta() 
	{
		//return new CodedPhase(_mu).negate().subtractRight();
		return new CodedPhase(_mu).addRight().negate(); //equivalent...
		//return _mu.angle().add(Angle.RIGHT).negate().codedPhase(); 
	}

	/**
	 * @return the _height
	 */
	public double getHeight() {
		return _height;
	}

	/**
	 * @return the North latitude
	 */
	public Angle getNorthLatitude() {
		//return _mu.angle().add(Angle.RIGHT).negate().signedPrinciple(); 
		//return _mu.angle().negate().subtract(Angle.RIGHT).signedPrinciple(); //equivalent...
				
		return new CodedPhase(_mu).addRight().negate().angle(); //fast equivalent...less precision?
		//return new CodedPhase(_mu).negate().subtractRight().angle(); //fast equivalent...less precision?
	}

	/**
	 * @return the East longitude
	 */
	public Angle getEastLongitude() {
		return _lambda.angle().unsignedPrinciple();
	}

	/**
	 * Factory: Cartesian position coordinates of this GeodeticLocation, earth-centered and earth-fixed. 
	 * @return Vector3 geocentric {E,F,G}
	 */
	public Vector3 getGeocentric() {
		return getGeocentric(this.getGeodeticRotator().getImage_k(-this.magnificationGeodeticRotator()), this.getHeight());
	}

	protected double magnificationGeodeticRotator() {
		return  this._mu.magnificationRotator() * this._lambda.magnificationRotator();
	}

	public Rotator getGeodeticRotator() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{	
		return RotatorMath.eulerRotate_kj(this.getLambda(),this.getMu());		
	}

	/**
	 * Undo local geodetic rotation to navigation frame:
	 * Transformation of global geocentric {XYZ} rotator to local navigation {ned} rotator.
	 * @return <b>local</b> Rotator in topocentric navigation frame
	 */
	public Rotator preGeodeticRotate(Rotator global) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{	
		return new Rotator(global).preRotate_j(this.getMu()).preRotate_k(this.getLambda());		
	}
	
	/**
	 * Do local geodetic (topocentric) rotation:
	 * Transformation of local navigation {ned} rotator to global geocentric {XYZ} rotator:
	 * @return <b>global</b> Rotator in geocentric frame
	 */
	public Rotator geodeticRotate(Rotator local) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{	
		return new Rotator(local).rotate_k(this.getLambda()).rotate_j(this.getMu());		
	}
	
//	/**
//	 * Transforms input rotator from local navigation {ned} to global geocentric {XYZ} frame:
//	 * @param local Rotator in navigation frame
//	 * @return <b>global</b> Rotator in geocentric frame
//	 */
//	public Rotator rotate(Quaternion local){
//		return (Rotator) this._geodeticLocalVersor.rotate(local);
//	}
	
//	/**
//	 * Transform geocentric rotator to local navigation frame rotator:
//	 */
//	public Rotator preRotate(Quaternion global){
//		return (Rotator) this._geodeticLocalVersor.preRotate(global);
//	}
	
	

//	public Vector3 getUpDir() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		//return this.getGeodetic().getImage_k().unit().negate();		
//		//return this.getGeodetic().getImage_k().divide(-this.magnificationRotator());		
//		return this.getGeodetic().getImage_k(-this.magnificationGeodeticRotator());		
//	}
	
//	public Vector3 getNorthDir() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		//return this.getGeodetic().getImage_i().unit();		
//		//return this.getGeodetic().getImage_i().divide(this.magnificationRotator());		
//		return this.getGeodetic().getImage_i(this.magnificationGeodeticRotator());		
//	}
	
//	public Vector3 getEastDir() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{
//		//return this.getGeodetic().getImage_j().unit();
//		//return this.getGeodetic().getImage_j().divide(this.magnificationRotator());
//		return this.getGeodetic().getImage_j(this.magnificationGeodeticRotator());
//	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Rotator q = new Rotator(1,0,0,0);
		System.out.println(new Rotator(q).rotate_i(Angle.inDegrees(60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_j(Angle.inDegrees(60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_i(Angle.inDegrees(-60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_j(Angle.inDegrees(-60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(-60).codedPhase()).unit().toString(10));
		System.out.println(" ");
		System.out.println(new Rotator(q).rotate_i(Angle.inDegrees(120).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_j(Angle.inDegrees(120).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(120).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_i(Angle.inDegrees(-120).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_j(Angle.inDegrees(-120).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(-120).codedPhase()).unit().toString(10));
		
		System.out.println(" ");
		
		System.out.println(" ");
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(120).codedPhase()).unit().toString(10));
//		System.out.println(Angle.inDegrees(180).codedPhase().cot()+" "+new Rotator(q).rotate_k(Angle.inDegrees(180).codedPhase()).unit().toString(10));
		
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(-60).codedPhase()).unit().toString(10));
		System.out.println(new Rotator(q).rotate_k(Angle.inDegrees(-120).codedPhase()).unit().toString(10));
//		System.out.println(Angle.inDegrees(-180).codedPhase().cot()+" "+new Rotator(q).rotate_k(Angle.inDegrees(-180).codedPhase()).unit().toString(10));
			
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
	
	
//	/* Reference ellipsoid coordinates: Defined per Ellipsoid */  
//	public static final double _a = 6378137.0d; //Ellipsoid semi-major ellipsoid radius meters
//	public static final double _f = 0.00335281068118d; //Ellipsoid flattening (unitless)
//    public static final double _b = _a * (1 - _f); //Ellipsoid semi-minor axis radius
//    protected static final double FLAT_FN = (2 - _f)*_f; //== 1-((1-f)^2) == 1 - RATIO_SQ
//
//	protected final Angle _latitude;
//	protected final Angle _longitude;
//	protected double _height;
//	
//	protected Ellipsoid(Angle latitude, Angle longitude, double height){
//		_latitude = new Angle(latitude);
//		_longitude = new Angle (longitude);
//		_height = height;
//	}
//
//	public Ellipsoid(){
//		this(Angle.EMPTY, Angle.EMPTY, Double.NaN);
//	}
//
//	public Ellipsoid(Ellipsoid coordinates) {
//		this(coordinates._latitude, coordinates._longitude, coordinates._height);
//	}
//
//	public void set(Angle latitude, Angle longitude, double height){
//		_latitude.set(latitude);
//		_longitude.set(longitude);
//		_height = height;
//	}
//	
//	public void set(Ellipsoid local){
//		_latitude.set(local._latitude);
//		_longitude.set(local._longitude);
//		_height = local._height;
//	}
//	
//	/**
//	 * @return the North latitude
//	 */
//	public Angle getNorthLatitude() {
//		return _latitude;
//	}
//
//	/**
//	 * @param set North latitude.
//	 */
//	public void setNorthLatitude(Angle latitude) {
//		_latitude.set(latitude);
//	}
//
//	/**
//	 * @return the East longitude
//	 */
//	public Angle getEastLongitude() {
//		return _longitude;
//	}
//	
//	/**
//	 * @param set East longitude
//	 */
//	public void setEastLongitude(Angle longitude) {
//		this._longitude.set(longitude);
//	}
//
//	/**
//	 * @return the _height
//	 */
//	public double getEllipsoidHeight() {
//		return _height;
//	}
//
//	/**
//	 * @param _height the _height to set
//	 */
//	public void setEllipsoidHeight(double height) {
//		this._height = height;
//	}
//	
////	public void set(Angle latitude,Angle longitude, double metersHeight){
////		_latitude.set(latitude);
////		_longitude.set(longitude);
////		_height = metersHeight;
////	}
//	
//	public Vector3 getGeocentric(){
//		double ellipsoidLatitudeRadians = this.getNorthLatitude().getRadians();
//		double sinEllipsoidLatitude = StrictMath.sin(ellipsoidLatitudeRadians);
//		double radiusInflatedEllipsoid = Ellipsoid._a 
//				/ StrictMath.sqrt(T_EFG_NED.ONE - FLAT_FN * sinEllipsoidLatitude * sinEllipsoidLatitude);
//		double rCosEllipsoidLatitiude = (radiusInflatedEllipsoid + this.getEllipsoidHeight())
//				* StrictMath.cos(ellipsoidLatitudeRadians);
//		double ellipsoidLongitudeRadians = this.getEastLongitude().getRadians();
//		return new Vector3( //Geocentric EFG
//				rCosEllipsoidLatitiude * StrictMath.cos(ellipsoidLongitudeRadians),
//				rCosEllipsoidLatitiude * StrictMath.sin(ellipsoidLongitudeRadians),
//				sinEllipsoidLatitude * (radiusInflatedEllipsoid * T_EFG_NED.MIN_RATIO_SQ + this.getEllipsoidHeight()));
//	}	
//
//	public void setGeocentric(Vector3 geocentricEFG){	
//	
//	    double x= geocentricEFG.getX(); //E
//	    double y= geocentricEFG.getY(); //F
//	    double z= geocentricEFG.getZ(); //G   
//	
//	    /* 2.0 compute intermediate values for latitude */
//		double r = StrictMath.hypot(x, y);
//		double s = r / _b;
//		double e = (StrictMath.abs(z) / _a - T_EFG_NED.DIFF_RATIOS) / s;
//		double f = (StrictMath.abs(z) / _a + T_EFG_NED.DIFF_RATIOS) / s;
//	    
//	    /* 3.0 Find solution to: t^4 + 2*E*t^3 + 2*F*t - 1 = 0  */
//	    double p= T_EFG_NED.FOUR_THIRDS * (e*f + T_EFG_NED.ONE);
//	    double q= T_EFG_NED.TWO * (e*e - f*f);
//	    
//	    double d = p*p*p + q*q;
//	    double v;
//	    if( d >= T_EFG_NED.ZERO ) {
//	            v= StrictMath.pow( (StrictMath.sqrt( d ) - q), T_EFG_NED.ONE_THIRD )
//	             - StrictMath.pow( (StrictMath.sqrt( d ) + q), T_EFG_NED.ONE_THIRD );
//	    } else {
//	            v= T_EFG_NED.TWO * StrictMath.sqrt( -p )
//	             * StrictMath.cos( StrictMath.acos( q/(p * StrictMath.sqrt( -p )) ) / T_EFG_NED.THREE );
//	    }
//	    
//	    /* 4.0 Improve v. NOTE: not really necessary unless point is near pole */
//	    if( v*v < StrictMath.abs(p) ) {
//	            v= -(v*v*v + T_EFG_NED.TWO*q) / (T_EFG_NED.THREE*p);
//	    }
//	    double g = (StrictMath.sqrt( e*e + v ) + e) / T_EFG_NED.TWO;
//	    double t = StrictMath.sqrt( g*g  + (f - v*g)/(T_EFG_NED.TWO*g - e) ) - g;
//	
//	    /* 5.0 Set B sign to get sign of latitude and height correct */
//	    double B = (z<T_EFG_NED.ZERO)?-_b:_b;
//		    
//	    _longitude.setRadians(StrictMath.atan2( y, x ));
//	    
//	    _latitude.setRadians(StrictMath.atan( (_a*(T_EFG_NED.ONE - t*t)) / (T_EFG_NED.TWO*B*t) ));
//	    
//	    _height = (r - _a*t)*StrictMath.cos(_latitude.getRadians()) + (z - B)*StrictMath.sin(_latitude.getRadians());
//	
//	}

//	public Rotator getGeodeticImage(Rotator operator) {
//	return new Rotator(operator).preRotate_j(this.getMu()).preRotate_k(this.getLambda());
//}

//public static Rotator geodetic(CodedPhase northGeodeticLatitude, CodedPhase eastGeodeticLongitude){
//	CodedPhase theta = new CodedPhase(northGeodeticLatitude).addRight().negate();
//	return RotatorMath.eulerRotate_kj(eastGeodeticLongitude,theta);		
//}

//private Ellipsoid(CodedPhase latitude, CodedPhase longitude, double height){
//	_mu = new CodedPhase(latitude).addRight().negate();
//	_lambda = new CodedPhase(longitude);
//	_height = height;
//}


}
