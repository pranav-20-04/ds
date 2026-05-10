#include <stdio.h>
#include <mpi.h>

int main(int argc, char *argv[])
{
    int rank, size;
    int num[20];

    MPI_Init(&argc, &argv);

    // Get process rank
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    // Get total number of processes
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Initialize array
    for (int i = 0; i < 20; i++)
    {
        num[i] = i + 1;
    }

    // Number of elements per process
    int chunk = 20 / size;

    // Master Process
    if (rank == 0)
    {
        int sum = 0;
        int local_sum = 0;
        int s[10]; // store local sums from workers

        printf("Distribution at rank %d\n", rank);

        // Send chunks to worker processes
        for (int i = 1; i < size; i++)
        {
            MPI_Send(&num[i * chunk], chunk, MPI_INT,
                     i, 1, MPI_COMM_WORLD);
        }

        // Local sum for master
        for (int i = 0; i < chunk; i++)
        {
            local_sum += num[i];
        }

        printf("Local sum at rank %d is %d\n",
               rank, local_sum);

        sum = local_sum;

        // Receive sums from workers
        for (int i = 1; i < size; i++)
        {
            MPI_Recv(&s[i], 1, MPI_INT,
                     i, 1, MPI_COMM_WORLD,
                     MPI_STATUS_IGNORE);

            sum += s[i];
        }

        printf("Final Sum = %d\n", sum);
    }

    // Worker Processes
    else
    {
        int k[20];
        int local_sum = 0;

        // Receive chunk from master
        MPI_Recv(k, chunk, MPI_INT,
                 0, 1, MPI_COMM_WORLD,
                 MPI_STATUS_IGNORE);

        // Calculate local sum
        for (int i = 0; i < chunk; i++)
        {
            local_sum += k[i];
        }

        printf("Local sum at rank %d is %d\n",
               rank, local_sum);

        // Send local sum back to master
        MPI_Send(&local_sum, 1, MPI_INT,
                 0, 1, MPI_COMM_WORLD);
    }

    MPI_Finalize();

    return 0;
}


//mpicc A3.c
// mpirun --oversubscribe -np 4 ./a.out
