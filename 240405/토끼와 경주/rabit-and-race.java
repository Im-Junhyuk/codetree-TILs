import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

public class Main{

	static int Q;
	static int N;
	static int M;
	static int P;
	
	static int race;
	
	static long totalScore;
	static Map<Integer, Rabbit> rMap;
	static PriorityQueue<Rabbit> pq;
	static Rabbit[] rArr;
	
	static long initTime;
	static long raceTime;
	static long changeTime;
	static long bestTime;
	static long pqTime;
	static long runTime;
	
	static class Rabbit implements Comparable<Rabbit>{
		int x; 
		int y;
		int id;
		int jump;
		long minusScore;
		int dist;
		int race;
		Rabbit(int id, int dist){
			this.id = id;
			this.race = 0;
			this.dist = dist;
			this.x = 0;
			this.y = 0;
			this.minusScore = 0L;
			this.jump = 0;
		}
		
		@Override
		public int compareTo(Rabbit o) {
			
			if(this.jump != o.jump)
				return this.jump - o.jump;
			
			if((this.x + this.y) != (o.x + o.y))
				return (this.x + this.y)- (o.x+o.y);
			
			if(this.x != o.x)
				return this.x - o.x;
			
			if(this.y != o.y)
				return this.y - o.y;
			
			return this.id - o.id;
		}
	}
	
	static Rabbit[][] map;
	
	public static void main(String[] args) throws Exception{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Q = Integer.parseInt(br.readLine());
		StringTokenizer st = null;
		
				
		initTime = 0L;
		changeTime = 0L;
		raceTime = 0L;
		bestTime = 0L;
		runTime = 0L;
		pqTime = 0L;
		
		long time;
		
		for(int q = 1; q <= Q; q++) {
			
			st = new StringTokenizer(br.readLine());
			int inst = Integer.parseInt(st.nextToken());
//			System.out.println("q " + q);
			switch (inst) {
			case 100:
//				System.out.println("init");
				time = System.currentTimeMillis();
				init(st);
				initTime += System.currentTimeMillis() - time;
				break;
			case 200:
				time = System.currentTimeMillis();
//				System.out.println("race");
				int K = Integer.parseInt(st.nextToken());
				int S = Integer.parseInt(st.nextToken());
				race(K, S);
				raceTime += System.currentTimeMillis() - time;
				break;
				
			case 300:
//				System.out.println("change");
				time = System.currentTimeMillis();
				int id = Integer.parseInt(st.nextToken());
				int L = Integer.parseInt(st.nextToken());
				changeDist(id, L);
				changeTime += System.currentTimeMillis() - time;
				break;
				
			case 400:
				time = System.currentTimeMillis();
//				System.out.println("best");
				bestRabbit();
				bestTime += System.currentTimeMillis() - time;
				break;
			}
		}
//		System.out.println("inittime " + initTime);
//		System.out.println("racetime " + raceTime);
//		System.out.println("change " + changeTime);
//		System.out.println("best " + bestTime);
//		System.out.println("pq " + (raceTime - runTime));
//		System.out.println("runtime " + runTime);
	}
	
	static void init(StringTokenizer st) {
		

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		
		rMap = new HashMap<>();
//		map = new Rabbit[N][M];
		pq = new PriorityQueue<>();
		rArr = new Rabbit[P];
		
		race = 0;
		
		for(int p = 0; p < P; p++) {
			int id = Integer.parseInt(st.nextToken());
			int dist = Integer.parseInt(st.nextToken());
			Rabbit r = new Rabbit(id, dist);
			rMap.put(id, r);
			rArr[p] = r;
		}
	}
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	static class Space implements Comparable<Space>{
		int x;
		int y;
		int sum;
		Space(int x, int y){
			this.x = x;
			this.y = y;
			this.sum = x + y;
		}
		@Override
		public int compareTo(Space o) {
			if(this.sum != o.sum)
				return o.sum - this.sum;
			
			if(this.x != o.x)
				return o.x - this.x;
			
			return o.y - this.y;
		}
	}
	
