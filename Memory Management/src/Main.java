import java.util.Scanner;

public class Main {
    public static void main (String[] args)
    {
        int number_of_processes;
        int number_of_partitions;
        String process_name;
        String partition_name;
        int process_size;
        int partition_size;
        int policy_choose;
        int do_compact;
        int again=1;
        FirstFit ff = null;
        WorstFit wf = null;
        BestFit bf = null;

        Scanner scanner = new Scanner(System.in);
        //enter partitions
        System.out.println("Enter number of partition");
        number_of_partitions = scanner.nextInt();

        Partition[] partitions = new Partition[number_of_partitions];

        for(int i=0 ;i<number_of_partitions ;i++)
        {
            System.out.println("Enter size of partition " + (i));
            partition_size = scanner.nextInt();
            partition_name = "Partition "+(i);
            partitions[i] = new Partition(partition_name,partition_size);
        }
        //enter processes
        System.out.println("Enter number of process");
        number_of_processes = scanner.nextInt();

        Process[] processes = new Process[number_of_processes];

        for(int i=0 ;i<number_of_processes ;i++)
        {
            System.out.println("Enter size of process " + (i+1));
            process_size = scanner.nextInt();
            process_name = "Process "+(i+1);
            processes[i] = new Process(process_name,process_size);
        }
        //choose way to put processes in memory
        while (again==1)
        {
            System.out.println("Select the policy you want to apply:");
            System.out.println("1. First fit");
            System.out.println("2. Worst fit");
            System.out.println("3. Best fit");
            System.out.println("Select policy:");
            policy_choose = scanner.nextInt();
            switch (policy_choose)
            {
                case 1:
                    ff =new FirstFit(processes,partitions);
                    ff.PrintPartitions();
                    break;
                case 2:
                    wf =new WorstFit(processes,partitions);
                    wf.PrintPartitions();
                    break;
                case 3:
                    bf =new BestFit(processes,partitions);
                    bf.PrintPartitions();
                    break;
            }
            System.out.println("Do you want to compact? 1.yes 2.no");
            do_compact = scanner.nextInt();
            if (do_compact == 1)
            {
                switch (policy_choose)
                {
                    case 1:
                        //ff =new FirstFit(processes,partitions);
                        ff.Compact();
                        ff.PrintPartitions();
                        break;
                    case 2:
                        //wf =new WorstFit(processes,partitions);
                        wf.Compact();
                        wf.PrintPartitions();
                        break;
                    case 3:
                        //bf =new BestFit(processes,partitions);
                        bf.Compact();
                        bf.PrintPartitions();
                        break;
                }
            }
            System.out.println("again? 1.yes 2.no");
            again = scanner.nextInt();
        }

    }
}