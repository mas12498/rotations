/**
 * 
 */
package test.model;

import junit.framework.TestCase;
import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Operator;
import rotation.Principle;
import tspi.model.WGS84;

/**
 * @author mike
 *
 */
public class TestWGS84 extends TestCase {

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Principle, rotation.Principle, double)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Angle, rotation.Angle, double)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(tspi.model.WGS84)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getLatitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getTheta()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getAngleLongitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getLongitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getHeight()}.
	 */
	public final void testWGS84WGS84() {
		System.out.println("Begin test of Constructor LLh and gets:");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(rotation.Angle, rotation.Angle, double)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(rotation.Principle, rotation.Principle, double)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(tspi.model.WGS84)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getLatitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getAngleLatitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getAngleLongitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getLongitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getTheta()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getHeight()}. ");
		int cnt = 0;
		for (int i = -3; i <= 3; i++) {
			double phi = i * 30.0d; //latitude pole to pole
			double theta = -phi-90.0;
			for (int j = 0; j <= 12; j++) {
				double lambda = j * 30.0d; //longitude 360
				for (int h = -1; h <= 2; h++) {
					double hgt = h * 1000.0d; //height above and below ellipsoid
					WGS84 tmp2 = new WGS84(Angle.inDegrees(phi), Angle.inDegrees(lambda), hgt);
					//WGS84 tmp1 = new WGS84(tmp2.getLatitude(), tmp2.getLongitude(), tmp2.getHeight());
					WGS84 tmp = new WGS84(tmp2);
					Operator q_NG = tmp.getFromNEDtoEFG();
					System.out.print(String.format(" latitude = %8f" , tmp.getLatitude().signedAngle().getDegrees()));
					System.out.print(String.format(" theta = %8f" , tmp.getTheta().signedAngle().getDegrees()));
					System.out.print(String.format(" longitude = %.8f", tmp.getLongitude().unsignedAngle().getDegrees() ));
					
					
					Principle plon = q_NG.getEuler_k_kji();
					Principle plat = q_NG.getEuler_j_kji().addRight().negate(); //Principle.arcTanHalfAngle(q_NG.getEuler_j_kji().cotHalf());	
					
					if(tmp.getLatitude().signedAngle().getRadians()>=0){
						plon=Principle.arcTanHalfAngle(plon.cotHalf()).negate();
						plat =plat.negate();
					}
					
					double qlon = plon.unsignedAngle().getDegrees();
					double qlat = plat.signedAngle().getDegrees();	
					
					//double qtwi = q_NG.getEuler_i_kji().signedAngle().getDegrees();
					
			//		//if(qtwi == 0.0 ) { //southern hemi == 0.0; o.w.= NaN from dump.
			//		double qlat = qthe;
			//		if(q_NG.getDump_kj()){
					
					
					
					//Operator qt = new Operator(q_NG).exp_k(tmp.getLongitude().negate()).flip(BasisUnit.J);
					System.out.print(String.format(" Qlon = %.8f", qlon ));
					System.out.print(String.format(" Qlat = %.8f", qlat ));
			//		System.out.print(String.format(" Qthe = %.8f", qthe ));
			//		System.out.print(String.format(" Qtwi = %.8f", qtwi ));
					// System.out.print(" Theta = "+tmp.getTheta().signedAngle().getDegrees());
					// System.out.print("test Theta == "+theta);
					// System.out.print(" height = "+ tmp.getHeight());
					// System.out.print(" Q_NG = " + tmp.getFromNEDtoEFG().unit().toString(10));
					System.out.println();
//					System.out.println("        Q_NG = " + q_NG.unit().toString(10));
//					System.out.println(" ");
					assertEquals(tmp.getLatitude().signedAngle().getDegrees(), phi, 1e-14);
					assertEquals(tmp.getLongitude().unsignedAngle().getDegrees(), lambda  % 360.0, 1e-13);
					assertEquals(tmp.getAngleLatitude().getDegrees(), phi, 1e-14);
					assertEquals(tmp.getAngleLongitude().getDegrees(), lambda % 360.0, 1e-13);
// // // //					assertEquals(tmp.getTheta().signedAngle().getDegrees(),theta, 1e-13 );

//					assertTrue(q_NG.getEuler_k_kji().isEqualTo(tmp.getLongitude(),Principle.arcTanHalfAngle(1e-10)));
//					assertTrue(q_NG.getEuler_j_kji().isEqualTo(tmp.getTheta(),Principle.arcTanHalfAngle(1e-10)));
						assertEquals(tmp.getHeight(), hgt, 1e-14);
					cnt = cnt+1;
					//System.out.println("Success Constructor LLh. Count = "+cnt);

				}
			}
		}
		System.out.println("Success Constructor LLh and getters. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Operator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Operator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getAngleLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.WGS84#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#put(rotation.Operator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}
