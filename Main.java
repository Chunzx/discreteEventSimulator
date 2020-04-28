import cs2030.simulator.Simulator;
import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

class Main {
    /**
     * Adds arrival events to the PriorityQueue, 
     * as well as additional events that follows a customer's arrival.
     * @param seed Seed to be used in RandomGenerator.
     * @param servers Number of Servers.
     * @param self Number of self-checkout counters.
     * @param limit Maximum queue length.
     * @param count Number of Customers.
     * @param lambda Arrival rate.
     * @param mu Service rate.
     * @param pq PriorityQueue of Events.
     * @param rho Resting rate.
     * @param prob Probability of resting.
     * @param greedyProb Probability of being a greedy Customer.
     */
    static void printQueue(int seed, int servers, int self, int limit, int count, 
        double lambda, double mu, PriorityQueue<Event> pq, double rho, 
        double prob, double greedyProb) {
        Simulator sim = new Simulator(servers,self);
        double[] arr = sim.serve(count, limit, pq, seed, lambda, mu, rho, prob, greedyProb);
        while (pq.size() > 0) {
            System.out.println(pq.poll());
        }
        int customerNumber = count;
        int customersLeft = (int) arr[0];
        int customersServed = customerNumber - customersLeft;
        double totalWaitingTime = arr[1];
        double avgWaitingTime = customersServed == 0 ? 0.000 : totalWaitingTime / customersServed;
        System.out.println(String.format("[%.3f %d %d]",avgWaitingTime, 
                    customersServed, customersLeft));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int seed = sc.nextInt();
        int servers = sc.nextInt();
        int self = sc.nextInt();
        int limit = sc.nextInt();
        int count = sc.nextInt();
        double lambda = sc.nextDouble();
        double mu = sc.nextDouble();
        double rho = sc.nextDouble();
        double probability = sc.nextDouble();
        double greedyProb = sc.nextDouble();
        PriorityQueue<Event> pq = new PriorityQueue<>(1, new EventComparator());
        printQueue(seed, servers, self, limit, count, lambda, mu, pq, rho, probability, greedyProb);
    }
}
