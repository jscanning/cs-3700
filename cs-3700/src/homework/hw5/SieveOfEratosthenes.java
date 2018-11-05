package homework.hw5;

public class SieveOfEratosthenes {

	public static void main(String[] args) {
		int n = 100000;
		System.out.println("Following are the prime numbers under " + n + ":");
		SieveOfEratosthenes g = new SieveOfEratosthenes();
		long startTime = System.currentTimeMillis();
		g.sieveOfEratosthenes(n);
		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
	}
	
	public void sieveOfEratosthenes(int n){
		boolean prime[] = new boolean[n];
		for (int i = 0; i < prime.length; i++) {
			prime[i] = true;
		}
		
		for(int p =2; p*p < n; p++){
			if(prime[p] == true){
				for(int i = p*2; i < n; i += p){
					prime[i] = false;
				}
			}
		}
		
		for(int i = 2; i < n; i++){
			if(prime[i] == true)
				System.out.print(i + " ");
			if(i % 100 == 0)
				System.out.println();
		}
	}

}
