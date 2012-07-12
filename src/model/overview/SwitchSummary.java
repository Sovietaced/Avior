package model.overview;

// This is an object implementation of the switch table summary. Since I would have to grab data from several different restAPI URLs it's simply easier to create an object
// and then later exctact that data.
public class SwitchSummary {

	String dpid, manufacturer;
	int packetCount, byteCount, flowCount;
	
	public SwitchSummary(String id){
		dpid = id; 
	}
	
	public String getDpid() {
		return dpid;
	}

	public void setDpid(String dpid) {
		this.dpid = dpid;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getPacketCount() {
		return packetCount;
	}

	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public int getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(int flowCount) {
		this.flowCount = flowCount;
	}

	public void setVendor(String man){
		this.manufacturer = man;
	}
	public String toString(){
		return "Switch: " + this.dpid + " | Vendor: " + this.manufacturer + " | Packets: " 
	+ this.packetCount + " | Bytes: " + this.byteCount + " | Flows: " + this.flowCount;
	}
}