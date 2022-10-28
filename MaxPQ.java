import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * name: Daniel Gil
 * class: csc210
 * purpose: to create a maxpriorityqueue ADT that is
 * implemented using a max heap. so the element
 * with the highest priority is stored at the front
 * of the queue. giving us extremely fast dequeue operations.
 */
public class MaxPQ {
	private MyBinaryMaxHeap priorityQueue;
	
	public MaxPQ() {
		priorityQueue = new MyBinaryMaxHeap();
	}
	
	/*
     * add the given person into heap. bubbles up
     * the given patient to keep heap order.
     * duplicate names and priorities are allowed.
     * duplicate priorities will be sorted by name alphabetically
     */
	public void enqueue(List<String> ladder, int priority) {
		priorityQueue.enqueue(ladder, priority);
	}
	
	/*
     * add the given person into heap. bubbles up
     * the given patient to keep heap order.
     * duplicate names and priorities are allowed.
     * duplicate priorities will be sorted by name alphabetically
     */
	public void enqueue(Ladder ladder) {
		priorityQueue.enqueue(ladder.ladder, ladder.priority);
	}
	
	/*
     * removes the frontmost patient in heap.
     * returns their name as a string.
     * 
     * if the heap is empty it returns an exception
     */
	public List<String> dequeue() throws ArrayIndexOutOfBoundsException {
		return priorityQueue.dequeue();
	}
	
	/*
     * return the name of the front most patient
     */
	public List<String> peek() {
		return priorityQueue.peek();
	}
	
	/*
     * return the integer priority of the front most patient
     */
	public int peekPriority() {
		return priorityQueue.peekPriority();
	}
	
	/*
     * return true if patient heap does not contain elements
     */
	public boolean isEmpty() {
		return priorityQueue.isEmpty();
	}
	
	/*
     * return the number of elements in heap
     */
	public int size() {
		return priorityQueue.size();
	}
	
	/*
     * remove all elements from heap
     */
	public void clear() {
		priorityQueue.clear();
	}
	
	@Override
	public String toString() {
		return priorityQueue.toString();
	}
	
	/*
     * purpose: to implement maxpq class
     * using a binary max heap. this nested class contains
     * all the code and functions that are used in the maxpq class
     * giving the actual maxpq class a cleaner look.
     * 
     * also for practice on oop programming.
     */
	private class MyBinaryMaxHeap {

		private List<Ladder> heap;
		private int size;
		private int heapCapacity;
		
		public MyBinaryMaxHeap() {
			heapCapacity = 1000;
			size = 0;
			heap = new ArrayList<Ladder>(Collections.nCopies(heapCapacity + 1, null));
		}
		
		/*
         * add the given person into heap. bubbles up
         * the given patient to keep heap order.
         * duplicate names and priorities are allowed.
         * duplicate priorities will be sorted by name alphabetically
         */
		public void enqueue(List<String> name, int priority) {
			if (size + 1 >= heapCapacity)
				resizeHeap();
			Ladder links = new Ladder(name, priority);
			heap.set(size + 1, links);
			size++;
			bubbleUp(size);
		}
		
		/*
         * moves the patient at the given index up in the heap
         * to main heap order. It checks with its parents to see
         * if the cur index has a higher or lower priority and
         * if parents have a lower priority then it moves the patient
         * up in the queue.
         */
		public void bubbleUp(int index) {
			int parentIndex = getParent(index);
			
			while (parentIndex != 0) {
				if (heap.get(index).priority > heap.get(parentIndex).priority) {
					Ladder exParentLadder = heap.get(parentIndex);
					heap.set(parentIndex, heap.get(index));
					heap.set(index, exParentLadder);
					index = parentIndex;
					parentIndex = getParent(index);
				}
				else if (heap.get(index).priority == heap.get(parentIndex).priority) {
					int childSize = heap.get(index).ladder.size();
					int parentSize = heap.get(parentIndex).ladder.size();
					
					if (childSize < parentSize) {
						Ladder exParentLadder = heap.get(parentIndex);
						heap.set(parentIndex, heap.get(index));
						heap.set(index, exParentLadder);
						index = parentIndex;
						parentIndex = getParent(index);
					}
					else
						return;
				}
				else
					break;
			}
		}
		
		/*
         * removes the frontmost patient in heap.
         * returns their name as a string.
         * 
         * if the heap is empty it returns an exception
         */
		public List<String> dequeue() throws ArrayIndexOutOfBoundsException {
			if (size == 0)
				throw new ArrayIndexOutOfBoundsException("Empty Array.");
			List<String> oldLadder = heap.get(1).ladder;
			heap.set(1, heap.get(size));
			heap.set(size, null);
			size--;
			bubbleDown(1);
			return oldLadder;
		}
		
