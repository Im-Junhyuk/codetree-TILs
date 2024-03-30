import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int L;
	static int Q;
	
	static int guestNum;
	static int sushiNum;
	
	static Map<String, Guest> guests;
	static PriorityQueue<Sushi> pq;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		guestNum = 0;
		sushiNum = 0;
		
		guests = new HashMap<>();
		pq = new PriorityQueue<>(
				(s1, s2) -> s1.time-s2.time
			
		);
		
		for(int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int inst = Integer.parseInt(st.nextToken());
			int t = Integer.parseInt(st.nextToken());
			
			int x;
			String name;
//			System.out.println(q);
			switch (inst) {
			case 100:
				x = Integer.parseInt(st.nextToken());
				name = st.nextToken();
				putSushi(t, x, name);
				break;
				
			case 200:
				x = Integer.parseInt(st.nextToken());
				name = st.nextToken();
				int n = Integer.parseInt(st.nextToken());
				guestCome(t, x, name, n);
				break;
				
			case 300:
				takePicture(t);
				break;
			}
			
//			System.out.println("--------" + t +  "  t" + guestNum + " " + sushiNum);
		}
	}
	
	static void putSushi(int time, int x, String name) {
		
		if(!guests.containsKey(name))
			guests.put(name, new Guest()); 
		// guest here
		Guest guest = guests.get(name);
		if(guest.here) {
			int t;
			if(x > guest.position)
				t = - x + guest.position + L + time;
			else {
				t = time + guest.position - x;
//				System.out.println("dfdfdf");
			}
			Sushi newSushi = new Sushi();
			newSushi.owner = name;
			newSushi.time = t;
			pq.add(newSushi);
//			System.out.println("   put " + t);
		} else {
			guest.sushis.add(new Sushi(x, time, name));
		}
			
		sushiNum++;
		
		// guest not here
	}
	
	static void guestCome(int time, int x, String name, int n) {
		
		if(guests.containsKey(name)) {
		// sushi first
			Guest g = guests.get(name);
			g.here = true;
			g.alreadyEat = 0;
			g.willEat = n;
			g.position = x;
			
			for(Sushi s : g.sushis) {
				int sushiPosition = (s.x + time - s.time) % L;
				int t;
				if(sushiPosition > x)
					t = - sushiPosition + x + L + time;
				else
					t = time + x - sushiPosition;
				
				s.time = t;
				pq.add(s);
			}
			g.sushis = null;
		}
		// guest first
		else {
			guests.put(name, new Guest(true, n, x));
			
		}
		guestNum++;
	}
	
	static void takePicture(int time) {
		
		while(!pq.isEmpty()) {
			if(pq.peek().time > time)
				break;
			Sushi s = pq.poll();
//			System.out.println("    " + s.x + " time " + s.time);
//			System.out.println("      " + s.owner);
			Guest g = guests.get(s.owner);
			if(g.willEat > 1)
				g.willEat--;
			else {
				guests.remove(s.owner);
				guestNum--;
//				System.out.println("    게스트 떠남 " + s.owner);
			}
			sushiNum--;
		}
		System.out.println(guestNum + " " + sushiNum);
	}
	
	static class Guest{
		boolean here;
		LinkedList<Sushi> sushis;
		int willEat;
		int alreadyEat;
		int position;
		
		Guest(){	// sushi first
			here = false;
			sushis = new LinkedList<>();
		}
		Guest(boolean here, int willEat, int position){ // guest first
			this.position = position;
			this.here = here;
			this.willEat = willEat;
			alreadyEat = 0;
		}
	}
	
	static class Sushi{
		int time;
		String owner;
		int x;
		
		Sushi(){}
		Sushi(int x, int time, String owner){
			this.owner = owner;
			this.x = x;
			this.time = time;
		}
		
	}
}