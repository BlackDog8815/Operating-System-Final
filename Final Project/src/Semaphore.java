public class Semaphore {
    private int count;
    public Semaphore(int count){
        this.count = count;
    }
    public synchronized void waitSem(){
        while(count <= 0){
            try {
                wait();//Puts the thread to sleep
            } catch (Exception e){
                System.out.println("An error has occured");
            }
        }
        count--;
    }
    public synchronized void signal(){
        count++;
        notify();//Wakes up the sleeping thread
    }
}
