/**
 * 
 */
package rotationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import rotation.Angle;
import rotation.BasisUnit;
import rotation.Rotator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.QuaternionMath;
import rotation.Vector3;

/**
 * @author mike
 *
 */
public class OperatorTest {

	/**
	 * Test method for constructing Operator from Quaternion
	 * {@link rotation.Rotator#Operator(rotation.Quaternion)}.
	 */
	@Test
	public void testOperatorQuaternion() {
		Rotator t = new Rotator(1, 2, 3, 4);
		Rotator z = new Rotator(t);
		assertTrue(z.isEquivalent(t, 1e-13));
		// fail("Not yet implemented");
	}

	/**
	 * Test method for constructing operator from scalar and vector components
	 * {@link rotation.Rotator#Operator(double, rotation.Vector3)}.
	 */
	@Test
	public void testOperatorDoubleVector3() {
		double d = 1;
		Vector3 v = new Vector3(2, 3, 4);
		Rotator z = new Rotator(d, v);
		Rotator t = new Rotator(1, 2, 3, 4);
		assertTrue(z.isEquivalent(t, 1e-13));
		// fail("Not yet implemented");
	}

	/**
	 * Test method for magnification to re-scale Operator
	 * {@link rotation.Rotator#getInverseAbs()}.
	 */
	@Test
	public void testGetScaleFactor() {
		Rotator z = new Rotator(1, 2, 3, 4);
		assertEquals(z.getInverseAbs(), 1.d / StrictMath.sqrt(1 + 4 + 9 + 16),
				1e-12);
		assertEquals(z.getInverseDeterminant(), 1d / (1 + 4 + 9 + 16), 1e-12);

		assertEquals(1 / z.getDeterminant(), z.getInverseDeterminant(), 1e-12);
		assertEquals(z.getInverseAbs() * z.getInverseAbs(), z.getInverseDeterminant(),
				1e-12);

		// fail("Not yet implemented");
	}

	/**
	 * Test method for constructing Operator from Euler angles
	 * {@link rotation.Rotator#getEuler_k_kji()}.
	 */
	@Test
	public void testGetEuler() {

		Principle h = new Principle(Angle.inDegrees(10));
		Principle p = new Principle(Angle.inDegrees(30));
		Principle r = new Principle(Angle.inDegrees(40));

		assertEquals(h.signedAngle().getDegrees(), 10d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 30d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 40d, 1e-13);

		 Rotator z = QuaternionMath.exp_j(p);
		System.out.println(z.toString());
		//z.unit();
		//System.out.println(z.toString());
		
		//p.put(z.getEuler_j_kji());		
		//assertEquals(p.unsignedAngle().getDegrees(), 30d, 1e-13);		
		
		z = QuaternionMath.exp_k(h).exp_j(p).exp_i(r);

		h.set(z.getEuler_k_kji());
		p.set(z.getEuler_j_kji());
		r.set(z.getEuler_i_kji());

		assertEquals(h.unsignedAngle().getDegrees(), 10d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 30d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 40d, 1e-13);

		h.set(Angle.inDegrees(-20));
		p.set(Angle.inDegrees(50));
		r.set(Angle.inDegrees(10));

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 10d, 1e-13);

		z = QuaternionMath.exp_k(h).exp_j(p).exp_i(r);// .unit();

