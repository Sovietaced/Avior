package model.tools.firewall;

public class Rule {

    // This is the only data member declared an integer because it it cannot be manipulated.
    private int ruleid;

    private String name, dpid, in_port, dl_src, dl_dst, dl_type, nw_src_prefix,
            nw_src_maskbits, nw_dst_prefix, nw_dst_maskbits, nw_proto, tp_src,
            tp_dst, action, priority;

    private boolean wildcard_dpid, wildcard_in_port, wildcard_dl_src,
            wildcard_dl_dst, wildcard_dl_type, wildcard_nw_src,
            wildcard_nw_dst, wildcard_nw_proto, wildcard_tp_src,
            wildcard_tp_dst;

    public Rule() {
        this.name = "";
        this.dpid = "";
        this.in_port = "";
        this.action = "";
        this.priority = "";
        this.wildcard_dpid = true;
        this.wildcard_in_port = true;
        this.wildcard_dl_src = true;
        this.wildcard_dl_dst = true;
        this.wildcard_dl_type = true;
        this.wildcard_nw_src = true;
        this.wildcard_nw_dst = true;
        this.wildcard_nw_proto = true;
        this.wildcard_tp_src = true;
        this.wildcard_tp_dst = true;
    }

    public String getSrcIP() {
        if(nw_src_prefix != null && nw_src_maskbits != null)
            return nw_src_prefix + "/" + nw_src_maskbits;
        return "";
    }

    public String getDstIP() {
        if(nw_dst_prefix != null && nw_dst_maskbits != null)
            return nw_dst_prefix + "/" + nw_dst_maskbits;
        return "";
    }
    
    public void setSrcIP(String srcip){
        String[] split = srcip.split("/");
        nw_src_prefix = split[0];
        nw_src_maskbits = split[1];
    }
    
