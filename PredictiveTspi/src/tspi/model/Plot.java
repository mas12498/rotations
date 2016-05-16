package tspi.model;

import rotation.Angle;
import rotation.QuaternionMath;
import rotation.Rotator;
import rotation.Vector3;

public class Plot {
	protected Double _range;
	protected final Angle _azimuth;
	protected final Angle _elevation;

	public Plot() {
		_range = Double.NaN;
		_azimuth = Angle.inPiRadians(Double.NaN);
		_elevation = Angle.inPiRadians(Double.NaN);
	}

	public Plot(Plot template) {
		_azimuth = new Angle(template._azimuth);
		_elevation = new Angle(template._elevation);
		_range = template._range;
	}

	/**
	 * @return the unsigned principle azimuth
	 */
	public Angle getAzimuth() {
		return _azimuth.unsignedPrincipleAngle();
	}

	/**
	 * @return the signed principle elevation
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

	public Vector3 getTopocentric() { // NED of navigation
		double RcosE = _range * StrictMath.cos(_elevation.getRadians());
		return new Vector3(RcosE * StrictMath.cos(_azimuth.getRadians()), RcosE * StrictMath.sin(_azimuth.getRadians()),
				-_range * StrictMath.sin(_elevation.getRadians()));
	}

	public void setTopocentric(Vector3 r_NED) { // NED of navigation
		_range = r_NED.getAbs();
		_azimuth.set( Angle.inRadians(StrictMath.atan2(r_NED.getX(), r_NED.getY())));
		double rg = StrictMath.hypot(r_NED.getX(), r_NED.getY());
		_elevation.set(Angle.inRadians(StrictMath.atan2(r_NED.getZ(), rg)));
	}

	public void set(Plot position) {
		_range = position._range;
		_azimuth.set(position._azimuth);
		_elevation.set(position._elevation);
	}

	public void set(double range, Angle azimuth, Angle elevation) {
		_range = range;
		_azimuth.set(azimuth);
		_elevation.set(elevation);
	}

	/**
	 * @param azimuth
	 *            the azimuth to set
	 */
	public void setAzimuth(Angle azimuth) {
		this._azimuth.set(azimuth);
	}

	/**
	 * @param elevation
	 *            the elevation to set
	 */
	public void setElevation(Angle elevation) {
		this._elevation.set(elevation);
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(Double range) {
		_range = range;
	}

	public void clear(double missingValue) {
		_azimuth.set(Angle.inPiRadians(missingValue));
		_elevation.set(Angle.inPiRadians(missingValue));
		_range = missingValue;
	}

	public Rotator positionLocal() {
		// @MAS: Maybe this method should be removed from inside this class...
		return QuaternionMath.eulerRotate_kj(_azimuth.getPrinciple(), _elevation.getPrinciple());
	}

}
