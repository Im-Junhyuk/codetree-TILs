import java.io.*;
import java.util.*;

public class Main {
	
	static int N;
	static int M;
	
	static boolean[][] map;
	static int[][] camp;

	static Person[] people;
	static int remain;
	
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new boolean[N][N];
		camp = new int[N][N];
		
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				camp[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		people = new Person[M+1];
		for(int m = 1; m < M+1; m++) {
			st = new StringTokenizer(br.readLine());
			people[m] = new Person(Integer.parseInt(st.nextToken())-1,
					Integer.parseInt(st.nextToken())-1);
		}
		
		remain = M;
		int terminateTime = 0;
		int timeLimit = Integer.MAX_VALUE;
		for(int t = 1; t < timeLimit; t++) {
			
//			System.out.println("=========t " + t+ "======");
			Queue<Point> blockQueue = new LinkedList<>();
			for(int p = 1; p < M+1; p++) {
				if(people[p].playable) {
				// move
					Queue<Node> q = new LinkedList<>();
					q.add(new Node(people[p].x, people[p].y, null));
					boolean[][] visited = new boolean[N][N];
					Point move = null;
					while(!q.isEmpty()) {
						Node cur = q.poll();
						int x = cur.x;
						int y = cur.y;
						
						if(x == people[p].destx && y == people[p].desty) {
							move = cur.sp;
							break;
						}
						if(visited[x][y])
							continue;
						visited[x][y] = true;
						
						for(int d = 0; d < 4; d++) {
							int nx = x + dx[d];
							int ny = y + dy[d];
							
							if(nx < 0 || nx >= N || ny < 0 || ny >= N
									|| visited[nx][ny] || map[nx][ny])
								continue;
							
							Node newNode = new Node(nx, ny, cur.sp);
							if(newNode.sp == null) 
								newNode.sp = new Point(nx, ny);
							
							q.add(newNode);
						}
					}
					
					
					// if arrive
					people[p].x = move.x;
					people[p].y = move.y;
					
					if(people[p].x == people[p].destx && people[p].y == people[p].desty) {
						people[p].playable = false;
//						System.out.println("$$$$$escape");
						map[people[p].x][people[p].y] = true;
						blockQueue.add(new Point(people[p].x, people[p].y));
						remain--;
					}
				}
			}
			// block
			while(!blockQueue.isEmpty()) {
				Point cur = blockQueue.poll();
				
				map[cur.x][cur.y] = true;
			}
			
			
			// start to move
			if(t <= M) {
				// decide camp
				PriorityQueue<Point> pq = new PriorityQueue<>();
				Queue<Point> q = new LinkedList<>();
				q.add(new Point(people[t].destx, people[t].desty, 0));
				boolean[][] visited = new boolean[N][N];
				int minDist = Integer.MAX_VALUE;

				while(!q.isEmpty()) {
					Point cur = q.poll();
					int x = cur.x;
					int y = cur.y;
					int dist = cur.dist;
					
					if(minDist < dist) {
						break;
					}
					
					if(camp[x][y] == 1) {
						minDist = dist;
						pq.add(new Point(x, y));
					}
					
					for(int d = 0; d < 4; d++) {
						int nx = x + dx[d];
						int ny = y + dy[d];
						
						if(nx < 0 || nx >= N || ny < 0 || ny >= N
								|| visited[nx][ny] || map[nx][ny])
							continue;
						
						q.add(new Point(nx, ny, dist+1));
					}
				}
				
				// start, block
				Point sp = pq.poll();
				people[t].playable = true;
				people[t].x = sp.x;
				people[t].y = sp.y;
				map[sp.x][sp.y] = true;
				camp[sp.x][sp.y] = 0;
			}
			
			display();
			
			if(remain == 0) {
				terminateTime = t;
				break;
			}
		}
		
		System.out.println(terminateTime);
	}
	
	static class Person{
		int x;
		int y;
		int destx;
		int desty;
		boolean playable;
		Person(int destx, int desty){
			this.destx = destx;
			this.desty = desty;
			this.playable = false;
		}
	}
	
	static class Point implements Comparable<Point>{
		int x;
		int y;
		int dist;
		Point(int x, int y){
			this.x = x;
			this.y = y;
		}
		Point(int x, int y, int dist){
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
		@Override
		public int compareTo(Point o) {
			if(this.x != o.x)
				return this.x = o.x;
			return this.y = o.y;
		}
	}
	
	static class Node{
		int x;
		int y;
		Point sp;
		Node(int x, int y, Point sp){
			this.x = x;
			this.y = y;
			this.sp = sp;
		}
		
	}
	
	static void display() {
//		System.out.println();
//		System.out.println("display");
//		for(int p = 1; p < M+1; p++) {
//			Person per = people[p];
//			System.out.println(per.x + " " + per.y);
//		}
//		
//		System.out.println();
	}
}