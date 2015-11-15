/**
 * 
 */
package tspi.model;

import junit.framework.TestCase;
import rotation.Angle;
import rotation.BasisUnit;
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
		Location temp = new Location(Angle.inDegrees(-30),Angle.inDegrees(-90),3000);
//		Principle latitude = new Principle(Angle.inDegrees(-30));
//		Principle longitude = new Principle(Angle.inDegrees(270));
//		double height = 3000;
		System.out.println(temp.getLatitude().signedAngle().getDegrees());
		System.out.println(temp.getLongitude().unsignedAngle().getDegrees());
		System.out.println(temp.getEllipsoidHeight());
		
		temp.getLongitude().put(Angle.inDegrees(-30));
		temp.getLongitude().put(Angle.inDegrees(270));
		temp.setEllipsoidHeight(3000);
		Vector3 result = temp.getGeocentric();
		System.out.println(result.toString());
		temp.set(result);
		System.out.println(temp.getNorthLatitude().getDegrees());
		System.out.println(temp.getEastLongitude().getDegrees());
		System.out.println(temp.getEllipsoidHeight());
		
		//Principle theta = Principle.arcTanHalfAngle(temp.getLatitude().cotHalf());
		Principle ptheta = temp.getLatitude().addRight().negate();
		
		Operator q =QuaternionMath.eulerRotate_kj(temp.getLongitude(), ptheta);
		
		System.out.println(q.unit().toString());
		
		q = new Operator(1,0,0,0).exp_k(temp.getLongitude()).exp_j(ptheta);
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
			
			paz = new Principle(Angle.inDegrees(30));
			pel = new Principle(Angle.inDegrees(30));
			q_AN = QuaternionMath.eulerRotate_kj(paz, pel);
			
			Vector3 direction = new Vector3(pel.cos() * paz.cos(), pel.cos() * paz.sin(),-pel.sin());
			
			Vector3 rgNormal = q_AN.getImage_i();
			Vector3 azNormal = q_AN.getImage_j();
			Vector3 elNormal = q_AN.getImage_k();
			System.out.println("**************************************");
			System.out.println("Az:"+paz.unsignedAngle().getDegrees());
			System.out.println("El:"+pel.signedAngle().getDegrees());
			System.out.println("Direction: "+direction.toString(5));
			System.out.println("**************************************");
			System.out.println("rgNormal: "+rgNormal.toString(5));
			System.out.println("azNormal: "+azNormal.toString(5));
			System.out.println("elNormal: "+elNormal.toString(5));
			System.out.println("**************************************");
			System.out.println("rgNormal: "+rgNormal.unit().toString(5)); //unit is a mutator!!!
			System.out.println("azNormal: "+azNormal.toString(5));
			System.out.println("elNormal: "+elNormal.toString(5));
			System.out.println("**************************************");
			
//			Operator q_NG = new Operator(Quaternion.NAN); 	
//			Angle lat = Angle.inDegrees(60);
//			Angle lon = Angle.inDegrees(30);
//			Angle theta = Angle.inRadians(Angle.PI_2).subtract(lat);
//			Principle plon = lon.getPrinciple();
			
//			atheta = tst.getEuler_j_kji().signedAngle().add(Angle.QUARTER_REVOLUTION);
//			plon.put( tst.getEuler_k_kji());					
//			if (phi >= 0){ //test northern hemisphere!!!
//				qlat = atheta.getDegrees();
//				qlon = plon.signedAngle().add(Angle.HALF_REVOLUTION).getDegrees();
//			} else {
//				qlat = atheta.negate().getDegrees();
//				qlon = plon.unsignedAngle().getDegrees();						
//			}

			//Principle plat = lat.getPrinciple();
//			ptheta = (Angle.inRadians(Angle.QUARTER_REVOLUTION).add(lat)).negate().getPrinciple();
//			ptheta = Angle.inRadians(Angle.PI_2).subtract(lat).getPrinciple();
//		    q_NG.putRightJ(QuaternionMath.eulerRotate_kj(plon,
//				Angle.inRadians(Angle.QUARTER_REVOLUTION).subtract(lat).getPrinciple())).unit();

//			q_NG.putRightJ(QuaternionMath.eulerRotate_kj(plon,ptheta)).unit();
//			Vector3 north = q_NG.getImage_i();
//			Vector3 east = q_NG.getImage_j();
//			Vector3 down = q_NG.getImage_k();
			
//			Principle pplon = q_NG.getEuler_k_kji();
//			Principle pptheta = q_NG.getEuler_j_kji();
//			Principle pplat = pptheta.signedAngle().subtract(Angle.inRadians(Angle.PI_2)).getPrinciple();

			//q_NG.putRightJ(QuaternionMath.eulerRotate_kj(plon,	
			
			paz = new Principle(Angle.inDegrees(150));
			pel = new Principle(Angle.inDegrees(-30));
			q_AN.put(QuaternionMath.eulerRotate_kj(paz, pel).unit());
			
			Vector3 d_AN = q_AN.getImage_i(); 

			Principle yaw = q_AN.getEuler_k_kji();
			Principle pitch = q_AN.getEuler_j_kji();
			Principle twi = q_AN.getEuler_i_kji();
			
			System.out.println("**************************************");
			System.out.println("**************************************");
			System.out.println("yaw|bearing: "+paz.unsignedAngle().getDegrees());
			System.out.println("pitch|el   : "+pel.signedAngle().getDegrees()); //unit is a mutator!!!
			System.out.println("q_AN : "+q_AN.toString(5)); 
			System.out.println("**************************************");
			System.out.println("az : "+yaw.unsignedAngle().getDegrees()); 
			System.out.println("el : "+pitch.signedAngle().getDegrees());
			System.out.println("tw : "+twi.signedAngle().getDegrees());
			System.out.println("d_AN : "+d_AN.toString(5)); 
			
			Operator q_ANw = new Operator(Quaternion.NAN);
			//q_ANw.put(QuaternionMath.foldoverI(d_AN).conjugate());
			q_ANw.putRightTiltI(d_AN).conjugate();
//			q_ANw.put( new Quaternion(Quaternion.IDENTITY).rightMultiplyTiltI(d_AN).conjugate());
//			q_ANw.put( new Quaternion(Quaternion.IDENTITY).leftMultiplyTiltI(d_AN).conjugate());
			yaw = q_ANw.getEuler_k_kji();
			pitch = q_ANw.getEuler_j_kji();
			twi = q_ANw.getEuler_i_kji();
			System.out.println("**************************************");
			System.out.println("az : "+yaw.unsignedAngle().getDegrees()); 
			System.out.println("el : "+pitch.signedAngle().getDegrees());
			System.out.println("tw : "+twi.signedAngle().getDegrees());
			
			
			
		
		
	}
		
	}	
