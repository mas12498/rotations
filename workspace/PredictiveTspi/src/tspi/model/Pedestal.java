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
	protected final EFG_NED _local = new EFG_NED(); // local NED navigation state from EFG and local vertical Ellipsoid displacement
	//POSITIONING:
	protected final Vector3 _topocentric = new Vector3(Vector3.NAN); //local NED: North, East, Down
	protected final RAE _plot = new RAE(); //local RAE: Range, Azimuth, Elevation 
	protected final EFG_FRD _aperture = new EFG_FRD(); //aperture FRD measurement state from EFG and aperture ranging distance
	

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
	
	public RAE getPlot() { return this._plot; }
	public Double getRange() { return this._plot.getRange(); }
	public Angle getAzimuth() {	return this._plot.getAzimuth(); }
	public Angle getElevation() { return this._plot.getElevation(); }
	
	public EFG_FRD getAperture() { return this._aperture; }
	
	
	
	public Vector3 getEFG() { return this._geocentric; }	
	public double getE() { return this._geocentric.getX(); } 
	public double getF() { return this._geocentric.getY(); }
	public double getG() { return this._geocentric.getZ(); }
	
	public Ellipsoid getLLh() {return this._wgs84;}
	public Angle getLatitude() { return this._wgs84.getNorthLatitude(); }
	public Angle getLongitude() { return this._wgs84.getEastLongitude(); }
	public double getHeight() { return this._wgs84.getEllipsoidHeight(); }

	public EFG_NED getWGS84() { return this._local; }

	public void clearPedestalLocation() {
		_wgs84.set(Angle.EMPTY, Angle.EMPTY, Double.NaN);
		_geocentric.put(Vector3.NAN);
		_local.clear();
	};
	
	public void clearPedestalPositioning(){
		_plot.set(Double.NaN, Angle.EMPTY, Angle.EMPTY);
		_topocentric.put(Vector3.NAN);
		_aperture.clear();
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
	
	public void setRAE(RAE position) {
		this._plot.set(position);
		
		this._aperture.set(position, this._local._horizontal);
	}

	public void setRange( double meters ) {
		this._plot.setRange(meters);
		
		this._aperture.setRange(meters);
	}
	
	public void setAperturePosition(Angle azimuth, Angle elevation) {
		this._plot.setAzimuth(azimuth);
		this._plot.setElevation( elevation);
		
		this._aperture.set(azimuth, elevation, this._local._horizontal);
	}
	
	public void setAperturePosition(double range, Angle azimuth, Angle elevation) {
		this._plot.set(range, azimuth, elevation);
		
		this._aperture.set(range, azimuth, elevation, this._local._horizontal);
	}
	
	public void setAzimuth(Angle azimuth) {
		this._plot.setAzimuth(azimuth);
		
		this._aperture.set(azimuth, _plot._elevation, this._local._horizontal);
	}
	
	public void setElevation(Angle elevation) {
		this._plot.setElevation(elevation);
		
		this._aperture.set(_plot._azimuth, elevation, this._local._horizontal);
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
		
		Rotator pointTwisted = EFG_FRD.pointTwisted(offsetPedestalEFG,this._local._horizontal); 
		Principle pAzimuth = pointTwisted.getEuler_k_kji(); 
		Principle pElevation = pointTwisted.getEuler_j_kji(); 	
		this._plot.set(offsetPedestalEFG.getAbs(),pAzimuth.unsignedAngle() ,pElevation.signedAngle() );
		
		this._aperture._direction.set(EFG_FRD.point(pAzimuth, pElevation,this._local._horizontal));
		//this._aperture.pointEFG(offsetEFG, this._local._horizontal);	// if do not need the above to get the _plot state...			
	}
	
	public Vector3 directionEFG(){
		return _aperture._direction.getImage_i().multiply(_plot.getRange());
	}

	
	public String toString() { 
		return this._systemId 
				+ "("+ this._wgs84.getNorthLatitude().toDegrees(8) +", "+ this._wgs84.getEastLongitude().toDegrees(8)+", "+this.getHeight()+")"
				+"("+_plot.getAzimuth().toDegrees(4)+", "+_plot.getElevation().toDegrees(4)+")";
	}

}
