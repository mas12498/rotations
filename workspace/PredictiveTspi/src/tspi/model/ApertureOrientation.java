	package tspi.model;
	
	import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.QuaternionMath;
import rotation.Vector3;
	
	public class ApertureOrientation {
		protected final Angle _azimuth;
		protected final Angle _elevation;
		protected Double _range;
		
		public ApertureOrientation(double missingValue){
			_azimuth = Angle.inPiRadians(missingValue);
			_elevation = Angle.inPiRadians(missingValue);
			_range = missingValue;
		}
		public ApertureOrientation(ApertureOrientation template){
			_azimuth = new Angle(template._azimuth);
			_elevation = new Angle(template._elevation);
			_range = template._range;
		}

		/**
		 * @return the azimuth
		 */
		public Angle getAzimuth() {
			return _azimuth.unsignedPrincipleAngle();
		}
		/**
		 * @return the elevation
		 */
		public Angle getElevation() {
			return _elevation.signedPrincipleAngle();
		}
		/**
		 * @return the range
		 */
		public double getRange() {
			return _range;
		}

		public void set(final Angle az, final Angle el, Double rg ){
			_azimuth.set(az);
			_elevation.set(el);
			_range = rg;
		}

		/**
		 * @param azimuth the azimuth to set
		 */
		public void setAzimuth(Angle azimuth) {
			this._azimuth.set(azimuth);
		}
		/**
		 * @param elevation the elevation to set
		 */
		public void setElevation(Angle elevation) {
			this._elevation.set(elevation);
		}
		/**
		 * @param range the range to set
		 */
		public void setRange(Double range) {
			_range = range;
		}
		
		public void clear(double missingValue){
			_azimuth.set(Angle.inPiRadians(missingValue));
			_elevation.set(Angle.inPiRadians(missingValue));
			_range = missingValue;
		}

		public Operator op_AN(){
			return QuaternionMath.eulerRotate_kj(_azimuth.getPrinciple(), _elevation.getPrinciple());
		}
		
		public Vector3 getDirection(){
		return new Vector3(StrictMath.cos(_azimuth.getRadians()) * StrictMath.cos(_elevation.getRadians()),
				StrictMath.sin(_azimuth.getRadians()) * StrictMath.cos(_elevation.getRadians()),
				-StrictMath.sin(_elevation.getRadians()));
		}
		
	}
