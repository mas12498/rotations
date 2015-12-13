/**
 * 
 */
package rotationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.QuaternionMath;
import rotation.Vector3;

/**
 * @author mike
 */
public class QuaternionTest {
	                   
	/**
	 * Test method for {@link rotation.Quaternion#getW()}.
	 * Test method for {@link rotation.Quaternion#getX()}.
	 * Test method for {@link rotation.Quaternion#getY()}.
	 * Test method for {@link rotation.Quaternion#getZ()}.
	 */
	@Test
	public void testReal() {
		Quaternion t = new Quaternion(1,2,3,4);
		double r = t.getW();
		double r1 = t.getW();
		assertEquals(r,1,1e-15);
		assertEquals(r1,1,1e-15);
		assertEquals(t.getX(),2,1e-15);
		assertEquals(t.getY(),3,1e-15);
		assertEquals(t.getZ(),4,1e-15);
	}

	/**
	 * Test method for {@link rotation.Quaternion#getV()}.
	 */
	@Test
	public void testImaginary() {
		Quaternion t = new Quaternion(1,2,3,4);
		Vector3 v = new Vector3(t.getV());
		assertEquals(v.getX(),2,1e-15);
		assertEquals(v.getY(),3,1e-15);
		assertEquals(v.getZ(),4,1e-15);		
		assertTrue(t.getV().isEquivalent(new Vector3(t.getX(),t.getY(),t.getZ()), 1e-15));
	}

	@Test
	public void testIsEqual() {
		Quaternion t = new Quaternion(1,3,0,4);
		Quaternion p = new Quaternion(0,3,1,4);
		Quaternion q = new Quaternion(0,3,1,4);
		assertTrue(p.isEquivalent(q, 1e-13));
		q.put(0,3,1,4-(1e-10));
		assertFalse(p.isEquivalent(q, 1e-13));
		assertTrue(p.isEquivalent(q, 1e-7));
		// assertTrue(p.isEqual(t, 1e-13));
		t.put(1,2,3,4);
	}
	
	
	/**
	 * Test method for {@link rotation.Quaternion#hasNan()}.
	 */
	@Test
	public void testHasNan() {
		Quaternion t = new Quaternion(Double.NaN,3,1,4);
		t.put(1,2,3,4);
//        long id = t.hashCode();
		t = new Quaternion(1,Double.NaN,3,4);
        assertTrue(t.hasNan());
		t = new Quaternion(3,1,Double.NaN,4);
        assertTrue(t.hasNan());
		t = new Quaternion(1,3,4,Double.NaN);
        assertTrue(t.hasNan());
		t = new Quaternion(2,3,1,4);
        assertFalse(t.hasNan());
		t = new Quaternion(1,Double.NEGATIVE_INFINITY,3,4);
        assertFalse(t.hasNan());
		t = new Quaternion(3,1,Double.NEGATIVE_INFINITY,4);
        assertFalse(t.hasNan());
		t = new Quaternion(-0,Double.MAX_VALUE,Double.NEGATIVE_INFINITY,Double.MIN_VALUE);
        assertFalse(t.hasNan());
	}

	/**
	 * Test method for {@link rotation.Quaternion#getDeterminant()}.
	 */
	@Test
	public void testDeterminant() {
		Quaternion t = new Quaternion(-1,2,-3,4);
		assertEquals(t.getDeterminant(),1+4+9+16,1e-15);	
		assertEquals(t.getDeterminant(),t.getW()*t.getW()+t.getX()*t.getX()+t.getY()*t.getY()+t.getZ()*t.getZ(),1e-15);
		assertEquals(t.getDeterminant(),t.getAbs()*t.getAbs(),1e-13);	
		
		t.put(1,1,1,1);
		assertEquals(t.getDeterminant(),t.getW()*t.getW()+t.getX()*t.getX()+t.getY()*t.getY()+t.getZ()*t.getZ(),1e-15);	
		assertEquals(t.getDeterminant(),t.getAbs()*t.getAbs(),1e-15);	
		
		t.put(2,1,5,7);
		assertEquals(t.getDeterminant(),t.getW()*t.getW()+t.getX()*t.getX()+t.getY()*t.getY()+t.getZ()*t.getZ(),1e-15);	
		assertEquals(t.getDeterminant(),t.getAbs()*t.getAbs(),1e-15);	
	}

