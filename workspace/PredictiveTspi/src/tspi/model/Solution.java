package tspi.model;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import rotation.Operator;
import rotation.Vector3;

// TODO this needs to be fleshed out with system matrices, error models, etc.
// and it probably needs a much more specific name than 'Solution'
// I think Pedestal.computeTarget() actually belongs here too...

public class Solution {
	public Vector3 position_EFG;
	public double condition;
	public double error;
	
	public Solution(Vector3 origin, List<Pedestal> pedestals) {		
		//count pedestal sensors active in fuzed solution
		int pedSensorCnt =0;
		for(Pedestal pedestal : pedestals) {
			// begin with case (by 2) for az,el of pedestal... need not always be 2...
			pedSensorCnt += 2;

			//Debug
			System.out.println( "Pedestal "+pedestal.getSystemId()+" : "
//					+ "EFG=" + pedestal.getGeocentricCoordinates().toString(5)
					+ " lon="+pedestal.getEllipsoidalCoordinates().getEastLongitude().getDegrees()
					+ " lat="+pedestal.getEllipsoidalCoordinates().getNorthLatitude().getDegrees()
					+ " h="+pedestal.getHeight()
					+ " az=" + pedestal.getAzimuth()
					+ " el=" + pedestal.getElevation());
		}		
		Vector3 row = new Vector3(Double.NaN,Double.NaN,Double.NaN);
		double [] rhs = new double [pedSensorCnt];
		double [][] matrixData = new double [pedSensorCnt][3];
		int i = 0; 
		for(Pedestal pedestal : pedestals) {	
			//compute q_AG
			System.out.println("q_NG="+pedestal.q_NG.toString(5));
			System.out.println("q_AN="+pedestal.q_AN.toString(5));
			pedestal.q_AG.put(new Operator(pedestal.q_NG).rightMultiply(pedestal.q_AN));
			System.out.println("Pedestal "+pedestal.getSystemId()+": q_AG = "+pedestal.q_AG.toString(5));
			//Assuming two axial sensors per pedestal...			
			row = pedestal.q_AG.getImage_k();//axial AZ
			matrixData[i][0] = row.getX();
			matrixData[i][1]= row.getY();
			matrixData[i][2] = row.getZ();
			
			rhs[i] = new Vector3(origin).subtract(pedestal.r_G).getDot(row);
			i+=1;
			
			row = pedestal.q_AG.getImage_j();//axial EL
			matrixData[i][0] = row.getX();
			matrixData[i][1]= row.getY();
			matrixData[i][2] = row.getZ();
			rhs[i] = new Vector3(origin).subtract(pedestal.r_G).getDot(row);
			i+=1;	
		}
		
		RealMatrix a = new Array2DRowRealMatrix(matrixData);
	System.out.println("Sensors in solution: "+a.getRowDimension());
	System.out.println(a.getColumnDimension()); // 3
		
		SingularValueDecomposition svd = new SingularValueDecomposition(a);
		RealVector b = new ArrayRealVector(rhs);
		RealVector y = svd.getSolver().solve(b);
		
		this.condition = svd.getConditionNumber();
//		this.position_EFG = new Vector3(y.getEntry(0), y.getEntry(1), y.getEntry(2)).add(origin);
		this.position_EFG = new Vector3(y.getEntry(0), y.getEntry(1), y.getEntry(2)).negate().add(origin);
	System.out.println("Condition number : "+this.condition);
	System.out.println( this.position_EFG.toString(5) );
	}
	
	public void measureError(Vector3 truth) {
		Vector3 targetPrime;
		if( truth==null )
			targetPrime = new Vector3(0.0,0.0,0.0);
		else targetPrime = new Vector3(this.position_EFG);
		targetPrime.subtract( truth );
		this.error = targetPrime.getAbs();
	}
}