		/*
         * moves the patient at the given index down the heap
         * to main heap order. it checks with both the children
         * to see which one if any have a higher priority than
         * their parent. if one of them do then they switch places
         * and it keeps finding.
         * if both children have the same priority then it checks
         * and switches with lowest size
         */
		private void bubbleDown(int index) {
			int leftChild = getLeftChild(index);
			int rightChild = getRightChild(index);
			
			while (heap.get(leftChild) != null || heap.get(rightChild) != null) {
				if (heap.get(leftChild) == null && heap.get(rightChild) != null) {
					if (heap.get(index).priority < heap.get(rightChild).priority) {
						Ladder ladder = heap.get(rightChild);
						heap.set(rightChild, heap.get(index));
						heap.set(index, ladder);
						
						//new indexes
						index = rightChild;
						leftChild = getLeftChild(index);
						rightChild = getRightChild(index);
					}
					break;
				}
				else if (heap.get(leftChild) != null && heap.get(rightChild) == null) {
					if (heap.get(index).priority < heap.get(leftChild).priority) {
						Ladder ladder = heap.get(leftChild);
						heap.set(leftChild, heap.get(index));
						heap.set(index, ladder);
						
						//new indexes
						index = leftChild;
						leftChild = getLeftChild(index);
						rightChild = getRightChild(index);
					}
					break;
				}
				else {
					if (heap.get(index).priority < heap.get(leftChild).priority || 
							heap.get(index).priority < heap.get(rightChild).priority) {
						if (heap.get(leftChild).priority > heap.get(rightChild).priority) {
							Ladder ladder = heap.get(leftChild);
							heap.set(leftChild, heap.get(index));
							heap.set(index, ladder);
							
							//new indexes
							index = leftChild;
							leftChild = getLeftChild(index);
							rightChild = getRightChild(index);
						}
						else if (heap.get(rightChild).priority > heap.get(leftChild).priority) {
							Ladder ladder = heap.get(rightChild);
							heap.set(rightChild, heap.get(index));
							heap.set(index, ladder);
							
							//new indexes
							index = rightChild;
							leftChild = getLeftChild(index);
							rightChild = getRightChild(index);
						}
						else {
							//lowest size gets prio
							int leftSize = heap.get(leftChild).ladder.size();
							int rightSize = heap.get(rightChild).ladder.size();
							
							if (leftSize < rightSize) {
								//bubble left
								Ladder ladder = heap.get(leftChild);
								heap.set(leftChild, heap.get(index));
								heap.set(index, ladder);
								
								//new indexes
								index = rightChild;
								leftChild = getLeftChild(index);
								rightChild = getRightChild(index);
							}
							else if (rightSize < leftSize) {
								//bubble right
								Ladder ladder = heap.get(rightChild);
								heap.set(rightChild, heap.get(index));
								heap.set(index, ladder);
								
								//new indexes
								index = rightChild;
								leftChild = getLeftChild(index);
								rightChild = getRightChild(index);
							}
							else {
								//System.out.println("IDK WHAT TO DO");
								break;
							}
						}
					}
					else
						break;
				}
				//idk if this is right
				if (leftChild > heapCapacity || rightChild > heapCapacity) {
					break;
				}
			}
		}
		
		/*
         * return the name of the front most patient
         */
		public List<String> peek() {
			return heap.get(1).ladder;
		}
		
		/*
         * return the integer priority of the front most patient
         */
		public int peekPriority() {
			return heap.get(1).priority;
		}
		
		/*
         * return true if patient heap does not contain elements
         */
		public boolean isEmpty() {
			return size == 0;
		}
		
		/*
         * return the number of elements in heap
         */
		public int size() {
			return size;
		}
		
		/*
         * remove all elements from heap
         */
		public void clear() {
			heapCapacity = 1000;
			size = 0;
			heap = new ArrayList<Ladder>(heapCapacity);
		}
		
		@Override
		public String toString() {
			String big = "{";
			
			for (int i = 1; i <= size; i++) {
				big += heap.get(i).priority;
				if (i != size) {
					big += ", ";
				}
			}
			big += "}";
			return big;
		}
		
		/*
         * resizes the heap to double its capacity
         */
		private void resizeHeap() {
			heapCapacity = heapCapacity * 2;
			
			List<Ladder> heapCopy = heap;
			heap = new ArrayList<Ladder>(Collections.nCopies(heapCapacity + 1, null));
			
			//copy values
			for (int i = 1; i <= size; i++) {
				heap.set(i, heapCopy.get(i));
			}
		}
		
		/*
         * returns the left child of the given index
         */
		private int getLeftChild(int index) {
			return index * 2;
		}
		
		/*
         * returns the right child of the given index
         */
		private int getRightChild(int index) {
			return index * 2 + 1;
		}
		
		/*
         * returns the parent of the given index
         */
		private int getParent(int index) {
			return index / 2;
		}
		
	}
	
	public static class Ladder {
		public List<String> ladder;
	    public int priority;

	    public Ladder(List<String> ladder, int priority) {
	        this.ladder = ladder;
	        this.priority = priority;
	    }

		public String toString() {
			return ladder + " (" + priority + ")";
		}
	}
}
