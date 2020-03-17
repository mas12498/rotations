/**
 * 
 */
package tspi.rotation;

/**
 * Class for encoding principle arguments of angle rotators.
 * 
 * Use to speed computations: minimize storage of intermediate units
 * conversions. minimize unnecessary trigonometric computations.
 * <p>
 * As privately encoded <i>trigonometric</i> representation:
 * tan(principle(Angle)/2)
 * <p>
 * -- unit-less: No preferred units to interface.
 * <p>
 * -- tan(),Sin(),Cos(),tanHalf() member functions with minimal resort to math
 * library.
 * 
 * 
 * <p>
 * Principle angle extraction methods span one revolution, unambiguously:
 * [0..360) or [-180..180)
 * <p>
 * -- internal angle representations augmented: [ -180d : -0d, +0d : 180d ]
 * degrees.
 * <p>
 * -- maps ieee double representations: [-Inf .. +Inf] for principle angle
 * CodedPrinciple definitions.
 * <p>
 * -- maps Double.NaN for empty angle CodedPrinciple definition.
 * <p>
 * -- CAUTION: CodedPhase is a lossy conversion from some Angle 
 * floating point representations. In fact, Angle measures near to {0,90,180,-180,-90,-0} degrees may 
 * be rounded to exact coded angle cardinal measures. 
 * Observe CODE_LARGEST and CODE_SMALLEST representations.
 * 
 * @author mike
 *
 */
public class CodedPhase {
	// representatin bounds...
	private final static   Double _SMALLEST = StrictMath.scalb(1d, -26); // underflow mark
	private final static   Double _LARGEST = StrictMath.scalb(1d, 27); // overflow mark
	private final static  double _EMPTY = Double.NaN;
	private final static  double _ZERO = 0d;
	private final static  double _ONE = 1d;
	private final static  double _HALF_PI = StrictMath.PI/2;
	private final static  double _HALF = StrictMath.scalb(_ONE, -1);
	public final static  double _STRAIGHT = Double.POSITIVE_INFINITY;

	final static public CodedPhase EMPTY = encodes(_EMPTY);
	final static public CodedPhase ZERO = encodes(_ZERO);
	final static public CodedPhase RIGHT = encodes(_ONE);
	final static public CodedPhase STRAIGHT = encodes(_STRAIGHT);
	final static public CodedPhase NEGATIVE_ZERO = encodes(-_ZERO);
	final static public CodedPhase NEGATIVE_RIGHT = encodes(-_ONE);
	final static public CodedPhase NEGATIVE_STRAIGHT = encodes(-_STRAIGHT);

	/**
	 * Coded Angle Static Factory.
	 * 
	 * @param tanHalfAngle
	 *            a value from [-inf..+inf] covers angles [-1..+1] pi radians.
	 * @return CodedPhase constructed as it is encoded: tangent half
	 *         signed-phase angle.
	 */
	public static final CodedPhase encodes(double tanHalfAngle) {		
		return new CodedPhase(tanHalfAngle);
	}

