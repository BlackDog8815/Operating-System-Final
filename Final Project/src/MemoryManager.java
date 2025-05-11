import java.util.AbstractList;
public class MemoryManager {
    int[] memory = new int[100];
    AbstractList<Integer> pidList;
    public void allocate(int pid, int size) {
        //I'm gonna implement first fit for right now
        /*
        Problems to fix
        If user input was of a pid that does not exist, must print error message.
        If user input was of a pid that was already allocated, either error message or not a problem. Update: Fixed
         */
        int startIndex = 0;
        int freeSize = 0;
        for(int i = 0; i<=pidList.size(); i++) {//Checks if pid was already allocated.
            if(pidList.get(i) == pid){
                free(pid);
                pidList.remove(i);
            }
        }
        pidList.add(pid);
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
                System.out.println("Allocation is too big or too small");
            }
        }
        /*
        if(freeSize < size){
            System.out.println("Allocation is to big");
        }

         */
    }
    public void free ( int pid){
        //Should this remove the PCB from the PCB list.
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

