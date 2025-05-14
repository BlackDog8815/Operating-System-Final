
public class PCB {
    public int pid;
    public String name;
    public State state;
    public boolean active;
    public int burstTime;

    //public enum is used here to give state multiple a defined set of fixed constants
    public enum State {
        Ready,
        Running,
        Blocked
    }

    public int getPid() {return pid;}

    public String getName() {return name;}

    public State getState() {return state;}

    public void setState(State state) {this.state = state;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}
    public PCB(int pid, String name){
        this.pid = pid;
        this.name = name;
        this.state = State.Ready;
        this.active = true;

    }
}