	public static void main(String args[]) {
		double i;
		Angle a = Angle.inDegrees(Double.NaN);
		for (int h = -32; h <= 32; h++) {
			i = h;// * 22.5;
			// a = Angle.inDegrees(i);
			a = Angle.inPiRadians(i / 8d);
			CodedPhase c = a.codedPhase();
			CodedPhase cG = new CodedPhase(c).addRight();
			CodedPhase cL = new CodedPhase(c).subtractRight();

			// System.out.println("("+i+")"
			// +" Unsigned Angle =
			// "+a.unsignedPrincipleComponent().toDegreesString(17)
			// +" Signed Angle =
			// "+a.signedPrincipleComponent().toDegreesString(17)
			// +" near rev =
			// "+StrictMath.round(Angle.inDegrees(i).getRevolutions())
			// );
			System.out.println(
					// "(" + i + ")"
					"\n Coded Angle = " + c._ta + "\n " // Unsigned Principle
														// Angle = "
							+ c.angle().unsignedPrinciple().toDegreesString(17) + "\n " // "+90
																						// =
																						// "
							+ cG.angle().signedPrinciple().toDegreesString(17) + "\n " // "-90
																						// =
																						// "
							+ cL.angle().signedPrinciple().toDegreesString(17) + "    Signed Coded Angle = "
							+ StrictMath.toDegrees(c.angle().signedPrinciple().getRadians())
							+ "    Signed Principle Angle = "
							+ new CodedPhase(c).angle().signedPrinciple().toDegreesString(17) + " near rev = "
							+ StrictMath.round(Angle.inDegrees(i).getRevolutions()));
		}

		System.out.println("coded straight angles:");
		System.out.println(2 * StrictMath.atan(Double.NaN));
		System.out.println(StrictMath.toDegrees(2 * StrictMath.atan(Double.POSITIVE_INFINITY)));
		System.out.println(StrictMath.toDegrees(2 * StrictMath.atan(Double.NEGATIVE_INFINITY)));

		CodedPhase t0P = new CodedPhase(ZERO);
		CodedPhase t0N = new CodedPhase(ZERO).negate();
		CodedPhase t = new CodedPhase(t0P);
		System.out.println("coded principle angles equal copies: " + t.equals(t0P));
		System.out.println("coded principle zeros (+|-) equal: " + t.equals(t0N));
		t0N = new CodedPhase(ZERO).negate();
		System.out.println("coded principle integer auto box'd zeros (+|-) equal: " + t.equals(t0N));
		CodedPhase tInf = new CodedPhase(STRAIGHT);
		System.out.println("coded principle two straights (+|-) equal: " + NEGATIVE_STRAIGHT.equals(tInf));

		System.out.println("isZero(-0)= " + CodedPhase.encodes(-0.0).isZero());

		System.out.println((StrictMath.tan(StrictMath.PI / 8) / (0.5d
				- (StrictMath.scalb(StrictMath.tan(StrictMath.PI / 8), -1) * StrictMath.tan(StrictMath.PI / 8))))
				+ " : "
				+ ((StrictMath.nextDown(StrictMath.nextDown(StrictMath.sqrt(2) - 1d))) / (0.5d
						- StrictMath.scalb((StrictMath.nextDown(StrictMath.nextDown(StrictMath.sqrt(2) - 1d))), -1)
								* (StrictMath.nextDown(StrictMath.nextDown(StrictMath.sqrt(2) - 1d))))));

		// double aaa = ( (2*(StrictMath.sqrt(2)-1d)) /( 0.5d -
		// StrictMath.scalb((2*(StrictMath.sqrt(2)-1d)),-1)*(2*(StrictMath.sqrt(2)-1d))
		// ));
		System.out.println("********End of AngleTest exercises *******************");
		CodedPhase d = new CodedPhase(CodedPhase.RIGHT);
		// assertTrue(d.isOrthogonal());
		// System.out.println(d.getRadians() + " is orthog:"+ d.isOrthogonal());
		d.set(d.negate());
		// assertTrue(d.isOrthogonal());
		// System.out.println(d.getRadians() + " is orthog:"+ d.isOrthogonal());

		// assertEquals(d.getRadians(), Angle.RADIANS_RIGHT,1e-15);
		d = CodedPhase.encodes(1d);
		// assertTrue(d.isOrthogonal());
		// System.out.println(d.getRadians() + " is orthog:"+ d.isOrthogonal());
		// assertEquals(d.getRadians(), Angle.RADIANS_RIGHT,1e-15);
		double test = 1.0d;
		CodedPhase testAngle = Angle.inPiRadians(1d).codedPhase();
		for (int m = 1; m <= 200; m++) {
			test = test / 2;
			testAngle = Angle.inPiRadians(test * test).codedPhase();
			if (testAngle.addRight().tanHalf() == 1.0d) {
				System.out.println((m - 1) + "  " + test);
				break;
			}
			System.out.println(m + " failed " + test);
		}

	}

	private double _ta;


	/** CodedPhase Copy constructor */
	public CodedPhase(CodedPhase copy) {
			_ta = copy._ta;
	}

	/**
	 * CodedPhase by double constructor
	 * <p>
	 * -- by externally encoded angle representations: tanHalfAngle.
	 */
	private CodedPhase(double encoded) {
			_ta = encoded;
	}

