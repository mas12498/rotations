/**
 * 
 */
package test.model;

import junit.framework.TestCase;
import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Rotator;
import rotation.Vector3;
//import rotation.Principle;
import tspi.model.T_EFG_NED;
import tspi.model.Ellipsoid;

/**
 * @author mike
 *
 */
public class LocationTest extends TestCase {

	public final void testWGS84WGS84() {
		int cnt = 0;
		Ellipsoid geodetic = new Ellipsoid();
		Ellipsoid tgeodetic = new Ellipsoid();

		Double qHeight = Double.NaN;
		Rotator q_EFG_NED = new Rotator(Rotator.EMPTY);
		Vector3 efg = new Vector3(Vector3.NAN);
		Vector3 tefg = new Vector3(Vector3.NAN);
		T_EFG_NED tNavigation = new T_EFG_NED();
		double qlat;
		double qlon;
		for (int i = -3; i <= 3; i++) {
            //int i = 1; {//lats
			double phi = i * 30.0d; // + 0.001d; //deg latitude pole to pole
			for (int j = 0; j <= 12; j++) {
				//int j=2; {//lon
				double lambda = j * 30.0d; // - 0.01d; //deg longitude 360
				//for (int h = -1; h <= 2; h++) {
					int h=2; {
					double hgt = h * 1000.0d; //meters height above and below ellipsoid
					
					//set Ellipsoid object..: geodetic
//					geodetic.setNorthLatitude(Angle.inDegrees(phi));
//					geodetic.setEastLongitude(Angle.inDegrees(lambda));
//					geodetic.setEllipsoidHeight(hgt);
					geodetic.set(Angle.inDegrees(phi),Angle.inDegrees(lambda),hgt);
					
					//set Geocentric with Ellipsoid object
					efg.put(geodetic.getGeocentric());
					
					//set transform to navigation object with Ellipsoid coordinate object
					tNavigation.set(geodetic);
					
					tefg.put(tNavigation.getGeocentric());
//					System.out.println(efg.toString(15)+tefg.toString(15)+"vector difference magnitude: " + new Vector3(efg).subtract(tefg).getAbs());
					assertTrue(efg.isEquivalent(tefg,1e-8));//to the .01 micrometer!
					
					//get rotator and translation components from transform to navigation object
					q_EFG_NED = tNavigation.getLocalHorizontal(); 	//geodetic-tangent "level"
                    qHeight = tNavigation.getLocalVertical();		//geodetic-normal "vertical"
                    
					//output navigation transform's rotator norms and rotator
//					System.out.print(String.format("max = %5f; ",q_EFG_NED.getNormInf()));
//					System.out.print(String.format("abs = %5f; ",q_EFG_NED.getAbs()));
//					System.out.print(String.format(" q = " + q_EFG_NED.toString(15))); 
					assertTrue(1>=q_EFG_NED.getNormInf());
					assertTrue(2>=q_EFG_NED.getAbs());
					
                    //set Ellipsoid coordinate object with transform to navigation object
					tgeodetic.set(tNavigation.getEllipsoid());
					qlat = tgeodetic.getNorthLatitude().getDegrees();						
					qlon = tgeodetic.getEastLongitude().getDegrees();												
//					System.out.print(String.format(" Qlat = %.8f", qlat ));
//					System.out.print(String.format(" Qlon = %.8f", qlon ));
					assertEquals(qlat,phi,1e-13);
					assertEquals(qlon,lambda % 360.0,1e-13);
					
//					System.out.println();	
					cnt = cnt+1;
					
				}
			}
		}
		System.out.println("***Success of model.locationTest.java: Geo-location structures and earth coordinate transformations. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.T_EFG_NED#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#WGS84(rotation.Rotator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#WGS84(rotation.Rotator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#getLocationXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#getNorthLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#set(rotation.Rotator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.T_EFG_NED#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}