		h.set(z.getEuler_k_kji());
		p.set(z.getEuler_j_kji());
		r.set(z.getEuler_i_kji());

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 10d, 1e-13);

		h.set(Angle.inDegrees(-20));
		p.set(Angle.inDegrees(-50));
		r.set(Angle.inDegrees(10));

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), -50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 10d, 1e-13);

		z = QuaternionMath.exp_k(h).exp_j(p).exp_i(r);// .unit();

		h.set(z.getEuler_k_kji());
		p.set(z.getEuler_j_kji());
		r.set(z.getEuler_i_kji());

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), -50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 10d, 1e-13);

		h.set(Angle.inDegrees(-20));
		p.set(Angle.inDegrees(50));
		r.set(Angle.inDegrees(-10));

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), -10d, 1e-13);

		z = QuaternionMath.exp_k(h).exp_j(p).exp_i(r);// .unit();

		h.set(z.getEuler_k_kji());
		p.set(z.getEuler_j_kji());
		r.set(z.getEuler_i_kji());

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), -10d, 1e-13);

		h.set(Angle.inDegrees(-20));
		p.set(Angle.inDegrees(50));
		r.set(Angle.inDegrees(10));

		Vector3 v = new Rotator(1, 0, 0, 0).getEuler_kji().multiply(
				180 / StrictMath.PI);
		assertTrue(v.isEquivalent(new Vector3(Vector3.ZERO), 1e-13));

		v = z.getEuler_kji();

		assertEquals(h.unsignedAngle().getDegrees(), 340d, 1e-13);
		assertEquals(p.signedAngle().getDegrees(), 50d, 1e-13);
		assertEquals(r.signedAngle().getDegrees(), 10d, 1e-13);

		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link rotation.Rotator#turn_i()}.
	 */
	@Test
	public void testTurn() {
		Rotator z = new Rotator(0,0,1,0);
		z.turn(BasisUnit.I).multiply(z.getInverseAbs());
		System.out.println("turn i(j): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,StrictMath.sqrt(2)/2,StrictMath.sqrt(2)/(-2)),1e-12));
		z.set(0,0,0,1);
		z.turn(BasisUnit.J).multiply(z.getInverseAbs());
//		System.out.println("turn j(k): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,StrictMath.sqrt(2)/(2),0,StrictMath.sqrt(2)/-2),1e-13));
		z.set(0,1,0,0);
		z.turn(BasisUnit.K).multiply(z.getInverseAbs());
//		System.out.println("turn k(i): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,StrictMath.sqrt(2)/(2),StrictMath.sqrt(2)/(-2),0),1e-13));
		
		Vector3 v = new Vector3(1,0,0);
		z.set(0,0,1,0);
		z.turn(v).multiply(z.getInverseAbs());
//		System.out.println("turn i(j): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,StrictMath.sqrt(2)/-2,StrictMath.sqrt(2)/(2)),1e-13));
		
		v.put(0,1,0);
		z.set(0,0,0,1);
		z.turn(v).multiply(z.getInverseAbs());
//		System.out.println("turn j(k): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,StrictMath.sqrt(2)/(-2),0,StrictMath.sqrt(2)/2),1e-13));
		
		
		v.put(0,0,1);
		z.set(0,1,0,0);
		z.turn(v).multiply(z.getInverseAbs());
//		System.out.println("turn k(i): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,StrictMath.sqrt(2)/(2),StrictMath.sqrt(2)/(-2),0),1e-13));

		v.put(Vector3.ZERO); //no axis to turn about...
		z.set(0,1,0,0);
		z.turn(v);
//		System.out.println("a. turn i(0): "+z.toString());
		z.multiply(z.getInverseAbs());
//		System.out.println("b. turn i(0): "+z.toString());

		z.set(0,1,0,0);
		z.turn(v).multiply(z.getInverseAbs());
//		System.out.println("c. turn i(0): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,1,0,0),1e-13));
		
		//composite flip-check...
		
		Quaternion q = new Quaternion(1,2,3,4);
		z.set(q);		
		z.turn(BasisUnit.I).turn(BasisUnit.J).turn(BasisUnit.K).turn(BasisUnit.I).turn(BasisUnit.J).turn(BasisUnit.K).multiply(1/8d);
		assertTrue(z.negate().equals(q));

		z.set(q);		
		z.turn(BasisUnit.I).turn(BasisUnit.I).turn(BasisUnit.J).turn(BasisUnit.J).turn(BasisUnit.K).turn(BasisUnit.K).multiply(1/8d);
		assertTrue(z.negate().equals(q));

		//		fail("Not yet implemented");
	}


	/**
	 * Test method for {@link rotation.Rotator#flip_i()}.
	 */
	@Test
	public void testFlip() {
		Rotator z = new Rotator(0,0,1,0);
		z.flip(BasisUnit.I);
//		System.out.println("a. flip i(j): "+z.toString());
//		System.out.println("b. flip i(j): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,0,-1),1e-13));
		z.set(0,0,0,1);
		z.flip(BasisUnit.J);
//		System.out.println("flip j(k): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,-1,0,0),1e-13));
		z.set(0,1,0,0);
		z.flip(BasisUnit.K);
//		System.out.println("flip k(i): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,-1,0),1e-13));
		
		Vector3 v = new Vector3(1,0,0);
		z.set(0,0,1,0);
		z.flip(v);
//		System.out.println("flip i(j): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,0,-1),1e-13));
		
		v.put(0,1,0);
		z.set(0,0,0,1);
		z.flip(v);
//		System.out.println("flip j(k): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,-1,0,0),1e-13));
		
		
		v.put(0,0,1);
		z.set(0,1,0,0);
		z.flip(v);
//		System.out.println("flip k(i): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(0,0,-1,0),1e-13));
		

		//composite flip-checks...maintains order, changes signs...

		Quaternion q = new Quaternion(1,2,3,4);
		z.set(q);		
		z.flip(BasisUnit.I).flip(BasisUnit.J).flip(BasisUnit.K);
		assertTrue(z.negate().equals(q));

		z.set(q);		
		z.flip(BasisUnit.J).flip(BasisUnit.K).flip(BasisUnit.I);
		assertTrue(z.negate().equals(q));

		z.set(q);		
		z.flip(BasisUnit.K).flip(BasisUnit.I).flip(BasisUnit.J);
		assertTrue(z.negate().equals(q));

		//		fail("Not yet implemented");
	}


	/**
	 * Test method for {@link rotation.Rotator#exp_i(rotation.Principle)}.
	 */
	@Test
	public void testExp() {
//		System.out.println("=============== TEST EXP =================");

//		Operator z = new Operator(0,(Angle.inDegrees(30).bisectorPrincipleAngle().getRadians()),0,0);
		Rotator z = new Rotator(0,(Angle.inDegrees(15).getRadians()),0,0);
		Vector3 v = new Vector3(1,0,0);
		Principle theta = new Principle(Angle.inDegrees(30));
		
		z.set(1,0,0,0);
		z.exp_i(Principle.ZERO); //.half().getRadians());
//		System.out.println("exp i(0): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(
				Quaternion.IDENTITY),1e-13));
		z.set(1,0,0,0);
		z.exp_j(Principle.ZERO); //.half().getRadians());
