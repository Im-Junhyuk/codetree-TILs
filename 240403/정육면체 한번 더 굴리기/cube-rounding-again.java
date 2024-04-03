import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int N;
	static int M;
	
	static int[][] map;
	static int[][] scoreMap;
	
	static boolean[][] visited;
	static Queue<Node> q;
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	public static void main(String[] args) throws Exception{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N+2][N+2];
		scoreMap = new int[N+2][N+2];
		
		for(int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 1; j <= N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		// pre process
		
		visited = new boolean[N+2][N+2];
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				if(visited[i][j] == false) {
					q = new LinkedList<>();
					dfs(i, j);
					int score = q.size() * map[i][j];
//					System.out.println(q.size());
					while(!q.isEmpty()) {
						Node cur = q.poll();
						scoreMap[cur.x][cur.y] = score;
					}
				}
			}
		}
		
//		for(int i = 0; i < N+2; i++) {
//			for(int j = 0; j < N+2; j++) {
//				System.out.print(scoreMap[i][j] + " ");
//			}System.out.println();
//		}
		// start
		int direction = 0;
		int[] dice = new int[6];
		dice[0] = 6;	//down
		dice[1] = 5;	//behind
		dice[2] = 4;	// left
		dice[3] = 1;	//up
		dice[4] = 2;	//front
		dice[5] = 3;	// right
		
		int x = 1;
		int y = 1;
		
		int totalScore = 0;
		
		for(int m = 0; m < M; m++) {
//			System.out.println(x + " " + y);
			// calculation
			
//			System.out.println(scoreMap[x][y]);
			// next position
			
			

			
			x += dx[direction];
			y += dy[direction];
			
			if(map[x][y] == 0) {
				direction = (direction+2)%4;
				x += 2 * dx[direction];
				y += 2 * dy[direction];
				
			}
			int temp;
			
			switch (direction) {
			case 0: 
				temp = dice[0];
				dice[0] = dice[5];
				dice[5] = dice[3];
				dice[3] = dice[2];
				dice[2] = temp;
				break;
				
			case 1: 
				temp = dice[0];
				dice[0] = dice[4];
				dice[4] = dice[3];
				dice[3] = dice[1];
				dice[1] = temp;
				
				break;
				
			case 2:
				temp = dice[0];
				dice[0] = dice[2];
				dice[2] = dice[3];
				dice[3] = dice[5];
				dice[5] = temp;
				
				break;
			case 3:
				temp = dice[0];
				dice[0] = dice[1];
				dice[1] =dice[3];
				dice[3] = dice[4];
				dice[4] = temp;
				break;
//				dice[0] = 6;	//down
//				dice[1] = 5;	//behind
//				dice[2] = 4;	// left
//				dice[3] = 1;	//up
//				dice[4] = 2;	//front
//				dice[5] = 3;	// right
			
			}
			totalScore += scoreMap[x][y];	
			if(dice[0] > map[x][y]) {
				direction = (direction+1)%4;
				
			} else if(dice[0] < map[x][y]) {
				direction = (direction+3)%4;
			}
		}
		System.out.println(totalScore);
	}
	
	static void dfs(int x, int y) {
		
		visited[x][y] = true;
		
		q.add(new Node(x, y));
		for(int d = 0; d < 4; d++) {
			int nx = x + dx[d];
			int ny = y + dy[d];
			
			if(visited[nx][ny] == false && map[nx][ny] == map[x][y])
				dfs(nx, ny);
		}
	}
	static class Node{
		int x;
		int y;
		Node(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}