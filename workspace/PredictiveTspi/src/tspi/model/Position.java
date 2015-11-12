	package tspi.model;
	
	import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.QuaternionMath;
import rotation.Vector3;
	
	public class Position {
		protected final Principle _azimuth;
		protected final Principle _elevation;
		protected double _range;
		
		public Position(final Angle az, final Angle el, double rg ){
			_azimuth = new Principle(az);
			_elevation = new Principle(el);
			_range = rg;
		}
		/**
		 * @return the azimuth
		 */
		public Angle getAzimuth() {
			return _azimuth.unsignedAngle();
		}
		/**
		 * @return the elevation
		 */
		public Angle getElevation() {
			return _elevation.signedAngle();
		}
		
		protected Principle getPrincipleAzimuth() {
			return _azimuth;
		}
		/**
		 * @param azimuth the azimuth to set
		 */
		public void setAzimuth(Angle azimuth) {
			this._azimuth.put(azimuth);
		}
		/**
		 * @return the elevation
		 */
		protected Principle getPrincipleElevation() {
			return _elevation;
		}
		/**
		 * @param elevation the elevation to set
		 */
		public void setElevation(Angle elevation) {
			this._elevation.put(elevation);
		}
		/**
		 * @return the range
		 */
		public double getRange() {
			return _range;
		}
		/**
		 * @param range the range to set
		 */
		public void setRange(double range) {
			this._range = range;
		}
		
		public Operator op_A_N(){
			return QuaternionMath.eulerRotate_kj(_azimuth, _elevation);
		}
		
		public Vector3 getDirection(){
			return new Vector3(_azimuth.cos()*_elevation.cos(),_azimuth.sin()*_elevation.cos(), -_elevation.sin());	
		}
		
	}