	/**
	 * Mutator -- sum.
	 * 
	 * @param addend
	 *            Principle
	 */
	public CodedPhase add(CodedPhase addend) {
		double d = _ONE;
		if (this.isAcute()) {
			if (addend.isAcute()) {
				d -= (_ta * addend._ta);
				_ta /= d;
				_ta += (addend._ta / d);
			} else {
				d /= addend._ta;
				d -= _ta;
				_ta /= (addend._ta * d);
				_ta += (_ONE / d);
			};
		} else { 
			if (addend.isAcute()) {
				d /= _ta;
				d -= addend._ta;
				_ta = addend._ta / (_ta * d);
				_ta += _ONE / d;
			} else {
				d /= (_ta * addend._ta);
				d-= _ONE;
				_ta = _ONE / (_ta * d);
				_ta += (_ONE / (addend._ta * d));
			};
		};
//		_ta = (test1)
//				? (test2) ? (_ta + addend._ta) /    (_ONE - _ta * addend._ta)
//						: (_ta / addend._ta + 1d) / (_ONE / addend._ta - _ta)
//				: (test2) ? (_ONE + addend._ta / _ta) /      (_ONE / _ta - addend._ta)
//						: (_ONE / addend._ta + _ONE / _ta) / (_ONE / _ta / addend._ta - _ONE);			
		return this;
	}

	/**
	 * use Mutator -- sum with right angle.
	 * tan  + sec = _ta + sqrt(1+_ta*ta)
	 */
	public CodedPhase addRight() { //improve ???
		//_ta = (isAcute()) ? (_ta + 1d) / (1d - _ta) : (1d + 1d / _ta) / (1d / _ta - 1d);		
		boolean test = !isAcute();
		if(test) _ta = _ONE / _ta;
		double d = test ?   _ta - _ONE : _ONE - _ta ;
		_ta /= d;
		_ta += (_ONE / d);
		return this;
	}

	/**
	 * use Mutator -- sum with straight angle.
	 */
	public CodedPhase addStraight() {
		_ta = -_ONE / _ta;
		return this;
	}

	/**
	 * Angle factory: Angle of this CodedPhase.
	 */
	public Angle angle() {
		return Angle.inPiRadians(StrictMath.atan(_ta) / _HALF_PI);
	}

	/**
	 * Positive magnitude of Tangent of half Angle.
	 * 
	 * @return double
	 */
	public double codedMagnitude() {
		return StrictMath.abs(_ta);
	}

	/**
	 * Cosine of Principle Angle.
	 * 
	 * @return double
	 */
	public double cos() {
		boolean test = !isAcute();
		double c =  _ta * _ta ;
		if(test) c = _ONE / c;
		double d = _ONE + c;
		c /=  d;
		c -= (_ONE / d) ;
		return (test) ? c : -c;	
	}

	/**
	 * CoTangent of Principle Angle.
	 * <p>
	 * Domain pi radians: [-1 .. +1]
	 * <p>
	 * Range cotangent : [+Inf .. -Inf]
	 * 
	 * @return double
	 */
	public double cot() {
		return _ONE / tan();
	}

	/**
	 * Cotangent of half Angle.
	 * 
	 * @return double
	 */
	public double cotHalf() {
		return _ONE / _ta;
	}

	/*
	 * (non-Javadoc) use
	 * 
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
		if (!(obj instanceof CodedPhase)) {
			return false;
		}
		CodedPhase other = (CodedPhase) obj;
		if (Double.doubleToLongBits(_ta) != Double.doubleToLongBits(other._ta)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() This hash detects differences between
	 * +ZERO and -ZERO as well as +STRAIGHT and -STRAIGHT [?]
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

	/** True if <i>absolute</i> Principle angle is acute. */
	public boolean isAcute() {
		return (codedMagnitude() < _ONE);
	}

	/** True if phase coded is undefined. */
	public boolean isEmpty() {
		return Double.isNaN(_ta);
	}


	// mutators

	/** True if <i>absolute</i> phase coded is obtuse angle. */
	public boolean isObtuse() {
		return (codedMagnitude() > _ONE);
	}

	/** True if <i>absolute</i> phase coded is exactly right angle. */
	public boolean isOrthogonal() {
		return (codedMagnitude() == _ONE);
	}