	static void race(int K, int S){
//		System.out.println("rece=============");
		long time;
		race++;
//		PriorityQueue<Rabbit> pq = new PriorityQueue<>();
		PriorityQueue<Rabbit> candidate = new PriorityQueue<>((r1, r2)
				->  {
			if( (r1.x + r1.y) != ( r2.x + r2.y))
				return (r2.x+r2.y) - (r1.x+r1.y);
			if(r1.x != r2.x)
				return r2.x-r1.x;
			if(r1.y != r2.y)
				return r2.y-r1.y;
			return r2.id - r1.id; 
			});
		
		Set<Integer> candidateSet = new HashSet<>();
		
		for(int k = 0; k < K; k++) {
//			System.out.println("k " + k);
			display();
			if(pq.isEmpty())
				for(int p = 0; p < P; p++) {
					pq.add(rArr[p]);
				}
			
			Rabbit cur = pq.poll();
			cur.jump++;
//			System.out.println("play   " + cur.id);
			PriorityQueue<Space> spaces = new PriorityQueue<>();
			
			time = System.currentTimeMillis();
			for(int direction = 0; direction < 4; direction++) {
				int dist = cur.dist;
				int x = cur.x;
				int y = cur.y;
				int d = direction;
				
				if(d == 0) {	// right
					dist = y + dist;
					y = 0;
					dist = dist % (2 * (M-1));
					if(dist <= M -1)
						y = dist;
					else
						y = 2 * (M-1) - dist;
				}
				
				if(d == 1) {	// down
					dist = x + dist;
					
					dist = dist % (2 * (N-1));
					if(dist <= N -1)
						x = dist;
					else
						x = 2 * (N-1) - dist;
				}
				
				if(d == 2) {	// left
					dist = M-1 -y + dist;
					y = 0;
					dist = dist % (2 * (M-1));
					if(dist <= M -1)
						y = M-1 - dist;
					else
						y = dist - (M-1);
				}

				if(d == 3) {	// up
					dist = N-1 -x + dist;
					
					dist = dist % (2 * (N-1));
					if(dist <= N -1)
						x = N-1 -dist;
					else
						x = dist - (N-1);
				}
				
//				System.out.println(d + " " + x + " " + y);
//				if(d % 2 == 0) {
//					dist = dist % (2 * (M-1));
//					while(dist > 0) {
//						int ny = y + dy[d];
//						if(ny < 0 || ny >= M) {
//							d = (d+2) % 4;
//						}
//						y = y + dy[d];
//						dist--;
//					}
//					
//				} else {
//					dist = dist % (2 * (N-1));
//					while(dist > 0) {
//						int nx = x + dx[d];
//						if(nx < 0 || nx >= N) {
//							d = (d+2) % 4;
//						}
//						x = x + dx[d];
//						dist--;
//					}
//					
//				}
//				System.out.println("space q");
				spaces.add(new Space(x, y));
			}
			
			runTime += System.currentTimeMillis() - time;
			Space result = spaces.poll();
			cur.x = result.x;
			cur.y = result.y;
			cur.minusScore -= (cur.x + cur.y + 2);
			totalScore += cur.x + cur.y + 2;
//			pq.add(cur);
			candidateSet.add(cur.id);
			display();
			
			//=========
//			System.out.println(result.x + " " + result.y);
//			while(!spaces.isEmpty()) {
//				Space s = spaces.poll();
//				System.out.println(s.x + " " + s.y);
//			}
		}
		

		// score add
		for(int i : candidateSet) {
			candidate.add(rMap.get(i));
		}
		Rabbit reward = candidate.poll();
		reward.minusScore += S;
//		Rabbit r = new Rabbit(-1, -1);
//		r.x = -1;
//		r.y = -1;
//		
//		for(int p = 0; p < P; p++) {
//			Rabbit cur = rArr[p];
//			if((cur.x+cur.y) > (r.x + r.y)) {
//				r = cur;
//			}
//			if( (cur.x+cur.y)== (r.x+r.y) ) {
//				if(cur.x > r.x)
//					r = cur;
//				if(cur.x == r.x) {
//					if(cur.y > r.y)
//						r = cur;
//					if(cur.y == r.y) {
//						if(cur.id > r.id)
//							r = cur;
//					}
//				}
//					
//			}
//		}
////		System.out.println("reward " + S);
//		r.minusScore += S;
	}
	
	static void changeDist(int id, int L) {
		Rabbit r = rMap.get(id);
		r.dist *= L;
//		rMap.replace(id, r);
	}
	
	static void bestRabbit() {
		
		long maxScore = Integer.MIN_VALUE;
		for(int p = 0; p < P; p++) {
//			System.out.print(rArr[p].minusScore + " ");
			maxScore = Math.max(maxScore, rArr[p].minusScore);
		}
//		System.out.println(totalScore);
		System.out.println(totalScore+maxScore);
	}
	
	static long getScore(Rabbit r) {
		return r.minusScore + totalScore;
	}
	
	static void display() {
//		System.out.println("========score======");
//		for(int p = 0; p < P; p++) {
//			System.out.print(rArr[p].id + " x " + rArr[p].x + " y " + rArr[p].y + " d " + rArr[p].dist);
//			System.out.println(" s " +getScore(rArr[p]) + " ");
//		}
//		System.out.println();
	}
}