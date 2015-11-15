package rotation;
/**
 * Adapter class for angle measure:
 * 	<p>-- positive and negative units.
 *  <p>-- signed measures.
 *  <p>-- stores more than span of revolution.
 */
public class Angle //adapter class -- Angle
{
	//Units:
	public final static double PI                      = StrictMath.PI;
	public final static double PI_2      = StrictMath.scalb(StrictMath.PI,-1);
	public final static double PI_4   = StrictMath.scalb(StrictMath.PI,-2);//=StrictMath.atan(1);
	public final static double DEGREES_REVOLUTION      = 360d;
	public final static double ARCMINUTES_REVOLUTION   = DEGREES_REVOLUTION*60;
	public final static double ARCSECONDS_REVOLUTION   = DEGREES_REVOLUTION*3600;
	public final static double PI_RADIANS_REVOLUTION   = 2;
	public final static double RADIANS_REVOLUTION      = PI*2;
	public final static double MILLIRADIANS_REVOLUTION = PI*2000;
	public final static double MILS_REVOLUTION         = 6000d;	
	public final static Angle REVOLUTION  = Angle.inRadians(RADIANS_REVOLUTION);
	public final static Angle HALF_REVOLUTION  = Angle.inRadians(PI);
	public final static Angle QUARTER_REVOLUTION  = Angle.inRadians(PI_2);
	public final static Angle EIGHTH_REVOLUTION  = Angle.inRadians(PI_4);
	
	private double _angle; //internally stores radian measure...

	/** Protected constructor: Internals assume radians Angle measure... */
	protected Angle(double a) { _angle = a; }
	
	/** Copy constructor. */
	public Angle(Angle a) { _angle = a._angle; }

    /** Angle Static Factory -- Produce <b><i>Angle</i></b> of <b>r</b> radians.*/
	public static Angle inRadians(double r){ return new Angle(r); }
	
	/** Angle Static Factory -- Produce <b><i>Angle</i></b> of <b>d</b> degrees. */
	public static Angle inDegrees(double d){ return new Angle(StrictMath.toRadians(d)); }
		
	/** Angle Static Factory -- Produce <b><i>Angle</i></b> measured. */
	public static Angle inMeasure(double unitsInAngle, double unitsInRevolution)
	{ return new Angle(PI_4*( unitsInAngle/StrictMath.scalb(unitsInRevolution,-3) )); }
	
	/** Angle Static Factory -- Produce <b><i>Angle</i></b> from binary representation of <b>numBits</b>. */
	public static Angle inBinary(int binaryAngle, byte numBits){
	  int signed=-(numBits-3); double cast = binaryAngle;
	  return new Angle(PI_4*( StrictMath.scalb(cast,signed) ));  }

	/** Angle Static Factory -- Produce <b><i>Angle</i></b> from binary representation of <b>numBits</b>. */
	public static Angle inBinary(long binaryAngle, byte numBits){
	  int signed=-(numBits-3); double cast = binaryAngle;
	  return new Angle(PI_4*( StrictMath.scalb(cast,signed) ));  }


	public Angle add(Angle rotation){
		this._angle += rotation._angle;
		return this;
	}
	
	public Angle subtract(Angle rotation){
		this._angle -= rotation._angle;
		return this;
	}
	
	public Angle abs(){
		_angle = StrictMath.abs(_angle);
		return this;
	}
	
	public Angle negate(){
		_angle = -_angle;
		return this;
	}
	
	public Angle copySign(Angle signed){
		_angle = StrictMath.copySign(_angle, signed._angle);
		return this;
	}
	
	/** Mutator: Return <b>halved<i>this</i></b> set equal to <b>copy</b>. */
	public Angle half() { Angle half = new Angle(this); half._angle /= 2; return half; }
	
	/** Mutator: Return <b><i>this</i></b> set equal to <b>copy</b>. */
	public Angle put(Angle copy) { _angle = copy.getRadians(); return this; }
	
	/** Mutator: Return <b><i>this</i></b> set equal to <b>r</b> radians.*/ 
	public Angle putRadians(double r){ _angle = r; return this; }	

	/** Mutator -- Return <b><i>this</i></b> set equal to <b>d</b> degrees. */ 
	public Angle putDegrees(double d){ _angle = StrictMath.toRadians(d); return this;}

    /** Mutator: Return <b><i>this</i></b> set equal to measured angle of units per revolution specified.*/ 
	public Angle put(double unitsInAngle, double unitsInRevolution)
	{ _angle=PI_4*( unitsInAngle/StrictMath.scalb(unitsInRevolution,-3) ); return this; }

   /** Mutator: Return <b><i>this</i></b> set to <b>binaryAngle</b> of modulus <b>angleBits</b>.*/ 
	public Angle putBinary(int binaryAngle, byte angleBits) 
	{
		int signed=-(angleBits-3); 	
		double cast = binaryAngle; 
		_angle=PI_4*( StrictMath.scalb(cast,signed) ); 
		return this; 
	}

