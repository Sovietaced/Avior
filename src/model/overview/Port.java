package model.overview;

public class Port {

	String portNumber, receivePackets, transmitPackets, receiveBytes, transmitBytes, receiveDropped, transmitDropped, receiveErrors,
	transmitErrors, receieveFrameErrors, receieveOverrunErrors, receiveCRCErrors, collisions, advertisedFeatures, config, currentFeatures, 
	hardwareAddress, name, peerFeatures, state, supportedFeatures, errors;

	public String getAdvertisedFeatures() {
		return advertisedFeatures;
	}

	public void setAdvertisedFeatures(String advertisedFeatures) {
		this.advertisedFeatures = advertisedFeatures;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getCurrentFeatures() {
		return currentFeatures;
	}

	// Credit to Wes Felter for his bit shift operations here
	public void setCurrentFeatures(String currentFeatures) {
		switch(Integer.valueOf(currentFeatures) & 127){
		case 1:
            this.currentFeatures = " - 10 Mbps";
            break;
        case 2:
        	this.currentFeatures = " - 10 Mbps FDX";
            break;
        case 4:
        	this.currentFeatures = " - 100 Mbps";
            break;
        case 8:
        	this.currentFeatures = " - 100 Mbps FDX";
            break;
        case 16:
        	this.currentFeatures = " - 1 Gbps"; // RLY?
            break;
        case 32:
        	this.currentFeatures = " - 1 Gbps FDX";
            break;
        case 64:
        	this.currentFeatures = " - 10 Gbps FDX";
            break;
        default:
        	this.currentFeatures = "";
        	break;
		}
	}

	public String getStatus() {
		return this.state + this.currentFeatures;
	}
	
	public String getErrors(){
		return String.valueOf(Integer.valueOf(this.receieveFrameErrors) + Integer.valueOf(this.receieveOverrunErrors) + 
				Integer.valueOf(this.receiveCRCErrors) + Integer.valueOf(this.receiveErrors) + Integer.valueOf(this.transmitErrors));
	}

	public String getHardwareAddress() {
		return hardwareAddress;
	}

	public void setHardwareAddress(String hardwareAddress) {
		this.hardwareAddress = hardwareAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeerFeatures() {
		return peerFeatures;
	}

	public void setPeerFeatures(String peerFeatures) {
		this.peerFeatures = peerFeatures;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		switch(Integer.valueOf(state) & 1){
		case 0: 
			this.state = "UP";
			break;
		case 1:
			this.state = "DOWN";
			break;
		}
	}

	public String getSupportedFeatures() {
		return supportedFeatures;
	}

	public void setSupportedFeatures(String supportedFeatures) {
		this.supportedFeatures = supportedFeatures;
	}

	public Port(String pn){
		portNumber = pn;	
	}
	
	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getReceivePackets() {
		return receivePackets;
	}

	public void setReceivePackets(String receivePackets) {
		this.receivePackets = receivePackets;
	}

	public String getTransmitPackets() {
		return transmitPackets;
	}

	public void setTransmitPackets(String transmitPackets) {
		this.transmitPackets = transmitPackets;
	}

	public String getReceiveBytes() {
		return receiveBytes;
	}

	public void setReceiveBytes(String receiveBytes) {
		this.receiveBytes = receiveBytes;
	}

	public String getTransmitBytes() {
		return transmitBytes;
	}

	public void setTransmitBytes(String transmitBytes) {
		this.transmitBytes = transmitBytes;
	}

	public String getReceiveDropped() {
		return receiveDropped;
	}

	public void setReceiveDropped(String receiveDropped) {
		this.receiveDropped = receiveDropped;
	}

	public String getTransmitDropped() {
		return transmitDropped;
	}

	public void setTransmitDropped(String transmitDropped) {
		this.transmitDropped = transmitDropped;
	}

	public String getReceiveErrors() {
		return receiveErrors;
	}

	public void setReceiveErrors(String receiveErrors) {
		this.receiveErrors = receiveErrors;
	}

	public String getTransmitErrors() {
		return transmitErrors;
	}

	public void setTransmitErrors(String transmitErrors) {
		this.transmitErrors = transmitErrors;
	}

	public String getReceieveFrameErrors() {
		return receieveFrameErrors;
	}

	public void setReceieveFrameErrors(String receieveFrameErrors) {
		this.receieveFrameErrors = receieveFrameErrors;
	}

	public String getReceieveOverrunErrors() {
		return receieveOverrunErrors;
	}

	public void setReceieveOverrunErrors(String receieveOverrunErrors) {
		this.receieveOverrunErrors = receieveOverrunErrors;
	}

	public String getReceiveCRCErrors() {
		return receiveCRCErrors;
	}

	public void setReceiveCRCErrors(String receiveCRCErrors) {
		this.receiveCRCErrors = receiveCRCErrors;
	}

	public String getCollisions() {
		return collisions;
	}

	public void setCollisions(String collisions) {
		this.collisions = collisions;
	} 
}