//		System.out.println("exp j(0): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(1,0,0,0),1e-13));
		z.set(1,0,0,0);
		
		z.exp_k(Principle.ZERO); //.half().getRadians());
//		System.out.println("exp k(0): "+z.multiply(z.getNormalizeFactor()).toString());
		assertTrue(z.isEquivalent(new Rotator(1,0,0,0),1e-13));
		
		z.set(1,0,0,0);
		z.exp_i(theta).multiply(z.getInverseAbs()); //.half().getRadians());
//		System.out.println("exp i(30): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(
				StrictMath.cos(Angle.inDegrees(15).getRadians()),
				StrictMath.sin(Angle.inDegrees(15).getRadians()),
				0,
				0),1e-13));
		z.set(1,0,0,0);
		z.exp_j(theta).multiply(z.getInverseAbs()); //.half().getRadians());
//		System.out.println("Here exp j(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), 0, StrictMath.sin(Angle.inDegrees(15)
						.getRadians()), 0), 1e-13));
		
		z.set(1,0,0,0);
		z.exp_k(theta).multiply(z.getInverseAbs()); //.half().getRadians());
		//System.out.println("exp k(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), 0, 0, StrictMath.sin(Angle
						.inDegrees(15).getRadians())), 1e-13));

		theta.set(Angle.inDegrees(120));
		z.set(1,0,0,0);
		z.exp_i(theta).multiply(z.getInverseAbs()); //.half().getRadians());
//		System.out.println("exp i(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()), StrictMath.sin(Angle
						.inDegrees(60).getRadians()),0,0), 1e-13));
		
		z.set(1,0,0,0);
		z.exp_j(theta).multiply(z.getInverseAbs()); //.half().getRadians());