    /** Mutator: Return <b><i>this</i></b> set to <b>binaryAngle</b> of modulus <b>angleBits</b>.*/ 
	public Angle putModuloBinary(int binaryAngle, byte angleBits) {
		int signed=-(angleBits-3); 	
		// mod in cast prevents impossible binary inputs being accepted.
		double cast = binaryAngle % StrictMath.scalb(2, (int) (angleBits) - 1); 
		_angle=PI_4*( StrictMath.scalb(cast,signed) ); //double cast = binaryAngle;
		return this; 
	}

	/** Get <i>double</i>: radians measure of <b><i>Angle</b></i>. */
	public double getRadians(){ return _angle; }
	
	/** Get <i>double</i>: degrees measure of <b><i>Angle</b></i>. */
	public double getDegrees(){ return StrictMath.toDegrees(_angle); }

	/** Get <i>double</i>: <i>units measure</i> of <b><i>Angle</b></i>. 
	 * @param double <b>units_in_revolution</b>.
	 * */
	public double getMeasure(double units_in_revolution)
	{ return _angle*(StrictMath.scalb(units_in_revolution,-3)/PI_4); }	

	/** Get <i>int</i>: <i>binary</i> angle representation of <b><i>Angle</b></i>. 
	 * @param byte <b>number_bits</b> coded in angle of revolution.
	 */
	public int getBinary(byte number_bits)
	{ return (int) StrictMath.rint(StrictMath.scalb(_angle/PI_4,number_bits-3)); }

	/** 
	 * Factory. 
	 * Get <b><i>Principle</b> Angle</i>. 
	 */
	public Principle getPrinciple() {
		return Principle.arcTanHalfAngle(this.getCodedPrinciple());	
	}
	
	public Angle signedPrincipleAngle(){
		/*return new angle defined in Radians interval: (-PI..PI] */	
		if(_angle==0){//fast return...
			return Angle.inRadians(StrictMath.copySign(0,_angle));
		}
		double r = StrictMath.IEEEremainder(_angle,RADIANS_REVOLUTION);	
		return Angle.inRadians(r);
	}

	public Angle unsignedPrincipleAngle(){
		 double r = signedPrincipleAngle().getRadians();
		 return (r>=0)? Angle.inRadians(r):Angle.inRadians(r+RADIANS_REVOLUTION);
	}
	
	/** 
	 * Get <i>double</i>: <i>coded</i> <b><i>Principle</b> Angle</i>. 
	 */
	protected double getCodedPrinciple()
	{ 
		/*return tangent of one-half of the signed _angle defined in interval: (-PI..PI] */
//		
//		if(_angle==0){
//			//trap zero...fast return...
//			//System.out.print("trapZZZZZ");
//			return StrictMath.copySign(0,_angle);
//		}
//
//		double r = StrictMath.IEEEremainder(StrictMath.scalb(_angle,-1),StrictMath.PI);	
//		//double r = StrictMath.scalb(_angle ,-1) % StrictMath.PI; //alternative development
//		
//		
//		if (StrictMath.abs(r)>=PI_2) { //center the residual half angle
//			r -= StrictMath.copySign(StrictMath.PI, r); 	
//		}
//
//		if (StrictMath.abs(r)==PI_2) { //trap residual right angle...fast return
//			//System.out.print("trapInfiny");
//			return StrictMath.copySign(Double.POSITIVE_INFINITY, -r);									
//		}
//		
//		//System.out.print("default");
//		return StrictMath.tan(r);
		double r = this.signedPrincipleAngle().half().getRadians();
		if(r==0) return StrictMath.copySign(0, r);		
		if(r==PI) return Double.POSITIVE_INFINITY;
		if(r==-PI) return Double.NEGATIVE_INFINITY;
		return StrictMath.tan(r);
		
	}
	
//	/**
//	 * Get <i>int</i>: <i>nearest full revolution count</i> of stored
//	 * <b>Angle</b>.
//	 */
//	public int getNearestRevolution() {
//		return (int) StrictMath.round(angle / REVOLUTION_RADIANS);
//	}
//	/**
//	 * Get <i>double</i>: Principle <i>radians</i> of stored
//	 * <b>Angle</b>.
//	 */
//	public double getSignedPrincipleRadians() {
//		return StrictMath.IEEEremainder(angle , REVOLUTION_RADIANS);
//		//returns [-PI..+PI] revolution!
//	}


//	/** Get <i>int</i>: <i>binary</i> angle representation of <b><i>Angle</b></i>. 
//	 * @param byte <b>number_bits</b> coded in angle of revolution.
//	 */
//	public long getLongBinary(byte number_bits)
//	{ return (long) StrictMath.rint(StrictMath.scalb(angle/QUARTER_PI_RADIANS,number_bits-3)); }

	
	
//	/**
//	 * 
//	 * Sum factory
//	 * @param radians
//	 * @return
//	 */
//	protected Angle getSum(double radians){
//	    return inRadians(_angle+radians);	
//	}
//	/**
//	 * 
//	 * Sum factory
//	 * @param radians
//	 * @return
//	 */
//	protected Angle getDifference(double radians){
//	    return inRadians(radians - _angle);	
//	}
	
	
}
