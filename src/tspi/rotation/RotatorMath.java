package tspi.rotation;

public class RotatorMath {


	/** Conjugate static factory. */
	public static final Rotator conjugate(Rotator p) {
		return (Rotator) QuaternionMath.reciprocal(p);
	}

	/**
	 * Factory. Operator quaternion from Tait-Bryan | Centerline Euler Angle static
	 * factory.
	 * 
	 * @param pK (k-axis) Angle #1: yaw | az
	 * @param pJ (j-axis) Angle #2: pitch | el
	 * @return Quaternion body | centerline frame orientation
	 */
	public static final Rotator eulerRotate_kj(CodedPhase pK, CodedPhase pJ) {
		/**
		 * this algorithm has issues when pJ 90 or when greater than 90 and rotate pK (=
		 * 0|360)... or
		 */
		return RotatorMath.rotate_k(pK).rotate_j(pJ);
	}

	/**
	 * Rotator (Quaternion) operator from Tait-Bryan | Centerline Euler Angle static factory.
	 * 
	 * @param pK (k-axis) Angle #1: yaw | az
	 * @param pJ (j-axis) Angle #2: pitch | el
	 * @param pI (i-axis) Angle #3: roll | twist
	 * @return Quaternion body | centerline frame orientation
	 */
	public static final Rotator eulerRotate_kji(CodedPhase pK, CodedPhase pJ, CodedPhase pI) {
		return RotatorMath.rotate_k(pK).rotate_j(pJ).rotate_i(pI);
	}

	/**
	 * Exponentiate: Put <b><i>e</i></b> raised to this Rotator (Quaternion) power mutator
	 * <br>
	 * (e to the t cosine absolute value of V, e to the t sine of the absolute value
	 * of V times V normalized to V )
	 * 
	 */
	public static Rotator exp(Rotator p) {
		return (Rotator) QuaternionMath.exp(p);
	}

	/**
	 * The natural log of q mutator:
	 */
	public static Rotator ln(Rotator q) {
		return (Rotator) QuaternionMath.ln(q);
	}

	/**
	 * Hamiltonian Quaternion Product static factory.
	 * 
	 * @param Quaternion left.
	 * @param Quaternion right.
	 */
	public static Rotator multiply(Rotator left, Rotator right) {
		return (Rotator) QuaternionMath.multiply(left, right);
	}

	/**
	 * Hamiltonian Quaternion Product static factory.
	 * 
	 * @param Quaternion q (left).
	 * @param Vector3    v (right).
	 */
	public static Rotator multiply(Rotator q, Vector3 v) {
		return (Rotator) QuaternionMath.multiply(q, v);
	}

	/**
	 * Hamiltonian Quaternion Product static factory.
	 * 
	 * @param Vector3    v.
	 * @param Quaternion q.
	 */
	public static Rotator multiply(Vector3 v, Rotator q) {
		return (Rotator) QuaternionMath.multiply(v, q);
	}


	/**
	 * Reciprocal Quaternion static factory.
	 */
	public static Rotator reciprocal(Rotator p) {
		return (Rotator) QuaternionMath.reciprocal(p);
	}


	/**
	 * Rotation operator factory: Princple angle of theta about vector
	 * 
	 * @return FactoredQuaternion
	 */
	public static final Rotator rotate(CodedPhase theta, Vector3 v) {
		double m = v.getAbs();
		if (m == 0) {
			return new Rotator(Quaternion.IDENTITY);
		}
		if (theta.isAcute()) {
			return new Rotator(1.d, v.multiply(theta.tanHalf() / m));
		}
		return new Rotator(theta.cotHalf(), v.divide(m));
	}

	/**
	 * Rotation operator factory: Angle of vector magnitude about vector
	 * 
	 * @return FactoredQuaternion
	 */
	public static final Rotator rotate(Vector3 v) {
		double m = v.getAbs();
		if (m == 0) {
			return new Rotator(Quaternion.IDENTITY);
		}
		double t = StrictMath.tan(m);
//			double t = StrictMath.tan(StrictMath.IEEEremainder(m, StrictMath.PI));
		if (StrictMath.abs(t) > 1) {
			return new Rotator(1.d / t, v.divide(m));
		}
		return new Rotator(1.d, v.multiply(t / m));
	}

