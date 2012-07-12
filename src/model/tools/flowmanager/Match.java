package model.tools.flowmanager;

public class Match {

	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
			dataLayerPCP, inputPort, networkDestination,
			networkDestinationMaskLength, networkProtocol, networkSource,
			networkSourceMaskLength, networkTypeOfService,
			transportDestination, transportSource, wildcards;

	public Match(){
		
	}
	public String getDataLayerDestination() {
		return dataLayerDestination;
	}

	public void setDataLayerDestination(String dataLayerDestination) {
		this.dataLayerDestination = dataLayerDestination;
	}

	public String getDataLayerSource() {
		return dataLayerSource;
	}

	public void setDataLayerSource(String dataLayerSource) {
		this.dataLayerSource = dataLayerSource;
	}

	public String getDataLayerType() {
		return dataLayerType;
	}

	public void setDataLayerType(String dataLayerType) {
		this.dataLayerType = dataLayerType;
	}

	public String getDataLayerVLAN() {
		return dataLayerVLAN;
	}

	public void setDataLayerVLAN(String dataLayerVLAN) {
		this.dataLayerVLAN = dataLayerVLAN;
	}

	public String getDataLayerPCP() {
		return dataLayerPCP;
	}

	public void setDataLayerPCP(String dataLayerPCP) {
		this.dataLayerPCP = dataLayerPCP;
	}

	public String getInputPort() {
		return inputPort;
	}

	public void setInputPort(String inputPort) {
		this.inputPort = inputPort;
	}

	public String getNetworkDestination() {
		return networkDestination;
	}

	public void setNetworkDestination(String networkDestination) {
		this.networkDestination = networkDestination;
	}

	public String getNetworkDestinationMaskLength() {
		return networkDestinationMaskLength;
	}

	public void setNetworkDestinationMaskLength(
			String networkDestinationMaskLength) {
		this.networkDestinationMaskLength = networkDestinationMaskLength;
	}

	public String getNetworkProtocol() {
		return networkProtocol;
	}

	public void setNetworkProtocol(String networkProtocol) {
		this.networkProtocol = networkProtocol;
	}

	public String getNetworkSource() {
		return networkSource;
	}

	public void setNetworkSource(String networkSource) {
		this.networkSource = networkSource;
	}

	public String getNetworkSourceMaskLength() {
		return networkSourceMaskLength;
	}

	public void setNetworkSourceMaskLength(String networkSourceMaskLength) {
		this.networkSourceMaskLength = networkSourceMaskLength;
	}

	public String getNetworkTypeOfService() {
		return networkTypeOfService;
	}

	public void setNetworkTypeOfService(String networkTypeOfService) {
		this.networkTypeOfService = networkTypeOfService;
	}

	public String getTransportDestination() {
		return transportDestination;
	}

	public void setTransportDestination(String transportDestination) {
		this.transportDestination = transportDestination;
	}

	public String getTransportSource() {
		return transportSource;
	}

	public void setTransportSource(String transportSource) {
		this.transportSource = transportSource;
	}

	public String getWildcards() {
		return wildcards;
	}

	public void setWildcards(String wildcards) {
		this.wildcards = wildcards;
	}

	public String Serialize() {
		String serial = "";
		if (this.dataLayerSource != null){
			serial = serial.concat(",\"src-mac\":\"" + this.dataLayerSource + "\"");
		}
		if (this.dataLayerDestination != null){
			serial = serial.concat(",\"dst-mac\":\"" + this.dataLayerDestination
				+ "\"");
		}
		if (this.dataLayerType != null){
			serial = serial.concat(",\"ether-type\":\"" + this.dataLayerType + "\"");
		}
		if (this.dataLayerVLAN != null){
			serial = serial.concat(",\"vlan-id\":\"" + this.dataLayerVLAN + "\"");
		}
		if (this.dataLayerPCP != null){
			serial = serial.concat(",\"vlan-priority\":\"" + this.dataLayerPCP
				+ "\"");
		}
		if (this.inputPort != null){
			serial = serial.concat(",\"ingress-port\":\"" + this.inputPort + "\"");
		}
		if (this.networkDestination != null){
			serial = serial
				.concat(",\"dst-ip\":\"" + this.networkDestination + "\"");
		}
		if (this.networkSource != null){
			serial = serial.concat(",\"src-ip\":\"" + this.networkSource + "\"");
		}
		if (this.networkTypeOfService != null){
			serial = serial.concat(",\"tos-bits\":\"" + this.networkTypeOfService
				+ "\"");
		}
		if (this.transportDestination != null){
			serial = serial.concat(",\"dst-port\":\"" + this.transportDestination
				+ "\"");
		}
		if (this.transportSource != null){
			serial = serial.concat(",\"src-port\":\"" + this.transportSource + "\"");
		}
		if (this.wildcards != null){
			serial = serial.concat(",\"wildcards\":\"" + this.wildcards + "\"");
		}
		// This is not able to be set by the user yet
		// if(!this.networkDestinationMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkDestinationMaskLength + "\"");
		// This is not able to be set by the user yet
		// if(!this.networkSourceMaskLength.isEmpty())
		// if(serial.length() > 5)
		// serial = serial.concat(",");
		// serial = serial.concat("\"ingress-port\":\"" +
		// this.networkSourceMaskLength + "\"");
		return serial;
	}
}
