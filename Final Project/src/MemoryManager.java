import java.util.Arrays;

public class MemoryManager {
    int[] memory = new int[100];
    public final int counter = 0;

    public void allocate(int pid, int size) {
        //I'm gonna implement first fit for right now
        int startIndex = 0;
        int freeSize = 0;

        for (int i = 0; i < memory.length; i++) {
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
                    for(int j = 0; j<size; j++){
                        memory[startIndex] = pid;
                        startIndex++;
                    }
                }
            }
            else{
                System.out.println("Allocation is to big");
            }
        }
        /*
        if(freeSize < size){
            System.out.println("Allocation is to big");
        }

         */
    }
    public void free ( int pid){
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == pid) {
                memory[i] = 0;
            }
        }
    }
    public void printMemory () { //Prints out memory, every line has 20 memory slots shown
        for (int i = 0; i < memory.length; i++) {
            System.out.println(memory[i] + " ");
            if(i % 20 == 0){
                System.out.println("\n");
            }
        }
    }
}

