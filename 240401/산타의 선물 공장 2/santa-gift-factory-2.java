import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {

	static int Q;
	static int N;
	static int M;
	
	static int[] pre;
	static int[] post;
	
	static LinkedList[] belts;
	
	static class LinkedList{
		Node head;
		Node tail;
		int size;
		public LinkedList() {
			head = new Node(0);
			tail = new Node(0);
			
			this.size = 0;
		}
		
		void addFirst(int num) {
			Node newNode = new Node(num);
			
			if(size == 0) {
				head.next = newNode;
				tail.next = newNode;
			}
			
			if(size > 0) {
				int sec = this.head.next.num;
				pre[sec] = num;
				post[num] = sec;
				newNode.next = head.next;
				head.next = newNode;
			}
			
			size++;
		}
		void addLast(int num) {
			
			Node newNode = new Node(num);
			
			if(this.size == 0) {
				pre[num] = -1;
				head.next = newNode;
			}
			
			if(this.size != 0) {
				pre[num] = tail.next.num;
				post[tail.next.num] = num;
				tail.next.next = newNode;
			}
			
			tail.next = newNode;
			post[newNode.num] = -1;
			size++;
		}
		
		void shiftAll(int dst) {
			if(size == 0)
				return;
			
			if(belts[dst].size == 0) {
				belts[dst].head.next = this.head.next;
				belts[dst].tail.next = this.tail.next;
			}
				
			if(belts[dst].size != 0) {
				this.tail.next.next = belts[dst].head.next;
				post[this.tail.next.num] = belts[dst].peekFirst();
				pre[belts[dst].head.next.num] = this.tail.next.num;
				belts[dst].head.next = this.head.next;
				
			}
			
			this.head.next = null;
			this.tail.next = null;
			
			belts[dst].size += size;
			size = 0;
		}
		
		int peekFirst() {
			if(size == 0)
				return 0;
			
			return head.next.num;
		}
		
		int peekLast() {
			if(size == 0)
				return 0;
			
			return tail.next.num;
		}
		
		int pollFirst() {
			
			if(size == 0) {
				return 0;
			}
			
			int num = this.head.next.num;
			if(size == 1) {
				this.head.next = null;
				this.tail.next = null;
			}
			
			if(size > 1) {
				post[num] = -1;
				this.head.next = this.head.next.next;
				int sec = this.head.next.num;
				pre[sec] = -1;
			}
			
			size--;
			return num;
		}
		void print() {
			Node cur = head.next;
			
			for(int i = 0; i < size; i++) {
				System.out.print(cur.num + " ");
				cur = cur.next;
			}
			
			System.out.println();
		}
	}
	
	static class Node{
		int num;
		Node next;
		Node(int num){
			this.num = num;
			this.next = null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		
		Q = Integer.parseInt(br.readLine());
		
		for(int q = 1; q <= Q; q++) {
			
			st = new StringTokenizer(br.readLine());
			int inst = Integer.parseInt(st.nextToken());
			
			int src;
			int dst;
			
			int p_num;
			int b_num;
//			System.out.println("====================");
//			System.out.println("turn " + q);
//			
			switch (inst) {
			case 100:
				
				init(st);
				break;
				
			case 200:
				src = Integer.parseInt(st.nextToken());
				dst = Integer.parseInt(st.nextToken());
				
				shiftAll(src, dst);
				break;
			case 300:
				src = Integer.parseInt(st.nextToken());
				dst = Integer.parseInt(st.nextToken());
				
				changeFirst(src, dst);
				break;
			case 400:
				src = Integer.parseInt(st.nextToken());
				dst = Integer.parseInt(st.nextToken());
				
				divide(src, dst);
				break;
			case 500:
				p_num = Integer.parseInt(st.nextToken());
				getPresentInfo(p_num);
				break;
			case 600:
				b_num = Integer.parseInt(st.nextToken());
				getBeltInfo(b_num);
				break;

				
			}
			display();
			
		}

	}
	static void init(StringTokenizer st) {
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		pre = new int[M+1];
		post = new int[M+1];
		
		belts = new LinkedList[N+1];
		for(int i = 1; i <= N; i++) {
			belts[i] = new LinkedList();
		}
		
		for(int i = 1; i <= M; i++) {
			belts[Integer.parseInt(st.nextToken())].addLast(i);
		}
		
	}

	static void shiftAll(int src, int dst) {
		belts[src].shiftAll(dst);
		System.out.println(belts[dst].size);
	}
	static void getPresentInfo(int p_num) {
		int a = pre[p_num];
		int b = post[p_num];
		
		int result = a + 2 * b;
		System.out.println(result);
	}
	
	static void changeFirst(int src, int dst) {
		if(belts[src].size == 0 && belts[dst].size == 0) {
			System.out.println(belts[dst].size);
			return;
		}
		
		if(belts[src].size == 0) {
			belts[src].addFirst(belts[dst].pollFirst());
			System.out.println(belts[dst].size);
			return;
		}
		
		if(belts[dst].size == 0) {
			belts[dst].addFirst(belts[src].pollFirst());
			System.out.println(belts[dst].size);
			return;
		}
		
		int srcFir = belts[src].pollFirst();
		int dstFir = belts[dst].pollFirst();
		
		belts[src].addFirst(dstFir);
		belts[dst].addFirst(srcFir);
		
		System.out.println(belts[dst].size);
		
	}
	
	static void divide(int src, int dst) {
		
		int num = belts[src].size/2;
		Stack<Integer> stack = new Stack<>();
		
		for(int i = 0; i < num; i++) {
			stack.add(belts[src].pollFirst());
		}
		
		while(!stack.isEmpty()) {
			belts[dst].addFirst(stack.pop());
		}
		
		System.out.println(belts[dst].size);
	}
	static void getBeltInfo(int b_num) {
		
		int a = -1;
		int b = -1;
		int c = belts[b_num].size;

		
		if(belts[b_num].size != 0) {
			a = belts[b_num].peekFirst();
			b = belts[b_num].peekLast();
		}
		
		int result = a + 2 * b + 3 * c;
		
		System.out.println(result);
	}
	static void display() {
//		System.out.println("----------------");
//		for(int i = 1; i <= N; i++) {
//			System.out.println("belt " + i + " size " + belts[i].size);
//			belts[i].print();
//		} System.out.println();
//		
//		System.out.println("pre");
//		for(int i = 1; i <= M; i++) {
//			System.out.print(pre[i] + " ");
//		}System.out.println();
//		System.out.println("post");
//		for(int i = 1; i <= M; i++) {
//			System.out.print(post[i] + " ");
//		}System.out.println();
//		System.out.println("--------------");
	}
}