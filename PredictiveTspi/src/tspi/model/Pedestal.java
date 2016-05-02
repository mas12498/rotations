package tspi.model;

import rotation.Angle;
import rotation.Rotator;
import rotation.Principle;
import rotation.Vector3;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String _systemId; // system identifier
	//LOCATION:
	protected final Vector3 _geocentric = new Vector3(Vector3.NAN); // geocentric position vector: EFG
	protected final Ellipsoid _wgs84 = new Ellipsoid(); //lat, lon angles, h meters ellipsoid coordinates	
	protected final T_EFG_NED _local = new T_EFG_NED(); // local NED navigation state from EFG and local vertical Ellipsoid displacement
	//POSITIONING:
	protected final Vector3 _topocentric = new Vector3(Vector3.NAN); //local NED: North, East, Down
	protected final Mount _sensor = new Mount(); //local RAE: Range, Azimuth, Elevation 
	protected final T_EFG_FRD _position = new T_EFG_FRD(); //aperture FRD measurement state from EFG and aperture ranging distance
	

	public Pedestal( String id, Angle lat, Angle lon, double h) {
		//_wgs84: Constructed from Pedestal's geodetic-ellipsoid coordinates Latitude, Longitude, height
		this._systemId = id;
		
		this._wgs84.set(lat,lon, h); //Ellipsoid
		
		this._local.set(_wgs84);	//EFG_NED
		
		this._geocentric.put(_wgs84.getGeocentric()); //Cartesian
		
		this.clearPedestalPositioning();
	}
	
	public String getSystemId() { return this._systemId; }
	
	public Vector3 getNED() { return this._topocentric; }	
	public double getNorth() { return this._topocentric.getX(); } 
	public double getEast() { return this._topocentric.getY(); }
	public double getDown() { return this._topocentric.getZ(); }
	
	public Mount getPlot() { return this._sensor; }
	public Double getRange() { return this._sensor.getRange(); }
	public Angle getAzimuth() {	return this._sensor.getAzimuth(); }
	public Angle getElevation() { return this._sensor.getElevation(); }
	
	public T_EFG_FRD getAperture() { return this._position; }
	
	
	
	public Vector3 getEFG() { return this._geocentric; }	
	public double getE() { return this._geocentric.getX(); } 
	public double getF() { return this._geocentric.getY(); }
	public double getG() { return this._geocentric.getZ(); }
	
	public Ellipsoid getLLh() {return this._wgs84;}
	public Angle getLatitude() { return this._wgs84.getNorthLatitude(); }
	public Angle getLongitude() { return this._wgs84.getEastLongitude(); }
	public double getHeight() { return this._wgs84.getEllipsoidHeight(); }

	public T_EFG_NED getWGS84() { return this._local; }

	public void clearPedestalLocation() {
		_wgs84.set(Angle.EMPTY, Angle.EMPTY, Double.NaN);
		_geocentric.put(Vector3.NAN);
		_local.clear();
	};
	
	public void clearPedestalPositioning(){
		_sensor.set(Double.NaN, Angle.EMPTY, Angle.EMPTY);
		_topocentric.put(Vector3.NAN);
		_position.clear();
	};
	
	public void setSystemId(String id) { this._systemId = id; }
	
	public void setLatitude(Angle lat) {
		this._wgs84.setNorthLatitude(lat);
		this._local.set(_wgs84);		
		this._geocentric.put(_wgs84.getGeocentric());
	}
	
	public void setLongitude(Angle lon) {
		this._wgs84.setEastLongitude(lon);
		this._local.set(_wgs84);		
		this._geocentric.put(_wgs84.getGeocentric());
	}
		
	public void setHeight(double meters) {
		this._wgs84.setEllipsoidHeight(meters);
		this._local.set(_wgs84); 
		this._geocentric.put(_wgs84.getGeocentric());		
	}
	
	public void setE(double E) {
		this._geocentric.setX(E);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_geocentric);
		this._local.set(_wgs84);
	}

	public void setF(double F) {
		this._geocentric.setY(F);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_geocentric);
		this._local.set(_wgs84);
	}

	public void setG(double G) {
		this._geocentric.setZ(G);
		// this._wgs84.set(_geocentric);//with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_geocentric);
		this._local.set(_wgs84);
	}	
	
	// After location is set... deal with pedestal rotator and range positioning...
	
	public void setRAE(Mount position) {
		this._sensor.set(position);
		
		this._position.set(position, this._local._horizontal);
	}

	public void setRange( double meters ) {
		this._sensor.setRange(meters);
		
		this._position.setRange(meters);
	}
	
	public void setAperturePosition(Angle azimuth, Angle elevation) {
		this._sensor.setAzimuth(azimuth);
		this._sensor.setElevation( elevation);
		
		this._position.set(azimuth, elevation, this._local._horizontal);
	}
	
	public void setAperturePosition(double range, Angle azimuth, Angle elevation) {
		this._sensor.set(range, azimuth, elevation);
		
		this._position.set(range, azimuth, elevation, this._local._horizontal);
	}
	
	public void setAzimuth(Angle azimuth) {
		this._sensor.setAzimuth(azimuth);
		
		this._position.set(azimuth, _sensor._elevation, this._local._horizontal);
	}
	
	public void setElevation(Angle elevation) {
		this._sensor.setElevation(elevation);
		
		this._position.set(_sensor._azimuth, elevation, this._local._horizontal);
	}
	
	
	/** Updates pedestal RAE positioning [state] to point at geocentric location
	 * -- Assumes no atmosphere and WGS84 earth model in positioing.
	 * @param locationGeocentric Position of the taget in geocentric coordinates.  */
	public void pointGeocentric(Vector3 locationGeocentric) {
		//Define geocentric vector from ped to target -- obtain pedestal.aperture range to target.
		Vector3 r_PT_G = new Vector3(locationGeocentric).subtract(_geocentric);
		pointEFG(r_PT_G); //updates RAE _position and Rotator _positionGeodetic
	}
	
	
	
	public void pointEFG(Vector3 offsetPedestalEFG){
		
		Rotator pointTwisted = T_EFG_FRD.pointTwisted(offsetPedestalEFG,this._local._horizontal); 
		Principle pAzimuth = pointTwisted.getEuler_k_kji(); 
		Principle pElevation = pointTwisted.getEuler_j_kji(); 	
		this._sensor.set(offsetPedestalEFG.getAbs(),pAzimuth.unsignedAngle() ,pElevation.signedAngle() );
		
		this._position._direction.set(T_EFG_FRD.point(pAzimuth, pElevation,this._local._horizontal));
		//this._aperture.pointEFG(offsetEFG, this._local._horizontal);	// if do not need the above to get the _plot state...			
	}
	
	public Vector3 directionEFG(){
		return _position._direction.getImage_i().multiply(_sensor.getRange());
	}

	
	public String toString() { 
		return this._systemId 
				+ "("+ this._wgs84.getNorthLatitude().toDegrees(8) +", "+ this._wgs84.getEastLongitude().toDegrees(8)+", "+this.getHeight()+")"
				+"("+_sensor.getAzimuth().toDegrees(4)+", "+_sensor.getElevation().toDegrees(4)+")";
	}

}
