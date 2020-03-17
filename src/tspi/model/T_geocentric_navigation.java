/**
 * 
 */
package tspi.model;

import tspi.rotation.Quaternion;
//import tspi.rotation.CodedPhase;
import tspi.rotation.Rotator;
import tspi.rotation.RotatorMath;
import tspi.rotation.Vector3;

/**
 * @author mike
 *
 */
public class T_geocentric_navigation {
	
	private double _geodeticLocalHeight;
	private Rotator _geodeticLocalVersor;
	
	/**
	 * Constructor... copy
	 */
	public T_geocentric_navigation(T_geocentric_navigation t){
		_geodeticLocalHeight = t._geodeticLocalHeight; //default for computations is ellipsoid surface
		_geodeticLocalVersor = new Rotator(t._geodeticLocalVersor);
	}
	
	/** 
	 * Constructor...
	 * @param Ellipsoid (WGS84 geodetic) coordinates
	 */
	public T_geocentric_navigation(Ellipsoid e) {
		_geodeticLocalHeight = e.getHeight();
		_geodeticLocalVersor = (Rotator) RotatorMath.eulerRotate_kj(e.getLambda(), e.getMu())
				.divide(StrictMath.sqrt(e.magnificationGeodeticRotator()));
	}

	/** 
	 * Constructor...
	 * @param Vector3 XYZ (WGS84 geocentric) coordinates
	 */
	public T_geocentric_navigation(Vector3 geocentricEFG){
//		new T_geocentric_navigation(new Ellipsoid(geocentricEFG));
		Ellipsoid e = new Ellipsoid(geocentricEFG);
		_geodeticLocalHeight = e.getHeight();
		_geodeticLocalVersor = (Rotator) RotatorMath.eulerRotate_kj(e.getLambda(), e.getMu())
				.divide(StrictMath.sqrt(e.magnificationGeodeticRotator()));
	}
	
	/**
	 * Constructor... empty
	 */
	public T_geocentric_navigation(){
		_geodeticLocalHeight = Double.NaN; //default for computations is ellipsoid surface
		_geodeticLocalVersor = new Rotator(Rotator.EMPTY);
	}
	
//	/**
//	 * Transforms input rotator from local navigation {ned} to global geocentric {XYZ} frame:
//	 * @param local Rotator in navigation frame
//	 * @return <b>global</b> Rotator in geocentric frame
//	 */
//	public Rotator rotate(Quaternion local){
//		return (Rotator) this._geodeticLocalVersor.rotate(local);
//	}
	
//	/**
//	 * Transform geocentric rotator to local navigation frame rotator:
//	 */
//	public Rotator preRotate(Quaternion global){
//		return (Rotator) this._geodeticLocalVersor.preRotate(global);
//	}
	
	
	
//	public void set(Ellipsoid e) //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
//	{	
//		_geodeticHeight = e.getHeight();
//		_geodeticVersor = (Rotator) RotatorMath.eulerRotate_kj(e.getLambda(), e.getMu())
//				.divide(StrictMath.sqrt(e.getMu().magnificationRotator() * e.getLambda().magnificationRotator()));
//	}
	
//	/** 
//	 * @param geocentricEFG earth-centered, earth-fixed Cartesian position
//	 */
//	public void setGeocentric(Vector3 geocentricEFG) {
//		this.set(new Ellipsoid(geocentricEFG));
//		return; //return for pole latitudes
//	}

	public void setGeodeticHeight(double geodeticHeight) {
		_geodeticLocalHeight = geodeticHeight;
	}
	
	public double getGeodeticHeight() 
	{
		return _geodeticLocalHeight;
	}

	public void setRotator(Rotator  geodeticVersor) {
		_geodeticLocalVersor = new Rotator(geodeticVersor.unit());
	}
	
	public Rotator getRotator() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{	
		return new Rotator(_geodeticLocalVersor);		
	}

	public Ellipsoid ellipsoidCoordinates() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{			
		return new Ellipsoid(_geodeticLocalVersor.getEuler_k_kj()
				,_geodeticLocalVersor.getEuler_j_kj()
				,_geodeticLocalHeight);
	}
	
	/**
	 * Factory: Cartesian position coordinates of this GeodeticLocation, earth-centered and earth-fixed. 
	 * @return Vector3 geocentric {E,F,G}
	 */
	public Vector3 geocentricCoordinates() {
		return Ellipsoid.getGeocentric(this.u_DirCos(),this.getGeodeticHeight());	
	}

	/**
	 * Inner product with geocentric vector produces local navigation "north" component.
	 * @return north direction cosines
	 */
	public Vector3 n_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getImage_i(); //_normalization);		
	}
	/**
	 * Inner product with geocentric vector produces local navigation "east" component.
	 * @return east direction cosines
	 */
	public Vector3 e_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getImage_j(); //_normalization);
	}

	/**
	 * Inner product with geocentric vector produces local navigation "down" component.
	 * @return down direction cosines
	 */
	public Vector3 d_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getImage_k(); 		
	}
	
	/**
	 * Inner product with geocentric vector produces local navigation "up" component.
	 * @return up direction cosines
	 */
	public Vector3 u_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getImage_k().negate(); //-_normalization);		
	}

	/**
	 * Inner product with local navigation vector produces global geocentric "X" component.
	 * @return X direction cosines
	 */
	public Vector3 X_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getPreImage_i(); //_normalization);		
	}
	
	/**
	 * Inner product with local navigation vector produces global geocentric "Y" component.
	 * @return Y direction cosines
	 */
	public Vector3 Y_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getPreImage_j(); //_normalization);
	}

	/**
	 * Inner product with local navigation vector produces global geocentric "Z" component.
	 * @return Z direction cosines
	 */
	public Vector3 Z_DirCos() //CodedAngle northGeodeticLatitude, Angle eastGeodeticLongitude)
	{
		return _geodeticLocalVersor.getPreImage_k(); 		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
