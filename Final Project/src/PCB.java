
public class PCB {
    public int pid;
    public String name;
    public State state;
    public boolean active;

    //public enum is used here to give state multiple a defined set of fixed constants
    public enum State {
        Ready,
        Running,
        Blocked
    }
    public PCB(int pid, String name, State state, boolean active){
        this.pid = pid;
        this.name = name;
        this.state = state;
        this.active = active;

    }
}