	/**
	 * Test method for {@link rotation.Quaternion#getAbs()}.
	 */
	@Test
	public void testAbs() {
		Quaternion t = new Quaternion(-0,0,-3,4);
		assertEquals(t.getAbs(),5,1e-15);	
		t.put(-0,new Vector3(0,-0.6,0.8));
//		System.out.println("The magnitude is: "+t.abs());
		assertEquals(t.getAbs(),1.0,1e-15);	
		t = new Quaternion(-0,0,-0,0);
		assertEquals(t.getAbs(),0,1e-15);	
		t = new Quaternion(Quaternion.ZERO);
		assertEquals(t.getAbs(),0,1e-15);	
		t = new Quaternion(Quaternion.IDENTITY);
		assertEquals(t.getAbs(),1,1e-15);	
		t = new Quaternion(-0,(0),-0.6,0.8);
//		System.out.println("The magnitude is: "+t.abs());
		assertEquals(t.getAbs(),1.0,1e-15);
//		
		t.put(-0,0,-0.06,0.08);
		assertEquals(t.getAbs(),0.1,1e-13);
//		
		t.put(-0,0,-6,8);
		assertEquals(t.getAbs(),10d,1e-13);	
//		
		
		t.put(-0,0,-0.06,0.08);
		t.put(t);
//		System.out.println("The magnitude is: "+t.abs());
		assertEquals(t.getAbs(),0.1,1e-15);	
		
		assertEquals(t.getAbs()*t.getAbs(),t.getDeterminant(),1e-15);
	}

	/**
	 * Test method for {@link rotation.Quaternion#getArg()}.
	 */
	@Test
	public void testArg() {
		
		//use 30:60:90 triangle.		

		//30 degrees
		
		Quaternion t = new Quaternion(1,0,0,0);
		assertEquals(t.getArg().getPiRadians(),0,1e-15);	
		
		//MAS: error!!! -60=2 * -30 deg. rotation
		t.put( new Quaternion(StrictMath.sqrt(3),-1,0,0));
//		System.out.println("angle1 deg= "+t.arg().getRadians()*180/StrictMath.PI);
		assertEquals(t.getArg().getPiRadians(),StrictMath.PI/3,1e-15);	
		
		
		//60=2 * 30 deg. rotation
		t.put( new Quaternion(StrictMath.sqrt(3),1,0,0));
		assertEquals(t.getArg().getPiRadians(),StrictMath.PI/3,1e-15);	
		
		//-60=2 * -30 deg. rotation [150 ?]
		t.put( new Quaternion(-StrictMath.sqrt(3),1,0,0));
		assertEquals(t.getArg().getPiRadians(),-StrictMath.PI/3,1e-15);	
		
		//-60=2 * -30    [210 ?]
		t.put( new Quaternion(-StrictMath.sqrt(3),-1,0,0));
		assertEquals(t.getArg().getPiRadians(),-StrictMath.PI/3,1e-15);	

		
		//60 degrees
		
		//120=2 * 60
		t.put( new Quaternion(.5,StrictMath.sqrt(3)/2,0,0));
//		System.out.println("angle2 deg= "+t.arg().getRadians()*180/StrictMath.PI);
		assertEquals(t.getArg().getPiRadians(),StrictMath.PI/1.5,1e-15);	
		
		//-120=2 * -60    [240 ?]
		t.put( new Quaternion(-1,-StrictMath.sqrt(3),0,0));
		assertEquals(t.getArg().getPiRadians(),-StrictMath.PI/1.5,1e-15);	
		
		//180=2 * 90
		t = new Quaternion(0,-9,1,-3);
//		System.out.println("angle right deg= "+t.arg().getRadians()*180/StrictMath.PI);
		assertEquals(t.getArg().getPiRadians(),StrictMath.PI,1e-15);	
		
		//90=2 * 45
		t = new Quaternion(1,0,1,0);
//		System.out.println("angle right deg= "+t.arg().getRadians()*180/StrictMath.PI);
		assertEquals(t.getArg().getPiRadians(),StrictMath.PI/2,1e-15);	
	}

