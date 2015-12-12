package tspi.model;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTable;
//import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/** A model which represents a list of Pedestals. Also provides a file load and save, as well as a CellRenderer appropriate for the model. */
@SuppressWarnings("serial")
public class PedestalModel extends AbstractTableModel implements Iterable<Pedestal> {

	protected ArrayList<Pedestal> pedestals;
	public static final int ID=0, LAT=1, LON=2, H=3, AZ=4, EL=5, R=6;
	public static final int GEOCENTRIC=1, ELLIPSOIDAL=2;
	protected int system = ELLIPSOIDAL;
	
	public PedestalModel() {
		this.pedestals = new ArrayList<Pedestal>();
	}
	
	public Pedestal getPedestal(int row) {
		return pedestals.get(row);
	}
	
	public Pedestal getPedestal(String systemId) {
		for(Pedestal pedestal : this.pedestals)
			if(pedestal.systemId.equals(systemId))
				return pedestal;
		return null;
	} // might want to index if this is a common operation...
	
	public void add(int index, Pedestal pedestal) {
		this.pedestals.add(index, pedestal);
		this.fireTableRowsInserted(index, index);
	}
	
	public void remove(int index) {
		this.pedestals.remove(index);
		this.fireTableRowsDeleted(index, index);
	}
	
	public void clearOrientations() {
		for(Pedestal pedestal : pedestals)
			pedestal.aer.clear(Double.NaN);
	}
	
	public void setCooordinateSystem(int system) {
		if(system == ELLIPSOIDAL || system == GEOCENTRIC) {
			this.system = system;
			this.fireTableStructureChanged();
		}
	}
	
	@Override
	public Iterator<Pedestal> iterator() { return this.pedestals.iterator(); }
	
	@Override
	public int getColumnCount() { return 7; }

	@Override
	public int getRowCount() { return pedestals.size(); }

	@Override
	public String getColumnName(int col) {
		if( this.system == ELLIPSOIDAL ) {
			switch(col) {
			case ID: return "System";
			case LAT: return "Latitude";
			case LON: return "Longitude";
			case H: return "Height";
			case AZ: return "Azimuth";
			case EL: return "Elevation";
			case R: return "Range";
			}
		} else if( this.system == GEOCENTRIC ) {
			switch(col) {
			case ID: return "System";
			case LAT: return "E";
			case LON: return "F";
			case H: return "G";
			case AZ: return "Azimuth";
			case EL: return "Elevation";
			case R: return "Range";
			}
		}
		return "";
	}  
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case ID: return String.class;
		case LAT: return Double.class;
		case LON: return Double.class;
		case H: return Double.class;
		case AZ: return Double.class;
		case EL: return Double.class;
		case R: return Double.class;
		default: return Object.class;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if(col==AZ || col==EL || col==R)
			return false;
		return true;
	} 
	
	@Override
	public Object getValueAt(int row, int col) {
		Pedestal pedestal = pedestals.get(row);
		if( this.system == ELLIPSOIDAL ) {
			switch(col) {
			case ID: return pedestal.getSystemId();
			case LAT: return pedestal.getLatitude();
			case LON: return pedestal.getLongitude();
			case H: return pedestal.getHeight();
			case AZ: return pedestal.getAzimuth();
			case EL: return pedestal.getElevation();
			case R: return pedestal.getRange();
			}
		} else if( this.system == GEOCENTRIC ) {
			switch(col) {
			case ID: return pedestal.getSystemId();
			case LAT: return pedestal.getE();
			case LON: return pedestal.getF();
			case H: return pedestal.getG();
			case AZ: return pedestal.getAzimuth();
			case EL: return pedestal.getElevation();
			case R: return pedestal.getRange();
			}
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Pedestal pedestal = pedestals.get(row);
		if( this.system == ELLIPSOIDAL ) {
			switch(col) {
			case ID: pedestal.setSystemId( (String)value ); break;
			case LAT: pedestal.setLatitude( (Double)value ); break;
			case LON: pedestal.setLongitude( (Double)value ); break;
			case H: pedestal.setHeight( (Double)value ); break;
			case AZ: pedestal.setAzimuth( (Double)value ); break;
			case EL: pedestal.setElevation( (Double)value ); break;
			case R: pedestal.setRange( (Double)value ); break;
			}
		} else if( this.system == GEOCENTRIC ) {
			switch(col) {
			case ID: pedestal.setSystemId( (String)value ); break;
			case LAT: pedestal.setE( (Double)value ); break;
			case LON: pedestal.setF( (Double)value ); break;
			case H: pedestal.setG( (Double)value ); break;
			case AZ: pedestal.setAzimuth( (Double)value ); break;
			case EL: pedestal.setElevation( (Double)value ); break;
			case R: pedestal.setRange( (Double)value ); break;
			}
		}
	}
	
	// TODO replace this with something standard!!!!!!!!!!!
	public void load(File file) throws Exception {
		pedestals.clear();
		BufferedReader reader = new BufferedReader( new FileReader(file) );
		String line = "";
		int n=1;
		try {
			while( (line=reader.readLine()) != null) {
				String cols[] = line.split(",");
				if(cols.length==0 || (cols.length==1&&cols[0].equals("")))
					continue;
				String id = cols[0].trim();
				Double lat = Double.parseDouble(cols[1].trim());
				Double lon = Double.parseDouble(cols[2].trim());
				Double h = Double.parseDouble(cols[3].trim());
				Pedestal pedestal = new Pedestal(id, lat, lon, h);
				pedestals.add(pedestal);
				n++;
			}
		} catch(Exception exception) {
			throw new Exception( "Error, line "+n+" : First 4 columns should be id, lat, lon, h\n\""+line+"\"\nStopped loading." );
		} finally {
			reader.close();
			this.fireTableDataChanged();
		}
	}
	
	// TODO replace this with something that doesn't suck.
	public void save(File file) throws Exception {
		this.system = ELLIPSOIDAL;
		PrintWriter writer = new PrintWriter(file);
		for(Pedestal pedestal : pedestals) {
			writer.append(pedestal.getSystemId());
			writer.append(",");
			writer.append(Double.toString(pedestal.getLatitude()));
			writer.append(",");
			writer.append(Double.toString(pedestal.getLongitude()));
			writer.append(",");
			writer.append(Double.toString(pedestal.getHeight()));
			if(pedestal.aer.getAzimuth()!=null && pedestal.aer.getElevation()!=null) {
				writer.append(",");
				writer.append(Double.toString(pedestal.getAzimuth()));
				writer.append(",");
				writer.append(Double.toString(pedestal.getElevation()));
				// TODO add range?
//				writer.append(",");
//				writer.append(Double.toString(pedestal.getRange()));
			}
			writer.println();
		}
		writer.close();
	}
	
	public static class CellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int col) {
			// get a default label for the cell
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			// this is from back when we were only fusing two sensors
			// turn off the selection if it is not the first selected or last selected
			// this communicates to the user only two rows can be selected
//			ListSelectionModel selections = table.getSelectionModel();
//			if( row == selections.getMaxSelectionIndex()
//					|| row == selections.getMinSelectionIndex() )
//				cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
//			else cell = super.getTableCellRendererComponent(table, value, false, hasFocus, row, col);
			
			if(col==AZ || col==EL || col== R )
				cell.setEnabled(false);
			else
				cell.setEnabled(true);
						
			return cell;
		}
	}
}
