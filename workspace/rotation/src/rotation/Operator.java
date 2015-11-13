/**
 * 
 */
package rotation;

/**
 * 
 * Stores factored Quaternion: Ignores magnitude
 * 
 * @author mike
 * 
 */
public class Operator extends Quaternion {

	/**
	 * Constructor FastQuaternion:
	 */
	public Operator(Quaternion q) {
		super(q);
	}

	/**
	 * Constructor FastQuaternion:
	 * 
	 * @param w
	 *            (double) -- Quaternion factor scalar part
	 * @param v
	 *            (Vector3) -- Quaternion factor Vector part
	 */
	public Operator(double w, Vector3 v) {
		super(w, v);
	}

	/**
	 * 
	 * Constructor FastQuaternion:
	 * 
	 * @param w
	 *            (double) -- Quaternion factor scalar part
	 * @param x
	 *            (double) -- Quaternion factor Vector3 element
	 * @param y
	 *            (double) -- Quaternion factor Vector3 element
	 * @param z
	 *            (double) -- Quaternion factor Vector3 element
	 */
	public Operator(double w, double x, double y, double z) {
		super(w, x, y, z);
	}

	public double getInverseAbs() {
		return 1d / (getAbs());
	}

	public double getInverseDeterminant() {
		return 1d / (getDeterminant());
	}
	
	public Vector3 getRotationAxis(){
		return getV().unit();
	}

	/**
	 * Tait-Bryan Yaw | Centerline azimuth :
	 * 
	 * @return Principle
	 */
	public Principle getEuler_k_kji() {
		if(getZ() == 0){ //On prime meridian
			if(getW()>0){
//				System.out.print("trap00000");
				return new Principle(Principle.ZERO);				
			}
			//not necessary... but faster return.
//			System.out.print("trap11111");
			return new Principle(Principle.STRAIGHT).negate();							
		}
		double sk = getW() * getZ() + getX() * getY();
		if (sk == 0) { //On the equator...
//			System.out.print("trapEquatorialZZ");

			return new Principle(getW() / -getZ());
		}
		double ck = StrictMath.scalb(
				getW() * getW() 
				+ getX() * getX() 
				- getY() * getY() 
				- getZ() * getZ()
				, -1);
		double r = StrictMath.hypot(sk, ck);
		return new Principle(sk / (r + ck));
	}

	/**
	 * Tait-Bryan Pitch | Centerline elevation : [-90..90]
	 * 
	 * @return Principle
	 */
	public Principle getEuler_j_kji() {
		double sj = (getW() * getY() - getZ() * getX()) 
				/ StrictMath.scalb(getDeterminant(), -1);
//		if(sj==1){
//			return new Principle(1); //90 degrees...
//		}
		return new Principle(sj / (1 + StrictMath.sqrt(1 - sj * sj)));
	}

