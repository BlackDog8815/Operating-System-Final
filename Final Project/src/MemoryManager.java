import java.util.ArrayList;

public class MemoryManager {
    int[] memory = new int[100];
    ArrayList<Integer> pidList;
    public MemoryManager(){
        pidList = new ArrayList<>();
    }
    public void allocate(int pid, int size) {
        //I'm gonna implement first fit for right now
        boolean allocation = false;
        int startIndex = 0;
        int freeSize = 0;
        if(size > 100 || size <= 0){
            allocation = true;
        }
        for (int i = 0; i < memory.length; i++) {
            if(allocation){
                System.out.println("Invalid size input");
                break;
            }
            /*
            1 1 1 1 1 0 0 0 0 1 1 1 0 0 0 0 0
             */
            if(memory[i] != 0){ //Resets freeSize counter
                freeSize = 0;
            }
            else if(memory[i] == 0){ //Counts available free contiguous space in the memory
                freeSize++;
                if(freeSize == 1){ // I want to capture the start index if the memory is big enough
                    startIndex = i; //Start index grabs the index for starting when we want to allocate
                }
                if(freeSize == size){ //checks if current free size is big enough
                    System.out.println("Process Allocated as " + pid + " at "  + startIndex);
                    for(int j = 0; j<size; j++){
                        memory[startIndex] = pid;
                        startIndex++;
                    }
                    allocation = true;
                    break;
                }
            }
        }
        if(!allocation){
            System.out.println("Allocation is too big or too small");
        }
    }
    public void free (int pid){
        //Should this remove the PCB from the PCB list.
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == pid) {
                memory[i] = 0;
            }
        }
    }
    public void printMemory () { //Prints out memory, every line has 20 memory slots shown
        for (int i = 0; i < memory.length; i++) {
            System.out.print(memory[i] + " ");
            if((i + 1) % 20 == 0){
                System.out.print("\n");
            }
        }
    }
}

