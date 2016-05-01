/**
 * 
 */
package hold;

import rotation.Principle;


/**
 * Class <b><i>Op</i></b> decorates rotation package classes with <i>sign-sense</i> attribution.
 * <p>Attribution of { <b><i>LEFT</i></b> | <b><i>RIGHT</i></b> } signifies: 
 * <br>-- handedness to crossed-axis (angle-axis) definitions.
 * <br>-- algebraic sequence to operator <i>application</i> in operator chains.
 * <p>Methods adapt between <i>sign-sense</i> attributions for unit, Angle, and Principle angle measures. 
 * 
 * @author mike
 */
public enum HandRule {

	LEFT(-1), RIGHT(1);

	protected int sign;
	
	/** private constructor */
	private HandRule(int ptrSide) { sign = ptrSide; }
	
	public boolean isRight() { return (sign == 1) ? true : false; }
//	public boolean isLeft() { return (sign == -1) ? true : false; }
	
//	/** @return sign-sense */
//	public int sign() {return sign;}

	/** @return <b>angleUnits</b> copy <i>with</i> reverse <i>sign-sense</i> attribution. */
	public static double reverse(double angleUnits) {
		return -angleUnits;
	}
//	/** @return <b>angle</b> copy <i>with</i> reverse <i>sign-sense</i> attribution. */	
//	public static Angle reverse(Angle angle) {
//		return new Angle(-angle.getRadians());
//	}
	/** @return <b>angle</b> copy <i>with</i> reverse <i>sign-sense</i> attribution. */	
	public static Principle reverse(Principle angle) {
		return new Principle(angle).negate();
	}

	/** @return <b>angleUnits</b> copy <i>with</i> <b><i>RIGHT</b></i> <i>sign-sense</i> attribution. */	
	public double right(double angleUnits) {
		return (sign == 1) ? angleUnits : -angleUnits;
	}
//	/** @return <b>angle</b> copy <i>with</i> <b><i>RIGHT</b></i> <i>sign-sense</i> attribution. */	
//	public Angle right(Angle angle) {
//		return (sign == 1) ? new Angle(angle.getRadians()) : new Angle(-angle.getRadians());
//	}
	/** @return <b>angle</b> copy <i>with</i> <b><i>RIGHT</b></i> <i>sign-sense</i> attribution. */	
	public Principle right(Principle angle) {
		return (sign == 1) ? new Principle(angle) : new Principle(angle).negate();
	}
	
	/** @return <b>angleUnits</b> copy <i>with</i> <b><i>LEFT</b></i> <i>sign-sense</i> attribution. */	
	public double left(double angleUnits) {
		return (sign == 1) ? -angleUnits : angleUnits;
	}
//	/** @return <b>angle</b> copy <i>with</i> LEFT <i>sign-sense</i> attribution. */	
//	public Angle left(Angle angle) {
//		return (sign == 1) ? new Angle(-angle.getRadians()) : new Angle(angle.getRadians());
//	}	
	/** @return <b>angle</b> copy <i>with</i> LEFT <i>sign-sense</i> attribution. */	
	public Principle left(Principle angle) {
		return (sign == 1) ? new Principle(angle).negate() : new Principle(angle);
	}

	
}