	/**
	 * Test method for {@link rotation.Quaternion#unit()}.
	 */
	@Test
	public void testNormalize() {
		Quaternion t = new Quaternion(3,0,4,0).unit();
		assertFalse(t.hasNan());	
//		System.out.println("t = "+t.toString());
		assertTrue(t.isEquivalent(new Quaternion(.6,0,.8,0), 1e-14));
		Quaternion q = new Quaternion(Quaternion.IDENTITY);
		int l; int k; int j; int i;
		for (l = -2; l <= 2; ++l) {
			for (k = -2; k <= 2; ++k) {
				for (j = -2; j <= 2; ++j) {
					for (i = -2; i <= 2; ++i) {
						q.put(i, j, k, l);
						if (!q.equals(Quaternion.ZERO))
							assertEquals(new Quaternion(i, j, k, l).unit()
									.getDeterminant(), 1.0, 1e-10);
					}
				}
			}

		}
		assertTrue(new Quaternion(Quaternion.ZERO).unit().equals(Quaternion.NAN));
		
		t.put(Quaternion.ZERO).unit();
//		System.out.println("t = NaN: "+t.toString());
		t.put(Quaternion.NAN).unit();
		assertTrue(t.hasNan());			
		t.put(Quaternion.ZERO).divide(0);
//		System.out.println("t = NaN: "+t.toString());
		assertTrue(t.hasNan());	

//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#conjugate()}.
	 */
	@Test
	public void testConjugate() {
		Quaternion t = new Quaternion(1,2,3,4).conjugate();
//		System.out.println("t = "+t.toString());
		assertTrue(t.isEquivalent(new Quaternion(1,-2,-3,-4), 1e-15));

//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#reciprocal()}.
	 */
	@Test
	public void testInvert() {
		Quaternion t = new Quaternion(1,2,3,4);
		double d = t.getDeterminant();
		t.reciprocal().multiply(d);
//		System.out.println("t = "+t.toString());
		assertTrue(t.isEquivalent(new Quaternion(1,-2,-3,-4), 1e-15));
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#negate()}.
	 */
	@Test
	public void testNegate() {
		Quaternion t = new Quaternion(1,2,3,4).negate();
		assertTrue(t.isEquivalent(new Quaternion(-1,-2,-3,-4), 1e-15));
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#add(rotation.Quaternion)}.
	 */
	@Test
	public void testAdd() {
		Quaternion t = new Quaternion(1,2,3,4);
		Quaternion p = new Quaternion(1,2,3,4);
		t.add(p);
		assertTrue(t.isEquivalent(new Quaternion(2,4,6,8), 1e-15));

	}

	/**
	 * Test method for {@link rotation.Quaternion#subtract(rotation.Quaternion)}.
	 */
	@Test
	public void testSubtract() {
		Quaternion t = new Quaternion(1,2,3,4);
		Quaternion p = new Quaternion(1,1,2,2);
		t.subtract(p);
		assertTrue(t.isEquivalent(new Quaternion(0,1,1,2), 1e-15));

		//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#multiply(double)}.
	 */
	@Test
	public void testMultiplyDouble() {
		Quaternion t = new Quaternion(1,2,3,4);
		double d = 2;
		t.multiply(d);
		assertTrue(t.isEquivalent(new Quaternion(2,4,6,8), 1e-15));
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#divide(double)}.
	 */
	@Test
	public void testDivide() {
		Quaternion t = new Quaternion(1,2,3,4);
		double d = .5;
		t.divide(d);
		assertTrue(t.isEquivalent(new Quaternion(2,4,6,8), 1e-15));
		
		t.divide(0);
		assertTrue(t.hasNan());	

//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#rightMultiply(rotation.Vector3)}.
	 */
	@Test
	public void testMultiplyVector3() {
			Quaternion p = new Quaternion(1,2,3,4);
			Quaternion q = new Quaternion(0,5,7,6);
//			Vector3 vp = new Vector3(p.imaginary());
			Vector3 vq = new Vector3(q.getV());
			//assume Quaternion multiply verified!!!
			Quaternion t = p.rightMultiply(q);
			Quaternion t0 = p.rightMultiply(vq);
//			System.out.println("constructed product= "+t.toString());
//			System.out.println("implemented product= "+t0.toString());
			assertTrue(t.isEquivalent(t0, 1e-15));

//			Quaternion t1 = p.multiply(vq, Op.RIGHT);
			Quaternion t1 = p.rightMultiply(vq);
			assertTrue(t.isEquivalent(t1, 1e-15));
			
//			fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#leftMultiply(rotation.Vector3)}.
	 */
	@Test
	public void testPreMultiplyVector3() {
		Quaternion p = new Quaternion(0,2,3,4);
		Quaternion q = new Quaternion(4,5,7,6);
		Vector3 vp = new Vector3(p.getV());
//		Vector3 vq = new Vector3(q.imaginary());
		//assume Quaternion multiply verified!!!
		Quaternion t = p.rightMultiply(q);
		Quaternion t0 = new Quaternion(q).leftMultiply(vp);
//		System.out.println("constructed product= "+t.toString());
//		System.out.println("implemented product= "+t0.toString());
		assertTrue(t.isEquivalent(t0, 1e-15));

//		Quaternion t1 = new Quaternion(q).multiply(vp, Op.LEFT);
		Quaternion t1 = new Quaternion(q).leftMultiply(vp);
//		System.out.println("constructed product= "+t.toString());
//		System.out.println("implemented product= "+t1.toString());
		assertTrue(t.isEquivalent(t1, 1e-15));
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#rightMultiply(rotation.Quaternion)}.
	 */
	@Test
	public void testMultiplyQuaternion() {
		Quaternion p = new Quaternion(1,2,3,4);
		Quaternion q = new Quaternion(8,5,7,6);
		double wp = p.getW();
		double wq = q.getW();
		Vector3 vp = new Vector3(p.getV());
		Vector3 vq = new Vector3(q.getV());
		double wt = wp * wq - vp.getDot(vq);
		Vector3 vt = new Vector3(vp).multiply(wq)
				.add(new Vector3(vq).multiply(wp))
				.add(new Vector3(vp).cross(vq));
		Quaternion t = new Quaternion(wt, vt);
		Quaternion t0 = new Quaternion(p).rightMultiply(q);
//		System.out.println("constructed product= "+t.toString());
//		System.out.println("implemented product= "+t0.toString());
		assertTrue(t.isEquivalent(t0, 1e-15));

		Quaternion t1 = new Quaternion(p).rightMultiply(q);
		assertTrue(t.isEquivalent(t1, 1e-15));

		
		p.put(1,2,3,4);
		q.put(1,1,2,2);
		t = new Quaternion(p).add(new Quaternion(q).multiply(3));
//		t= new Quaternion(p).addMultiply(3,q);
//		t.put(p).addMultiply(3,q);
		assertTrue(t.isEquivalent(new Quaternion(4, 5, 9, 10), 1e-15));

//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#leftMultiply(rotation.Quaternion)}.
	 */
	@Test
	public void testPreMultiplyQuaternion() {
		Quaternion p = new Quaternion(1,2,3,4);
		Quaternion q = new Quaternion(8,5,7,6);
		double wp = p.getW();
		double wq = q.getW();
		Vector3 vp = new Vector3(p.getV());
		Vector3 vq = new Vector3(q.getV());
		double wt = wp * wq - vp.getDot(vq);
		Vector3 vt = new Vector3(vp).multiply(wq)
				.add(new Vector3(vq).multiply(wp))
				.add(new Vector3(vp).cross(vq));
		Quaternion t = new Quaternion(wt, vt);
		Quaternion t0 = new Quaternion(q).leftMultiply(p);
//		System.out.println("constructed product= "+t.toString());
//		System.out.println("implemented product= "+t0.toString());
		assertEquals(t.getW(),t0.getW(),1e-14);
		assertEquals(t.getX(),t0.getX(),1e-14);
		assertEquals(t.getY(),t0.getY(),1e-14);
		assertEquals(t.getZ(),t0.getZ(),1e-14);

		Quaternion t1 = new Quaternion(q).leftMultiply(p);
		assertEquals(t.getW(),t1.getW(),1e-14);
		assertEquals(t.getX(),t1.getX(),1e-14);
		assertEquals(t.getY(),t1.getY(),1e-14);
		assertEquals(t.getZ(),t1.getZ(),1e-14);		
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#exp()}.
	 */
	@Test
	public void testExp() {
		Angle theta = Angle.inDegrees(30); //operator angle is 15 degees...
		Quaternion p = new Quaternion(0,1,0,0);
		Quaternion t = QuaternionMath.exp_j(new Principle(theta));
		
//		System.out.println("Theta H="+t.toString());
		Quaternion r = new Quaternion(t).reciprocal();
		Quaternion answer = p.leftMultiply(t).rightMultiply(r);
//		System.out.println("ROTATE E_i="+answer.toString());
		assertTrue(p.isEquivalent(new Quaternion(0, StrictMath.sqrt(3) / 2, 0, -.5),
				1e-15));
		
		theta = Angle.inDegrees(30);
//		Principle pt = new Principle(theta);		
//		t = new Quaternion(0,0,theta.bisectorPrincipleAngle().getRadians(),0).exp(); //get half angle operator
		t = new Quaternion(0,0,theta.getRadians(),0).exp(); //get half angle operator
		System.out.println("Theta H="+theta.getDegrees()+"deg  "+t.toString());
		System.out.println("LN[H]="+new Quaternion(t).ln().multiply(180.0/StrictMath.PI).toString());
		p = new Quaternion(0,1,0,0);
		r = new Quaternion(t).reciprocal();
		answer = p.leftMultiply(t).rightMultiply(r);
		System.out.println("ROTATE E_i="+answer.toString());
		assertTrue(p.isEquivalent(new Quaternion(0, StrictMath.sqrt(3) / 2, 0, -.5),
				1e-15));
		
		theta = Angle.inDegrees(-30);
//		pt = new Principle(theta);		
//		t = new Quaternion(0,0,theta.bisectorPrincipleAngle().getRadians(),0).exp();
		t = new Quaternion(0,0,theta.getRadians(),0).exp();
		System.out.println("Theta H="+theta.getDegrees()+"deg  "+t.toString());
		System.out.println("LN[H]="+new Quaternion(t).ln().multiply(180.0/StrictMath.PI).toString());
		p = new Quaternion(0,1,0,0);
		r = new Quaternion(t).reciprocal();
        answer = p.leftMultiply(t).rightMultiply(r);
		System.out.println("ROTATE E_i="+answer.toString());
		assertTrue(p.isEquivalent(new Quaternion(0, StrictMath.sqrt(3) / 2, 0, .5),
				1e-15));
		
		theta = Angle.inDegrees(120);
//		pt = new Principle(theta);		
//		t = new Quaternion(0,0,theta.bisectorPrincipleAngle().getRadians(),0).exp();
		t = new Quaternion(0,0,theta.getRadians(),0).exp();
		System.out.println("Theta H="+theta.getDegrees()+"deg  "+t.toString());
		System.out.println("LN[H]="+new Quaternion(t).ln().multiply(180.0/StrictMath.PI).toString());
		p = new Quaternion(0,1,0,0);
		r = new Quaternion(t).reciprocal();
		answer = p.leftMultiply(t).rightMultiply(r);
		System.out.println("ROTATE E_i="+answer.toString());
		assertTrue(p.isEquivalent(new Quaternion(0, -.5, 0, -StrictMath.sqrt(3) / 2),
				1e-15));
		
		theta = Angle.inDegrees(-120);
//		pt = new Principle(theta);		
//		t = new Quaternion(0,0,theta.bisectorPrincipleAngle().getRadians(),0).exp();
		t = new Quaternion(0,0,theta.getRadians(),0).exp();
		System.out.println("Theta H="+theta.getDegrees()+"deg  "+t.toString());
		System.out.println("LN[H]="+new Quaternion(t).ln().multiply(180.0/StrictMath.PI).toString());
		p = new Quaternion(0,1,0,0);
		r = new Quaternion(t).reciprocal();
		answer = p.leftMultiply(t).rightMultiply(r);
		System.out.println("ROTATE E_i="+answer.toString());
		assertTrue(p.isEquivalent(new Quaternion(0, -.5, 0, StrictMath.sqrt(3) / 2),
				1e-15));
// test scalar functional equivalence...
		p.put(StrictMath.log(2),0,0,0);
		p.exp();
		assertEquals( p.getW(),2,1e-15);
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#ln()}.
	 */
	@Test
	public void testLn() {
		
		
		Quaternion t = new Quaternion(1,2,3,4);
		Quaternion e = new Quaternion(t);
		System.out.println("start: "+t.toString());
		System.out.println("stop e: "+e.toString());	
		e.ln();
		System.out.println("stop e: "+e.toString());	
		e.exp();
		System.out.println("stop e: "+e.toString());	
		System.out.println("start: "+t.toString());
		System.out.println("stop e: "+e.toString());	
		assertTrue(t.isEquivalent(e, 1e-13));
		
		t = new Quaternion(1,2,3,4);
		e = new Quaternion(t);
		e.exp();
		t.put(e.ln());
		e.exp();
		e.ln();
//		System.out.println("start: "+t.toString());
//		System.out.println("stop:  "+e.toString());	
		assertTrue(t.isEquivalent(e, 1e-15));
		
		t = new Quaternion(2,0,0,0);
		e = new Quaternion(t);
		e.ln();
		e.exp();
//		System.out.println("start: "+t.toString());
//		System.out.println("stop: "+e.toString());	
		assertTrue(t.isEquivalent(e, 1e-15));
		
		
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#log10()}.
	 */
	@Test
	public void testLog10() {
	Quaternion p = new Quaternion(100,0,0,0);
	Quaternion x = new Quaternion(p);
	Quaternion y = new Quaternion(p);
//	System.out.println(" p = "+p.toString());
//	System.out.println("InvLn10*Ln: p = "+x.put(p).ln().multiply(Quaternion.INVERSE_LN10).toString());
//	System.out.println("Log10: p = "+y.put(p).log10().toString());
	x.put(p).ln().multiply(Quaternion.INVERSE_LN10).toString();
	y.put(p).log10().toString();
	assertTrue(x.isEquivalent(y, 1e-15));

	p.put(10,0,0,0);
//	System.out.println(" p = "+p.toString());
//	System.out.println("InvLn10*Ln: p = "+new Quaternion(p).ln().multiply(Quaternion.INVERSE_LN10).toString());
//	System.out.println("Log10: p = "+new Quaternion(p).log10().toString());
	x.put(p).ln().multiply(Quaternion.INVERSE_LN10).toString();
	y.put(p).log10().toString();
	assertTrue(x.isEquivalent(y, 1e-15));

	Angle a = Angle.inDegrees(30);
	Principle t = new Principle(a);
	p.put(t.cos(),0,t.sin(),0);
//	System.out.println("InvLn10*Ln: 30 deg = "+new Quaternion(p).ln().multiply(Quaternion.INVERSE_LN10).toString());
//	System.out.println("Log10: 30 deg = "+new Quaternion(p).log10().toString());
	x.put(p).ln().multiply(Quaternion.INVERSE_LN10).toString();
	y.put(p).log10().toString();
	assertTrue(x.isEquivalent(y, 1e-15));

	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#power(double)}.
	 */
	@Test
	public void testPower() {
		
		Quaternion p = new Quaternion(StrictMath.E,0,0,0);
		double d = 2;
		Quaternion q = new Quaternion(2,0,0,0);
		
		Quaternion x = new Quaternion(p).power(q);
		Quaternion y = new Quaternion(p).power(d);
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));

		p = new Quaternion(StrictMath.E,0,0,0);
		q = new Quaternion(0,0,Angle.inDegrees(30).getRadians(),0);
		x = new Quaternion(p).power(q);
		y = new Quaternion(q).exp();
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		
		p = new Quaternion(StrictMath.E,0,0,0);
		q = new Quaternion(0,Angle.inDegrees(30).getRadians(),Angle.inDegrees(30).getRadians(),Angle.inDegrees(30).getRadians());
		x = new Quaternion(p).power(q);
		y = new Quaternion(q).exp();
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		
		p = new Quaternion(StrictMath.E,0,0,0);
		q = new Quaternion(1,2,3,4);
		x = new Quaternion(p).power(q);
		y = new Quaternion(q).exp();
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		

		p = new Quaternion(10,0,0,0);
		q = new Quaternion(0,0,Angle.inDegrees(30).getRadians(),0);
		x = new Quaternion(q);
		y = new Quaternion(p).power(new Quaternion(q).log10());
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		
		p = new Quaternion(10,0,0,0);
		q = new Quaternion(0,Angle.inDegrees(30).getRadians(),Angle.inDegrees(30).getRadians(),Angle.inDegrees(30).getRadians());
		x = new Quaternion(q);
		y = new Quaternion(p).power(new Quaternion(q).log10());
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		
		p = new Quaternion(10,0,0,0);
		q = new Quaternion(1,2,3,4);
		x = new Quaternion(q);
		y = new Quaternion(p).power(new Quaternion(q).log10());
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-13));

		x = new Quaternion(q);
		y = new Quaternion(p).power(new Quaternion(q).log10());

//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());		
		assertTrue(x.equals(x));
		assertFalse(x.equals(y));
		assertTrue(x.isEquivalent(y,1e-10));
		assertFalse(x.put(-1,2,3,4).equals(y.put(1,2,3,4)));
		assertFalse(x.put(1,-2,3,4).equals(y.put(1,2,3,4)));
		assertFalse(x.put(1,2,-3,4).equals(y.put(1,2,3,4)));
		assertFalse(x.put(1,2,3,-4).equals(y.put(1,2,3,4)));
		assertTrue(x.put(1,2,3,4).equals(y.put(1,2,3,4)));
		assertFalse(x.put(-1,2,3,4).isEquivalent(y.put(1,2,3,4),1e-10));
		assertFalse(x.put(1,-2,3,4).isEquivalent(y.put(1,2,3,4),1e-10));
		assertFalse(x.put(1,2,-3,4).isEquivalent(y.put(1,2,3,4),1e-10));
		assertFalse(x.put(1,2,3,-4).isEquivalent(y.put(1,2,3,4),1e-10));
		assertTrue(x.put(1,2,3,4).isEquivalent(y.put(1,2,3,4),1e-10));
		assertTrue(x.isEquivalent(x,1e-10));
		assertFalse(x.isEquivalent(new Double(5),1e-10));
		assertFalse(x.equals(new Double(5)));
		assertFalse(x.isEquivalent(null,1e-10));
		assertFalse(x.equals(null));

		d = 2;
		q = new Quaternion(d,0,0,0);
		p = new Quaternion(3,0,0,0);
		x = new Quaternion(p).power(d);
		y = new Quaternion(p).power(q);
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		d = 2;
		q = new Quaternion(d,0,0,0);
		p.put(1,2,3,4);
		x = new Quaternion(p).power(d);
		y = new Quaternion(p).power(q);
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-13));
		
		q.put(0,StrictMath.log(2),0,0);
		Vector3 v = new Vector3(StrictMath.log(2),0,0);
		p.put(1,2,3,4);
		x = new Quaternion(p).power(v);
		y = new Quaternion(p).power(q);
//		System.out.println("x = "+x.toString());
//		System.out.println("y = "+y.toString());
		assertTrue(x.isEquivalent(y,1e-15));
		
//		fail("Not yet implemented");
	}


	/**
	 * Test method for {@link rotation.Quaternion#slerp(rotation.Quaternion, double)}.
	 */
	@Test
	public void testSlerp() {
		Quaternion p = new Quaternion(0,1,0,0);
		Quaternion q = new Quaternion(0,0,1,0);
//		System.out.println("Slerp: p "+p.toString());
//		System.out.println("Slerp: q "+q.toString());
//		System.out.println("Slerp: t "+1/3d);
		Operator t = new Operator(q).slerp(p, (1d/3d));
//		System.out.println("Slerp 1/3: "+t.toString());
		assertTrue(t.isEquivalent(new Operator(0, .5, StrictMath.sqrt(3) / 2d, 0),
				1e-15));

		p.put(0,1,0,0);
		q.put(0,0,1,0);
		t.put(q).slerp(p, (2d/3d));
//		System.out.println("Slerp 2/3: "+t.toString());	
		assertTrue(t.isEquivalent(new Operator(0, StrictMath.sqrt(3) / 2, .5, 0),
				1e-15));

		Vector3 vp = new Vector3(1,0,0);
		Vector3 vq = new Vector3(0,1,0);
		Vector3 vt = new Vector3(0,1,0);
//		System.out.println("Slerp: vp "+vp.toString());
//		System.out.println("Slerp: vq "+vq.toString());
//		t.put(vq.slerp(vp, (1d/3d)));
//		t = (Operator)(vq.slerp(vp, (1d/3d)));
		vt = vq.slerp(vp, (1d/3d));
//		System.out.println("Slerp 1/3: "+t.toString());
//		System.out.println("Here!!!: ");

		assertTrue(vt.isEquivalent(new Vector3(.5, StrictMath.sqrt(3) / 2d, 0),
				1e-15));

//		vp = new Vector3(1,0,0);
//		vq = new Vector3(0,1,0);
		vt.put(vq.slerp(vp, (2d/3d)));
//		System.out.println("Slerp: vp "+vp.toString());
//		System.out.println("Slerp: vq "+vq.toString());
//		System.out.println("Slerp 2/3: "+t.toString());

		assertTrue(vt.isEquivalent(new Vector3(StrictMath.sqrt(3) / 2, .5, 0),
				1e-15));

//		t.put(vq.slerp(vp, (1d/2d)));
//		System.out.println("Slerp 1/2: "+t.toString());
		vp.put(1,0,0);
		vq.put(0,0,1);
		//t.put(vq.slerp(vp, (1d/2d)));
//		System.out.println("Slerp 1/2: "+t.toString());
		vp.put(1,0,0);
		vq.put(0,0,1);
		System.out.println("Faster Slerp 1/2: "+vq.slerp(vp, (1d/2d)).toString());
		System.out.println("Faster Slerp to I [1]: "+vq.slerp(vp, (1d)).toString());
		vp.put(5,2,.5);
		vq.put(0,1,1);
//		System.out.println("Faster Slerp 1/2: "+vq.slerp(vp, (1d/2d)).toString());
		System.out.println("Faster Slerp to I [1]: "+vq.slerp(vp, (1d)).toString());
		p.put(0,5,2,.5);
		q.put(0,0,1,1);
		System.out.println("Faster Slerp to I [1]: "+q.slerp(p, (1d)).toString());
		p.put(0,5,2,.5);
		q.put(0,0,1,1);
		System.out.println("Faster Slerp to I [1]: "+q.slerp(p, (.25d)).unit().toString());
		vp.put(5,2,.5);
		vq.put(0,1,1);
		System.out.println("Faster Slerp to I [1]: "+vq.slerp(vp, (.25d)).unit().toString());
		
		vp.put(1,2,1);
		vq.put(1,1,1);
		System.out.println("Faster Slerp: "+vq.slerp(vp, (.5d)).unit().toString());
		vp.put(1,-1,1);
		vq.put(1,1,1);
		System.out.println("Faster Slerp: "+vq.slerp(vp, (.5d)).unit().toString());
		vp.put(0,6,8);
		vq.put(0,0,20);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" mag= "+q.getDeterminant());
		System.out.println("Check Slerp: "+vq.toString()+" mag= "+vq.getAbs());		
		vp.put(0,6,8);
		vq.put(0,0,20);
		System.out.println("Last Faster Slerp: "+vq.put(vq.slerp(vp, (1d))).toString()+" mag= "+new Vector3(vq).getAbs());
		//System.out.println("Check Slerp: "+vq.toString()+" mag= "+vq.abs());		
		System.out.println("==========");
		vp.put(0,6,8);
		vq.put(0,0,20);
		p.put(0,vp);
		q.put(0,vq);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" arg= "+q.getArg().signedAngle().getDegrees());
		System.out.println("Last Faster Slerp: "+vq.put(new Vector3(vq).slerp(vp, (0d))).toString()+" mag= "+new Vector3(vq).getAbs()*vq.getAbs());
		//System.out.println("Check Slerp: "+vp.toString()+" mag= "+vp.abs());		
		//System.out.println("Check Slerp: "+vq.toString()+" det= "+vq.abs()*vq.abs());		
		System.out.println("----------");
		vp.put(0,6,8);
		vq.put(0,0,20);
		p.put(0,vp);
		q.put(0,vq);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" arg= "+q.getArg().signedAngle().getDegrees());
		System.out.println("Last Faster Slerp: "+vq.put(new Vector3(vq).slerp(vp, (.25d))).toString()+" mag= "+new Vector3(vq).getAbs()*vq.getAbs());
		//System.out.println("Check Slerp: "+vp.toString()+" mag= "+vp.abs());		
		//System.out.println("Check Slerp: "+vq.toString()+" det= "+vq.abs()*vq.abs());		
		System.out.println("----------");
		vp.put(0,6,8);
		vq.put(0,0,20);
		p.put(0,vp);
		q.put(0,vq);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" arg= "+q.getArg().signedAngle().getDegrees());
		System.out.println("Last Faster Slerp: "+vq.put(new Vector3(vq).slerp(vp, (.5d))).toString()+" mag= "+new Vector3(vq).getAbs()*vq.getAbs());
		//System.out.println("Check Slerp: "+vp.toString()+" mag= "+vp.abs());		
		//System.out.println("Check Slerp: "+vq.toString()+" det= "+vq.abs()*vq.abs());		
		System.out.println("----------");
		vp.put(0,6,8);
		vq.put(0,0,20);
		p.put(0,vp);
		q.put(0,vq);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" arg= "+q.getArg().signedAngle().getDegrees());
		System.out.println("Last Faster Slerp: "+vq.put(new Vector3(vq).slerp(vp, (.75d))).toString()+" mag= "+new Vector3(vq).getAbs()*vq.getAbs());
		//System.out.println("Check Slerp: "+vp.toString()+" mag= "+vp.abs());		
		//System.out.println("Check Slerp: "+vq.toString()+" det= "+vq.abs()*vq.abs());		
		System.out.println("----------");
		vp.put(0,6,8);
		vq.put(0,0,20);
		p.put(0,vp);
		q.put(0,vq);
		System.out.println("Last Faster Slerp Q: "+q.put(new Quaternion(q).slerp(p, (1d))).toString()+" arg= "+q.getArg().signedAngle().getDegrees());
		System.out.println("Last Faster Slerp: "+vq.put(new Vector3(vq).slerp(vp, (1d))).toString()+" mag= "+new Vector3(vq).getAbs()*vq.getAbs());
		//System.out.println("Check Slerp: "+vp.toString()+" mag= "+vp.abs());		
		//System.out.println("Check Slerp: "+vq.toString()+" det= "+vq.abs()*vq.abs());		
		System.out.println("==========");
		//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#getNorm1()}.
	 */
	@Test
	public void testNorm1() {
		Quaternion t = new Quaternion(1,2,3,4);
		double d = t.getNorm1();
		assertEquals(d,10,1e-14);	
        t.put(1,-2,-3,-4);
		d = t.getNorm1();
		assertEquals(d,10,1e-14);	
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Quaternion#getNormInf()}.
	 */
	@Test
	public void testNormInf() {
		Quaternion t = new Quaternion(1,2,3,4);
		double d = t.getNormInf();
		assertEquals(d,4,1e-14);	
        t.put(1,-2,-3,-4);
		d = t.getNormInf();
		assertEquals(d,4,1e-14);	
//		fail("Not yet implemented");
	}

}
