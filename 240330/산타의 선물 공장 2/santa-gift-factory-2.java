import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    
	static int N;
	static int M;
	static int q;
	
	static LinkedList[] belts;
	static int[] pre;
	static int[] post;
	
	static class Node{
		int present;
		Node next;
		Node(int num){
			this.present = num;
			next = null;
		}
		Node(){
			this.present = 0;
			next = null;
		}
	}
	
	static class LinkedList{
		Node head;
		Node tail;
		int size;
		
		public LinkedList() {
			head = new Node();
			tail = new Node();
			head.next = tail;
			tail.next = head;
			size = 0;
		}
		
		void addFirst(int num) {
			Node newNode = new Node(num);
			if(size > 0) {
				int firVal = head.next.present;
				pre[firVal] = num;
				post[num] = firVal;
			}
			newNode.next = head.next;
			head.next = newNode;
			if(size == 0) {
				tail.next = newNode;
			}

			size++;
		}
		
		void addLast(int num){
			
			Node newNode = new Node(num);

//			newNode.next = tail;
			if(size > 0) {
				post[tail.next.present] = num;
				pre[num] = tail.next.present;
			}
			tail.next.next = newNode;
			tail.next = newNode;
			if(size == 0) {
				head.next = newNode;
			}
			size++;
			
		}
		
		int pollFist() {
//			System.out.println("dddsize " + size);
			int firValue = head.next.present;
			post[firValue] = -1;
			head.next = head.next.next;
			
			if(size > 1) {
				int secValue = head.next.present;
				pre[secValue] = -1;
			}
			
			if(size == 1) {
				tail.next = head;
			}
			
			size--;
			return firValue;
		}
		
		int peekFirst() {
			return head.next.present;
		}
		
		int peekLast() {
			return tail.next.present;
		}
		
		void print() {
			Node cur = head.next;
			System.out.println("size " + size);
			for(int s = 0; s < size; s++) {
				System.out.print(cur.present + " ");
				cur = cur.next;
			}
			System.out.println();
		}
	}
	
    public static void main(String[] args) throws IOException {
 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;
        int Q = Integer.parseInt(br.readLine());
        
        for(q = 0; q < Q; q++) {
        	st = new StringTokenizer(br.readLine());
        	int inst = Integer.parseInt(st.nextToken());
        	int src;
        	int dst;
        	int p_num;
        	int b_num;
//        	System.out.println("inst " + inst);
        	switch (inst) {
			case 100: 
				init(st);
				
				break;
			case 200: 
				src = Integer.parseInt(st.nextToken());
				dst = Integer.parseInt(st.nextToken());
//				System.out.println("src dst " + src + " " + dst);
				
				moveAll(src, dst);
				
				break;
			case 300: 
				src = Integer.parseInt(st.nextToken());
				dst = Integer.parseInt(st.nextToken());
//				System.out.println("src dst " + src + " " + dst);
//				belts[src].print();
//				belts[dst].print();
				moveFirst(src, dst);
				
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
    	
    	belts = new LinkedList[N+1];
    	for(int n = 1; n < N+1; n++) {
    		belts[n] = new LinkedList();
    	}
    	
    	pre = new int[M+1];
    	post = new int[M+1];
    	
    	for(int m = 1; m < M+1; m++) {
    		int bNum = Integer.parseInt(st.nextToken());
//    		System.out.println(" input " + bNum);
    		
    		if(belts[bNum].size == 0) {
    			pre[m] = -1;
    			belts[bNum].addLast(m);
    		} else {
    			int myPre = belts[bNum].tail.next.present;
    			pre[m] = myPre;
    			post[myPre] = m;
    			belts[bNum].addLast(m);
    		}
    		
    		post[m] = -1;
    	}
    }
    
    static void moveAll(int src, int dst) {
    	if(belts[src].size != 0) {
//    		if(belts[dst].size == 0) {
//    			
//    		}
//    		
    		if(belts[dst].size != 0) {
	    		int dstFir = belts[dst].head.next.present;
	    		int srcLat = belts[src].tail.next.present;
	    		
	    		pre[dstFir] = srcLat;
	    		post[srcLat] = dstFir;
    		}
    		
    		belts[dst].head.next = belts[src].head.next;
    		if(belts[dst].size == 0) {
    			belts[dst].tail.next = belts[src].tail.next;
    		} else {
    			
    		}
    		
    		belts[src].tail.next.next = belts[dst].head.next;
    		
    		
    		belts[src].head.next = belts[src].tail;
    		belts[src].tail.next = belts[dst].head;
    		
    		belts[dst].size += belts[src].size;
    		belts[src].size = 0;
    	}
    	
    	System.out.println(belts[dst].size);
    }
    
    static void moveFirst(int src, int dst) {
    	
    	if(belts[src].size > 0 && belts[dst].size > 0) {
    		int srcFir = belts[src].pollFist();
    		int dstFir = belts[dst].pollFist();
    		
    		belts[src].addFirst(dstFir);
    		belts[dst].addFirst(srcFir);
    		
    	} else if(belts[src].size == 0 && belts[dst].size > 0){
    		belts[src].addFirst(belts[dst].pollFist());	
    		
    	} else if(belts[src].size > 0 && belts[dst].size == 0) {
    		belts[dst].addFirst(belts[src].pollFist());	
    	}
    	System.out.println(belts[dst].size);
    }
    
    static void divide(int src, int dst) {
    	int num = belts[src].size/2;
    	for(int n = 0; n < num; n++) {
    		belts[dst].addFirst(belts[src].pollFist());
    	}
    	System.out.println(belts[dst].size);
    }
    
    static void getPresentInfo(int p) {
    	int result = pre[p] + 2 * post[p];
    	System.out.println(result);
    }
    
    static void getBeltInfo(int belt) {
    	int a;
    	int b;
    	int c;
    	if(belts[belt].size == 0) {
    		a = -1;
    		b = -1;
    	} else {
    		a = belts[belt].peekFirst();
    		b = belts[belt].peekLast();
    	}
    	
    	c = belts[belt].size;
    	int result = a + 2 * b + 3 * c;
    	System.out.println(result);
    }
    
    static void display() {
    	
//    	System.out.println("turn " + q);
//    	
//    	for(int n = 1; n < N+1; n++) {
//    		System.out.println(n+"list");
//    		belts[n].print();
//    	}
//    	
//    	System.out.println("pre");
//    	for(int m = 1; m < M+1; m++) {
//    		System.out.print(pre[m] + " ");
//    	}
//    	System.out.println();
//    	System.out.println("post");
//    	for(int m = 1; m < M+1; m++) {
//    		System.out.print(post[m] + " ");
//    	}
//    	System.out.println();
//    	System.out.println();
    }
}