/**
 * 
 */
package rotation;

/**
 * Class for encoding principle arguments of angle rotators. 
 * 
 * Use to speed computations: 
 * 		minimize storage of intermediate computations.
 * 		minimize unnecessary trigonometric conversions.
 * 
 * Companion to the Angle adapter class in the rotation package.
 * 
 * <p>Principle arguments span rotation up to a single revolution: 
 * <p>  -- case (Signed)S: [-1/2 .. 1/2]
 * <p>  -- case Unsigned: [   0 .. 1  ]
 *   
 * <p>As privately encoded <i>trigonometric</i> representation:
 * <p>  -- unit-less: No preferred units to interface.
 * <p>  -- spans Doubles from [-Inf .. Inf]
 * <p>  -- provides publicly decoded Tan,Sin,Cos,TanHalf methods without additional resort to math trig library funcitons.
 *  
 * @author mike
 *
 */
public class Principle {
	
	static final public Principle ZERO = new Principle(0.0d);
	static final public Principle RIGHT = new Principle(1.0d);
	static final public Principle STRAIGHT = new Principle(Double.POSITIVE_INFINITY);
	static final public Principle EMPTY = new Principle(Double.NaN);
	
	private double _ta;

	/** Principle double constructor -- by externally encoded angle representations. */
	protected Principle(double encoded) {   _ta = encoded; }	
	
	/** Principle Angle constructor */
	public Principle(Angle a){ _ta = (double) a.getCodedPrinciple(); }

	/** Principle Copy constructor */
	public Principle(Principle copy) { _ta = copy._ta; }
	

	
	/** 
	 * return double representing signed Principle angle in PI radians: 
	 *   (Reserved for rotation package internal use)
	 * */
	public double getPiRadians() { 
		if(_ta==0){
			return StrictMath.copySign(0,_ta);
		}
		if(Double.isInfinite(_ta)){ //tan +/-90 degrees... makes straight angle
			return StrictMath.copySign(1d,_ta);			
		}
		if(Double.isNaN(_ta)){
			return _ta;
		}
		return StrictMath.atan(_ta)/Angle.PI_2; }

	/** 
	 * return double representing unsigned Principle angle in pi radians: 
	 *   (Reserved for rotation package internal use)
	 * */
	public double getUnsignedPiRadians()   {   
		return isPositive() ? StrictMath.atan(_ta)/Angle.PI_2
				            : StrictMath.atan(_ta)/Angle.PI_2 + 2d; 
	}
	
	
	/** 
	 * Factory. 
	 * 
	 * @param tanHalfAngle
	 * @return Principle Angle as-is encoded.
	 */
	public static final Principle arcTanHalfAngle(double tanHalfAngle) { return new Principle(tanHalfAngle); }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(_ta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Principle)) {
			return false;
		}
		Principle other = (Principle) obj;
		if (Double.doubleToLongBits(_ta) != Double.doubleToLongBits(other._ta)) {
			return false;
		}
		return true;
	}

//	/** Mutator -- Set Principle equal to right angle. */
//	public Principle setRight(){ return this.put(Principle.RIGHT); }
	
//	/** Mutator -- Set Principle equal to straight angle. */
//    public Principle setStraight() { return this.put(Principle.STRAIGHT);}

//    /** Mutator -- Set Principle equal to zero angle. */
//    public Principle setZero() { return this.put(Principle.ZERO); }
	
    /** Setter to Principle angle of Angle measure. */
	public void set(Angle measure) { 
		_ta = measure.getCodedPrinciple();
		return;
	}

    /** Setter to another Principle angle's measure. */
	public void set(Principle measure){
		_ta = measure._ta; 
		return;
	}

	/** Factory. Get <i>signed</i> Angle.
	 *  @return Angle */
	public Angle signedAngle(){return Angle.inPiRadians(this.getPiRadians());}
	
	
	
	/** Factory Get <i>unsigned</i> Angle. 
	 * @return Angle */
	public Angle unsignedAngle(){return Angle.inPiRadians(this.getUnsignedPiRadians());}
	
	
	

	/** Tangent of half of Principle Angle.
	 * @return double 	 */
	public double tanHalf(){ return _ta; }
	
	/** Cotangent of half of Principle Angle.
	 * @return double 	 */
	public double cotHalf(){ 
		if(_ta==0){
			return StrictMath.copySign(Double.POSITIVE_INFINITY,_ta);
		}
		if(isRight()){
			return _ta;
		}
		if(Double.isInfinite(_ta)){
			return StrictMath.copySign(0d,_ta);			
		}
		return 1/_ta; 
		
	}
	
	/** Magnification of fast rotation operation.
	 * @return double 	 */
	public double fastRotMagnification(){ 
		double t = (_ta < 1d) ? _ta : 1d/_ta;
	    return 1d+t*t;
	}
	