	/**
	 * Tait-Bryan Roll | Centerline twist :
	 * 
	 * @return Principle
	 */
	public boolean getDump_kj() {
		double si = getW() * getX() + getY() * getZ();
		if (si == 0) {
			double ci = StrictMath.scalb(getW() * getW() - getX() * getX() - getY() * getY() + getZ() * getZ(), -1);
			if (ci < 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tait-Bryan Roll | Centerline twist :
	 * 
	 * @return Principle
	 */
	public Principle getEuler_i_kji() {		
		double si = getW() * getX() + getY() * getZ();
		double ci = StrictMath.scalb(getW() * getW() - getX() * getX() - getY() * getY()
				+ getZ() * getZ(), -1);			
		double r = StrictMath.hypot(si, ci);
		return new Principle(si / (r + ci));
	}

	// @Note: need array of Principle.
	public Vector3 getEuler_kji() {
		double ii = getX() * getX();
		double jj = getY() * getY();
		double kk = getZ() * getZ();
		double semiDet = StrictMath.scalb(getW() * getW() + ii + jj + kk, -1);
		double ck = semiDet - (jj + kk);
		double ci = semiDet - (ii + jj);
		double sk = getW() * getZ() + getX() * getY();
		double sj = (getW() * getY() - getZ() * getX()) / semiDet;
		double si = getW() * getX() + getY() * getZ();
		return new Vector3((si / (StrictMath.hypot(si, ci) + ci)),
				(sj / (1 + StrictMath.sqrt(1 - sj * sj))),
				(sk / (StrictMath.hypot(sk, ck) + ck)));
	}

	/**
	 * Vector3 basis unit-I (image mapped by rotation operator q) static factory
	 */
	public Vector3 getImage_i() {
		return new Vector3(getW() * getW() + getX() * getX() - getY() * getY() - getZ() * getZ(),
				2 * (getX() * getY() + getW() * getZ()), 2 * (getX() * getZ() - getW() * getY()));
	}

	/**
	 * Vector3 Projected unit-J (Basis transformed by rotation operator q)
	 * static factory
	 */
	public Vector3 getImage_j() {
		return new Vector3(2 * (getX() * getY() - getW() * getZ()),
				getW() * getW() - getX() * getX() + getY() * getY() - getZ() * getZ(),
				2 * (getW() * getX() + getY() * getZ()));
	}

	/**
	 * Vector3 Projected unit-K (Basis transformed by rotation operator q)
	 * static factory
	 */
	public Vector3 getImage_k() {
		return new Vector3(2 * (getW() * getY() + getX() * getZ()), 2 * (getY() * getZ() - getW() * getX()),
				getW() * getW() - getX() * getX() - getY() * getY() + getZ() * getZ());
	}
	
	/**
	 * Vector3 basis unit-I (pre-image mapped by rotation operator q) static
	 * factory
	 */
	public Vector3 getPreImage_i() {
		return new Vector3(getW() * getW() + getX() * getX() - getY() * getY() - getZ() * getZ(),
				2 * (getX() * getY() - getW() * getZ()), 2 * (getW() * getY() + getX() * getZ()));
	}

	/**
	 * Vector3 basis unit-J (pre-image mapped by rotation operator q) static
	 * factory
	 */
	public Vector3 getPreImage_j() {
		return new Vector3(2 * (getX() * getY() + getW() * getZ()),
				getW() * getW() - getX() * getX() + getY() * getY() - getZ() * getZ(),
				2 * (getY() * getZ() - getW() * getX()));
	}

	/**
	 * Vector3 basis unit-K (pre-image mapped by rotation operator q) static
	 * factory
	 */
	public Vector3 getPreImage_k() {
		return new Vector3(2 * (getX() * getZ() - getW() * getY()), 2 * (getW() * getX() + getY() * getZ()),
				getW() * getW() - getX() * getX() - getY() * getY() + getZ() * getZ());
	}
	
//	public Vector3 getPreImage_i() {
//		// return QuaternionMath.multiplyGetV( //project I-axis unit (Op.Right)
//		// new Quaternion(p.getS(),-p.getI(),-p.getJ(),-p.getK())
//		// new Quaternion(-p.getI(),p.getS(),-p.getK(),p.getJ()); }
//		// )//.divide(p.determinant())
//		// ;
//		double a = getW() - getX();
//		double b = getW() + getX();
//		double e = (getX() + getZ()) * (-getW() + getZ());
//		double f = (getX() - getZ()) * (getW() + getZ());
//		double g = (getY() - getW()) * (getX() + getY());
//		double h = (getW() + getY()) * (-getX() + getY());
//		return new Vector3(a * a - StrictMath.scalb((e - f + g + h), -1), b
//				* (getY() - getZ()) + StrictMath.scalb((e + f + g - h), -1), b
//				* (getY() + getZ()) + StrictMath.scalb((e + f - g + h), -1))
//				.divide(getDeterminant());
//	}
//		public Vector3 getPreImage_j() {
//		// return QuaternionMath.multiplyGetV( //project J-axis unit (Op.Right)
//		// new Quaternion(p.real(),-p.getI(),-p.getJ(),-p.getK())
//		// new Quaternion(-p.getJ(),p.getK(),p.getS(),-p.real()); }
//		// )//.divide(p.determinant())
//		// ;
//		double a = getY() + getZ();
//		double b = getW() - getX();
//		double e = -(getX() + getZ()) * (getZ() + getW());
//		double f = (getX() - getZ()) * (getZ() - getW());
//		double g = (getW() - getY()) * (-getY() + getX());
//		double h = (getW() + getY()) * (-getY() - getX());
//		return new Vector3(b * (getZ() - getY())
//				- StrictMath.scalb((e - f + g + h), -1), b * (getW() + getX())
//				+ StrictMath.scalb((e + f + g - h), -1), a * a
//				+ StrictMath.scalb((e + f - g + h), -1)).divide(getDeterminant());
//	}
//	public Vector3 getPreImage_k() {
//		// return QuaternionMath.multiplyGetV( //project K-axis unit (Op.Right)
//		// new Quaternion(real(),-i(),-j(),-k());
//		// new Quaternion(-k(),-j(),i(),real()); }
//
//		double a = (getW() - getX()) * (-getZ() - getY());
//		double c = (getW() + getX()) * (getX() + getW());
//		double d = (-getY() - getZ()) * (-getZ() + getY());
//		double e = (-getX() - getZ()) * (-getY() + getX());
//		double f = (-getX() + getZ()) * (-getY() - getX());
//		double g = (getW() - getY()) * (-getZ() - getW());
//		double h = (getW() + getY()) * (-getZ() + getW());
//		return new Vector3(a - StrictMath.scalb((e + f + g + h), -1), c
//				+ StrictMath.scalb((e - f + g - h), -1), d
//				+ StrictMath.scalb((e - f - g + h), -1)).divide(getDeterminant());
//	}
//	public Vector3 getImage_k() {
//	// return QuaternionMath.multiplyGetV( //project K-axis unit (Op.Right)
//	// new Quaternion(-p.getK(),p.getJ(),-p.getI(),p.getS())
//	// new Quaternion(p.getS(),-p.getI(),-p.getJ(),-p.getK())
//	// )//.divide(p.determinant())
//	// ;
//	double a = (getZ() + getY());
//	double b = (getW() - getX());
//	double e = -(getY() + getW()) * (getX() + getY());
//	double f = (getY() - getW()) * (getX() - getY());
//	double g = -(getZ() + getX()) * (getW() + getZ());
//	double h = (getX() - getZ()) * (getW() - getZ());
//	return new Vector3(b * (getY() - getZ())
//			- StrictMath.scalb((e - f + g + h), -1), a * a
//			+ StrictMath.scalb((e + f + g - h), -1), b * (getW() + getX())
//			+ StrictMath.scalb((e + f - g + h), -1)).divide(getDeterminant());
//}
//public Vector3 getImage_i() {
//// return QuaternionMath.multiplyGetV( //project I-axis unit (Op.Right)
//// new Quaternion(-p.getI(),p.getS(),p.getK(),-p.getJ()),
//// new Quaternion(p.getS(),-p.getI(),-p.getJ(),-p.getK())
//// )//.divide(p.determinant())
//// ;
//double a = getW() - getX();
//double b = getW() + getX();
//double e = (getY() - getW()) * (getX() + getY());
//double f = (getW() + getY()) * (getX() - getY());
//double g = (getZ() - getX()) * (getW() + getZ());
//double h = (getX() + getZ()) * (getZ() - getW());
//return new Vector3(a * a - StrictMath.scalb((e - f + g + h), -1), b
//		* (getY() + getZ()) + StrictMath.scalb((e + f + g - h), -1), b
//		* (getZ() - getY()) + StrictMath.scalb((e + f - g + h), -1))
//		.divide(getDeterminant());
//}
//public Vector3 getImage_j() {
//// return QuaternionMath.multiplyGetV( //project J-axis unit (Op.Right)
//// new Quaternion(-p.getJ(),-p.getK(),p.getS(),p.getI()),
//// new Quaternion(p.getS(),-p.getI(),-p.getJ(),-p.getK())
//// )//.divide(p.determinant())
//// ;
//double a = (getW() + getX());
//double b = (getY() + getZ());
//double e = (getZ() - getX()) * (getX() + getY());
//double f = (getZ() + getX()) * (getY() - getX());
//double g = (getW() - getY()) * (getW() + getZ());
//double h = (getY() + getW()) * (getZ() - getW());
//return new Vector3(b * (getX() - getW())
//		- StrictMath.scalb((e - f + g + h), -1), b * (getY() - getZ())
//		+ StrictMath.scalb((e + f + g - h), -1), a * a
//		+ StrictMath.scalb((e + f - g + h), -1)).divide(getDeterminant());
//}

	public Vector3 getImage(Vector3 object) {
		return QuaternionMath.multiply(QuaternionMath.multiply(this, object),
				QuaternionMath.reciprocal(this)).getV();
	}

	public Vector3 getPreImage(Vector3 object) {
		return QuaternionMath
				.multiply(
						QuaternionMath.multiply(
								QuaternionMath.reciprocal(this), object), this)
				.getV();
	}

	
	
//	/**
//	 * <b>Quarter (90) Degree-Pre-Turn about frame axis i mutator.</B> (45 degrees
//	 * per multiplication)
//	 */
//	public Operator preTurn_i() {
//		return (Operator) this.addLeftI(new Quaternion(this));
//	}


//	/**
//	 * <B>FactoredQuaternion 90-Degree-Pre-Turn about frame axis j mutator.</B> (45
//	 * degrees per multiplication)
//	 */
//	public Operator preTurn_j() {
//		return (Operator) this.addLeftJ(new Quaternion(this));
//	}

//	/**
//	 * <B>FactoredQuaternion 90-Degree-Pre-Turn about frame axis k mutator.</B> (45
//	 * degrees per multiplication)
//	 */
//	public Operator preTurn_k() {
//		return (Operator) this.addLeftK(new Quaternion(this));
//	}

//	/**
//	 * <b>Quarter (90) Degree-Turn about frame axis i mutator.</B> (45 degrees
//	 * per multiplication)
//	 */
//	public Operator turn_i() {
//		return (Operator) this.addRightI(new Quaternion(this));
//	}
//
//	/**
//	 * <B>FactoredQuaternion 90-Degree-Turn about frame axis j mutator.</B> (45
//	 * degrees per multiplication)
//	 */
//	public Operator turn_j() {
//		return (Operator) this.addRightJ(new Quaternion(this));
//	}
//	/**
//	 * <B>FactoredQuaternion 90-Degree-Turn about frame axis k mutator.</B> (45
//	 * degrees per multiplication)
//	 */
//	public Operator turn_k() {
//		return (Operator) this.addRightK(new Quaternion(this));
//	}

	/**
	 * <b>Quarter (90) Degree-Pre-Turn about basis frame axis mutator.</B> (45 degrees
	 * per multiplication)
	 * @param e BasisUnit imaginary axis to turn-about 90 degrees.
	 */
	public Operator preTurn(BasisUnit e){
		switch(e)
		{
		case I:			
			return (Operator) this.addLeftI(new Quaternion(this));
			//break;
		case J:
			return (Operator) this.addLeftJ(new Quaternion(this));
			//break;
		case K:
			return (Operator) this.addLeftK(new Quaternion(this));
			//break;
		default:
		//case S:
			System.out.println("Error: not BasisAxis to pre-turn about...");
			return this;
		}
	}
	

	
	//	/**
	//	 * Mutator.
	//	 * 
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @Note axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_i() {
	//		return (Operator) putRightI(new Quaternion(this));
	//	}
	//
	//	/**
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @param axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_j() {
	//		return (Operator) putRightJ(new Quaternion(this));
	//	}
	//
	//	/**
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @param axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_k() {
	//		return (Operator) putRightK(new Quaternion(this));
	//	}
	
	
		/**
		 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
		 * (90 degrees right per multiplication)
		 * 
		 * @param axis
		 *            UnitBasis fixed for pre-flip
		 */
		public Operator preFlip(BasisUnit e){
			Quaternion t = new Quaternion(this);
			switch(e)
			{
			case I:			
				return (Operator) this.putLeftI(t);
				//break;
			case J:
				return (Operator) this.putLeftJ(t);
				//break;
			case K:
				return (Operator) this.putLeftK(t);
				//break;
			default:
				System.out.println("Error: not BasisAxis to pre-flip about...");
				return this;
			}
		}

	/**
	 * <B>FactoredQuaternion 90-Degree-Pre-Turn about vector-axis mutator.</B> (45
	 * degrees per multiplication)
	 * 
	 * @param v
	 *            Vector3 pre-turn-axis
	 */
	public Operator preTurn(Vector3 v) {
		if (v.getNormInf() == 0)
			return new Operator(Quaternion.NAN); // ????
		Vector3 u = new Vector3(v).unit(); // vector magnitude...
		Quaternion t = new Quaternion(this);
		return (Operator) this
				.addMultiplyLeftI(u.getX(), t)
				.addMultiplyLeftJ(u.getY(), t)
				.addMultiplyLeftK(u.getZ(), t);
	}

	//	/**
	//	 * Mutator.
	//	 * 
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @Note axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_i() {
	//		return (Operator) putRightI(new Quaternion(this));
	//	}
	//
	//	/**
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @param axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_j() {
	//		return (Operator) putRightJ(new Quaternion(this));
	//	}
	//
	//	/**
	//	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	//	 * (90 degrees right per multiplication)
	//	 * 
	//	 * @param axis
	//	 *            UnitBasis fixed {i}
	//	 */
	//	public Operator flip_k() {
	//		return (Operator) putRightK(new Quaternion(this));
	//	}
	
	
		/**
		 * <B>FactoredQuaternion 180-Degree-Pre-Turn about unit basis axis mutator.</B>
		 * (90 degrees left per multiplication)
		 * 
		 * @param axis
		 *            UnitBasis fixed {i}
		 */
		public Operator preFlip(Vector3 v) {
			Vector3 u = new Vector3(v).unit(); // vector magnitude...
			Quaternion t = new Quaternion(this);
			return (Operator) this.putLeftI(t).multiply(u.getX()).addMultiplyLeftJ(u.getY(), t)
					.addMultiplyLeftK(u.getZ(), t);
		}

	/**
	 * <b>Quarter (90) Degree-Turn about basis frame axis mutator.</B> (45 degrees
	 * per multiplication)
	 * @param e BasisUnit imaginary axis to turn-about 90 degrees.
	 */
	public Operator turn(BasisUnit e){
		switch(e)
		{
		case I:			
			return (Operator) this.addRightI(new Quaternion(this));
			//break;
		case J:
			return (Operator) this.addRightJ(new Quaternion(this));
			//break;
		case K:
			return (Operator) this.addRightK(new Quaternion(this));
			//break;
		default:
		//case S:
			System.out.println("Error: not BasisAxis to turn about...");
			return this;
		}
	}
	

	/**
	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	 * (90 degrees right per multiplication)
	 * 
	 * @param axis
	 *            UnitBasis fixed for flip
	 */
	public Operator flip(BasisUnit e){
		Quaternion t = new Quaternion(this);
		switch(e)
		{
		case I:			
			return (Operator) this.putRightI(t);
			//break;
		case J:
			return (Operator) this.putRightJ(t);
			//break;
		case K:
			return (Operator) this.putRightK(t);
			//break;
		default:
			System.out.println("Error: not BasisAxis to flip about...");
			return this;
		}
	}

	/**
	 * <B>FactoredQuaternion 90-Degree-Turn about vector-axis mutator.</B> (45
	 * degrees per multiplication)
	 * 
	 * @param v
	 *            Vector3 turn-axis
	 */
	public Operator turn(Vector3 v) {
		if (v.getNormInf() == 0)
			return new Operator(Quaternion.NAN); // ????
		Vector3 u = new Vector3(v).unit(); // vector magnitude...
		Quaternion t = new Quaternion(this);
		return (Operator) this
				.addMultiplyRightI(u.getX(), t)
				.addMultiplyRightJ(u.getY(), t)
				.addMultiplyRightK(u.getZ(), t);
	}

/**
	 * <B>FactoredQuaternion 180-Degree-Turn about unit basis axis mutator.</B>
	 * (90 degrees right per multiplication)
	 * 
	 * @param axis
	 *            UnitBasis fixed {i}
	 */
	public Operator flip(Vector3 v) {
		Vector3 u = new Vector3(v).unit(); // vector magnitude...
		Quaternion t = new Quaternion(this);
		return (Operator) this.putRightI(t).multiply(u.getX()).addMultiplyRightJ(u.getY(), t)
				.addMultiplyRightK(u.getZ(), t);
//		return (Operator) this.putRightI(u.getX(), t).addMultiplyRightJ(u.getY(), t)
//				.addMultiplyRightK(u.getZ(), t);
	}


	/**
	 * pre- sense i rotation mutator.
	 * <p>
	 * [leftMultiply( exp(i*angle) ) ]
	 * 
	 * @param angle
	 *            Principle rotation (halved per factor multiplication)
	 */
	public Operator preExp_i(Principle angle) {
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return preExp_iAcute(angle) ;
			return (Operator) this.addMultiplyLeftI(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.preFlip_i();
			return (Operator) this.putLeftI(clone);
		}
		// return preExp_iObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addLeftI(clone);
	}

	/**
	 * Pre- sense J rotation mutator. 
	 * <p>
	 * [ leftMultiply( exp(j*angle) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator preExp_j(Principle angle) {		
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return preExp_jAcute(angle) ;
			return (Operator) this.addMultiplyLeftJ(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.preFlip_j();
			return (Operator) this.putLeftJ(clone);
		}
		// return preExp_jObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addLeftJ(clone);				
	}

	/**
	 * Pre- sense K rotation mutator. 
	 * <p>
	 * [ leftMultiply( exp(k*angle) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator preExp_k(Principle angle) {
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return preExp_kAcute(angle) ;
			return (Operator) this.addMultiplyLeftK(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.flip_k();
			return (Operator) this.putLeftK(clone);
		}
		// return preExp_kObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addLeftK(clone);								
	}

	/**
	 * Right sense i rotation mutator.
	 * <p>
	 * [Right.multiply( exp(i*angle) ) ]
	 * 
	 * @param angle
	 *            Principle rotation (halved per factor multiplication)
	 */
	public Operator exp_i(Principle angle) {
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return exp_iAcute(angle) ;
			return (Operator) this.addMultiplyRightI(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.flip_i();
			return (Operator) this.putRightI(clone);
		}
		// return exp_iObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addRightI(clone);
	}

	/**
	 * Right sense J rotation mutator. 
	 * <p>
	 * [ Right.multiply( exp(j*angle) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator exp_j(Principle angle) {		
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return exp_iAcute(angle) ;
			return (Operator) this.addMultiplyRightJ(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.flip_i();
			return (Operator) this.putRightJ(clone);
		}
		// return exp_iObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addRightJ(clone);				
	}

	/**
	 * Right sense K rotation mutator. 
	 * <p>
	 * [ Right.multiply( exp(k*angle) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator exp_k(Principle angle) {
		if (angle.isZero()) {
			return this;
		}
		Quaternion clone = new Quaternion(this);
		if (angle.isAcute()) {
			// return exp_iAcute(angle) ;
			return (Operator) this.addMultiplyRightK(angle.tanHalf(), clone);
		}
		if (angle.isStraight()) {
			// return this.flip_i();
			return (Operator) this.putRightK(clone);
		}
		// return exp_iObtuse(angle) ;
		return (Operator) this.multiply(angle.cotHalf()).addRightK(clone);								
	}

	/**
	 * Right sense K rotation mutator. [ Right multiply( exp(angle*v.unit()) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator exp(Principle angle, Vector3 v) {
		if (angle.isZero()) {
			return this;
		}
		Vector3 clone = new Vector3(v); 
		double m = clone.getAbs();
		if (angle.isAcute()) {
			clone.multiply(angle.tanHalf() / m);
			return (Operator) this.rightMultiply(new Quaternion(1.d, clone.getX(), clone.getY(), clone.getZ()));
		}
		if (angle.isStraight()) {
			clone.divide(m);
			return (Operator) this.rightMultiply(clone);
		}
		// (angle.isObtuse())
		clone.divide(m);
		return (Operator) this.rightMultiply(new Quaternion(angle.cotHalf(), clone.getX(), clone.getY(), clone.getZ()));
	}

	/**
	 * Right sense rotation mutator. [ Right multiply( exp(angle*v.unit()) ) ]
	 * 
	 * @param Angle
	 *            rotation (halved per factor multiplication)
	 */
	public Operator exp(Vector3 v) {
		
		Vector3 clone = new Vector3(v);
		double m = clone.getAbs();
		Principle angle = new Principle(Angle.inRadians(m));

		if (angle.isZero()) {
			return this;
		}
		if (angle.isAcute()) {
			clone.multiply(angle.tanHalf() / m);
			return (Operator) this.rightMultiply(new Quaternion(1.d, clone.getX(),clone.getY(),clone.getZ()));
		}
		if (angle.isStraight()) {
			clone.divide(m);
			return (Operator) this.rightMultiply(clone);
		}
		// (angle.isObtuse())
		clone.divide(m);
		return (Operator) this.rightMultiply(new Quaternion(angle.cotHalf(), clone.getX(),clone.getY(),clone.getZ()));	
	}

	/**
	 * mutator.
	 * <p>
	 * [Right] multiply( t )
	 * 
	 * @param q
	 *            Operator
	 **/
//	@Override
//	public Operator rightMultiply(Quaternion q) {
//		// return (Operator) multiply(q);
//		put(QuaternionMath.multiply(this, q));
//		return this;
//	}

	/**
	 * mutator.
	 * <p>
	 * Left multiply( t )
	 * 
	 * @param p
	 *            Operator
	 **/
//	@Override
//	public Operator leftMultiply(Quaternion p) {
//		// return (Operator) premultiply(p);
//		put(QuaternionMath.multiply(p, this));
//		return this;
//	}

	/**
	 * Mutator.
	 * 
	 * SLERP mutator:
	 */
	@Override
	public Operator slerp(Quaternion p, double t) {
		put(QuaternionMath.slerp(this, p, t));
		return this;
	}

	/**
	 * Mutator.
	 * 
	 * this image after operation by object:
	 */
	public Operator image(Quaternion object) {
		this.put(QuaternionMath.multiply(QuaternionMath.multiply(this, object),
				QuaternionMath.reciprocal(this)));
		return this;
	}


	/**
	 * Mutator.
	 * 
	 * this pre-image before operation by object:
	 */
	public Operator preImage(Quaternion object) {
		this.put(QuaternionMath.multiply(QuaternionMath.multiply(
				QuaternionMath.reciprocal(this), object), this));
		return this;
	}

}
