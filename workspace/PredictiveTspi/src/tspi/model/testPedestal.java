/**
 * 
 */
package tspi.model;

import junit.framework.TestCase;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;
import rotation.Quaternion;
import rotation.QuaternionMath;
import rotation.Vector3;

/**
 * @author mike
 *
 */
public class testPedestal extends TestCase {
	public void testSetWGS84() {
		WGS84 temp = new WGS84(Angle.inDegrees(-30),Angle.inDegrees(270),3000);
//		Principle latitude = new Principle(Angle.inDegrees(-30));
//		Principle longitude = new Principle(Angle.inDegrees(270));
//		double height = 3000;
		System.out.println(temp.getLatitude().signedAngle().getDegrees());
		System.out.println(temp.getLongitude().unsignedAngle().getDegrees());
		System.out.println(temp.getHeight());
		
		temp.putLatitude(Angle.inDegrees(-30).getPrinciple());
		temp.putLongitude(Angle.inDegrees(270).getPrinciple());
		temp.putHeight(3000);
		Vector3 result = temp.getXYZ();
		System.out.println(result.toString());
		temp.putXYZ(result);
		System.out.println(temp.getAngleLatitude().getDegrees());
		System.out.println(temp.getAngleLongitude().getDegrees());
		System.out.println(temp.getHeight());
		Principle theta = Principle.arcTanHalfAngle(temp.getLatitude().cotHalf());
		
		Operator q =QuaternionMath.eulerRotate_kj(temp.getLongitude(), temp.getTheta());
		
		System.out.println(q.unit().toString());
		
		q = new Operator(1,0,0,0).exp_k(temp.getLongitude()).exp_j(theta);
		System.out.println(q.unit().toString());
		System.out.println(q.getEuler_j_kji().signedAngle().getDegrees());
		System.out.println(q.getEuler_k_kji().unsignedAngle().getDegrees());
		
			//initial Euler angles definition!
			Angle az = Angle.inDegrees(120);
			Angle el = Angle.inDegrees(60);
			Angle tw = Angle.inDegrees(0);
			
			//initial principle Euler angles definition!
			Principle paz = az.getPrinciple();
			Principle pel = el.getPrinciple();
			Principle ptw = tw.getPrinciple();
			
			az = Angle.inDegrees(45);
			el = Angle.inDegrees(30);
			tw = Angle.inDegrees(0);			
			System.out.println("Euler Angles {A,E,T}={ "+ az.getDegrees()+", "+ el.getDegrees()+", "+ tw.getDegrees()+" }");		
			
			//Operator q_AN = (Operator) QuaternionMath.exp_i(ptw.put(tw)).leftMultExpJ(pel.put(el)).leftMultExpK(paz.put(az));
			//Operator q_AN = (Operator) QuaternionMath.exp_k(paz.put(az)).rightMultExpJ(pel.put(el)).rightMultExpI(ptw.put(tw));
			//Operator q_AN = (Operator) QuaternionMath.exp_k(paz.put(az)).rightMultExpJ(pel.put(el));
			
			//Operator q_AN = QuaternionMath.exp_k(paz.put(az)).exp_j(pel.put(el));
			Operator q_AN = QuaternionMath.eulerRotate_kj(paz.put(az), pel.put(el));
			
			System.out.println("q_AN = "+q_AN.toString(10));
			System.out.println("twist     q_AN = "+q_AN.getEuler_i_kji().signedAngle().getDegrees());
			System.out.println("elevation q_AN = "+q_AN.getEuler_j_kji().signedAngle().getDegrees());
			System.out.println("azimuth   q_AN = "+q_AN.getEuler_k_kji().signedAngle().getDegrees());
			System.out.println("A.i to N = "+ q_AN.getImage_i().toString(15));	
			System.out.println("A.j to N = "+ q_AN.getImage_j().toString(15));	
			System.out.println("A.k to N = "+ q_AN.getImage_k().toString(15));	
			
			
			//do rotation to get direction!
			System.out.println("tmp = "+new Quaternion(q_AN).rightMultiply(Vector3.UNIT_I).rightMultiply(QuaternionMath.conjugate(q_AN)).unit().toString(15));
			System.out.println("dir = "+new Vector3( paz.cos()*pel.cos(), paz.sin()*pel.cos(),-pel.sin() ).toString(15));	
			
			
//			paz.put(az.putDegrees(120.0));
//			pel.put(el.putDegrees(60));
//			ptw.put(tw.putDegrees(0));
//			
//			q_AN = (Operator) QuaternionMath.exp_k(paz).rightMultExpJ(pel).rightMultExpI(ptw);
//			System.out.println("q_AN = "+q_AN.toString(10));
//			System.out.println("twist     q_AN = "+q_AN.getEuler_i_kji().signedAngle().getDegrees());
//			System.out.println("elevation q_AN = "+q_AN.getEuler_j_kji().signedAngle().getDegrees());
//			System.out.println("azimuth   q_AN = "+q_AN.getEuler_k_kji().signedAngle().getDegrees());
			
			
		

		
		
		
	}
		
	}	
