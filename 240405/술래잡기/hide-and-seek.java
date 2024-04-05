import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
	
	static int N;
	static int M;
	static int H;
	static int K;
	static int k;

	static Player[] runners;
	static Player sulae;
	
	static Set<Integer>[][] runnerMap;
	static boolean[][] treeMap;
	static boolean[][] sulaeMap;
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1}; 
	
	static boolean forward = true;
	
	static int score = 0;
	
	static class Player{
		int x;
		int y;
		int d;
		boolean alive;
		Player(int x, int y, int d){
			this.x = x;
			this.y = y;
			this.d = d;
			this.alive = true;
		}
		@Override
		public String toString() {
			return "Player [x=" + x + ", y=" + y + ", d=" + d + ", alive=" + alive + "]";
		}
		
		
	}
	
	public static void main(String[] args) throws Exception{
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		H = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		runnerMap = new Set[N][N];
		treeMap = new boolean[N][N];
		sulaeMap = new boolean[N][N];
		
		sulae = new Player(N/2, N/2, 0);
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				runnerMap[i][j] = new HashSet<>();
			}
		}
		
		runners = new Player[M];
		for(int m = 0; m < M; m++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken())-1;
			int y = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken());
			
			runners[m] = new Player(x, y, d);
			runnerMap[x][y].add(m);
		}
		
		for(int h = 0; h < H; h++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken())-1;
			int y = Integer.parseInt(st.nextToken())-1;
			
			treeMap[x][y] = true;
		}
		
		for(k = 1; k <= K; k++) {
			play();
		}
		
		System.out.println(score);
	}
	
	static void play() {
		
		for(int m = 0; m < M; m++) {
			if(runners[m].alive && inRange(m))
				runnerMove(m);
		}
		
		sulaeMove();
		
		kill();
		
		display();
	}
	
	static boolean inRange(int rNum) {
		int dist = Math.abs(sulae.x - runners[rNum].x) + Math.abs(sulae.y - runners[rNum].y);
		if(dist <= 3)
			return true;
		return false;
	}
	
	static void runnerMove(int rNum) {
		Player cur = runners[rNum];
		int x = cur.x;
		int y = cur.y;
		int d = cur.d;
		
		int nx = x + dx[d];
		int ny = y + dy[d];
		
		if(nx < 0 || nx >= N || ny < 0 || ny >= N) {
			d = (d+2)%4;
			nx = x + dx[d];
			ny = y + dy[d];
		}
		
		if(sulae.x == nx && sulae.y == y)
			return;
		
		runnerMap[x][y].remove(rNum);
		runnerMap[nx][ny].add(rNum);
		cur.x = nx;
		cur.y = ny;
	}
	
	static void sulaeMove() {

		int x = sulae.x;
		int y = sulae.y;
		sulaeMap[x][y] = !sulaeMap[x][y];
		
		if(sulae.x == N/2 && sulae.y == N/2) {
			sulae.d = 0;
			forward = true;
		}
		
		if(sulae.x == 0 && sulae.y == 0) {
			sulae.d = 2;
			forward = false;
		}
		
		sulae.x = sulae.x + dx[sulae.d];
		sulae.y = sulae.y + dy[sulae.d];
		
		if(forward) {
			
			int d = (sulae.d+1)%4;
			if(sulaeMap[sulae.x+dx[d]][sulae.y+dy[d]] == false)
				sulae.d = (sulae.d+1)%4;
		} 
		
		if(forward == false) {
			
			int nx = sulae.x + dx[sulae.d];
			int ny = sulae.y + dy[sulae.d];
			
			if(nx < 0 || nx >= N || ny < 0 || ny >= N)
				sulae.d = (sulae.d+3)%4;
		}
		
		sulaeMap[sulae.x][sulae.y] = !sulaeMap[sulae.x][sulae.y];
	}
	
	static void kill() {
		
		int killCnt = 0;

		for(int dist = 1; dist <=3; dist++) {
			int nx = sulae.x + dx[sulae.d] * dist;
			int ny = sulae.y + dy[sulae.d] * dist;
			
			if(nx < 0 || nx >= N || ny < 0 || ny >= N)
				continue;
			
			if(treeMap[nx][ny])
				continue;
			
			killCnt += runnerMap[nx][ny].size();
			for(int i : runnerMap[nx][ny]) {
				runners[i].alive = false;
//				killCnt++;
//				System.out.println("kilol");
//				System.out.println(runnerMap[nx][ny].size());
			}
			runnerMap[nx][ny] = new HashSet<>();
		}
		
		score += k * killCnt;
//		System.out.println("score "  + score + " k " + k + " cnt " +  killCnt);
	}
	
	static void display() {
//		System.out.println("turn " + k);
//		System.out.println("sulae " + sulae.x + " " + sulae.y);
//		for(int m = 0; m < M; m++) {
//			System.out.println(runners[m].toString());
//		}
//		System.out.println("score " + score);
	}
}