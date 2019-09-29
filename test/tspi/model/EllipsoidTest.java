/**
 * 
 */
package tspi.model;

import junit.framework.TestCase;
import tspi.rotation.Angle;
import tspi.rotation.Vector3;

/**
 * @author mike
 *
 */
public class EllipsoidTest extends TestCase {

	/**
	 * Test method for
	 * {@link tspi.model.Ellipsoid#Ellipsoid(rotation.Angle, rotation.Angle, double)}.
	 */
	public final void testEllipsoidAngleAngleDouble() {
		Angle lat = Angle.inDegrees(30);
		Angle lon = Angle.inDegrees(-120);
		double h = 2000;
		Ellipsoid p = new Ellipsoid();
		p.set(lat, lon, h);
		Ellipsoid q = new Ellipsoid();
		Ellipsoid t = new Ellipsoid(p);
		//assertTrue(p.getNorthLatitude().equals(lat));
		assertEquals(p.getNorthLatitude().getPiRadians(),lat.getPiRadians(),1e-13);
		//assertTrue(p.getEastLongitude().equals(lon));
		assertEquals(p.getEastLongitude().unsignedPrinciple().getPiRadians(),lon.unsignedPrinciple().getPiRadians(),1e-13);
		assertEquals(p.getHeight(), 2000, 0);
		t.set(q);
		assertTrue(t.getNorthLatitude().equals(Angle.EMPTY));
		assertTrue(t.getEastLongitude().equals(Angle.EMPTY));
		assertTrue(Double.isNaN(t.getHeight()));
		t.set(p.getNorthLatitude(), p.getEastLongitude(), p.getHeight());
		//assertTrue(t.getNorthLatitude().equals(lat));
		assertEquals(t.getNorthLatitude().getPiRadians(),lat.getPiRadians(),1e-13);
		//assertTrue(t.getEastLongitude().equals(lon));
		assertEquals(t.getEastLongitude().unsignedPrinciple().getPiRadians(),lon.unsignedPrinciple().getPiRadians(),1e-13);
		assertEquals(t.getHeight(), 2000, 0);
		q.set(p);
		//assertTrue(q.getNorthLatitude().equals(lat));
		assertEquals(q.getNorthLatitude().getPiRadians(),lat.getPiRadians(),1e-13);
		//assertTrue(q.getEastLongitude().equals(lon));
		assertEquals(q.getEastLongitude().unsignedPrinciple().getPiRadians(),lon.unsignedPrinciple().getPiRadians(),1e-13);
		assertEquals(q.getHeight(), 2000, 0);
		q.setNorthLatitude(Angle.inDegrees(40));
		q.setEastLongitude(Angle.inDegrees(240));
		q.setHeight(1000);
		//assertTrue(q.getNorthLatitude().equals(Angle.inDegrees(40)));
		assertEquals(q.getNorthLatitude().getPiRadians(),Angle.inDegrees(40).getPiRadians(),1e-15);
		//assertTrue(q.getEastLongitude().equals(Angle.inDegrees(240)));
		assertEquals(q.getEastLongitude().unsignedPrinciple().getPiRadians(),lon.unsignedPrinciple().getPiRadians(),1e-13);
		assertEquals(q.getHeight(), 1000, 0);
		Vector3 x = p.getGeocentric();
		q.setGeocentric(x);
		assertEquals(q.getNorthLatitude().getDegrees(), p.getNorthLatitude().getDegrees(), 1e-14);
		assertEquals(q.getEastLongitude().getDegrees(), p.getEastLongitude().getDegrees(), 1e-15);
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Ellipsoid#getGeocentric()}.
	 */
	public final void testGeocentric() {

		int cnt = 0;
		Ellipsoid geodetic = new Ellipsoid();
		Ellipsoid tgeodetic = new Ellipsoid();

		Double qHeight = Double.NaN;
		
		//Note: efg cannot handle dumping: ambiguous Euler assignments!
		
		Vector3 efg = new Vector3(Vector3.EMPTY);
		Vector3 tefg = new Vector3(Vector3.EMPTY);
		double qlat;
		double qlon;
		double qhgt;
		for (int k = -1; k <= 1; k++) {
			double fudge = k * 0.00d;
			for (int i = -3; i <= 3; i++) { // latitudes [-90..90]
				double phi = i * 30.0d; // - 0.00001d;
				for (int j = 0; j <= 12; j++) { // longitudes [-360..360]
					double lambda = j * 30.0d + fudge; // - 0.01d;
					for (int h = -1; h <= 2; h++) { // heights meters above, on, and below ellipsoid
						double hgt = h * 1000.0d + fudge; // - 0.1d;
//						System.out.println(phi+" "+lambda + " near rev = "
//						 + StrictMath.round(Angle.inDegrees(j*30).getRevolutions()));
						geodetic.set(Angle.inDegrees(phi), Angle.inDegrees(lambda), hgt);
						
						efg.set(geodetic.getGeocentric());
						tgeodetic.setGeocentric(efg); //has poles...
						
						qlat = tgeodetic.getNorthLatitude().getDegrees();
						assertEquals(qlat, phi, 1e-8);
						//assertEquals(qlat, phi, 1e-13);
						qlon = tgeodetic.getEastLongitude().unsignedPrinciple().getDegrees();
						qhgt = tgeodetic.getHeight();
						System.out.print(String.format(" Qlat = %14.10f", qlat));
//						System.out.print(String.format(" phi = %14.10f", phi));
						System.out.print(String.format(" Qlon = %15.10f", qlon));
//						System.out.print(String.format(" lambda = %15.10f", lambda));
//						System.out.print(String.format(" height = %8.3f", hgt));
						System.out.print(String.format(" Qheight = %8.3f", qhgt));
						
						if(StrictMath.abs(phi) == 90) {
							assertTrue(Double.isNaN(qlon));
							assertTrue((Double.isNaN(Angle.inDegrees(qlon).unsignedPrinciple().getDegrees())));								
						} else {
						assertEquals(
								Angle.inDegrees(qlon).unsignedPrinciple().getDegrees(),
								Angle.inDegrees(lambda).unsignedPrinciple().getDegrees(),
								1e-10);
						}
///						assertTrue((Double.isNaN(Angle.inDegrees(qlon).unsignedPrinciple().getDegrees())));
						assertEquals(qhgt, hgt, 1e-3);

						System.out.println();
						cnt = cnt + 1;

					}
				}
			}
		}
		
		System.out.println(
				"Test -0 < +0: " + (-0d < +0d) + "    Double.compare: -0 < +0: " + (Double.compare(-0d, +0d) < 0d));
		System.out.println("Test -0 == +0: " + (-0d == +0d));

		System.out.println("Success of geo-location structures and earth coordinate transformations. Count = " + cnt);

		// fail("Not yet implemented"); // TODO
	}

}