    public void setDstIP(String dstip){
        String[] split = dstip.split("/");
        nw_dst_prefix = split[0];
        nw_dst_maskbits = split[1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRuleid() {
        return ruleid;
    }

    public void setRuleid(int ruleid) {
        this.ruleid = ruleid;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        if(!dpid.equals("ff:ff:ff:ff:ff:ff:ff:ff"))
            this.dpid = dpid;
    }

    public String getIn_port() {
        return in_port;
    }

    public void setIn_port(String in_port) {
        if(!in_port.equals("0"))
            this.in_port = in_port;
    }

    public String getDl_src() {
        return dl_src;
    }

    public void setDl_src(String dl_src) {
        if(!dl_src.equals("00:00:00:00:00:00"))
            this.dl_src = dl_src;
    }

    public String getDl_dst() {
        return dl_dst;
    }

    public void setDl_dst(String dl_dst) {
        if(!dl_dst.equals("00:00:00:00:00:00"))
            this.dl_dst = dl_dst;
    }

    public String getDl_type() {
        return dl_type;
    }

    public void setDl_type(String dl_type) {
        if(!dl_type.equals("0")){
        	if(dl_type.equals("ARP"))
        		dl_type = "2054";
        	else
        		this.dl_type = dl_type;
        }
    }

    public String getNw_src_prefix() {
        return nw_src_prefix;
    }

    public void setNw_src_prefix(String nw_src_prefix) {
        if(!nw_src_prefix.equals("0.0.0.0"))
            this.nw_src_prefix = nw_src_prefix;
    }

    public String getNw_src_maskbits() {
        return nw_src_maskbits;
    }

    public void setNw_src_maskbits(String nw_src_maskbits) {
        if(!nw_src_maskbits.equals("0"))
            this.nw_src_maskbits = nw_src_maskbits;
    }

    public String getNw_dst_prefix() {
        return nw_dst_prefix;
    }

    public void setNw_dst_prefix(String nw_dst_prefix) {
        if(!nw_dst_prefix.equals("0.0.0.0"))
            this.nw_dst_prefix = nw_dst_prefix;
    }

    public String getNw_dst_maskbits() {
        return nw_dst_maskbits;
    }

    public void setNw_dst_maskbits(String nw_dst_maskbits) {
        if(!nw_dst_maskbits.equals("0"))
            this.nw_dst_maskbits = nw_dst_maskbits;
    }

    public String getNw_proto() {
        return nw_proto;
    }

    public void setNw_proto(String nw_proto) {
        if(nw_proto.equals("6"))
            this.nw_proto = "TCP";
        else if(nw_proto.equals("17"))
            this.nw_proto = "UDP";
        else if(nw_proto.equals("1"))
            this.nw_proto = "ICMP";
        else if(!nw_proto.equals("0"))
            this.nw_proto = nw_proto;
    }

    public String getTp_src() {
        return tp_src;
    }

    public void setTp_src(String tp_src) {
        if(!tp_src.equals("0"))
            this.tp_src = tp_src;
    }

    public String getTp_dst() {
        return tp_dst;
    }

    public void setTp_dst(String tp_dst) {
        if(!tp_dst.equals("0"))
            this.tp_dst = tp_dst;
    }

    public boolean isWildcard_dpid() {
        return wildcard_dpid;
    }

    public void setWildcard_dpid(boolean wildcard_dpid) {
        this.wildcard_dpid = wildcard_dpid;
    }

    public boolean isWildcard_in_port() {
        return wildcard_in_port;
    }

    public void setWildcard_in_port(boolean wildcard_in_port) {
        this.wildcard_in_port = wildcard_in_port;
    }

    public boolean isWildcard_dl_src() {
        return wildcard_dl_src;
    }

    public void setWildcard_dl_src(boolean wildcard_dl_src) {
        this.wildcard_dl_src = wildcard_dl_src;
    }

    public boolean isWildcard_dl_dst() {
        return wildcard_dl_dst;
    }

    public void setWildcard_dl_dst(boolean wildcard_dl_dst) {
        this.wildcard_dl_dst = wildcard_dl_dst;
    }

    public boolean isWildcard_dl_type() {
        return wildcard_dl_type;
    }

    public void setWildcard_dl_type(boolean wildcard_dl_type) {
        this.wildcard_dl_type = wildcard_dl_type;
    }

    public boolean isWildcard_nw_src() {
        return wildcard_nw_src;
    }

    public void setWildcard_nw_src(boolean wildcard_nw_src) {
        this.wildcard_nw_src = wildcard_nw_src;
    }

    public boolean isWildcard_nw_dst() {
        return wildcard_nw_dst;
    }

    public void setWildcard_nw_dst(boolean wildcard_nw_dst) {
        this.wildcard_nw_dst = wildcard_nw_dst;
    }

    public boolean isWildcard_nw_proto() {
        return wildcard_nw_proto;
    }

    public void setWildcard_nw_proto(boolean wildcard_nw_proto) {
        this.wildcard_nw_proto = wildcard_nw_proto;
    }

    public boolean isWildcard_tp_src() {
        return wildcard_tp_src;
    }

    public void setWildcard_tp_src(boolean wildcard_tp_src) {
        this.wildcard_tp_src = wildcard_tp_src;
    }

    public boolean isWildcard_tp_dst() {
        return wildcard_tp_dst;
    }

    public void setWildcard_tp_dst(boolean wildcard_tp_dst) {
        this.wildcard_tp_dst = wildcard_tp_dst;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        if(!priority.equals("0"))
            this.priority = priority;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if(action.toLowerCase().equals(("deny")))
                this.action = action.toUpperCase();
    }
    
    public String serialize(){
        String serial = "{";

        if (dpid != null && !dpid.equals("")) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"switchid\":\"" + dpid + "\"");
        }
        if (in_port != null && !in_port.equals("")) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"src-inport\":\"" + in_port + "\"");
        }
        if (dl_src != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"src-mac\":\"" + dl_src + "\"");
        }
        if (dl_dst != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"dst-mac\":\"" + dl_dst + "\"");
        }
        if (nw_src_prefix != null && nw_src_maskbits != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"src-ip\":\"" + nw_src_prefix + "/" + nw_src_maskbits + "\"");
        }
        if (nw_dst_prefix != null && nw_dst_maskbits != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"dst-ip\":\"" + nw_dst_prefix + "/" + nw_dst_maskbits + "\"");
        }
        if (nw_proto != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"nw-proto\":\"" + nw_proto + "\"");
        }
        if (dl_type != null && !dl_type.equals("2048")) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"dl-type\":\"" + dl_type + "\"");
        }
        if (tp_src != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"tp-src\":\"" + tp_src + "\"");
        }
        if (tp_dst != null) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"tp-dst\":\"" + tp_dst + "\"");
        }
        if (priority != null && !priority.equals("")) {
            if (serial.length() > 10)
                serial = serial.concat(", ");
            serial = serial.concat("\"priority\":\"" + priority + "\"");
        }
        if (action != null && !action.equals("")) {
            if(action.equals("DENY")){
                if (serial.length() > 10)
                    serial = serial.concat(", ");
                serial = serial.concat("\"action\":\"" + action + "\"");
            }
        }
        serial = serial.concat("}");
        return serial;
    }
    
    public boolean equals(Rule other){
        return this.serialize().equals(other.serialize());
    }
    
    public String deleteString(){
        return "{\"ruleid\":\"" + this.ruleid + "\"}";
    }

}
