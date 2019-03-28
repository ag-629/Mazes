class UnionFind{
	
	Cell set;

	
	void makeset(Cell c){
    	c.head = new LLAddOnly();
    	c.head.add(c);
    }

    LLAddOnly find(Cell c){
    	return c.head;
    }

    void union(Cell c1, Cell c2){
    	c1.head.last.next = c2.head.first;
    	c1.head.last = c2.head.last;
    	Cell current = c2.head.first;
    		while(current != null){
    			current.head = c1.head;
    			current = current.next;
    		}
    }
}