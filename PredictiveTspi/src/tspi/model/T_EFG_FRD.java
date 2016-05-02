package tspi.model;

import rotation.Angle;
import rotation.Principle;
import rotation.Rotator;
import rotation.Vector3;

public class T_EFG_FRD {
	
	protected final Rotator _direction; // = new Rotator(); //rotater
	protected double _range; // = Double.NaN;  //translater		

	protected final Rotator _positionGeodetic = new Rotator(Rotator.EMPTY); //aperture range, horz-right, vert-down  {RHV} to geocentric {EFG}: A-->G

	/**
	 * Constructor: initialize 'Empty'.
	 */
	public T_EFG_FRD(){ //aperture emit {Forward,Right,Downward}
		_range = Double.NaN;
		_direction = new Rotator(Rotator.EMPTY);
	}	

	
	/**
	 * Geodetic local copy-constructor:
	 */
	public T_EFG_FRD(T_EFG_FRD aperture){
		_range = aperture._range;
		_direction = new Rotator(aperture._direction);
	}


	/** 
	 * Clears this Geodetic location -- re-initializes as empty.
	 */
	public void clear(){			
		_range = Double.NaN;
		_direction.set(Rotator.EMPTY);
	}

	
	public void set(Mount plot, Rotator localHorizontal) {
		_range = plot._range;
		_direction.set(localHorizontal);
		_direction.rightMultExpK(plot._azimuth.getPrinciple()).rightMultExpJ(plot._elevation.getPrinciple());
	}

	public void set(Angle paz, Angle pel, Rotator localHorizontal) {
		_direction.set(localHorizontal);
		_direction.rightMultExpK(paz.getPrinciple()).rightMultExpJ(pel.getPrinciple());
	}

	public void set(double prg, Angle paz, Angle pel, Rotator localHorizontal) {
		_range = prg;
		_direction.set(localHorizontal);
		_direction.rightMultExpK(paz.getPrinciple()).rightMultExpJ(pel.getPrinciple());
	}

	
	public  static Rotator pointTwisted(Vector3 r_EFG, Rotator localHorizontal){
		Rotator twistedPositionGeodetic = new Rotator(localHorizontal); // init topocentric orientation
		twistedPositionGeodetic.leftMultiplyTiltI(r_EFG).conjugate(); //pre-point [r_EFG] fold-over
		return twistedPositionGeodetic;
	}

	public  static Rotator point(Principle pAzimuth, Principle pElevation, Rotator localHorizontal){ 
		Rotator positionGeodetic = new Rotator(localHorizontal); // init topocentric orientation
		positionGeodetic.rightMultExpK(pAzimuth).rightMultExpJ(pElevation); //definition pedestal Euler angles -- no twist.
		return positionGeodetic;
	}

	public void pointEFG(Vector3 offsetEFG, Rotator localHorizon){	
		Rotator pointTwisted = T_EFG_FRD.pointTwisted(offsetEFG,localHorizon); 
		Principle pAzimuth = pointTwisted.getEuler_k_kji(); 
		Principle pElevation = pointTwisted.getEuler_j_kji();
		//assume not interested in range here...
		//_range = offsetEFG.getAbs(); //not meaningful if undefined or unit direction.
		_direction.set(T_EFG_FRD.point(pAzimuth,pElevation,localHorizon));
	}

	
	
	public double get_range() {
		return _range;
	}


	public void setRange(double _range) {
		this._range = _range;
	}


	public Rotator get_direction() {
		return _direction;
	}
	

	
	

}