//		System.out.println("exp j(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()), 0, StrictMath.sin(Angle
						.inDegrees(60).getRadians()),0), 1e-13));
		
		z.set(1,0,0,0);
		z.exp_k(theta).multiply(z.getInverseAbs()); //.half().getRadians());
//		System.out.println("exp k(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()), 0, 0, StrictMath.sin(Angle
						.inDegrees(60).getRadians())), 1e-13));
			
//		System.out.println("=============== TEST EXP VECTOR =================");
		
		theta.set(Principle.ZERO);
		z.set(1,0,0,0);
		v.put(1,0,0);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(0): "+z.toString());
		assertTrue(z.isEquivalent(new Rotator(1, 0, 0, 0), 1e-13));			
		theta.set(Angle.inDegrees(30));
		z.set(1,0,0,0);
		v.put(1,0,0);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), StrictMath.sin(Angle.inDegrees(15)
						.getRadians()), 0, 0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,1,0);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector j(30): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), 0, StrictMath.sin(Angle.inDegrees(15)
						.getRadians()), 0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,0,1);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector k(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), 0,0,StrictMath.sin(Angle.inDegrees(15)
						.getRadians())), 1e-13));
		
		theta.set(Angle.inDegrees(120));
		z.set(1,0,0,0);
		v.put(1,0,0);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()), StrictMath.sin(Angle.inDegrees(60)
						.getRadians()),0,0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,1,0);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector j(120): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians()),0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,0,1);
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector k(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0, 0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians())), 1e-13));
		
		
		theta.set(Principle.ZERO);
		z.set(1,0,0,0);
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(0): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(
						1,0, 0, 0), 1e-13));
		
		theta.set(Angle.inDegrees(30));
		z.set(1,0,0,0);
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()),StrictMath.sin(Angle.inDegrees(15)
						.getRadians()),0,0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,1,0).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector j(30): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()),0,StrictMath.sin(Angle.inDegrees(15)
						.getRadians()),0), 1e-13));
		
		
		z.set(1,0,0,0);
		v.put(0,0,1).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector k(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()),0,0,StrictMath.sin(Angle.inDegrees(15)
						.getRadians())), 1e-13));
		
		theta.set(Angle.inDegrees(120));
		z.set(1,0,0,0);
		z.multiply(theta.getPiRadians());
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector i(120): "+z.toString());
		
		z.set(1,0,0,0);
		v.put(0,1,0).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector j(120): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians()), 0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,0,1).multiply(theta.getPiRadians());
		z.exp( theta ,v).multiply(z.getInverseAbs());
//		System.out.println("vector k(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0,0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians())), 1e-13));
		

		theta.set(Principle.ZERO);
		z.set(1,0,0,0);
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector i(0): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(
						1,0,0, 0), 1e-13));
		
		
		theta.set(Angle.inDegrees(30));
		z.set(1,0,0,0);
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector i(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()), StrictMath.sin(Angle.inDegrees(15)
						.getRadians()),0,0), 1e-13));
		
		
		z.set(1,0,0,0);
		v.put(0,1,0).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector j(30): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()),0, StrictMath.sin(Angle.inDegrees(15)
						.getRadians()),0), 1e-13));
		
		z.set(1,0,0,0);
		v.put(0,0,1).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector k(30): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(15)
						.getRadians()),0,0, StrictMath.sin(Angle.inDegrees(15)
						.getRadians())), 1e-13));
		
		theta.set(Angle.inDegrees(120));
		z.set(1,0,0,0);
		z.multiply(theta.getPiRadians());
		v.put(1,0,0).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector i(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()), StrictMath.sin(Angle.inDegrees(60)
						.getRadians()),0,0), 1e-13));
		
		
		z.set(1,0,0,0);
		v.put(0,1,0).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector j(120): "+z.toString());		
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians()),0), 1e-13));
		
		
		z.set(1,0,0,0);
		v.put(0,0,1).multiply(theta.getPiRadians());
		z.exp( v ).multiply(z.getInverseAbs());