//	/** ...
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double sinHalf(){ return this.isAcute() ? ta/StrictMath.sqrt(1+ta*ta) : 1/StrictMath.sqrt(1/ta/ta+1); }
///** ...
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double cosHalf(){ return this.isAcute() ? 1/StrictMath.sqrt(1+ta*ta)  : 1/ta/StrictMath.sqrt(1/ta/ta+1); }
	
//	/** 
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double sinHalf()
//	{ return (isObtuse()) ? ta/StrictMath.hypot(1d,ta*ta) : 1/StrictMath.hypot(1d/ta,ta); }
//
//	/** 
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double cosHalf()//{ return 1/StrictMath.hypot(1d,ta*ta); }
//	{ double tt = ta*ta;
//	  return (isObtuse()) ? 1d/(tt*StrictMath.hypot(1d,1/(tt))) :1d/StrictMath.hypot(1d,tt) ; }

//	/** 
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double secHalf(){ return StrictMath.hypot(1d,ta*ta); }

//	/** test if better...
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double exp(){ 
//		double t = (isObtuse()) ? 1/ta : ta;
//		return (1+t)/(1-t) ;	
//	}
//	
//	/** test if better...
//	 * Method avoids calls to trigonometric functions.
//	 */
//	public double negexp(){ 
//		double t = (isObtuse()) ? 1/ta : ta;
//		return (1-t)/(1+t) ;	
//	}
	
	/** Tangent of Principle Angle.
	 * @return double  */
	public double tan()
	{ 		
     //	return 2/( 1/ta - ta ); 
		
		if (isZero()) { //not observed
			//System.out.print("Woweeeeee");
			return StrictMath.copySign(0,_ta); 
		} 	
		if(isRight()){ //handle right angles!
			return StrictMath.copySign(Double.POSITIVE_INFINITY,_ta);
		}
		if (isStraight()){//Not observed
		//	System.out.print("Woweeeeee");
			return StrictMath.copySign(0,_ta);			
		}
		//Everywhere...
		//System.out.print("Woweeeeee");		
		double ma = (StrictMath.abs(_ta) < 1d) ? _ta : -1d/_ta; 
		return  ma/( 0.5d - StrictMath.scalb(ma,-1)*ma );		 
	}

	/** CoTangent of Principle Angle.
	 * @return double  */
	public double cot()
	{ 	
		if(isZero()){//not observed
		//	System.out.print("Woweeeeee");
			return StrictMath.copySign(Double.POSITIVE_INFINITY,_ta);
		}
		if(isRight()){//at poles
		//	System.out.print("WoweeeeeePole");
			return StrictMath.copySign(0d,_ta);
		}
		if(isStraight()){//
		//	System.out.print("WoweeeeeePole");
			return StrictMath.copySign(0d,_ta);			
		}//everywhere... seems to work ?
		//	System.out.print("WoweeeeeePole:");
		return 1.0d/tan();
	}

	/** Sine of Principle Angle.
	 * @return double  */
	public double sin()
	{
     //	return 2d/( 1/ta + ta ); 		
		if (isZero()) {	return 0d; } //fast zero return...	
		double ma = (StrictMath.abs(_ta) < 1d) ? _ta : 1d/_ta; 
		return ma/(StrictMath.scalb(ma,-1)*ma + 0.5d);
	}
	

	/** Cosine of Principle Angle.
	 * @return double  */
	public double cos()
	{ 		
		if (isZero()) {	return 1d; } //fast zero return...					
		double m2 = _ta*_ta;			
		return ( m2 < 1d ) 
			? (1d - m2)/(1d + m2)
			: (1d/m2 - 1) /(1d/m2 + 1d);		
//		return  (1d - m2)/(1d + m2);
//		return  (1d/m2 - 1) /(1d/m2 + 1d);		
	}

	//mutators	
	
	/** Mutator -- negate: 
	 * Changes <i>sign</i>.  */
	public Principle negate(){ 
		_ta = -_ta; return this;
	}

	/** Mutator -- abs: 
	 * Makes <i>positive</i>. */
	public Principle abs(){ 
		_ta = StrictMath.abs(_ta); return this;
	}

	/** Mutator -- sum.
	 * @param addend Principle */
	public Principle add(Principle addend)
	{ 				
		_ta = (isAcute())
		? (addend.isAcute()) 
				? (_ta + addend._ta)/(1d - _ta*addend._ta)		     // (tx   + ty)/(1  - txty)			
				: (_ta/addend._ta + 1d)/(1d/addend._ta - _ta)		 // (txcy +  1)/(cy - tx   )
		: (addend.isAcute()) 
				? (1d + addend._ta/_ta)/(1d/_ta - addend._ta)		 // (1  + tycx)/(cx    - ty)
				: (1d/addend._ta + 1d/_ta)/(1d/_ta/addend._ta - 1d); // (cy +   cx)/(cxcy - 1 )					
//		ta = (ta + addend.ta)/(1 - ta*addend.ta); 		
		return this; 
		
	}
	
	public Principle addStraight(){
//		Double negZero= new Double(-0);
//		if((negZero.equals(_ta))){
//			_ta=Double.NEGATIVE_INFINITY;
//			return this;
//		}
//		if(_ta==0){
//			_ta=Double.POSITIVE_INFINITY;
//			return this;
//		}
//		if(_ta==Double.POSITIVE_INFINITY){
//			_ta=0; //Double.NEGATIVE_INFINITY;
//			return this;
//		}
//		if(_ta==Double.NEGATIVE_INFINITY){
//			_ta=0; //Double.POSITIVE_INFINITY;
//			return this;
//		}
		_ta=1/_ta;
		return this;
	}
	
	
	
	/** Mutator -- sum right angle.
	 */
	public Principle addRight() //addend == RightAngle [90 degrees]: addend.ta = 1
	{ 	//problem when dealing with principle of +/-45 degrees...denominators goto infinitie or zeros!
		
//		if(isRight()){//needed for sign of zero...
////			if(_ta>0){//not observed
////				System.out.print("Wahhhhoooo111");
////				_ta=Double.POSITIVE_INFINITY;
////				return this;
////			}
//			//needed for equator...Something odd.
//			//System.out.print("Wahhhhoooo22222");
//			_ta = 0;
//			return this;
//		}		
//		if (isStraight()) { //not observed
//		//	System.out.print("WahhhhooooSSSSSINFINY");
//			_ta = -1d;
//			return this;
//		}	
//		if (isZero()) { //On all of zero latitudes...become rightangled!!!
//			//System.out.print("Wahhhh000");
//			_ta = 1d;
//			return this;
//		}	
		//assigns to ta...blows up as approach 1.
		//System.out.print("Wahhhhoooo");		
		_ta = (isAcute())
		? (_ta + 1d)/(1d - _ta)	
		: (1d + 1d/_ta)/(1d/_ta - 1d); 
		return this; 
	}
	
	/** Mutator -- sum right angle.
	 */
	public Principle subtractRight() //addend == RightAngle [90 degrees]: addend.ta = 1
	{ 	//problem when dealing with 45 degrees...denominators goto zero!
		
		if(isRight()){//needed for sign of zero...
			if(_ta>0){//not observed
				System.out.print("Wahhhhoooo111");
				_ta=0; //
				return this;
			}
			//needed for equator...Something odd.
			//System.out.print("Wahhhhoooo22222");
			_ta=Double.NEGATIVE_INFINITY;
			return this;
		}		
		if (isStraight()) { //not observed
		//	System.out.print("WahhhhooooSSSSSINFINY");
			_ta = 1d;
			return this;
		}	
		if (isZero()) { //On all of zero latitudes...become rightangled!!!
			//System.out.print("Wahhhh000");
			_ta = -1d;
			return this;
		}	
		//assigns to ta...blows up as approach 1.
		//System.out.print("Wahhhhoooo");		
		_ta = (isAcute())
		? (_ta - 1d)/(1d + _ta)	
		: (1d - 1d/_ta)/(1d/_ta + 1d); 
		return this; 
	}
	

	/** Mutator -- difference.
	 * @param subtrahend Principle */
	public Principle subtract(Principle subtrahend)
	{  

		_ta = (isAcute())
		? (subtrahend.isAcute()) 
				? (_ta - subtrahend._ta)/(1d + _ta*subtrahend._ta)		     // (tx   + ty)/(1  - txty)			
				: (_ta/subtrahend._ta - 1d)/(1d/subtrahend._ta + _ta)		 // (txcy +  1)/(cy - tx   )
		: (subtrahend.isAcute()) 
				? (1d - subtrahend._ta/_ta)/(1d/_ta + subtrahend._ta)		 // (1  + tycx)/(cx    - ty)
				: (1d/subtrahend._ta - 1d/_ta)/(1d/_ta/subtrahend._ta + 1d); // (cy +   cx)/(cxcy - 1 )	
		//	ta = (ta - subtrahend.ta)/(1 + ta*subtrahend.ta); 		
		return this; 
	}

	/** Mutator -- scalar product.
	 * @param scalarFactor double */
	public Principle multiply(double scalarFactor)
	{   //this is not clear...need to check this out.
		_ta = StrictMath.tan( scalarFactor * StrictMath.atan(_ta) );
		return this;
	}

	/** True if Principle exactly equals zero angle. */
	public boolean isEmpty() { return (_ta==Double.NaN); } //Math.abs(t)<2*Epsilon;

	/** True if Principle exactly equals zero angle. */
	public boolean isZero() { return (_ta==0.0); } //Math.abs(t)<2*Epsilon;

	/** True if Principle angle is positive or zero. */
	public boolean isPositive() { return (_ta>=0.0d); } //t>= Epsilon;

	/** True if <i>absolute</i> Principle angle is acute. */
	public boolean isAcute() {	return (StrictMath.abs(_ta)<1.0d); } //t>= Epsilon;

	/** True if Principle is right angle. */
	public boolean isRight() {// TODO: put in tolerance...
		return ( StrictMath.abs(_ta)==1.0d ); 
	} //Math.abs(t)<2*Epsilon;

	/** True if <i>absolute</i> Principle angle is obtuse. */
	public boolean isObtuse() {	return (StrictMath.abs(_ta)>1.0d); } //t>= Epsilon;

	/** True if Principle is straight angle. */
	public boolean isStraight() { 
//		return (StrictMath.abs(_ta)==Double.POSITIVE_INFINITY);
		return (_ta==Double.NEGATIVE_INFINITY)||(_ta==Double.POSITIVE_INFINITY);
		} //Math.abs(t)<2*Epsilon;

	/** True if Principle is equal to principle angle measure by Tolerance. 
	 * @param measure Principle
	 * @param byTolerance Principle */
	public boolean isEqualTo(Principle measure, Principle byTolerance) 
	{ 
		return (new Principle(this).subtract(measure).abs()._ta <= StrictMath.abs(byTolerance._ta));
	} 

	public boolean isLesserAbs(Principle measure) 
	{ 
		return (StrictMath.abs(this._ta)<StrictMath.abs(measure._ta));
	} 
	
	public boolean isGreaterAbs(Principle measure) 
	{ 
		return (StrictMath.abs(this._ta)>StrictMath.abs(measure._ta));
	} 


	
//	public boolean isEqual(Principle reference)
//	{ 
//		//return ( this.tanHalf() == reference.tanHalf() );
//		return ( this.ta == reference.ta );
//	}
	
	
//	public boolean isEquivalent(Angle reference, Principle diffTolerance) 
//	{ 
//		return (new Principle(reference).subtract(this).abs().ta <= StrictMath.abs(diffTolerance.ta));
//	} //Math.abs(t)<2*Epsilon;
	
//	//StrictMath.scalb(ta/(1+ta*ta),1) ;			
//	return (isObtuse())
//	? 2/(ta+1/ta) 
//	: StrictMath.scalb(ta/(1+ta*ta),1) ;	

//  //		return (isObtuse())
//	? (1/ta2-1)/(1/ta2+1)
//	: (1-ta2)/(1+ta2);	

	
//	// (tx + ty)/(1-tx*ty)			
//	ta = (isAcute())
//	? (addend.isAcute()) 
//			? (ta + addend.ta)/(1 - ta*addend.ta)
//			: (ta/addend.ta + 1)/(1/addend.ta - ta)
//	: (addend.isAcute()) 
//			? (1 + addend.ta/ta)/(1/ta - addend.ta)
//			: (1/addend.ta + 1/ta)/(1/ta/addend.ta - 1)
//	; return this;

	
//	ta = (isAcute())
//	? (subtrahend.isAcute()) 
//			? (ta - subtrahend.ta)/(1 + ta*subtrahend.ta)
//			: (ta/subtrahend.ta - 1)/(1/subtrahend.ta + ta)
//	: (subtrahend.isAcute()) 
//			? (1 - subtrahend.ta/ta)/(1/ta + subtrahend.ta)
//			: (1/ta - 1/subtrahend.ta)/(1/ta/subtrahend.ta + 1)
//	;
//	return this;

//	public Principle subtract(Principle subtrahend)
//	{  
////		ta = (isAcute())
////		? (subtrahend.isAcute()) 
////				? (ta - subtrahend.ta)/(1 + ta*subtrahend.ta)
////				: (ta/subtrahend.ta - 1)/(1/subtrahend.ta + ta)
////		: (subtrahend.isAcute()) 
////				? (1 - subtrahend.ta/ta)/(1/ta + subtrahend.ta)
////				: (1/ta - 1/subtrahend.ta)/(1/ta/subtrahend.ta + 1)
////		;
//
//		ta = (ta - subtrahend.ta)/(1 + ta*subtrahend.ta); 
//		
//		return this; 
//	}

//	/** Private Constructor */
//	private Principle(double encoded) { ta = encoded; }	
	
}