	/** True if phase coded is positive or zero. */
	public boolean isPositive() {
		return (_ta >= _ZERO);
	}
		
	/** True if <i>absolute</i> phase coded is straight angle. */
	public boolean isStraight() {
		//return (codedMagnitude() == _STRAIGHT );
		return Double.isInfinite(_ta);
	}
	

	/** True if phase coded equals zero angle. */
	public boolean isZero() {
		return (_ta == _ZERO);
	}

	/**
	 * Magnification of fast rotation operation.
	 * 
	 * @return double Op - Scaling
	 */
	public double magnificationRotator() {
		double t = (this.isObtuse()) ? _ONE / _ta : _ta;
		return _ONE + t*t;
	}

	/**
	 * Mutator -- negate: Changes <i>sign</i>.
	 */
	public CodedPhase negate() {
		_ta = -_ta;
		return this;
	}

	/** Setter to another CodedPrinciple angle's coded measure. */
	public void set(CodedPhase angle) {
		_ta = angle._ta;
		return;
	}

	/**
	 * Sine of Principle Angle.
	 * 
	 * @return double
	 */
	public double sin() {
		double ma = (this.isAcute()) ? _ta : _ONE / _ta;
		return ma / (StrictMath.scalb(ma * ma, -1)  + _HALF);
		//return StrictMath.scalb(ma, 1) / (ma * ma + 1);
	}

	/**
	 * Mutator -- difference.
	 * 
	 * @param subtrahend
	 *            Principle
	 */
	public CodedPhase subtract(CodedPhase subtrahend) {

//		_ta = (this.isAcute())
//				? (subtrahend.isAcute()) ? (_ta - subtrahend._ta) / (_ONE + _ta * subtrahend._ta)
//						: (_ta / subtrahend._ta - _ONE) / (_ONE / subtrahend._ta + _ta)
//				: (subtrahend.isAcute()) ? (_ONE - subtrahend._ta / _ta) / (_ONE / _ta + subtrahend._ta)
//						: (_ONE / subtrahend._ta - _ONE / _ta) / (_ONE / _ta / subtrahend._ta + _ONE);
	            add(new CodedPhase(subtrahend).negate());	
				return this;
	}

	/**
	 * Mutator -- subtract right angle.
	 */
	public CodedPhase subtractRight() {
//		_ta = (this.isAcute()) ? (_ta - 1d) / (1d + _ta) : (1d - 1d / _ta) / (1d / _ta + 1d);
		boolean test = !isAcute();
		if(test) _ta = _ONE / _ta;
		double d = _ONE + _ta;
		_ta /=d;
		_ta -= (_ONE / d);
		if(test) _ta = - _ta;
		return this;
	}

	/**
	 * use Mutator -- sum with straight angle.
	 */
	public CodedPhase subtractStraight() {
		//_ta = -_ONE / _ta;
		return this.addStraight();
	}

	/**
	 * Tangent of Phase Angle.
	 * 
	 * @return double
	 */
	public double tan() {
		if(Double.isInfinite(_ta)) return StrictMath.copySign(_ZERO, _ta);
		if(this.isOrthogonal()) return StrictMath.copySign(_STRAIGHT, _ta);
		return fastTan((this.isAcute()) ? _ta : -( _ONE / _ta));
	}	
	private static double fastTan(double t) {
		return  t / (_HALF - StrictMath.scalb(t*t, - 1) );	
	}

	/**
	 * Tangent of half Angle.
	 * 
	 * @return double
	 */
	public double tanHalf() {
		return _ta;
	}

	/**
	 * CodedPhase condition
	 * <p>
	 * -- reduce to cardinal directions when close to them!
	 */
	public CodedPhase trueRound() {		
		if (_SMALLEST > StrictMath.abs(_ta)) {
			_ta = StrictMath.copySign(_ZERO, _ta);
		} else if (_SMALLEST > StrictMath.abs(StrictMath.abs(_ta) - _ONE)) {
			_ta = StrictMath.copySign(_ONE, _ta);
		} else if (_LARGEST < StrictMath.abs(_ta)) {
			_ta = StrictMath.copySign(_STRAIGHT, _ta);
		}
		return this;
	}

}