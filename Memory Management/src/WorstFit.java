import java.util.LinkedList;
import java.util.Vector;

public class WorstFit {
    //array of process
    private final Process[] processes;
    //vector of out process that can not put in memory
    private Vector<Process> out_processes = new Vector<Process>();
    //linked list for print and compaction
    private LinkedList<Partition> partitions_for_print = new LinkedList<>();
    private int next_partition;
    private int c=0;
    //the constructor that take the original array of process and the  original array of partitions and copy them in their local arrays and call the function that is put process in memory
    WorstFit(Process[] proc,Partition[] part)
    {
        next_partition = part.length;
        processes = new Process[proc.length];
        for (int i = 0;i <proc.length;i++)
        {
            processes[i] = new Process(proc[i].name,proc[i].size);
        }
        for (int i = 0;i <part.length;i++)
        {
            // add all partitions into the linked list
            partitions_for_print.add(new Partition(part[i].name,part[i].size));
        }
        PutProcessInMemory();
    }
    private void PutProcessInMemory()
    {
        for(int i=0;i<=processes.length-1;i++)
        {
            //find the best place for the process
            int place = FindWorst(processes[i].size);
            if(place >=0)
            {
                int old_size = partitions_for_print.get(place).size;
                //put the process in the best partition
                partitions_for_print.get(place).process_name=processes[i].name;
                //check if the partition size is greater than the process size
                if((partitions_for_print.get(place).size-processes[i].size)!=0){
                    //make the size of the partition is the same size of the process
                    partitions_for_print.get(place).size=processes[i].size;
                    //add the remainder of the partition size and the process size in new partition next to the best partition
                    partitions_for_print.add((place+1),new Partition("Partition "+(next_partition+c),old_size-processes[i].size));
                    c++;
                }
                //if the partition size  and the process size is equal
                else {
                    partitions_for_print.get(place).size=processes[i].size;
                }
            } else {
                out_processes.add(processes[i]);
            }
        }
    }
    private void PutOutProcessInMemory()
    {
        for(int i=0;i<=out_processes.size()-1;i++)
        {
            //find the best place for the process
            int place = FindWorst(out_processes.get(i).size);
            if(place >=0)
            {
                int old_size = partitions_for_print.get(place).size;
                //put the process in the best partition
                partitions_for_print.get(place).process_name=out_processes.get(i).name;
                //check if the partition size is greater than the process size
                if((partitions_for_print.get(place).size-out_processes.get(i).size)!=0){
                    //make the size of the partition is the same size of the process
                    partitions_for_print.get(place).size=out_processes.get(i).size;
                    //add the remainder of the partition size and the process size in new partition next to the best partition
                    partitions_for_print.add((place+1),new Partition("Partition "+(next_partition+c),old_size-out_processes.get(i).size));
                    c++;
                }
                //if the partition size  and the process size is equal
                else {
                    partitions_for_print.get(place).size=out_processes.get(i).size;
                }
            }
            out_processes.remove(i);
        }
    }
    public void PrintPartitions()
    {
        for(int i=0;i< partitions_for_print.size();i++){
            System.out.println(" "+partitions_for_print.get(i).name+"("+partitions_for_print.get(i).size+"KB)"+" => "+partitions_for_print.get(i).process_name);
        }
        for(int i=0;i< out_processes.size();i++){
            System.out.println("\n "+out_processes.get(i).name+" can not be allocated");
        }
    }
    private int FindWorst (int s)
    {
        int max_size = -1;
        int place = -1;
        for(int i=0;i<partitions_for_print.size();i++)
        {
            if(max_size != -1)
            {
                if(partitions_for_print.get(i).size>=s && partitions_for_print.get(i).size>max_size && partitions_for_print.get(i).process_name == "External fragment")
                {
                    place = i;
                    max_size = partitions_for_print.get(i).size;
                }
            }else
            {
                if(partitions_for_print.get(i).size>=s && partitions_for_print.get(i).process_name == "External fragment")
                {
                    place = i;
                    max_size = partitions_for_print.get(i).size;
                }
            }
        }
        return place;
    }
    public void Compact ()
    {
        int total_frg = 0;
        LinkedList<Partition> new_memory = new LinkedList<>();
        while (!partitions_for_print.isEmpty())
        {
            Partition temp = partitions_for_print.remove();
            if(temp.process_name == "External fragment")
            {
                total_frg += temp.size;
            } else {
                new_memory.add(temp);
            }
        }
        new_memory.add(new Partition("Partition "+(next_partition+c),total_frg));
        c++;
        partitions_for_print = new_memory;
        PutOutProcessInMemory();
    }
}
