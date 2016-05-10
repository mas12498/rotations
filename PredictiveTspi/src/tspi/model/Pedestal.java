package tspi.model;

import rotation.Angle;
import rotation.Principle;
import rotation.Rotator;
import rotation.Vector3;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String _systemId; // system identifier
	//LOCATION:
	protected final Vector3 _efg = new Vector3(Vector3.NAN); // geocentric vector: EFG
	protected final Ellipsoid _wgs84 = new Ellipsoid(); //lat, lon angles, h meters geodetic ellipsoid coordinates	

	protected final T_EFG_NED _localT = new T_EFG_NED(); // local NED navigation from EFG and local vertical is normal Ellipsoid displacement
	
	//POSITIONING:
	protected final Vector3 _ned = new Vector3(Vector3.NAN); //local NED: North, East, Down
	protected final Positioning _pedestal = new Positioning(); //local RAE: Range, Azimuth, Elevation 
	
	protected final T_EFG_FRD _sensorT = new T_EFG_FRD(); //aperture FRD measurement from EFG orientation and aperture ranging distance

	


	public Pedestal( String id, Angle lat, Angle lon, double h) {
		//_wgs84: Constructed from Pedestal's geodetic-ellipsoid coordinates Latitude, Longitude, height
		this._systemId = id;
		
		this._wgs84.set(lat,lon, h); //Ellipsoid
		
		this._localT.set(this._wgs84);	//EFG_NED
		
		this._efg.put(this._wgs84.getGeocentric()); //Cartesian
		
		this.clearPedestalPositioning();
	}
	
	public String getSystemId() { return this._systemId; }
	
	public Vector3 getVectorNED() { return this._ned; }	
	public double getNorth() { return this._ned.getX(); } 
	public double getEast() { return this._ned.getY(); }
	public double getDown() { return this._ned.getZ(); }
	
	public Positioning getVectorPlot() { return this._pedestal; }
	public Double getRange() { return this._pedestal.getRange(); }
	public Angle getAzimuth() {	return this._pedestal.getAzimuth(); }
	public Angle getElevation() { return this._pedestal.getElevation(); }
	
	public T_EFG_FRD getSensorT() { return this._sensorT; }
	
	
	
	public Vector3 getEFG() { return this._efg; }	
	public double getE() { return this._efg.getX(); } 
	public double getF() { return this._efg.getY(); }
	public double getG() { return this._efg.getZ(); }
	
	public Ellipsoid getLLh() {return this._wgs84;}
	public Angle getLatitude() { return this._wgs84.getNorthLatitude(); }
	public Angle getLongitude() { return this._wgs84.getEastLongitude().signedPrincipleAngle(); }
	public double getHeight() { return this._wgs84.getEllipsoidHeight(); }

	public T_EFG_NED getLocalT
	() { return this._localT; }

	public void clearPedestalLocation() {
		_wgs84.set(Angle.EMPTY, Angle.EMPTY, Double.NaN);
		_efg.put(Vector3.NAN);
		_localT.clear();
	};
	
	public void clearPedestalPositioning(){
		_pedestal.set(Double.NaN, Angle.EMPTY, Angle.EMPTY);
		_ned.put(Vector3.NAN);
		_sensorT.clear();
	};
	
	public void setSystemId(String id) { this._systemId = id; }
	
	public void setLatitude(Angle lat) {
		this._wgs84.setNorthLatitude(lat);
		this._localT.set(_wgs84);		
		this._efg.put(_wgs84.getGeocentric());
	}
	
	public void setLongitude(Angle lon) {
		this._wgs84.setEastLongitude(lon);
		this._localT.set(_wgs84);		
		this._efg.put(_wgs84.getGeocentric());
	}
		
	public void setHeight(double meters) {
		this._wgs84.setEllipsoidHeight(meters);
		this._localT.set(_wgs84); 
		this._efg.put(_wgs84.getGeocentric());		
	}
	
	public void setE(double E) {
		this._efg.setX(E);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_efg);
		this._localT.set(_wgs84);
	}

	public void setF(double F) {
		this._efg.setY(F);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_efg);
		this._localT.set(_wgs84);
	}

	public void setG(double G) {
		this._efg.setZ(G);
		// this._wgs84.set(_geocentric);//with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._wgs84.setGeocentric(_efg);
		this._localT.set(_wgs84);
	}	
	
	// After location is set... deal with pedestal rotator and range positioning...
	
	public void setVectorPlot(Positioning position) {
		this._pedestal.set(position);
		
		this._sensorT.set(position, this._localT._horizontal);
	}

	public void setRange( double meters ) {
		this._pedestal.setRange(meters);
		
		this._sensorT.setRange(meters);
	}
	
	public void setAperturePosition(Angle azimuth, Angle elevation) {
		this._pedestal.setAzimuth(azimuth);
		this._pedestal.setElevation( elevation);
		
		this._sensorT.set(azimuth, elevation, this._localT._horizontal);
	}
	
	public void setVectorPlot(double range, Angle azimuth, Angle elevation) {
		this._pedestal.set(range, azimuth, elevation);
		
		this._sensorT.set(range, azimuth, elevation, this._localT._horizontal);
	}
	
	public void setAzimuth(Angle azimuth) {
		this._pedestal.setAzimuth(azimuth);
		
		this._sensorT.set(azimuth, _pedestal._elevation, this._localT._horizontal);
	}
	
	public void setElevation(Angle elevation) {
		this._pedestal.setElevation(elevation);
		
		this._sensorT.set(_pedestal._azimuth, elevation, this._localT._horizontal);
	}
	
	
	/** Updates pedestal RAE positioning [state] to point at geocentric location
	 * -- Assumes no atmosphere and WGS84 earth model in positioing.
	 * @param geocentricEFG Position of the taget in geocentric coordinates.  */
	public void pointToLocation(Vector3 geocentricEFG) {
		//Define geocentric vector from ped to target -- obtain pedestal.aperture range to target.
		Vector3 r_PT_G = new Vector3(geocentricEFG).subtract(_efg);
		pointTo(r_PT_G); //updates RAE _position and Rotator _positionGeodetic
	}
	
	
	
	public void pointTo(Vector3 offsetEFG){
		
		Rotator pointTwisted = T_EFG_FRD.pointTwisted(offsetEFG,this._localT._horizontal); 
		Principle pAzimuth = pointTwisted.getEuler_k_kji(); 
		Principle pElevation = pointTwisted.getEuler_j_kji(); 	
		this._pedestal.set(offsetEFG.getAbs(),pAzimuth.unsignedAngle() ,pElevation.signedAngle() );
		
		this._sensorT._direction.set(T_EFG_FRD.point(pAzimuth, pElevation,this._localT._horizontal));
		//this._aperture.pointEFG(offsetEFG, this._local._horizontal);	// if do not need the above to get the _plot state...			
	}
	
	public Vector3 vectorOffsetEFG(){
		return _sensorT._direction.getImage_i().multiply(_pedestal.getRange());
	}

	
	public String toString() { 
		return this._systemId 
				+ "("+ this._wgs84.getNorthLatitude().toDegrees(Angle.DIGITS) +", "+ this._wgs84.getEastLongitude().toDegrees(Angle.DIGITS)+", "+this.getHeight()+")"
				+"("+_pedestal.getAzimuth().toDegrees(Angle.DIGITS)+", "+_pedestal.getElevation().toDegrees(Angle.DIGITS)+")";
	}

}