	/**
	 * I-axis rotation factory: (halved per factor multiplication)
	 * <p>
	 * [ exp(I*theta/2) ]
	 * 
	 * @return factoredQuaternion Operator
	 * @param theta Principle angle (right-handed rotation sense)
	 */
	public static final Rotator rotate_i(CodedPhase theta) {
		if (theta.isAcute()) {
			return new Rotator(1d, theta.tanHalf(), 0, 0);
		}
		return new Rotator(theta.cotHalf(), 1d, 0, 0);
	}


	/**
	 * J-axis rotation factory: (halved per factor multiplication)
	 * <p>
	 * [ exp(J*theta) ]
	 * 
	 * @return FactoredQuaternion operator
	 * @param theta Principle angle (right-handed rotation sense)
	 */
	public static final Rotator rotate_j(CodedPhase theta) {
		if (theta.isAcute()) {
			return new Rotator(1d, 0, theta.tanHalf(), 0);
		}
		return new Rotator(theta.cotHalf(), 0, 1d, 0);
	}

	/**
	 * K-axis rotation operator factory: (halved per factor multiplication)
	 * <p>
	 * [ exp(K*theta) ]
	 * 
	 * @return FactoredQuaternion
	 * @param theta Principle angle (right-handed rotation sense)
	 */
	public static final Rotator rotate_k(CodedPhase theta) {
		if (theta.isAcute()) {
			return new Rotator(1d, 0, 0, theta.tanHalf());
		}
		return new Rotator(theta.cotHalf(), 0, 0, 1d);
	}

	public static Rotator slerp(Rotator q0, Rotator q1, double t) {
		return (Rotator) slerp(q0,q1,t);
	}


	// public static functions:
	public static final Rotator tilt_i(Vector3 direction) {
		// I==0...
		double w = direction.getAbs() + direction.getX();
		if (w > direction.getY()) {
			if (w > direction.getZ()) {
				// I
				return new Rotator(1, 0, direction.getZ() / w, -direction.getY() / w);
			}
		} else {
			if (direction.getY() > direction.getZ()) {
				// J
				return new Rotator(-w / direction.getY(), 0, -direction.getZ() / direction.getY(), 1);
			}
		}
		// K
		return new Rotator(w / direction.getZ(), 0, 1, -direction.getY() / direction.getZ());
	}

	public static final Rotator tilt_j(Vector3 direction) {
		// J==0...
		double w = direction.getAbs() + direction.getY();
		if (w > direction.getX()) {
			if (w > direction.getZ()) {
				// J
				return new Rotator(1, -direction.getZ() / w, 0, direction.getX() / w);
			}

		} else {
			if (direction.getX() > direction.getZ()) {
				// I
				return new Rotator(w / direction.getX(), -direction.getZ() / direction.getX(), 0, 1);
			}
		}
		// K
		return new Rotator(-w / direction.getZ(), 1, 0, -direction.getX() / direction.getZ());
	}

	public static final Rotator tilt_k(Vector3 direction) {
		// K==0...
		double w = direction.getAbs() + direction.getZ();
		if (w > direction.getX()) {
			if (w > direction.getY()) {
				// K
				return new Rotator(1, direction.getY() / w, -direction.getX() / w, 0);
			}

		} else {
			if (direction.getY() > direction.getX()) {
				// J
				return new Rotator(w / direction.getY(), 1, -direction.getX() / direction.getY(), 0);
			}
		}
		// I
		return new Rotator(-w / direction.getX(), -direction.getY() / direction.getX(), 1, 0);
	}

	/** Quaternion versor (Unit-sized) static factory. */
	public static Quaternion unit(Quaternion p) {		
		return (Rotator) QuaternionMath.unit(p);
	}

}
