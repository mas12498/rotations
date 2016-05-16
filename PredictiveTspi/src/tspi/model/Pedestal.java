package tspi.model;

import rotation.Angle;
import rotation.Principle;
import rotation.Rotator;
import rotation.Vector3;

//import org.apache.commons.math3.linear.*;

/**  */
public class Pedestal {
	
	protected String _systemId; // system identifier
	
	//GEO-LOCATION coordinates and local frame transformation:
	protected final Vector3 _efg = new Vector3(Vector3.NAN); // geocentric vector: EFG
	protected final Ellipsoid _geodetic = new Ellipsoid(); //lat, lon angles, h meters geodetic ellipsoid coordinates	

	protected final T_EFG_NED _local = new T_EFG_NED(); // local NED navigation from EFG and local vertical is normal Ellipsoid displacement
	
	//VECTOR-PLOT coordinates and measurement frame transformation:
	protected final Vector3 _ned = new Vector3(Vector3.NAN); //local NED: North, East, Down
	protected final Plot _topocentric = new Plot(); //local RAE: Range, Azimuth, Elevation 
	
	protected final T_EFG_FRD _aperture = new T_EFG_FRD(); //aperture FRD measurement from EFG orientation and aperture ranging distance
	

	public Pedestal( String id, Angle lat, Angle lon, double h) {
		//_wgs84: Constructed from Pedestal's geodetic-ellipsoid coordinates Latitude, Longitude, height
		this._systemId = id;
		
		this._geodetic.set(lat,lon, h); //Ellipsoid
		
		this._local.set(this._geodetic);	//T_EFG_NED
		
		this._efg.put(this._geodetic.getGeocentric()); //Cartesian
		
		this.clearPedestalPositioning();
	}
	
	public String getSystemId() { return this._systemId; }
	
	public Vector3 getVectorNED() { return this._ned; }	
	public double getNorth() { return this._ned.getX(); } 
	public double getEast() { return this._ned.getY(); }
	public double getDown() { return this._ned.getZ(); }
	
	public Plot getVectorPlot() { return this._topocentric; }
	public Double getRange() { return this._topocentric.getRange(); }
	public Angle getAzimuth() {	return this._topocentric.getAzimuth(); }
	public Angle getElevation() { return this._topocentric.getElevation(); }
	
	public T_EFG_FRD getSensorT() { return this._aperture; }
	
	
	
	public Vector3 getEFG() { return this._efg; }	
	public double getE() { return this._efg.getX(); } 
	public double getF() { return this._efg.getY(); }
	public double getG() { return this._efg.getZ(); }
	
	public Ellipsoid getLLh() {return this._geodetic;}
	public Angle getLatitude() { return this._geodetic.getNorthLatitude(); }
	public Angle getLongitude() { return this._geodetic.getEastLongitude().signedPrincipleAngle(); }
	public double getHeight() { return this._geodetic.getEllipsoidHeight(); }

	public T_EFG_NED getLocalT
	() { return this._local; }

	public void clearPedestalLocation() {
		_geodetic.set(Angle.EMPTY, Angle.EMPTY, Double.NaN);
		_efg.put(Vector3.NAN);
		_local.clear();
	};
	
	public void clearPedestalPositioning(){
		_topocentric.set(Double.NaN, Angle.EMPTY, Angle.EMPTY);
		_ned.put(Vector3.NAN);
		_aperture.clear();
	};
	
	public void setSystemId(String id) { this._systemId = id; }
	
	public void setLatitude(Angle lat) {
		this._geodetic.setNorthLatitude(lat);
		this._local.set(_geodetic);		
		this._efg.put(_geodetic.getGeocentric());
	}
	
	public void setLongitude(Angle lon) {
		this._geodetic.setEastLongitude(lon);
		this._local.set(_geodetic);		
		this._efg.put(_geodetic.getGeocentric());
	}
		
	public void setHeight(double meters) {
		this._geodetic.setEllipsoidHeight(meters);
		this._local.set(_geodetic); 
		this._efg.put(_geodetic.getGeocentric());		
	}
	
	public void setE(double E) {
		this._efg.setX(E);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._geodetic.set(_efg);
		this._local.set(_geodetic);
	}

	public void setF(double F) {
		this._efg.setY(F);
		// this._wgs84.set(_geocentric); //with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._geodetic.set(_efg);
		this._local.set(_geodetic);
	}

	public void setG(double G) {
		this._efg.setZ(G);
		// this._wgs84.set(_geocentric);//with moving pedestal -- could avoid need for Ellipsoid calculations below...
		this._geodetic.set(_efg);
		this._local.set(_geodetic);
	}	
	
	// After location is set... deal with pedestal rotator and range positioning...
	
	public void setVectorPlot(Plot position) {
		this._topocentric.set(position);
		
		this._aperture.set(position, this._local._horizontal);
	}

	public void setRange( double meters ) {
		this._topocentric.setRange(meters);
		
		this._aperture.setRange(meters);
	}
	
	public void setAperturePosition(Angle azimuth, Angle elevation) {
		this._topocentric.setAzimuth(azimuth);
		this._topocentric.setElevation( elevation);
		
		this._aperture.set(azimuth, elevation, this._local._horizontal);
	}
	
	public void setVectorPlot(double range, Angle azimuth, Angle elevation) {
		this._topocentric.set(range, azimuth, elevation);
		
		this._aperture.set(range, azimuth, elevation, this._local._horizontal);
	}
	
	public void setAzimuth(Angle azimuth) {
		this._topocentric.setAzimuth(azimuth);
		
		this._aperture.set(azimuth, _topocentric._elevation, this._local._horizontal);
	}
	
	public void setElevation(Angle elevation) {
		this._topocentric.setElevation(elevation);
		
		this._aperture.set(_topocentric._azimuth, elevation, this._local._horizontal);
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
		
		Rotator pointTwisted = T_EFG_FRD.pointTwisted(offsetEFG,this._local._horizontal); 
		Principle pAzimuth = pointTwisted.getEuler_k_kji(); 
		Principle pElevation = pointTwisted.getEuler_j_kji(); 	
		this._topocentric.set(offsetEFG.getAbs(),pAzimuth.unsignedAngle() ,pElevation.signedAngle() );
		
		this._aperture._direction.set(T_EFG_FRD.point(pAzimuth, pElevation,this._local._horizontal));
		//this._aperture.pointEFG(offsetEFG, this._local._horizontal);	// if do not need the above to get the _plot state...			
	}
	
	public Vector3 vectorOffsetEFG(){
		return _aperture._direction.getImage_i().multiply(_topocentric.getRange());
	}

	
	public String toString() { 
		return this._systemId 
				+ "("+ this._geodetic.getNorthLatitude().toDegrees(Angle.DIGITS) +", "+ this._geodetic.getEastLongitude().toDegrees(Angle.DIGITS)+", "+this.getHeight()+")"
				+"("+_topocentric.getAzimuth().toDegrees(Angle.DIGITS)+", "+_topocentric.getElevation().toDegrees(Angle.DIGITS)+")";
	}

}
