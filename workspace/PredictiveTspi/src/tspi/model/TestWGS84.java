/**
 * 
 */
package tspi.model;

import junit.framework.TestCase;
import rotation.Angle;
import rotation.BasisUnit;
//import rotation.BasisUnit;
import rotation.Operator;
import rotation.Principle;

/**
 * @author mike
 *
 */
public class TestWGS84 extends TestCase {

	public final void testWGS84WGS84() {
		int cnt = 0;

		Location tmp2 = new Location(Angle.inDegrees(Double.NaN), Angle.inDegrees(Double.NaN),Double.NaN);
		Principle plat = tmp2.getNorthLatitude().getPrinciple();
		Principle plon =  tmp2.getEastLongitude().getPrinciple();
		Principle ptheta = tmp2.getNorthLatitude().getPrinciple(); //Principle.arcTanHalfAngle(q_NG.getEuler_j_kji().cotHalf());	
		
		for (int i = -3; i <= 3; i++) {
			double phi = i * 30.0d; //latitude pole to pole
//			double theta = -phi-90.0; //theta ref
			for (int j = 0; j <= 12; j++) {
				double lambda = j * 30.0d; //longitude 360
				for (int h = -1; h <= 2; h++) {
					double hgt = h * 1000.0d; //height above and below ellipsoid
					
					tmp2.setNorthLatitude(Angle.inDegrees(phi));
					tmp2.setEastLongitude(Angle.inDegrees(lambda));
					tmp2.setEllipsoidHeight(hgt);
					Location tmp1 = new Location(tmp2.getNorthLatitude(), tmp2.getEastLongitude(), tmp2.getEllipsoidHeight());
					Location tmp = new Location(tmp1);
//					System.out.print(String.format(" theta = %8f" , theta)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
//					System.out.print(String.format(" phi = %8f" , phi)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
//					System.out.print(String.format(" lambda = %8f" , lambda)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
					System.out.print(String.format(" latitude = %8f" , tmp.getNorthLatitude().getDegrees()));
					System.out.print(String.format(" longitude = %.8f", tmp.getEastLongitude().getDegrees() ));
					
					Operator q_NG = tmp.getOpNavToGeo();
					Operator tst = new Operator(q_NG);
					ptheta.put(tst.getEuler_j_kji());
					plon.put( tst.getEuler_k_kji());
					
					//first to second quadrant....
					double qlat = plat.put(ptheta).addRight().negate().signedAngle().getDegrees();
					double qlon = plon.unsignedAngle().getDegrees();
					//if (ptheta.getRadians()>=-StrictMath.PI){
					if (phi >= 0){ //for dumped pedestal... fourth to first quadrant....
						qlon = (plon.unsignedAngle().getDegrees() + 180)%360;
						//wrong!!!//qlon = new Principle(Angle.inRadians(-plon.signedAngle().getRadians())).unsignedAngle().getDegrees();
						qlat = -(plat.signedAngle().getDegrees());
					}
					
//					System.out.print(String.format(" theta = %.8f", ptheta.signedAngle().getDegrees() ));
//					System.out.print(tst.unit().toString(3));
					System.out.print(String.format(" Qlon = %.8f", qlon ));
					System.out.print(String.format(" Qlat = %.8f", qlat ));

					System.out.println();
///					assertEquals(tmp.getLatitude().signedAngle().getDegrees(), phi, 1e-14);
					assertEquals(tmp.getLongitude().unsignedAngle().getDegrees(), lambda  % 360.0, 1e-13);
///					assertEquals(tmp.getNorthLatitude().getDegrees(), phi, 1e-14);
///					assertEquals(tmp.getEastLongitude().getDegrees(), lambda % 360.0, 1e-13);
// // // //					assertEquals(tmp.getTheta().signedAngle().getDegrees(),theta, 1e-13 );

//					assertTrue(q_NG.getEuler_k_kji().isEqualTo(tmp.getLongitude(),Principle.arcTanHalfAngle(1e-10)));
//					assertTrue(q_NG.getEuler_j_kji().isEqualTo(tmp.getTheta(),Principle.arcTanHalfAngle(1e-10)));
					assertEquals(tmp.getEllipsoidHeight(), hgt, 1e-14);
						
					cnt = cnt+1;
					
				}
			}
		}
		System.out.println("Success Constructor LLh and getters. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Operator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Operator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getLocationXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getNorthLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.Location#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#put(rotation.Operator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}