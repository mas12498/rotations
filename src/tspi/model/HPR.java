package tspi.model;
import tspi.rotation.Angle;
import tspi.rotation.Rotator;
import tspi.rotation.Vector3;

public class HPR {
	
	protected final Angle _heading;
	protected final Angle _pitch;
	protected final Angle _roll;


		public HPR() {
			_heading = new Angle();
			_pitch = new Angle();
			_roll = new Angle();
		}
		
		public  HPR( Angle heading, Angle pitch, Angle roll ) {
			_heading = heading;
			_pitch = pitch;
			_roll = roll;
		}

		public HPR(HPR template) {
			_heading = new Angle(template._heading);
			_pitch = new Angle(template._pitch);
			_roll = new Angle(template._roll);
		}

		/**
		 * @return the unsigned principle azimuth Angle
		 */
		public Angle getUnsignedHeading() {
			return _heading.unsignedPrinciple();
		}

		/**
		 * @return the signed principle heading Angle
		 */
		public Angle getSignedHeading() {
			return _heading.signedPrinciple();
		}

		/**
		 * @return the body pitch
		 */
		public Angle getPitch() {
			return _pitch.signedPrinciple();
		}

		
		/**
		 * @return body roll
		 */
		public Angle getRoll() {
			return _roll.signedPrinciple();
		}

//		public Vector3 getNED() { // NED of navigation
//			double RcosE = _heading * StrictMath.cos(_roll.getRadians());
//			return new Vector3(
//					RcosE * StrictMath.cos(_pitch.getRadians())
//					, RcosE * StrictMath.sin(_pitch.getRadians()),
//					-_heading * StrictMath.sin(_roll.getRadians())
//					);
//		}

//		public void setNED(Vector3 r_NED) { // NED of navigation
//			
//			_heading = r_NED.getAbs(); //always positive in this construction!!!
//			
//			_pitch.set( Angle.inRadians(StrictMath.atan2(r_NED.getY(), r_NED.getX())));
//			
//			double rg = StrictMath.hypot(r_NED.getX(), r_NED.getY());
//			
//			_roll.set(Angle.inRadians(StrictMath.atan2(-r_NED.getZ(), rg)));
//			
//		}

		public void set(HPR orientation) {
			_heading.set(orientation._heading);
			_pitch.set(orientation._pitch);
			_roll.set(orientation._roll);
		}

		public void set(Angle range, Angle azimuth, Angle elevation) {
			_heading.set(range);
			_pitch.set(azimuth);
			_roll.set(elevation);
		}

		/**
		 * @param heading
		 *            the body-k Euler angle KJI
		 */
		public void setHeading(Angle heading) {
			_heading.set(heading);
		}

		/**
		 * @param pitch
		 *            the body-J Euler angle KJI
		 */
		public void setPitch(Angle pitch) {
			this._pitch.set(pitch);
		}

		/**
		 * @param roll
		 *            the body-I Euler angle KJI
		 */
		public void setRoll(Angle roll) {
			this._roll.set(roll);
		}

		public void clear(double missingValue) {
			_pitch.set(Angle.EMPTY);
			_roll.set(Angle.EMPTY);
			_heading.set(Angle.EMPTY);
		}
		
		public void clear() {
			_pitch.set(Angle.EMPTY);
			_roll.set(Angle.EMPTY);
			_heading.set(Angle.EMPTY);
		}
	

}
