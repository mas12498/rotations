package tspi.model;

import tspi.rotation.Angle;
import tspi.rotation.CodedPhase;
import tspi.rotation.Rotator;
import tspi.rotation.Vector3;

public class T_geocentric_body {
	
	protected final Rotator _orientation; // = new Rotator(); //rotater
	protected double _range; // = Double.NaN;  //translater		

	//protected final Rotator _positionGeodetic = new Rotator(Rotator.EMPTY); //aperture range, horz-right, vert-down  {RHV} to geocentric {EFG}: A-->G

	/**
	 * Constructor: initialize 'Empty'.
	 */
	public T_geocentric_body(){ //aperture emit {Forward,Right,Downward}
		_range = Double.NaN;//expand def with axis levers?? parallax offsets???? registration?????
		_orientation = new Rotator(Rotator.EMPTY);
	}	

	
	/**
	 * Geodetic local copy-constructor:
	 */
	public T_geocentric_body(T_geocentric_body aperture){
		_range = aperture._range;
		_orientation = new Rotator(aperture._orientation);
	}


	/** 
	 * Clears this Geodetic location -- re-initializes as empty.
	 */
	public void clear(){			
		_range = Double.NaN;
		_orientation.set(Rotator.EMPTY);
	}

	
	public void set(RAE plot, Rotator localHorizontal) {
		_range = plot._range;
//		_orientation.set(localHorizontal);
//		_orientation.rotate_k(plot._azimuth.codedPhase()).rotate_j(plot._elevation.codedPhase());
		_orientation.set( NED_to_FRD(localHorizontal, plot._azimuth.codedPhase(), plot._elevation.codedPhase()) );
	}

	public void set(Angle paz, Angle pel, Rotator localHorizontal) {
//		_orientation.set(localHorizontal);
//		_orientation.rotate_k(paz.codedPhase()).rotate_j(pel.codedPhase());
		_orientation.set( NED_to_FRD(localHorizontal, paz.codedPhase(), pel.codedPhase()) );
	}

	public void set(double pedRg, Angle pedAz, Angle pedEl, Rotator pedLocal) {
		_range = pedRg;
//		_orientation.set(pedLocal);
//		_orientation.rotate_k(pedAz.codedPhase()).rotate_j(pedEl.codedPhase());
		_orientation.set( NED_to_FRD(pedLocal, pedAz.codedPhase(), pedEl.codedPhase()) );
	}
	
	public static Rotator NED_to_FRD(Rotator qNED, CodedPhase paz, CodedPhase pel) {
		Rotator qFRD = new Rotator(qNED).rotate_k(paz).rotate_j(pel);
		return qFRD;
	}

	
//	public  static Rotator direct(Vector3 r_EFG, Rotator localHorizontal){
////		Rotator twistedPositionGeodetic = new Rotator(localHorizontal); // init topocentric orientation
////		twistedPositionGeodetic.preTilt_i(r_EFG).conjugate(); //pre-point [r_EFG] fold-over
////		return twistedPositionGeodetic;
//		return (Rotator) new Rotator(localHorizontal).preTilt_i(r_EFG).conjugate();
//	}
	
	public  static Rotator face(CodedPhase pAzimuth, CodedPhase pElevation, Rotator localHorizontal){ 
		Rotator positionGeodetic = new Rotator(localHorizontal); // init topocentric orientation
		positionGeodetic.rotate_k(pAzimuth).rotate_j(pElevation); //definition pedestal Euler angles -- no twist.
		return positionGeodetic;
	}

	
	protected  static RAE commandLocal(Vector3 r_EFG, Rotator pedestalLocal){
		Rotator los = (Rotator) new Rotator(pedestalLocal).preTilt_i(r_EFG).conjugate();
		return new RAE(r_EFG.getAbs(), los.getEuler_k_kji().angle().signedPrinciple(), los.getEuler_j_kji().angle().signedPrinciple());
	}

//	public Rotator positionLocal() {
//		// @MAS: Maybe this method should be removed from inside this class...
//		return QuaternionMath.eulerRotate_kj(_azimuth.getPrinciple(), _elevation.getPrinciple());
//	}

	
	
	public void orient(Vector3 offsetEFG, Rotator localHorizon){	
		RAE localVector = commandLocal(offsetEFG,localHorizon);
		_orientation.set(T_geocentric_body.face(localVector.getUnsignedAzimuth().codedPhase()
				,localVector.getElevation().codedPhase(),localHorizon));
	}

	public double getRange() {
		return _range;
	}

	public void setRange(double _range) {
		this._range = _range;
	}

	public Rotator getOrientation() {
		return _orientation;
	}

	public Vector3 getVector() {
		return this._orientation.getImage_i().multiply(_range);
	}	
	
	public Vector3 getForwardUnit() {
		return this._orientation.getImage_i();
	}
	
	public Vector3 getRightUnit() {
		return this._orientation.getImage_j();
	}	
	
	public Vector3 getDownUnit() {
		return this._orientation.getImage_k();
	}	
	

}
