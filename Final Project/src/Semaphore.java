
public class Semaphore {
    private int count;
    public Semaphore(int count){
        this.count = count;
    }
    public synchronized void waitSem(){
        while(count <= 0){
            try {
                wait();
            } catch (Exception e){
                System.out.println("An error has occured");
            }
        }
        count--;
    }
    public synchronized void signal(){
        count++;
        notify();
    }
}