//		System.out.println("vector k(120): "+z.toString());
		assertTrue(z
				.isEquivalent(new Rotator(StrictMath.cos(Angle.inDegrees(60)
						.getRadians()),0,0, StrictMath.sin(Angle.inDegrees(60)
						.getRadians())), 1e-13));
		
		
		System.out.println("\nimage ej0 = :"+Vector3.UNIT_J.toString());
		
		Principle wi = new Principle(Angle.inDegrees(30));
		Rotator pi = QuaternionMath.exp_i(wi);
		
		int i;
		for(i=1; i<=12; i+=1)
		{
			System.out.println("\nimage ej"+i+" = :" + pi.getImage(Vector3.UNIT_J).toString());
			System.out.println("        "+i+" = :" + pi.getImage_j().toString());
			System.out.println("  "+ new Rotator(pi).image(new Quaternion(0,Vector3.UNIT_J)).toString());
			pi.exp_i(wi);

		}

		System.out.println("\n=====================================");

		System.out.println("\nimage ek0 = :"+Vector3.UNIT_K.toString());
		
		Principle wj = new Principle(Angle.inDegrees(60));
		Rotator pj = QuaternionMath.exp_j(wj);
		
		int j;
		for(j=1; j<=6; j+=1)
		{
			System.out.println("\nimage ek"+j+" = :" + pj.getImage(Vector3.UNIT_K).toString());
			System.out.println("        "+j+" = :" + pj.getImage_k().toString());
			System.out.println("  "+ new Rotator(pj).image(new Quaternion(0,Vector3.UNIT_K)).toString());
			pj.exp_j(wj);
		}
		
		System.out.println("\n=====================================");

		System.out.println("\nimage ei0 = :"+Vector3.UNIT_I.toString());
		
		Principle wk = new Principle(Angle.inDegrees(45));
		Rotator pk = QuaternionMath.exp_k(wk);
		
		int k;
		for(k=1; k<=8; k+=1)
		{
			System.out.println("\nimage ei"+k+" = :" + pk.getImage(Vector3.UNIT_I).toString());
			System.out.println("        "+k+" = :" + pk.getImage_i().toString());
			System.out.println("  "+ new Rotator(pk).image(new Quaternion(0,Vector3.UNIT_I)).toString());
			pk.exp_k(wk);
		}
		
		
		//		fail("Not yet implemented");
	}





	/**
	 * Test method for {@link rotation.Rotator#getImage_i()}.
	 */
	@Test
	public final void testImagePreimage() {
		
		Rotator t = new Rotator(Quaternion.IDENTITY).exp_i(new Principle(
				Angle.inDegrees(30)));
//		System.out.println("==> image i"+t.image_i().toString());
//		System.out.println("==> image j"+t.image_j().toString());
//		System.out.println("==> image k"+t.image_k().toString());
		assertTrue(t.getImage_i().isEquivalent(new Vector3(1,0,0), 1e-13));
		assertTrue(t.getImage_j().isEquivalent(new Vector3(0d,StrictMath.sqrt(3)/2,0.5), 1e-13));
		assertTrue(t.getImage_k().isEquivalent(new Vector3(0,-.5,StrictMath.sqrt(3)/2), 1e-13));
		
//		System.out.println("==> pre-image i"+t.preImage_i().toString());
//		System.out.println("==> pre-image j"+t.preImage_j().toString());
//		System.out.println("==> pre-image k"+t.preImage_k().toString());
		assertTrue(t.getPreImage_i().isEquivalent(new Vector3(1,0,0), 1e-13));
		assertTrue(t.getPreImage_j().isEquivalent(new Vector3(0d,StrictMath.sqrt(3)/2,-0.5), 1e-13));
		assertTrue(t.getPreImage_k().isEquivalent(new Vector3(0,.5,StrictMath.sqrt(3)/2), 1e-13));

		
//		System.out.println("==> pre-image i-hat"+t.preimage(new Vector3(1,0,0)).toString());
//		System.out.println("==> pre-image j-hat"+t.preimage(new Vector3(0,1,0)).toString());
//		System.out.println("==> pre-image k-hat"+t.preimage(new Vector3(0,0,1)).toString());
		assertTrue(t.getPreImage(new Vector3(1,0,0)).isEquivalent(new Vector3(1,0,0), 1e-13));
		assertTrue(t.getPreImage(new Vector3(0,1,0)).isEquivalent(new Vector3(0d,StrictMath.sqrt(3)/2,-0.5), 1e-13));
		assertTrue(t.getPreImage(new Vector3(0,0,1)).isEquivalent(new Vector3(0,.5,StrictMath.sqrt(3)/2), 1e-13));

//		System.out.println("a===> pre-image i-hat"+new Operator(t).preimage(new Quaternion(0,1,0,0)).toString());
//		System.out.println("b===> pre-image j-hat"+new Operator(t).preimage(new Quaternion(0,0,1,0)).toString());
//		System.out.println("c===> pre-image k-hat"+new Operator(t).preimage(new Quaternion(0,0,0,1)).toString());
		assertTrue(new Rotator(t).preImage(new Rotator(0,1,0,0)).isEquivalent(new Quaternion(0,1,0,0), 1e-13));
		assertTrue(new Rotator(t).preImage(new Rotator(0,0,1,0)).isEquivalent(new Rotator(0,0d,StrictMath.sqrt(3)/2,-0.5), 1e-13));
		assertTrue(new Rotator(t).preImage(new Rotator(0,0,0,1)).isEquivalent(new Rotator(0,0,.5,StrictMath.sqrt(3)/2), 1e-13));
	
		t.multiply(2);
//		System.out.println("==> 2*t image i"+t.image_i().toString());
//		System.out.println("==> 2*t image j"+t.image_j().toString());
//		System.out.println("==> 2*t image k"+t.image_k().toString());
		assertTrue(t.getImage_i().isEquivalent(new Vector3(1,0,0), 1e-13));
		assertTrue(t.getImage_j().isEquivalent(new Vector3(0,StrictMath.sqrt(3)/2,.5), 1e-13));
		assertTrue(t.getImage_k().isEquivalent(new Vector3(0,-.5,StrictMath.sqrt(3)/2), 1e-13));
			//assertTrue(t.image_i().equals(new Vector3(1,0,0), 1e-13));

//		System.out.println("==> 2*t image i-hat"+t.image(new Vector3(1,0,0)).toString());
//		System.out.println("==> 2*t image j-hat"+t.image(new Vector3(0,1,0)).toString());
//		System.out.println("==> 2*t image k-hat"+t.image(new Vector3(0,0,1)).toString());
		assertTrue(t.getImage(new Vector3(1,0,0)).isEquivalent(new Vector3(1,0,0), 1e-13));
		assertTrue(t.getImage(new Vector3(0,1,0)).isEquivalent(new Vector3(0,StrictMath.sqrt(3)/2,.5), 1e-13));
		assertTrue(t.getImage(new Vector3(0,0,1)).isEquivalent(new Vector3(0,-.5,StrictMath.sqrt(3)/2), 1e-13));

//		System.out.println("===> 2*t image i-hat"+new Operator(t).image(new Operator(0,1,0,0)).toString());
//		System.out.println("===> 2*t image j-hat"+new Operator(t).image(new Operator(0,0,1,0)).toString());
//		System.out.println("===> 2*t image k-hat"+new Operator(t).image(new Operator(0,0,0,1)).toString());
		assertTrue(new Rotator(t).image(new Rotator(0,1,0,0)).isEquivalent(new Quaternion(0,1,0,0), 1e-13));
		assertTrue(new Rotator(t).image(new Rotator(0,0,1,0)).isEquivalent(new Rotator(0,0,StrictMath.sqrt(3)/2,.5), 1e-13));
		assertTrue(new Rotator(t).image(new Quaternion(0,0,0,1)).isEquivalent(new Quaternion(0,  0,-.5,StrictMath.sqrt(3)/2), 1e-13));
		
//		fail("Not yet implemented");
	}


}
