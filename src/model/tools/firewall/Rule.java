package model.tools.firewall;

public class Rule {

	private int ruleid;

    private long dpid; 
    private short in_port; 
    private long dl_src; 
    private long dl_dst; 
    private short dl_type; 
    private int nw_src_prefix; 
    private int nw_src_maskbits;
    private int nw_dst_prefix;
    private int nw_dst_maskbits;
    private short nw_proto;
    private short tp_src;
    private short tp_dst;

    private boolean wildcard_dpid;
    private boolean wildcard_in_port;
	private boolean wildcard_dl_src;
    private boolean wildcard_dl_dst;
    private boolean wildcard_dl_type;
    private boolean wildcard_nw_src;
    private boolean wildcard_nw_dst;
    private boolean wildcard_nw_proto;
    private boolean wildcard_tp_src;
    private boolean wildcard_tp_dst;

    private int priority = 0;

    private FirewallAction action;

    private enum FirewallAction {
        /*
         * DENY: Deny rule
         * ALLOW: Allow rule
         */
        DENY, ALLOW
    }

    public Rule() {
        this.in_port = 0; 
        this.dl_src = 0;
        this.nw_src_prefix = 0;
        this.nw_src_maskbits = 0; 
        this.dl_dst = 0;
        this.nw_proto = 0;
        this.tp_src = 0;
        this.tp_dst = 0;
        this.dl_dst = 0;
        this.nw_dst_prefix = 0;
        this.nw_dst_maskbits = 0; 
        this.dpid = -1;
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
        this.priority = 0; 
        this.action = FirewallAction.ALLOW; 
        this.ruleid = 0; 
    }
    
    public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public long getDpid() {
		return dpid;
	}

	public void setDpid(long dpid) {
		this.dpid = dpid;
	}

	public short getIn_port() {
		return in_port;
	}

	public void setIn_port(short in_port) {
		this.in_port = in_port;
	}

	public long getDl_src() {
		return dl_src;
	}

	public void setDl_src(long dl_src) {
		this.dl_src = dl_src;
	}

	public long getDl_dst() {
		return dl_dst;
	}

	public void setDl_dst(long dl_dst) {
		this.dl_dst = dl_dst;
	}

	public short getDl_type() {
		return dl_type;
	}

	public void setDl_type(short dl_type) {
		this.dl_type = dl_type;
	}

	public int getNw_src_prefix() {
		return nw_src_prefix;
	}

	public void setNw_src_prefix(int nw_src_prefix) {
		this.nw_src_prefix = nw_src_prefix;
	}

	public int getNw_src_maskbits() {
		return nw_src_maskbits;
	}

	public void setNw_src_maskbits(int nw_src_maskbits) {
		this.nw_src_maskbits = nw_src_maskbits;
	}

	public int getNw_dst_prefix() {
		return nw_dst_prefix;
	}

	public void setNw_dst_prefix(int nw_dst_prefix) {
		this.nw_dst_prefix = nw_dst_prefix;
	}

	public int getNw_dst_maskbits() {
		return nw_dst_maskbits;
	}

	public void setNw_dst_maskbits(int nw_dst_maskbits) {
		this.nw_dst_maskbits = nw_dst_maskbits;
	}

	public short getNw_proto() {
		return nw_proto;
	}

	public void setNw_proto(short nw_proto) {
		this.nw_proto = nw_proto;
	}

	public short getTp_src() {
		return tp_src;
	}

	public void setTp_src(short tp_src) {
		this.tp_src = tp_src;
	}

	public short getTp_dst() {
		return tp_dst;
	}

	public void setTp_dst(short tp_dst) {
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public FirewallAction getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals("ALLOW")){
			this.action = FirewallAction.ALLOW;
		}
		else{
			this.action = FirewallAction.DENY;
		}
	}

}
